package primitivas;

public class Color {
	private int r, g, b;

	/* Indica el color en RGB */
	public Color(int r, int g, int b) {
		if (r < 0)
			r = 0;
		if (g < 0)
			g = 0;
		if (b < 0)
			b = 0;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Devuelve el color rojo
	 * @return
	 */
	public int getR() {
		return r;
	}

	/**
	 * Actualiza el rojo
	 * @param r
	 */
	public void setR(int r) {
		if (r < 0)
			r = 0;
		if (r > 255)
			r = 255;
		this.r = r;
	}

	/**
	 * Devuelve el color verde
	 * @return
	 */
	public int getG() {
		
		return g;
	}

	/**
	 * Actualiza el color verde
	 * @param g
	 */
	public void setG(int g) {
		if (g < 0)
			g = 0;
		if (g > 255)
			g = 255;
		this.g = g;
	}

	/**
	 * Devuelve el color azul
	 * @return
	 */
	public int getB() {
		return b;
	}

	/**
	 * Actualiza el color azul
	 * @param b
	 */
	public void setB(int b) {
		if (b < 0)
			b = 0;
		if (b > 255)
			b = 255;
		this.b = b;
	}

	/**
	 * Actualiza los tres colores dejandolo entre 0 y 255
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setRGB(int r, int g, int b) {
		if (r < 0)
			r = 0;
		if (r > 255)
			r = 255;
		if (g < 0)
			g = 0;
		if (g > 255)
			g = 255;
		if (b < 0)
			b = 0;
		if (b > 255)
			b = 255;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Devuelve el resultado de la suma de este color con n
	 * @param n
	 * @return
	 */
	public Color suma(Color n) {

		Color ret = new Color(r + n.getR(), g + n.getG(), b + n.getB());
		return ret;
	}

	/**
	 * Devuelve un color que es la media para n colores
	 * @param n
	 * @return
	 */
	public Color media(int n) {

		Color ret = new Color(r / n, g / n, b / n);
		return ret;
	}

	/**
	 * Devuelve un array con el color entre 0 y 1
	 * @return
	 */
	public double[] limitado() {
		double[] col = new double[3];
		col[0] = (double) r / 255;
		col[1] = (double) g / 255;
		col[2] = (double) b / 255;

		return col;
	}

	/**
	 * Normaliza el color dejandolo entre 0 y 1
	 */
	public void normalize() {
		if (r < 0)
			r = 0;
		if (r > 255)
			r = 255;
		if (g < 0)
			g = 0;
		if (g > 255)
			g = 255;
		if (b < 0)
			b = 0;
		if (b > 255)
			b = 255;
	}
	
	/**
	 * Multiplica interpolando por otro color conviertiendolo antes entre 0 y 1
	 * @param n
	 * @return
	 */
	public Color multiplica(Color n) {
		double[] col = n.limitado();
		Color ret = new Color((int) (r * col[0]),(int) (g * col[1]),(int)( b * col[2]));
		return ret;
	}
}
