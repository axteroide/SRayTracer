package primitivas;

/**
 * Clase de luz direccional
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class LuzDireccional extends Luz {
	
	Vector direction;
	public LuzDireccional(Vector v, Color c){		
		super(c);
		direction = v;
		direction.normalizar();
	}
	
	/**
	 * Devuelve la direccion de la luz direciconal
	 * @return
	 */
	public Vector getDirection() {
		return direction;
	}


	/**
	 * Actualiza la direccion de la luz direccional
	 * @param direction
	 */
	public void setDirection(Vector direction) {
		this.direction = direction;
	}


	@Override
	/**
	 * Dado un punto x devuelve la direccion en el que le da la luz
	 */
	public Vector pointNormal(Vector x) {
		
		return direction;
	}


	@Override
	/**
	 * Devuelve la posicion de la luz (Ya que la luz esta en el infinito se multiplica la direccion por un numero alto)
	 */
	public Vector getPosicion() {
		Vector temp = new Vector(-direction.getX() * 1000, -direction.getY() * 1000, -direction.getZ() * 1000);
		return temp;
	}

	/**
	 * Rota el vector de a donde apunta la luz
	 */
	@Override
	public void rotate(Matriz m) {
		direction = m.vectorMatriz(direction);
		
	}

	/**
	 * No hace nada ya que este tipo de luz es solo una direccion
	 */
	@Override
	public void translate(Matriz m) {
		
	}


	/**
	 * No hace nada ya que no podemos escalar la luz
	 */
	@Override
	public void scale(Matriz m) {
		
	}

}
