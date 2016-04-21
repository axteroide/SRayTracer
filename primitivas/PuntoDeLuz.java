package primitivas;

/**
 * Clase que representa un punto de luz
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class PuntoDeLuz extends Luz{

	private Vector posicion;

	/**
	 * Devuelve la posicion de la luz
	 */
	@Override
	public Vector getPosicion() {
		return posicion;
	}


	/**
	 * Actualiza la posicion de la luz
	 * @param posicion
	 */
	public void setPosicion(Vector posicion) {
		this.posicion = posicion;
	}



	/**
	 * Devuelve la direccion de la luz para un punto v
	 */
	@Override
	public Vector pointNormal(Vector v) {
		double x,y,z;
		Vector p = posicion;
		p = p.resta(v);
		p.normalizar();
		return p;
	}


	
	/**
	 * No hace nada ya que un punto de luz no puede ser rotado
	 */
	@Override
	public void rotate(Matriz m) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Mueve el punto central de luz
	 */
	@Override
	public void translate(Matriz m) {
		posicion = m.vectorMatriz(posicion);
		
	}



	/**
	 * No escala porque es un punto de luz
	 */
	@Override
	public void scale(Matriz m) {
	
	}

}
