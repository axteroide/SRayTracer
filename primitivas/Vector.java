package primitivas;

/**
 * Clase que representa un vector o un punto
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class Vector {

	private double x,y,z;
	/*Indica las tres componentes de un vector*/
	public Vector(double d, double e, double f){
		this.x=d;
		this.y=e;
		this.z=f;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public void setXYZ(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	/**
	 * Devuelve el producto escalar entre este y otro vector
	 * @param otro
	 * @return
	 */
	public double dotProduct(Vector otro){
		return this.x*otro.getX() + this.y*otro.getY() + this.z*otro.getZ();
	}
	
	/**
	 * Devuelve el producto vectorial entre este y otro vector
	 * @param otro
	 * @return
	 */
	public Vector vectProduct(Vector otro){
		double x = this.y*otro.getZ() - this.z*otro.getY();
		double y = this.z*otro.getX() - this.x*otro.getZ();
		double z = this.x*otro.getY() - this.y*otro.getX();
		return new Vector(x,y,z);
	}
	
	/**
	 * Normaliza el vector
	 */
	public void normalizar(){
		double denominador;
		denominador = magnitud();
		if(denominador != 0){
			this.x = this.x / denominador;
			this.y = this.y / denominador;
			this.z = this.z / denominador;
		}
	}
	
	/**
	 * Le resta a este vector el vector v2
	 * @param v2
	 * @return
	 */
	public Vector resta(Vector v2){
		Vector n = new Vector(x - v2.getX(), y - v2.getY(), z - v2.getZ());
		return n;
	}
	/**
	 * Devuelve el vector resultado de sumar este con el vector v2
	 * @param v2
	 * @return
	 */
	public Vector suma(Vector v2){
		Vector n = new Vector(x + v2.getX(), y + v2.getY(), z + v2.getZ());
		return n;
	}
	/**
	 * Devuelve un vector resultado de multiplicar este por un escalar
	 * @param e
	 * @return
	 */
	public Vector escalar(double e){
		Vector n = new Vector(x * e, y * e, z * e);
		return n;
	}
	
	/**
	 * Devuelve la magnitud del vector
	 * @return
	 */
	private double magnitud(){
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	/**
	 * Devuelve el vector con todas las componentes negadas
	 * @return
	 */
	public Vector negate(){
		Vector neg = new Vector(-x, -y,-z);
		return neg;
	}
	
	/**
	 * Devuelve la distancia entre este y otro vector
	 * @param v2
	 * @return
	 */
	public double distancia(Vector v2){
		double dist = Math.sqrt(Math.pow(v2.getX() - x,2) + Math.pow(v2.getY() - y,2) + Math.pow(v2.getZ() - z,2));
		return dist;
	}
	/**
	 * Devuelve el vector dividido para v2
	 * @param v2
	 * @return
	 */
	public Vector divide(double v2){
		Vector vn = new Vector(x /v2, y / v2 , z/v2);
		return vn;
	}
}
