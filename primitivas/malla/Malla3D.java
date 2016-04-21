package primitivas.malla;

import java.util.ArrayList;

import primitivas.Color;
import primitivas.Matriz;
import primitivas.Objeto;
import primitivas.Plano;
import primitivas.Rayo;
import primitivas.Vector;

/**
 * Clase que representa una malla de triangulos
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class Malla3D implements Objeto {
	private String nombre;
	private ArrayList<Vertice> vertex_head = new ArrayList<Vertice>();
	private ArrayList<Superficie> surface_head  = new ArrayList<Superficie>();
	private Matriz transformacion;
	// En estas variables guardamos el ultimo poligono y superficie intersectadas
	private Superficie[] supinter = null;
	private Poligono[] polinter = null;
	
	private Color c;
	private int n;
	private Color kr;
	private boolean transparent;
	private int spec;
	private Color refract;
	private Plano[] planos = new Plano[6];
	private Vector min, max;
	private boolean hasDifuse;
	

	public Malla3D(int threads){
		transformacion = new Matriz();
		transformacion.setZero();
		transformacion.setIdentidad();	
		supinter = new Superficie[threads];
		polinter = new Poligono[threads];
		hasDifuse = true;
	}
	
	/**
	 * Devuelve el valor minimo de las posiciones x,y,z locales encontrado
	 * @return
	 */
	public Vector getMin() {
		return min;
	}
	/**
	 * Actualiza el valor minimo de las posiciones x,y,z locales encontrado
	 * @return
	 */
	public void setMin(Vector min) {
		this.min = min;
	}
	
	/**
	 * Devuelve el valor maximo de las posiciones x,y,z locales encontrado
	 * @return
	 */
	public Vector getMax() {
		return max;
	}
	
	/**
	 * Actualiza el valor maximo de las posiciones x,y,z locales encontrado
	 * @return
	 */
	public void setMax(Vector max) {
		this.max = max;
	}
	
	/**
	 * Devuelve el nombre del objeto
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Actualiza el nombre del objeto
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la matriz de transformacion asociada a este objeto
	 * @return
	 */
	public Matriz getTransformacion() {
		return transformacion;
	}

	/**
	 * Actualiza la matriz de transformacion
	 * @param transformacion
	 */
	public void setTransformacion(Matriz transformacion) {
		this.transformacion = transformacion;
	}
	
	/**
	 * Añade una superficie a la lista de superficies
	 * @param s
	 */
	public void setSuperficie(Superficie s){
		surface_head.add(s);		
	}
	
	/**
	 * Devuelve la superficie en la posicion i
	 * @param i
	 * @return
	 */
	public Superficie getSuperficieAt(int i){
		return surface_head.get(i);
	}
	
	/**
	 * Actualiza la lista de vertices
	 * @param v
	 */
	public void setVertexList(ArrayList<Vertice> v){
		vertex_head = v;
	}

	
	/**
	 * Rota el objeto multiplicando la matriz guardada por la matriz m
	 */
	@Override
	public void rotate(Matriz m) {
		transformacion = transformacion.MultiplyMatrices(m);
	}

	/**
	 * Mueve el objeto multiplicando la matriz guardada por la matriz m
	 */
	@Override
	public void translate(Matriz m) {
		transformacion = transformacion.MultiplyMatrices(m);
		
	}
	
	/**
	 * Escala el objeto multiplicando la matriz guardada por la matriz m
	 */
	@Override
	public void scale(Matriz m) {
		transformacion = transformacion.MultiplyMatrices(m);
	}
	
	/**
	 * Actualiza las posiciones del mundo de todos los puntos
	 * multiplicando la posicion local por la matriz almacenada
	 */
	public void applyTransform(){
		for(Vertice v: vertex_head){
			v.setWorld_pos(transformacion.vectorMatriz(v.getLocal_pos()));
		}
		// Recalculamos las normales
		calculateNormals();
		// Recalculamos la envoltura
		minMax();		
	}
	
	/**
	 * Actualiza las posiciones del mundo de todos los puntos
	 * multiplicando la posicion del mundo por la matriz almacenada
	 */
	public void applyToWorld(){
		for(Vertice v: vertex_head){
			v.setWorld_pos(transformacion.vectorMatriz(v.getWorld_pos()));
		}
		// Recalculamos las normales
		calculateNormals();
		// Recalculamos la envoltura
		minMax();		
	}

	/**
	 * Devuelve la normal en el punto point para el thread t
	 */
	@Override
	public Vector getNormal(Vector point, int t) {
		return polinter[t].getPoly_normal();
	}

	/**
	 * Devuelve el color del punto de la ultima interseccion para el thread t
	 */
	@Override
	public Color getColor(int t) {
		if(supinter[t].getColor() == null)
			return c;
		return supinter[t].getColor();
	}

	/**
	 * Actualiza el color K del objeto
	 */
	@Override
	public void setColor(Color c) {
		this.c = c;
		
	}

	/**
	 * Comprueba si el rayo r intersecta nuestro objeto
	 * comprobando primero si intersecta el cubo que rodea el objeto
	 */
	@Override
	public boolean intersecta(Rayo r,int t) {
		if(!intersectPlanes(r))
			return false;
		return intersectTriangles(r, t);
		//return false;
	}

	/**
	 * Devuelve el exponente del objeto en el ultimo punto intersectado por el
	 * rayo para el thread t
	 */
	@Override
	public int getN(int t) {
		if(supinter[t].getN() == null)
			return n;
		return supinter[t].getN();
	}
	
	/**
	 * Actualiza el exponente de la especular
	 * @param n
	 */
	public void setN(int n){
		this.n = n;
	}

	/**
	 * Devuelve el factor especular Ks del objeto en el ultimo punto intersectado por el
	 * rayo para el thread t
	 */
	@Override
	public int getSpec(int t) {
		if(supinter[t].getSpec() == null)
			return spec;
		return supinter[t].getSpec();
	}
	
	/** Actualiza el factor especular Ks
	 * 
	 * @param s
	 */
	public void setSpec(int s){
		spec = s;
	}

	/**
	 * Devuelve true si el ultimo punto intersectado en el thread t es transparente
	 */
	@Override
	public boolean isTransparent(int t) {
		if(supinter[t].isTransparent() == null)
			return transparent;
		return supinter[t].isTransparent();
	}
	
	/**
	 * Actualiza si el objeto es transparente
	 * @param t
	 */
	public void setTransparent(boolean t){
		transparent = t;
	}

	/**
	 * Devuelve el componente reflexivo para el ultimo punto intersectado para
	 * el thread t
	 */
	@Override
	public Color getReflex(int t) {
		if(supinter == null)
			System.out.println("Esto no deberia suceder");
		if(supinter[t].getReflex() == null)
			return kr;
		return supinter[t].getReflex();
	}
	
	/**
	 * Actualiza el componente reflexivo
	 * @param r
	 */
	public void setReflex(Color r){
		kr = r;
	}

	/**
	 * Devuelve el componente de refraccion para el ultimo punto intersectado
	 * para el thread t
	 */
	@Override
	public Color getRefract(int t) {
		if(supinter[t].getRefract() == null)
			return refract;
		return supinter[t].getRefract();
	}
	
	public void setRefract(Color r){
		refract = r;
	}

	/**
	 * Devuelve true si tiene componente reflexiva el ultimo punto intersectado
	 */
	@Override
	public boolean hasReflex(int t) {
		if(supinter[t].getReflex() == null){
			if (kr.getR() > 0 || kr.getG() > 0 || kr.getB() > 0)
				return true;
			return false;
		}
		return supinter[t].hasReflex();
	}

	/**
	 * Devuelve is es un plano
	 */
	@Override
	public boolean isPlane() {
		return false;
	}
	
	/**
	 * Recorre el listado de superficies calculando la normal de cada poligono
	 */
	public void calculateNormals(){
		for(Superficie s:surface_head)
			s.calculateNormals();
	}
	
	/**
	 * Funcion que comprueba si un rayo intersecta con la caja que envuelve la malla
	 * @param r
	 * @return
	 */
	public boolean intersectPlanes(Rayo r){
		
		Vector p = r.getA();
		Vector m = r.getD();
		if((p.getX() < min.getX() && m.getX() <= 0 )||( p.getY() < min.getY() && m.getY() <= 0 )||( p.getZ() < min.getZ() && m.getZ() <= 0)){
			//System.out.println("No impacta el poligono");
			return false;
		}
		if((p.getX() > max.getX() && m.getX() >= 0 )||( p.getY() > max.getY() && m.getY() >= 0 )||( p.getZ() > max.getZ() && m.getZ() >= 0 )){
			//System.out.println("No impacta el poligono");
			return false;
	}
		return true;
		
	}
	
	/**
	 * Recorre el listado de triangulos comprobando con cada triangulo si hay una interseccion
	 * @param r
	 * @param t
	 * @return
	 */
	public boolean intersectTriangles(Rayo r,int t){
		Superficie smin = supinter[t];
		Poligono pmin = polinter[t];
		double lmin = Double.MAX_VALUE;
		boolean inter = false;
	
		for(Superficie s:surface_head){			
			for(Poligono p:s.getPolygon_head()){
				boolean i = p.intersecta(r);
				inter |= i;
				if(i){	// Si intersecta comprobamos si es el minimo lambda
					if(r.getLambda() < lmin){
						// si el lambda es el menor guardamos la superficie
						// y el poligono intersectado
						lmin = r.getLambda();
						smin = s;
						pmin = p;
					}
				}
			}		
		}
		// Para el thread t smin y pmin es el punto mas cercano al inicio del rayo
		// por lo que los guardamos como ultimo punto de interseccion
		supinter[t] = smin;
		polinter[t] = pmin;
		return inter;
	}
/*
	public ArrayList<Superficie> getSurface_head() {
		return surface_head;
	}

	public void setSurface_head(ArrayList<Superficie> surface_head) {
		this.surface_head = surface_head;
	}
	public Plano getPlanoAt(int i) {
		return planos[i];
	}
	public void setPlanoAt(Plano plano, int i) {
		this.planos[i] = plano;
	}
	*/
	
	/**
	 * Recorre todos los puntos de la malla para sacar los puntos maximos y minimos y crear la envoltura
	 */
	public void minMax(){
		double maxX, maxY, maxZ;
		double minX, minY, minZ;
		maxX = Double.MIN_VALUE;
		maxY = Double.MIN_VALUE;
		maxZ = Double.MIN_VALUE;
		minX = Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		minZ = Double.MAX_VALUE;
		for(Vertice v: this.vertex_head){
			Vector w = v.getWorld_pos();
			if(w.getX() < minX)
				minX = w.getX();
			if(w.getX() > maxX)
				maxX = w.getX();
			if(w.getY() < minY)
				minY = w.getY();
			if(w.getY() > maxY)
				maxY = w.getY();
			if(w.getZ() < minZ)
				minZ = w.getZ();
			if(w.getZ() > maxZ)
				maxZ = w.getZ();			
		}

		min = new Vector(minX,minY,minZ);
		max = new Vector(maxX,maxY,maxZ);
	}
	
	@Override
	public void disableDifuse() {
		hasDifuse = false;		
	}

	@Override
	public void enableDifuse() {
		hasDifuse = true;
		
	}

	@Override
	public boolean hasDifuse() {
		return hasDifuse;
	}

}
