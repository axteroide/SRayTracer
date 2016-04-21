package rayTracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import primitivas.Color;
import primitivas.Objeto;
import primitivas.Vector;
import primitivas.malla.Malla3D;
import primitivas.malla.Poligono;
import primitivas.malla.Superficie;
import primitivas.malla.Vertice;

public class LoadObject {
	/**
	 * Dada la localizacion de un archivo .obj devuelve una malla3D
	 * @param nombre
	 * @param threads
	 * @return
	 */
	static Malla3D Load(String nombre, int threads) {
		Malla3D malla = new Malla3D(threads);
		File file = new File(nombre);
		ArrayList<Vertice> vertices = new ArrayList<Vertice>();
		ArrayList<Vector> normales = new ArrayList<Vector>();
		ArrayList<Poligono> poligonos = new ArrayList<Poligono>();
		Superficie sur = new Superficie();
		double maxX, maxY, maxZ;
		double minX, minY, minZ;
		maxX = Double.MIN_VALUE;
		maxY = Double.MIN_VALUE;
		maxZ = Double.MIN_VALUE;
		minX = Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		minZ = Double.MAX_VALUE;
		try {

			Scanner sc = new Scanner(file);
			double x;
			double y;
			double z;
			Vector v;
			while (sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				String[] splited = line.split("\\s+");
				if (!line.equals("") && !line.equals(" "))
					switch (splited[0]) {
					case "v":	// Vertice
						// Sacamos las componentes x,y,z y comprobamos si son
						//minimas o maximas para crear la envoltura
						x = Double.parseDouble(splited[1]);
						if(x < minX)
							minX = x;
						if(x > maxX)
							maxX = x;
						y = Double.parseDouble(splited[2]);
						if(y < minY)
							minY = y;
						if(y > maxY)
							maxY = y;
						z = Double.parseDouble(splited[3]);
						if(z < minZ)
							minZ = z;
						if(z > maxZ)
							maxZ = z;
						v = new Vector(x, y, z);
						Vertice vert = new Vertice();
						vert.setLocal_pos(v);
						vert.setWorld_pos(v);
						vertices.add(vert);
						break;
					case "f":	// Poligono
						Poligono pol = new Poligono(malla);
						// Crea un poligono con los vertices proporcionados
						for (int i = 1; i < splited.length; i++) {
							String s = splited[i];
							Vertice verti = new Vertice();
							int vertno, normno;
							boolean ind = s.contains("/");
							// Comprobamos las distintas formas en las que se 
							// puede guardas una cara en un obj
							if (ind) {
								vertno = Math.abs(Integer.parseInt(s.substring(0,
										s.indexOf("/"))));
								normno = Math.abs(Integer.parseInt(s.substring(s.lastIndexOf("/") + 1, s.length())));
								try {
//									System.out.println(normno+" "+vertno);
//									System.out.println(normales.size());
									vertices.get(vertno - 1).setNormal(
											normales.get(normno - 1));
									pol.setVertex(vertices.get(vertno - 1));
								} catch (Exception e) {
									e.printStackTrace();
									System.err.println("Fichero mal formado");
									System.exit(1);
								}
							} else {

								vertno = Integer.parseInt(s);
								try {
									pol.setVertex(vertices.get(vertno - 1));
								} catch (Exception e) {
									e.printStackTrace();
									System.err.println("Fichero mal formado");
									System.exit(1);
								}
							}							
						}
						sur.setSurface(pol);
						break;
					case "vn": // Normal del vertice
						x = Double.parseDouble(splited[1]);
						y = Double.parseDouble(splited[2]);
						z = Double.parseDouble(splited[3]);
						v = new Vector(x, y, z);
						normales.add(v);
						break;
					case "o":	// Nombre del objeto
						malla.setNombre(splited[1]);
						break;
					case "usemtl":	// Usado para darle distintos colores a un solo objeto
						if(sur.getPolygon_head().size() != 0){
							malla.setSuperficie(sur);
							sur = new Superficie();
							for(int i = 1 ; i < splited.length ; i++){
								switch(splited[i]){
								case "refrac":
									int r = Integer.parseInt(splited[++i]);
									int g = Integer.parseInt(splited[++i]);
									int b = Integer.parseInt(splited[++i]);
									Color refrac = new Color(r,g,b);
									sur.setRefract(refrac);
									sur.setTransparent(true);
								break;
							case "color":
								int cr = Integer.parseInt(splited[++i]);
								int cg = Integer.parseInt(splited[++i]);
								int cb = Integer.parseInt(splited[++i]);
								Color col = new Color(cr,cg,cb);
								sur.setColor(col);
								break;
							case "reflex":
								int rr = Integer.parseInt(splited[++i]);
								int rg = Integer.parseInt(splited[++i]);
								int rb = Integer.parseInt(splited[++i]);
								Color ref = new Color(rr,rg,rb);
								sur.setReflex(ref);
								break;
							}
							}
						}
						break;

					}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("El fichero: " + nombre + " no existe.");
			System.exit(1);
		}
		Vector min = new Vector(minX,minY,minZ);
		Vector max = new Vector(maxX,maxY,maxZ);
		malla.setMin(min);
		malla.setMax(max);
		malla.setVertexList(vertices);
		malla.setSuperficie(sur);
		malla.calculateNormals();
		return malla;
	}
}
