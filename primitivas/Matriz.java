package primitivas;

/**
 * Clase que contiene las funciones para trabajar con matrices
 * @author Adrian Milla Español, Sandra Malpica Mallo
 */
public class Matriz {

	public double[][] matriz;

	
	public Matriz() {
		this.matriz = new double[4][4];
		setZero();
	}

	/**
	 * Rellena la matriz con ceros
	 */
	public void setZero() {
		matriz[0][0] = 0;
		matriz[0][1] = 0;
		matriz[0][2] = 0;
		matriz[0][3] = 0;
		matriz[1][0] = 0;
		matriz[1][1] = 0;
		matriz[1][2] = 0;
		matriz[1][3] = 0;
		matriz[2][0] = 0;
		matriz[2][1] = 0;
		matriz[2][2] = 0;
		matriz[2][3] = 0;
		matriz[3][0] = 0;
		matriz[3][1] = 0;
		matriz[3][2] = 0;
		matriz[3][3] = 0;
	}

	/**
	 * Pone la matriz como identidad
	 */
	public void setIdentidad() {
		matriz[0][0] = 1;
		matriz[0][1] = 0;
		matriz[0][2] = 0;
		matriz[0][3] = 0;
		matriz[1][0] = 0;
		matriz[1][1] = 1;
		matriz[1][2] = 0;
		matriz[1][3] = 0;
		matriz[2][0] = 0;
		matriz[2][1] = 0;
		matriz[2][2] = 1;
		matriz[2][3] = 0;
		matriz[3][0] = 0;
		matriz[3][1] = 0;
		matriz[3][2] = 0;
		matriz[3][3] = 1;
	}

	/**
	 * Devuelve un vector resultado de multiplicar esta matriz por el vector
	 * @param otro
	 * @return
	 */
	public Vector vectorMatriz(Vector otro) {
		double x, y, z;
		x = otro.getX() * matriz[0][0] + otro.getY() * matriz[1][0]
				+ otro.getZ() * matriz[2][0] + matriz[3][0];
		y = otro.getX() * matriz[0][1] + otro.getY() * matriz[1][1]
				+ otro.getZ() * matriz[2][1] + matriz[3][1];
		z = otro.getX() * matriz[0][2] + otro.getY() * matriz[1][2]
				+ otro.getZ() * matriz[2][2] + matriz[3][2];
		return new Vector(x, y, z);
	}

	/**
	 * Establece la matriz a partir de un array
	 * @param t
	 */
	public void setMatriz(double[][] t) {
		this.matriz = t;
	}

	/**
	 * Dados unos valores x,y,z establece una matriz de traslacion
	 * @param x
	 * @param y
	 * @param z
	 */
	public void trasladar(double x, double y, double z) {
		matriz[0][0] = 1;
		matriz[1][1] = 1;
		matriz[2][2] = 1;
		matriz[3][3] = 1;
		matriz[3][0] = x;
		matriz[3][1] = y;
		matriz[3][2] = z;
	}

	/**
	 * Dado un angulo en radianes establece una matriz que rota en el angulo x
	 * @param angulo
	 */
	public void rotarX(double angulo) {
		matriz[0][0] = 1;
		matriz[3][3] = 1;
		matriz[1][1] = Math.cos(angulo);
		matriz[1][2] = Math.sin(angulo);
		matriz[2][1] = -Math.sin(angulo);
		matriz[2][2] = Math.cos(angulo);
	}

	/**
	 * Dado un angulo establece una matriz que rota en el angulo y
	 * @param angulo
	 */
	public void rotarY(double angulo) {
		matriz[1][1] = 1;
		matriz[3][3] = 1;
		matriz[0][0] = Math.cos(angulo);
		matriz[2][0] = -Math.sin(angulo);
		matriz[0][2] = Math.sin(angulo);
		matriz[2][2] = Math.cos(angulo);
	}

	/**
	 * Dado un angulo establece una matriz que rota en el angulo z
	 * @param angulo
	 */
	public void rotarZ(double angulo) {
		matriz[2][2] = 1;
		matriz[3][3] = 1;
		matriz[0][0] = Math.cos(angulo);
		matriz[0][1] = Math.sin(angulo);
		matriz[1][0] = -Math.sin(angulo);
		matriz[1][1] = Math.cos(angulo);
	}
	
	/**
	 * Establece la matriz de escalado en x,y,z
	 * @param x
	 * @param y
	 * @param z
	 */
	public void scaleXYZ(double x, double y, double z ){
		matriz[0][0] = x;
		matriz[1][1] = y;
		matriz[2][2] = z;
	}
	
	/**
	 * Devuelve una matriz resultado de multiplicar esta matriz por m2
	 * @param m2
	 * @return
	 */
	public Matriz MultiplyMatrices ( Matriz m2){
		int i,j,k; //0..3
		double[][] m4 = new double[4][4];	
		for(i = 0; i<4; i++)
			for(j = 0; j<4; j++)
				for(k = 0; k<4; k++)		
					m4[i][j] = ((m4[i][j]) + (matriz[i][k] * m2.getXY(k,j)));			
						
		Matriz nueva = new Matriz();
		nueva.setMatriz(m4);
		return nueva;
	}
	
	/**
	 * Devuelve el valor almacenado en la posicion x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public double getXY(int x, int y){
		return matriz[x][y];
	}
	
}
