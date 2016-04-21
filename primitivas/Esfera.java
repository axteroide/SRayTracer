package primitivas;

/**
 * Clase que representa una esfera
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class Esfera implements Objeto {

	private Vector p; // centro de la esfera
	private float radio; // radio de la esfera
	private Color c;
	private int n;
	private Color kr;
	private boolean transparent;
	private int spec;
	private Color refract;
	private boolean hasDifuse;
	
	/**
	 * 
	 * @param pos
	 *            Posicion de la esfera
	 * @param m
	 *            Radio de la esfera
	 * @param c
	 *            Color de la esfera
	 * @param n
	 *            Factor para la especular
	 */
	public Esfera(Vector pos, float m, Color c, int n, int spec, Color kr,
			Color ref) {
		this.p = pos;
		this.radio = m;
		this.c = c;
		this.n = n;
		this.kr = kr;
		this.spec = spec;
		transparent = false;
		this.setRefract(ref);
		hasDifuse = true;
	}

	/**
	 * Devuelve la posicion central de la esfera
	 * @return
	 */
	public Vector getP() {
		return p;
	}

	/**
	 * Actualiza la posicion central de la esfera
	 * @param p
	 */
	public void setP(Vector p) {
		this.p = p;
	}

	/**
	 * Devuelve el radio de la esfera
	 * @return
	 */
	public float getR() {
		return radio;
	}

	/**
	 * Actualiza el radio de la esfera
	 * @param r
	 */
	public void setR(float r) {
		this.radio = r;
	}

	/**
	 * Comprueba si la esfera intersecta con el rayo r devolviendo true si es asi
	 * y guardando el lambda
	 */
	public boolean intersecta(Rayo r, int t) {
		double A, B, C;
		double D;
		Vector ac;
		double acx = r.getA().getX() - this.p.getX();
		double acy = r.getA().getY() - this.p.getY();
		double acz = r.getA().getZ() - this.p.getZ();
		ac = new Vector(acx, acy, acz);

		A = r.getD().dotProduct(r.getD());
		B = ac.dotProduct(r.getD());
		C = ac.dotProduct(ac) - Math.pow(radio,2);
		D = 4 * Math.pow(B, 2) - 4 * A * C;

		if (D < 0) { // El rayo no intersecta la esfera
			return false;
		} else if (D == 0) { // El rayo intersecta la esfera en un punto
			r.setLambda(-2 * B / (2 * A), this);
			return true;
		}
		// El rayo intersecta la esfera en dos puntos
		double sq = Math.sqrt(D);

		double min = Math.min((-2 * B - sq) / (2 * A), (-2 * B + sq) / (2 * A));
		double max = Math.max((-2 * B - sq) / (2 * A), (-2 * B + sq) / (2 * A));
		// Si el punto es menor que una tasa esta colisionando en el punto inicial
		// de este debido a precisiones. No lo guardamos
		if (min <= 0.001 && max > 0) {
			if (max < 0.001)
				return false;
			r.setLambda(max, this);
			return true;
		} else if (min <= 0) {
			return false;
		}
		r.setLambda(min, this);
		return true;
	}

	@Override
	/**
	 * Devuelve la normal de la esfera para un punto
	 */
	public Vector getNormal(Vector point,int t) {

		Vector ne = point.resta(p);
		ne.normalizar();
		return ne;
	}

	@Override
	/**
	 * Devuelve el factor K de la esfera para los calculos de color
	 */
	public Color getColor(int t) {
		return c;
	}

	@Override
	/**
	 * Actualiza el factor K de la esfera
	 */
	public void setColor(Color c) {
		this.c = c;
	}

	@Override
	/**
	 * Devuelve el exponente para el calculo del especular
	 */
	public int getN(int t) {
		return n;
	}

	@Override
	/**
	 * Devuelve true si la esfera es transparente
	 */
	public boolean isTransparent(int t) {
		return transparent;
	}

	/**
	 * Actualiza si la esfera es transparente
	 * @param b
	 */
	public void setTransparent(boolean b) {
		transparent = b;
	}

	@Override
	/**
	 * Devuelve el componente reflexivo
	 */
	public Color getReflex(int t) {
		return kr;
	}

	/**
	 * Actualiza el componente reflexivo
	 * @param s
	 */
	public void setReflex(Color s) {
		kr = s;
	}

	@Override
	/**
	 * Devuelve el factor especular
	 */
	public int getSpec(int t) {
		return spec;
	}
	
	/**
	 * Actualiza el componente especular
	 * @param s
	 */
	public void setSpec(int s) {
		spec = s;
	}

	@Override
	/**
	 * Devuelve true si tiene componente reflexivo
	 */
	public boolean hasReflex(int t) {
		if (kr.getR() > 0 || kr.getG() > 0 || kr.getB() > 0)
			return true;
		return false;
	}

	@Override
	/**
	 * Devuelve si es un plano
	 */
	public boolean isPlane() {

		return false;
	}

	@Override
	/**
	 * Devuelve el componente de refraccion
	 */
	public Color getRefract(int t) {
		return refract;
	}

	/**
	 * Actualiza el componente de refraccion
	 * @param refract
	 */
	public void setRefract(Color refract) {
		this.refract = refract;
	}

	/**
	 * No hace nada ya que una esfera da igual como la rotemos
	 */
	@Override
	public void rotate(Matriz m) {

	}

	/**
	 *	Mueve la esfera multiplicando el punto p por la matriz de traslacion m
	 */
	@Override
	public void translate(Matriz m) {
		p = m.vectorMatriz(p);
	}

	@Override
	/**
	 * Escala la esfera multiplicando el radio por uno de los componentes de escala de la matriz
	 */
	public void scale(Matriz m) {
		this.radio = (float) (this.radio * m.getXY(0, 0));
	}

	@Override
	/**
	 * Deshabilita la luz dejando solo la ambiental
	 */
	public void disableDifuse() {
		hasDifuse = false;		
	}

	@Override
	/**
	 * Habilita la luz difusa
	 */
	public void enableDifuse() {
		hasDifuse = true;
		
	}

	@Override
	/**
	 * Devuelve tru si tiene luz difusa la esfera
	 */
	public boolean hasDifuse() {
		return hasDifuse;
	}

}
