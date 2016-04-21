package primitivas;

/**
 * Clase base de todos los objetos que pueden ser renderizados
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public interface Objeto extends Posicionable {
	public Vector getNormal(Vector point, int t);
	public Color getColor(int t);
	public void setColor(Color c);
	public boolean intersecta (Rayo r, int t);
	public int getN(int t);
	public int getSpec(int t);
	public boolean isTransparent(int t);
	public Color getReflex(int t);
	public Color getRefract(int t);
	public boolean hasReflex(int t);
	public boolean isPlane();
	public void disableDifuse();
	public void enableDifuse();
	public boolean hasDifuse();
}
