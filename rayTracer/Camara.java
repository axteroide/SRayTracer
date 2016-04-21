package rayTracer;

import primitivas.Matriz;
import primitivas.Vector;
import primitivas.Posicionable;

public class Camara implements Posicionable{

	private Vector posicion;
	private float viewPlaneDist; //distancia de la camara al plano
	private Vector w,up;
	private Vector u,v;
	private float height, width;
	private Matriz transform;
	
	public Camara(Vector p, float vi, Vector ne){
		transform = new Matriz();
		this.up = new Vector(0,0,-1);
		this.posicion = p;
		this.viewPlaneDist = vi;
		this.w = new Vector(-ne.getX(),-ne.getY(),-ne.getZ());
		w.normalizar();
		//aspect ratio
		float tam = 1;
		width = 1*tam;
		height = tam*3/4;
		calcular();
	}
	
	/**
	 * Calcula los vectores de la camara
	 * y crea la matriz de transformacion de la camara
	 */
	private void calcular(){		
		this.u = up.vectProduct(w);
		u.normalizar();
		// Para que los ejes no salgan invertidos cambio el orden del producto vectorial
		this.v = u.vectProduct(w);
		v.normalizar();
		//matriz de transformacion
		double[][] t = new double[4][4];
		t[0][0] = u.getX();
		t[0][1] = u.getY();
		t[0][2] = u.getZ();
		t[0][3] = 0;

		t[1][0] = v.getX();
		t[1][1] = v.getY();
		t[1][2] = v.getZ();
		t[1][3] = 0;

		t[2][0] = w.getX();
		t[2][1] = w.getY();
		t[2][2] = w.getZ();
		t[2][3] = 0;

		t[3][0] = posicion.getX();
		t[3][1] = posicion.getY();
		t[3][2] = posicion.getZ();
		t[3][3] = 1;
		
		this.transform.setMatriz(t);
	}
	
	private void setTransform(){
		double[][] t = new double[4][4];
		t[0][0] = u.getX();
		t[0][1] = u.getY();
		t[0][2] = u.getZ();
		t[0][3] = 0;

		t[1][0] = v.getX();
		t[1][1] = v.getY();
		t[1][2] = v.getZ();
		t[1][3] = 0;

		t[2][0] = w.getX();
		t[2][1] = w.getY();
		t[2][2] = w.getZ();
		t[2][3] = 0;

		t[3][0] = posicion.getX();
		t[3][1] = posicion.getY();
		t[3][2] = posicion.getZ();
		t[3][3] = 1;
		
		this.transform.setMatriz(t);
	}
	
	/**
	 * Devuelve la distancia del plano al ojo
	 * @return
	 */
	public float getView(){
		return this.viewPlaneDist;
	}
	
	/**
	 * Actualiza la distancia del plano al ojo
	 * @param f
	 */
	public void setView(float f){
		this.viewPlaneDist=f;
	}
	
	/**
	 * Devuelve la posicion de la camara
	 * @return
	 */
	public Vector getPos(){
		return posicion;
	}
	
	/**
	 * Actualiza la posicion de la camara
	 * @param p
	 */
	public void setPos(Vector p){
		this.posicion=p;
		calcular();
	}
	
	public Vector getW(){
		return w;
	}
	
	/**
	 * Dado un vector w calcula el resto de vectores y actualiza la matriz
	 * @param ne
	 */
	public void setW(Vector ne){
		this.w = ne;
		w.normalizar();
		calcular();
	}
	
	public float getHeight(){
		return this.height;
	}
	
	public float getWidth(){
		return this.width;
	}
	
	public Matriz getM(){
		return this.transform;
	}
	
	/**
	 * Calculamos el ancho y alto de la camara de forma que mantengamos el
	 * aspect ratio con la resolucion de la pantalla para no deformar la imagen
	 * @param resX
	 * @param resY
	 */
	public void setAspectRatio(int resX,int resY){
		int max = Math.max(resX,resY);
		width  = (float) resX / max;
		height = (float) resY / max;
	}

	/**
	 * Rota los tres ejes de la camara
	 */
	@Override
	public void rotate(Matriz m) {
		u = m.vectorMatriz(u);
		v = m.vectorMatriz(v);
		w = m.vectorMatriz(w);
		setTransform();
		
	}

	/**
	 * Mueve el punto de la camara
	 */
	@Override
	public void translate(Matriz m) {
		posicion = m.vectorMatriz(posicion);
		setTransform();
	}

	@Override
	public void scale(Matriz m) {
		
	}
}
