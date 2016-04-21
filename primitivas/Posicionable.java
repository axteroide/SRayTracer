package primitivas;

import java.util.ArrayList;

/**
 * Interfaz que representa cualquier objeto que puede ser incluido en el mundo
 * Ya sean objetos o luces
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public interface Posicionable {

	public void rotate(Matriz m);
	public void translate(Matriz m);
	public void scale(Matriz m);
}
