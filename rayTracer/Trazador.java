package rayTracer;

import java.util.ArrayList;
import java.util.Random;

import primitivas.Color;
import primitivas.Esfera;
import primitivas.Luz;
import primitivas.LuzDireccional;
import primitivas.Matriz;
import primitivas.Objeto;
import primitivas.Plano;
import primitivas.PuntoDeLuz;
import primitivas.Rayo;
import primitivas.Vector;
import primitivas.malla.Malla3D;
//import toxi.*;
import BMP.BMP;

public class Trazador implements Runnable {

	private ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private ArrayList<Luz> luces = new ArrayList<Luz>();
	private int antialising = 1;
	private int rebounds = 1;
	private int resX, resY;
	private int resXmin, resXmax;
	private int resYmin, resYmax;
	private BMP image;
	private Random rand;
	private int threadno;
	static Color negro = new Color(0, 0, 0);
	private Camara camara;
	//static PuntoDeLuz foco = new PuntoDeLuz(); // TEMPORAL

	
	static float IA = 0.2f;
	
	public Trazador(int threadno,BMP image, int resX, int resY,int resXmin, int resXmax, int resYmin, int resYmax, int aa, int rebounds,ArrayList<Objeto> objetos,ArrayList<Luz> luces, Camara camara  ){
		rand = new Random();
		this.objetos = objetos;
		this.luces = luces;
		this.antialising = aa;
		this.rebounds = rebounds;
		this.resX = resX;
		this.resY = resY;
		this.resXmin =  resXmin;
		this.resXmax = resXmax;
		this.resYmax = resYmax;
		this.resYmin = resYmin;
		this.image = image;
		this.camara = camara;
		this.threadno = threadno;
		
	}


	/**
	 * Calcula los componentes difusos y especular para un punto
	 * @param o
	 * @param r
	 * @param l
	 * @return
	 */
	private Color calculateColor(Objeto o, Rayo r, Luz l) {
		Color c = new Color(0, 0, 0);
		if(o.hasDifuse()){
			c = c.suma(calculateDifuse(o, r, l));
			c = c.suma(calculateSpecular(o, r, l));
		}
		return c;
	}

	/**
	 * Calcula el componente ambiental
	 * @param o
	 * @return
	 */
	private Color calculateAmbient(Objeto o) {
		Color c = new Color(0, 0, 0);
		c.setR((int) (o.getColor(threadno).getR() * IA));
		c.setG((int) (o.getColor(threadno).getG() * IA));
		c.setB((int) (o.getColor(threadno).getB() * IA));
		return c;
	}

	/**
	 * Calcula la componente difusa
	 * @param o
	 * @param r
	 * @param l
	 * @return
	 */
	private Color calculateDifuse(Objeto o, Rayo r, Luz l) {
		Color c = new Color(0, 0, 0);
		double[] n = l.getColor().limitado();
		Vector intersectionPoint = r.getIntersectPoint(); // Punto de la esfera
															// en el que el rayo
															// intersecta
		Vector luzInter = l.pointNormal(intersectionPoint); // Vector que sale
															// del punto d
															// e interseccion a
															// la luz
		Vector normal = o.getNormal(intersectionPoint,threadno);
		double theta = normal.dotProduct(luzInter);
		// Si el angulo es menor que 0 la cara no se ve
		// se pone theta a 0 para evitar problemas
		if (theta < 0)
			theta = 0;
		
		c.setR((int) (o.getColor(threadno).getR() * n[0] * theta));
		c.setG((int) (o.getColor(threadno).getG() * n[1] * theta));
		c.setB((int) (o.getColor(threadno).getB() * n[2] * theta));
		return c;

	}

	/**
	 * Calcula la componente especular
	 * @param o
	 * @param r
	 * @param l
	 * @return
	 */
	private Color calculateSpecular(Objeto o, Rayo r, Luz l) {
		Color c = new Color(0, 0, 0);
		double[] n = l.getColor().limitado();
		Vector intersectionPoint = r.getIntersectPoint(); // Punto de la esfera
															// en el que el rayo
															// intersecta
		Vector luzInter = l.pointNormal(intersectionPoint); // Vector que sale
															// del punto d
															// einterseccion a
															// la luz
		Vector normal = o.getNormal(intersectionPoint,threadno); // Normal del objeto en
														// el punto
														// intersectionPoint

		double NL = normal.dotProduct(luzInter);
		if (NL < 0)
			NL = 0;
		Vector reflexion = new Vector((2 * NL) * normal.getX()
				- luzInter.getX(), (2 * NL) * normal.getY() - luzInter.getY(),
				(2 * NL) * normal.getZ() - luzInter.getZ());
		reflexion.normalizar();

		double fact = r.getD().dotProduct(reflexion);

		fact = Math.pow(fact, o.getN(threadno));
		c.setR((int) (o.getSpec(threadno) * n[0] * fact));
		c.setG((int) (o.getSpec(threadno) * n[1] * fact));
		c.setB((int) (o.getSpec(threadno) * n[2] * fact));

		return c;
	}

	/**
	 * Calcula el rayo reflejado
	 * 
	 * @param o
	 * @param r
	 * @return
	 */
	private Rayo reflexion(Objeto o, Rayo r) {
		Rayo reflectado = new Rayo();
		Vector ref;
		Vector inter = r.getIntersectPoint();
		double dot = r.getD().dotProduct(o.getNormal(inter,threadno)) * 2;
		ref = r.getD().resta(o.getNormal(inter,threadno).escalar(dot));

		reflectado.setA(inter);
		reflectado.setD(ref);
		return reflectado;
	}

	/**
	 * Calcula el rayo refractado
	 * 
	 * @param o
	 * @param r
	 * @return
	 */
	private Rayo refraction(Objeto o, Rayo r) {
		Rayo reflectado = new Rayo();
		Vector I = r.getD().negate();
		Vector inter = r.getIntersectPoint();
		Vector normal = o.getNormal(inter,threadno);

		if (r.isInside())
			normal = normal.negate();

		double dot = I.dotProduct(o.getNormal(inter,threadno));
		dot = dot
				/ (Math.sqrt(I.getX() * I.getX() + I.getY() * I.getY()
						+ I.getZ() * I.getZ()) * Math.sqrt(normal.getX()
						* normal.getX() + normal.getY() * normal.getY()
						+ normal.getZ() * normal.getZ()));

		double thetaI = Math.acos(dot);

		double refraccionVidrio = 1.52;
		// double refraccionVidrio = 1.33;
		double etar;
		if (!r.isInside())
			etar = 1 / refraccionVidrio;
		else
			etar = refraccionVidrio / 1;
		double thetaT = Math.asin(etar * Math.sin(thetaI));

		double cosi = dot;
		double cost = Math
				.sqrt(1 - etar * etar * Math.pow(Math.sin(thetaI), 2));
		Vector pri = normal.escalar(cosi).resta(I);
		pri = pri.escalar(etar);
		Vector sec = normal.escalar(cost);
		Vector fin = pri.resta(sec);
		fin.normalizar();
		

		Vector ib = I.negate();
		Vector uno = ib.escalar(etar);
		double escalar = ib.dotProduct(normal);
		double cons = etar * escalar
				+ Math.sqrt(1 - etar * etar * (1 - escalar * escalar));
		Vector dos = normal.escalar(cons);
		fin = uno.resta(dos);
		fin.normalizar();
		reflectado.setA(inter);
		reflectado.setD(fin);
		return reflectado;
	}

	private Color rayTrace(Rayo ray, int rebounds, Objeto ignorar) {
		Color color = new Color(0, 0, 0);
		if (rebounds <= 0) { // Si ya no hay mas rebotes devolvemos el negro
			return color;
		}

		Color fin = new Color(0, 0, 0);
		Rayo sombra;
		// Comprobamos si intersecta con alguno de los objetos
		// la variable ignorar la usamos para que en un rebote
		// el rayo ignore el objeto del que parte
		for (Objeto o : objetos) {
			if (!(ignorar != null && ignorar == o))
				o.intersecta(ray,threadno);
		}
		if (ray.haIntersectado()) {
			Objeto intersectObject = ray.getIntersectObject();

			for (Luz l : luces) {	// Calculamos para cada luz en la escena
				Objeto transparente = null;
				
				sombra = new Rayo(ray.getIntersectPoint(), l.pointNormal(ray
						.getIntersectPoint()));
				// Lanzamos un rayo desde el punto a la luz
				// para comprobar si en este punto hay sombra
				for (Objeto o : objetos) {
					if (o != ray.getIntersectObject())
						o.intersecta(sombra,threadno);
				}
				boolean transp = false;

				if (sombra.getIntersectObject() != null
						&& sombra.getIntersectObject().isTransparent(threadno)) {
					double distIntersec = ray.getIntersectPoint().distancia(sombra.getIntersectPoint());
					double distL = ray.getIntersectPoint().distancia(l.getPosicion());
					if (distIntersec < distL) { // Comprobamos que el objeto este entre la luz y el punto intersectado
						//Si el objeto intersectado es transparente lanzamos otro rayo
						// sin tener en cuenta la refraccion
						Rayo sombra2 = new Rayo(ray.getIntersectPoint(),
								l.pointNormal(ray.getIntersectPoint()));
						for (Objeto o : objetos) {
							if (o != ray.getIntersectObject()
									&& o != sombra.getIntersectObject())// &&
																		// !o.isTransparent())
								o.intersecta(sombra2,threadno);
						}
						transparente = sombra.getIntersectObject();
						sombra = sombra2;
						transp = true;
					}

				}
				// Si no hay algo enmedio o la luz esta mas cerca de lo que se
				// ha intersectado o el objeto de en medio es transparente
				if (!sombra.haIntersectado()
						|| ray.getIntersectPoint().distancia(l.getPosicion()) < ray
								.getIntersectPoint().distancia(
										sombra.getIntersectPoint())) {

					Color temp = calculateColor(ray.getIntersectObject(), ray,
							l);
					if (transp) {
						// Si habia un objeto transparente o translucido
						// multiplicamos por su factor de transparencia para
						// generar sombra
						temp = temp.multiplica(transparente.getRefract(threadno));
						
					}
					temp.normalize();
					fin = fin.suma(temp);
				}
			}
			
			// Creamos rayo de rebote si tiene factor de reflexion
			if (intersectObject.hasReflex(threadno)) {
				Rayo reflectado = reflexion(ray.getIntersectObject(), ray);
				reflectado.setInside(ray.isInside());
				Color colorreflejado = rayTrace(reflectado, rebounds - 1,
						ray.getIntersectObject());
				Color p = intersectObject.getReflex(threadno);
				fin = fin.suma(colorreflejado.multiplica(intersectObject
						.getReflex(threadno)));
			}
			// Creamos rayo transmitido si tiene factor de refraccion
			if (intersectObject.isTransparent(threadno)) {
				Rayo refractado = refraction(ray.getIntersectObject(), ray);
				// Si no es un plano ponemos el rayo viajando
				// por dentro del objeto
				if (!ray.isInside() && !ray.getIntersectObject().isPlane())
					refractado.setInside(true);
				Color colorrefractado = rayTrace(refractado, rebounds - 1, null);
				colorrefractado = colorrefractado.multiplica(intersectObject
						.getRefract(threadno));
				fin = fin.suma(colorrefractado);
			}
			Color temp = calculateAmbient(ray.getIntersectObject());

			temp.normalize();
			fin = fin.suma(temp);
		} else {
			fin.suma(negro);
		}
		fin.normalize();
		return fin;
	}

	/**
	 * Funcion que recorre cada pixel
	 */
	private void render() {		
		// Factor de incremento de cada rayo para x e y
		double incrX = (camara.getWidth() / resX);
		double incrY = (camara.getHeight() / resY);

		// Posicion inicial para x e y en coordenadas del plano
		float cameraX =((float) resXmin - (resX / 2));
		cameraX = cameraX/resX * camara.getWidth();
		float cameraY =(float) resYmin - (resY / 2);
		cameraY = cameraY/resY * camara.getHeight();

		Vector pixelWorld;
		Matriz m = camara.getM();
		Rayo r;
		
		Vector aux = new Vector(cameraX, cameraY, -camara.getView());
		Vector finalPixel;
		Color fin = new Color(0, 0, 0);
		double rayX, rayY;
		for (int i = resXmin; i < resXmax; i++) { // Bucle para eje X de la pantalla
			cameraY =(float) resYmin - (resY / 2);
			cameraY = cameraY/resY * camara.getHeight();
			for (int j = resYmin; j < resYmax; j++) { // Bucle para eje Y de la pantalla
				fin = new Color(0, 0, 0);
				for (int numrays = 0; numrays < antialising; numrays++) {
					// Bucle para calculo del antialising
					if (antialising != 1) {
						// Si hay antialising sumamos un incremento aleatorio
						rayX = aux.getX() + incrX * (rand.nextDouble() - 0.5);
						rayY = aux.getY() + incrY * (rand.nextDouble() - 0.5);
						finalPixel = new Vector(rayX, rayY, aux.getZ());
					} else {
						// Si el antialising es 1 lanzamos el rayo sin mas
						finalPixel = aux;
					}
					
					pixelWorld = m.vectorMatriz(finalPixel);
					r = new Rayo(camara.getPos(), pixelWorld.resta(camara
							.getPos()));
					fin = fin.suma(rayTrace(r, rebounds, null));
				}

				fin = fin.media(antialising);
				fin.normalize();
				image.setPixel(i, j, fin);
				// actualizamos el rayo en la coordenada Y
				cameraY += incrY;
				aux.setY(cameraY);
			}
			// Actualizamos el rayo en la coordenada X
			cameraX += incrX;
			aux.setX(cameraX);
		}
	}

	@Override
	public void run() {
		render();
		
	}
}
