package primitivas;

/**
 * Clase base para las luces
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public abstract class Luz implements Posicionable {

	private Color color;
	
	public Luz (){}
	
	public Luz (Color c){
		this.color = c;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	abstract public Vector pointNormal(Vector x);
	abstract public Vector getPosicion();
}
