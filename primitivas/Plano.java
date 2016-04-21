package primitivas;

/**
 * Clase que representa el plano
 * @author Adrian Milla Español, Sandra Malpica Mallo
 *
 */
public class Plano implements Objeto {
	private Vector p1, p2;
	private Vector p3, p4;
	private Vector normal;
	private Color c;
	private int n;
	private Color kr;
	private boolean transparent;
	private int spec;
	private Color refract;
	private boolean hasDifuse;

	public Plano() {
	}

	/**
	 * Si p2 no esta definido el plano es infinito
	 * @param p1
	 * @param p2
	 * @param norm
	 * @param c
	 * @param n
	 * @param spec
	 * @param kr
	 * @param ref
	 * @param p3
	 * @param p4
	 */
	public Plano(Vector p1, Vector p2, Vector norm, Color c, int n, int spec,
			Color kr, Color ref, Vector p3, Vector p4) {
		this.p1 = p1;
		this.p2 = p2;
		this.normal = norm;
		this.c = c;
		this.n = n;
		this.spec = spec;
		this.kr = kr;
		this.refract = ref;
		transparent = false;
		this.p3 = p3;
		this.p4 = p4;

		hasDifuse = true;
	}

	/**
	 * Actualiza la normal del plano
	 * @param n
	 */
	public void setNormal(Vector n) {
		this.normal = n;
	}

	/**
	 * Devuelve el primer punto del plano
	 * @return
	 */
	public Vector getP1() {
		return p1;
	}

	/**
	 * Actualiza el primer punto del plano
	 * @param p
	 */
	public void setP1(Vector p) {
		this.p1 = p;
	}

	public Vector getP2() {
		return p2;
	}

	public void setP2(Vector p2) {
		this.p2 = p2;
	}

	/**
	 * Comprueba si el plano intersecta con el rayo r devolviendo true si es asi
	 * y guardando el lambda
	 */
	@Override	
	public boolean intersecta(Rayo r, int t) {
		double dn = normal.dotProduct(r.getD());
		if (dn == 0) { // El rayo no intersecta el plano
			return false;

		}
		Vector resta = p1.resta(r.getA());
		double den = resta.dotProduct(normal);
		double res = den / dn;
		if (res <= 0)
			return false;

		// Si estos tres puntos no son null tenemos un plano finito
		// Comprobamos si el punto esta dentro de estos cuatro puntos
		if (p2 != null && p3!=null && p4!=null) {
			Vector point = r.getPointAt(res);
			// comprobar que si tienen el mismo signo,
			// crear dos triangulos y comprobar si intersecta con cada uno de
			// ellos
			boolean intersecta1 = false;
			boolean intersecta2 = false;
			double s1 = normal.dotProduct(p2.resta(p1).vectProduct(
					point.resta(p1)));
			double s2 = normal.dotProduct(p3.resta(p2).vectProduct(
					point.resta(p2)));
			double s3 = normal.dotProduct(p1.resta(p3).vectProduct(
					point.resta(p3)));
			intersecta1 = (s1 > 0 && s2 > 0 && s3 > 0)
					|| (s1 < 0 && s2 < 0 && s3 < 0);
			s1 = normal.dotProduct(p2.resta(p1).vectProduct(point.resta(p1)));
			s2 = normal.dotProduct(p4.resta(p2).vectProduct(point.resta(p2)));
			s3 = normal.dotProduct(p1.resta(p4).vectProduct(point.resta(p4)));
			intersecta2 = (s1 > 0 && s2 > 0 && s3 > 0)
					|| (s1 < 0 && s2 < 0 && s3 < 0);
			if (intersecta1 || intersecta2) {
				r.setLambda(res, this);
				return true;
			}
		} else {
			r.setLambda(res, this);
			return true;
		}
		return false;

	}

	/**
	 * Devuelve la normal del plano
	 */
	@Override
	public Vector getNormal(Vector point, int t) {
		return normal;
	}

	/**
	 * Devuelve el factor K del plano para los calculos de color
	 */
	@Override
	public Color getColor(int t) {
		return c;
	}

	/**

	 * Actualiza el factor K del plano
	 */
	@Override
	public void setColor(Color c) {
		this.c = c;

	}

	/**
	 * Devuelve el exponente para el calculo del especular
	 */
	@Override
	public int getN(int t) {
		return n;
	}

	/**
	 * Devuelve el componente de reflexion
	 */
	@Override
	public Color getReflex(int t) {
		return kr;
	}

	/**
	 * Actualiza el componente de reflexion
	 * @param s
	 */
	public void setReflex(Color s) {
		kr = s;
	}

	/**
	 * Devuelve el factor especular Ks
	 */
	@Override
	public int getSpec(int t) {
		return spec;
	}

	/**
	 * Actualiza el factor especular
	 * @param s
	 */
	public void setSpec(int s) {
		spec = s;
	}

	/**
	 * Devuelve si es transparente
	 */
	@Override
	public boolean isTransparent(int t) {
		return transparent;
	}

	/**
	 * Actualiza la transparencia
	 * @param b
	 */
	public void setTransparent(boolean b) {
		transparent = b;
	}

	/**
	 * Devuelve true si el objeto es reflexivo
	 */
	@Override
	public boolean hasReflex(int t) {
		if (kr.getR() > 0 || kr.getG() > 0 || kr.getB() > 0)
			return true;
		return false;
	}

	/**
	 * Devuelve si es un plano
	 */
	@Override
	public boolean isPlane() {

		return true;
	}

	/**
	 * Devuelve componente de refraccion
	 */
	@Override
	public Color getRefract(int t) {
		return refract;
	}

	/**
	 * Actualiza componente de refraccion
	 * @param refract
	 */
	public void setRefract(Color refract) {
		this.refract = refract;
	}

	/**
	 * Rota la normal del plano
	 */
	@Override
	public void rotate(Matriz m) {
		normal = m.vectorMatriz(normal);
		p1 = m.vectorMatriz(p1);
		if (p2 != null && p2!=null && p3!=null) {
			p2 = m.vectorMatriz(p2);
			p3 = m.vectorMatriz(p3);
			p4 = m.vectorMatriz(p4);
		}
	}

	/**
	 * Mueve el punto central del plano
	 */
	@Override
	public void translate(Matriz m) {
		p1 = m.vectorMatriz(p1);
		if (p2 != null && p2!=null && p3!=null) {
			p2 = m.vectorMatriz(p2);
			p3 = m.vectorMatriz(p3);
			p4 = m.vectorMatriz(p4);
		}

	}


	/**
	 * Escala el plano 
	 */
	@Override
	public void scale(Matriz m) {
		p1 = m.vectorMatriz(p1);
		if(p2 != null && p2!=null && p3!=null){
			p2 = m.vectorMatriz(p2);
			p3 = m.vectorMatriz(p3);
			p4 = m.vectorMatriz(p4);
		}
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
