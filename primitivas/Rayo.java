package primitivas;

/**
 * Clase que representa un rayo
 * Se inicializa lambda al valor maximo de un double
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class Rayo {

	private Vector a;
	private Vector d;
	private double lambda;	// En lambda guardamos el punto mas cercano de corte
	private Vector intersectPoint;
	private Objeto intersectObject;
	private boolean inside;
	
	/**
	 * a: Punto inicial del rayo
	 * d: direccion a donde apunta el rayo
	 */
	public Rayo (Vector a, Vector d){
		intersectObject = null;
		this.a=a;
		this.d=d;
		d.normalizar();
		this.lambda= Double.MAX_VALUE;
		setInside(false);
	}
	public Rayo (Vector a, Vector d, boolean ins){
		intersectObject = null;
		this.a=a;
		this.d=d;
		d.normalizar();
		this.lambda= Double.MAX_VALUE;
		setInside(ins);
	}
	public Rayo (){
		this.lambda= Double.MAX_VALUE;
	}
	/**
	 * Devuelve el punto en el que empieza el rayo
	 * @return
	 */
	public Vector getA() {
		return a;
	}

	/**
	 * Actualiza el punto en el que empieza un rayo
	 * @param a
	 */
	public void setA(Vector a) {
		this.a = a;
	}

	/**
	 * Devuelve la direccion del rayo
	 * @return
	 */
	public Vector getD() {
		return d;
	}

	/**
	 * Actualiza la direccion del rayo
	 * @param d
	 */
	public void setD(Vector d) {
		this.d = d;
		this.d.normalizar();
	}

	/**
	 * Devuelve la lamda
	 * @return
	 */
	public double getLambda() {		
		return lambda;
	}

	/**
	 * Añadimos el punto mas cercano de corte si este es menor que el que hay
	 * @param e
	 */
	public void setLambda(double e, Objeto o) {
		if(e < lambda){
			this.lambda = e;
			this.intersectObject = o;
			intersectPoint = a.suma(d.escalar(lambda));
		}
	}

	/**
	 * Devuelve el objeto que se ha intersectado
	 * @return
	 */
	public Vector getIntersectPoint() {
		return intersectPoint;
	}


	/**
	 * Devuelve el punto en el que se ha intersectado
	 * @return
	 */
	public Vector getPoint(){		
		Vector v = new Vector(a.getX() + lambda * d.getX(), a.getY() + lambda * d.getX(),a.getX() + lambda * d.getX());
		return v;
	}

	/**
	 * Devuelve el objeto con el que se ha intersectado 
	 * @return
	 */
	public Objeto getIntersectObject() {
		return intersectObject;
	}

	/**
	 * Devuelve el punto del rayo en coordenadas del mundo para una lambda dada
	 * @param l
	 * @return
	 */
	public Vector getPointAt(double l){
		Vector es = d.escalar(l);
		Vector b = a.suma(es);
		return a.suma(d.escalar(l));
	}
	
	/**
	 * Devuelve true si el rayo ha intersectado un objeto
	 * @return
	 */
	public boolean haIntersectado(){
		if(intersectObject != null)
			return true;
		return false;
	}
	
	/**
	 * Devuelve true si el rayo esta viajando por dentro de un objeto
	 * @return
	 */
	public boolean isInside() {
		return inside;
	}
	
	/**
	 * Actualiza si el rayo viaja por dentro de un objeto
	 * @param inside
	 */
	public void setInside(boolean inside) {
		this.inside = inside;
	}
}
