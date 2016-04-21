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
import BMP.BMP;

public class Main {
	private static ArrayList<Objeto> objetos = new ArrayList<Objeto>();
	private static ArrayList<Luz> luces = new ArrayList<Luz>();
	private static int antialising = 1;
	private static int rebounds = 1;
	private static int resX;
	private static int resY;
	private static String outFileName;
	private static BMP image;
	private static int threads = 4;
	private static Camara camara;

	public static void main(String[] args) {
		// argumentos -aa <antialisingLevel>
		// -resx <resolucionX> -resy <resolucionY>
		// -rebounds <numRebotes>
		// -out <nombreFichero>
		if (args.length < 3) {
			System.out
					.println("Usage: "
							+ "[-aa <antialisingLevel>] [-rebounds <numRebotes>] "
							+ "-resX <resolucionX> -resY <resolucionY> -out <nombreFichero>");
			System.exit(1);
		}
		try {
			boolean tieneX = false, tieneY = false, tieneO = false;
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-aa")) {
					int aux = Integer.parseInt(args[i + 1]);
					if (aux > 0) {
						antialising = aux;
					}
				} else if (args[i].equalsIgnoreCase("-resX")) {
					int aux = Integer.parseInt(args[i + 1]);
					resX = Math.abs(aux);
					tieneX = true;
				} else if (args[i].equalsIgnoreCase("-resY")) {
					int aux = Integer.parseInt(args[i + 1]);
					resY = Math.abs(aux);
					tieneY = true;
				} else if (args[i].equalsIgnoreCase("-rebounds")) {
					int aux = Integer.parseInt(args[i + 1]);
					if (aux > 0) {
						rebounds = aux;
					}
				} else if (args[i].equalsIgnoreCase("-out")) {
					outFileName = args[i + 1];
					tieneO = true;
				}else if(args[i].equalsIgnoreCase("-threads")){
					threads = Integer.parseInt(args[++i]);
				}
			}
			if (tieneX && tieneY && tieneO == false) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out
					.println("Usage: "
							+ "[-aa <antialisingLevel>] [-rebounds <numRebotes>] "
							+ "-resX <resolucionX> -resY <resolucionY> -out <nombreFichero>");
			System.exit(1);
		}

		// Objeto malla = LoadObject.Load("C:\\modelos\\cube.obj");
		// Objeto malla = LoadObject.Load("C:\\modelos\\Chevrolet.obj");
		// Matriz t = new Matriz();
		// t.trasladar(1, 0, 0);
		// malla.translate(t);
		escena();
		// create BMP
		image = new BMP(resX, resY, outFileName);
		// image.setBitDepth(32);
		// escena();
		Thread[] rT = new Thread[threads];
		if(threads != 1){	// Si hay threads separamos el renderizado
		for(int i = 0 ; i < threads ; i++){
			int resXmin;
		
			int resYmin;
			int resXmax;
			int resYmax;
			if(i == 1 || i == 3){
				resXmin = i % 2 * resX / 2;
				resXmax = resX;
			}else{
				resXmin = i % 2 * resX / 2 ;
				resXmax = resX /2;
			}
			if(i == 2 || i == 3){
				resYmin = ((int)i / 2) * resY /2;
				resYmax = resY;
			}else{
				resYmin = ((int)i / 2) * resY /2;
				resYmax = resY /2 + 1;
			}
			rT[i] = new Thread(new Trazador(i, image, resX, resY, resXmin, resXmax, resYmin, resYmax, antialising, rebounds, objetos, luces, camara  ));
		}
		}else{
			rT[0] = new Thread(new Trazador(0, image, resX, resY, 0, resX, 0, resY, antialising, rebounds, objetos, luces, camara  ));
		}

		System.out.println("Empezando el rendering...");
		long startTime = System.currentTimeMillis();
		//render();
		for(int i = 0 ; i < threads ; i++){
			rT[i].start();
		}
		for (Thread thread : rT) {
		    try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Acabando rendering...");
		System.out.println("Imagen renderizada en: " + (elapsedTime / 1000) + " segundos");
		image.imageClose();
		// image.writeToFile("../output.bmp");
	}
	
	/**
	 * Crea la escena
	 */
	private static void escena(){
		// LUCES
				PuntoDeLuz foco = new PuntoDeLuz();
				PuntoDeLuz foco2 = new PuntoDeLuz();

				LuzDireccional direc = new LuzDireccional(new Vector(4, 10, -1),
						new Color(100, 100, 100));
				foco.setPosicion(new Vector(4, 5, 1));
				foco.setColor(new Color(128, 160, 250));
				foco2.setPosicion(new Vector(8, 5, -4));
				foco2.setColor(new Color(100, 100, 100));
				luces.add(foco);
				 luces.add(foco2);
//				 luces.add(direc);

				// ESFERAS
				Esfera s;
				Vector pos = new Vector(0, 0, 0);
				s = new Esfera(pos, 0.5f, new Color(255, 20, 20), 0, 0, new Color(0, 0,
						0), new Color(0, 0, 0));
				// esfera centrada en origen de radio 1
				Esfera s2 = new Esfera(new Vector(0, 0, -7), 1,
						new Color(169, 169, 169), 50, 100, new Color(100, 100, 100),
						new Color(0, 0, 0));
				Matriz transla = new Matriz();
				transla.trasladar(1, 0, 1);
				s2.translate(transla);
				Esfera s3 = new Esfera(new Vector(0, 0, 2), 1, new Color(0, 0, 0), 100,
						100, new Color(0, 0, 0), new Color(210, 210, 210));
				s3.setTransparent(true);
				
				

//				objetos.add(s);
//				objetos.add(s2);
//				objetos.add(s3);

				
				
				
				camara = new Camara(new Vector(19, 10, 0.5), 1, new Vector(-8, 0,
						0));
				Matriz c = new Matriz();
				
				// Rotamos la camara para que no este hacia abajo
				c.rotarX(Math.PI / 2);
				camara.rotate(c);
				c.setZero();
				
				c.rotarZ(Math.toRadians(30));
				camara.rotate(c);
				camara.setAspectRatio(resX, resY);

				

				// PLANOS
				Plano p = new Plano(new Vector(0, 4, 4), new Vector(0,4,-4), new Vector(1, 0, 0),
						new Color(0, 200, 0), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0), new Vector(0,-4,4), new Vector(0,-4,-4));
				Plano p5 = new Plano(new Vector(0, 1, 1), new Vector(0,1,-1), new Vector(1, 0, 0),
						new Color(200, 0, 0), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0), new Vector(0,-5,5), new Vector(0,-5,-5));
				Matriz mov = new Matriz();
				mov.trasladar(-7, 0, 0);
				p.translate(mov);
				//p5.translate(mov);

				Plano p2 = new Plano(new Vector(10, 0, 0), null, new Vector(-1, 0, 0),
						new Color(0, 0, 200), 100, 20, new Color(50, 50, 50),
						new Color(0, 0, 0),null,null);
				Plano p3 = new Plano(new Vector(0, 0, -4), null, new Vector(0, 0, 1),
						new Color(200, 0, 200), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0),null,null);
				Plano p4 = new Plano(new Vector(0, 0, 4), null, new Vector(0, 0, -1),
						new Color(0, 200, 200), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0),null,null);
					
				//-2.96951
				/*Plano suelo = new Plano(new Vector(0, -1.208452, 0), new Vector(0,1,-1), new Vector(0, 1, 0),
						new Color(0, 200, 0), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0), new Vector(0,-5,5), new Vector(0,-5,-5));*/
				Plano suelo = new Plano(new Vector(0, -1.208452, 0), null, new Vector(0, 1, 0),
						new Color(0, 200, 0), 100, 20, new Color(0, 0, 0),
						new Color(0, 0, 0), null, null);
				Plano detras = new Plano(new Vector(-10,0,0), new Vector(0,1,-1), new Vector(-1, 0,0),
						new Color(0, 0, 180), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0), new Vector(0,-5,5), new Vector(0,-5,-5));
				objetos.add(suelo);
//				objetos.add(p);
				// objetos.add(p2);
				//objetos.add(p3);
				//objetos.add(p4);
				//objetos.add(p5);
				//objetos.add(detras);
				Plano prima = new Plano(new Vector(-3, -1.208452, 2),null, new Vector(1, 0, 0),
						new Color(148, 0, 211), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0), new Vector(-10,30,0), new Vector(-10,-1.208452,40));
				Matriz translate = new Matriz();
				translate.rotarY(Math.PI/3.0);
				prima.rotate(translate);
				translate = new Matriz();
				translate.trasladar(0, 0, -2);
				prima.translate(translate);
				objetos.add(prima);
				Plano primaprima = new Plano(new Vector(-4, -1.208452, 0),null, new Vector(1, 0, 0),
						new Color(148, 100, 211), 100, 20, new Color(00, 00, 00),
						new Color(0, 0, 0), new Vector(-10,30,0), new Vector(-10,-1.208452,40));
				translate = new Matriz();
				translate.rotarY(-Math.PI/3.0);
				primaprima.rotate(translate);
				objetos.add(primaprima);
				
				Esfera ambiental = new Esfera(new Vector(6.5, -0.208452, 5f), 1f, new Color(255, 0, 0), 0,
						0, new Color(0, 0, 0), new Color(0, 0, 0));
				ambiental.disableDifuse();
				Esfera difusa = new Esfera(new Vector(6.5, -0.208452, 2.8), 1f, new Color(255, 140, 0), 0,
						0, new Color(0, 0, 0), new Color(0, 0, 0));
				Esfera especular = new Esfera(new Vector(6.5, -0.208452, 0.7), 1f, new Color(0, 0, 140), 10,
						200, new Color(0, 0, 0), new Color(0, 0, 0));
				Esfera reflexiva = new Esfera(new Vector(6.5, -0.208452, -1.4), 1f, new Color(255, 140, 0), 20,
						100, new Color(100, 100, 100), new Color(10, 10, 10));
				Esfera transparente = new Esfera(new Vector(6.5, 5, 0), 1f, new Color(0, 0, 0), 100,
						20, new Color(0, 0, 0), new Color(230, 230, 230));
				transparente.setTransparent(true);
				objetos.add(ambiental);
				objetos.add(difusa);
				objetos.add(especular);
				objetos.add(reflexiva);
				objetos.add(transparente);
				// MALLAS
				/*
				 Malla3D malla = LoadObject.Load("C:\\modelos\\teapot2.obj", threads);
				//
				
				// Malla3D malla = LoadObject.Load("C:\\modelos\\cube.obj",threads);
				  
				  malla.setColor(new Color(80,50,20)); malla.setN(50);
				  malla.setReflex(new Color(00,00,00));
				  malla.setRefract(new  Color(0,0,0)); 
				  malla.setSpec(100); 
				  Matriz t = new Matriz();
				  //t.scaleXYZ(0.02, 0.02, 0.02); 
				  //t.trasladar(0, 0, 0);
				  //malla.translate(t); // t.setZero(); 
				  t.rotarY(Math.PI/4);
				  malla.rotate(t); 
				  //t.setZero(); t.rotarY(Math.PI/4); malla.rotate(t);
				  malla.applyTransform(); 
				  objetos.add(malla);
				 */
//				Malla3D malla = LoadObject.Load("C:\\modelos\\Chevrolet Camaro 20092.obj", threads);
//				 malla.setColor(new Color(179,179,179)); malla.setN(10);
//				  malla.setReflex(new Color(20,20,20));
//				  malla.setRefract(new  Color(0,0,0)); 
//				  malla.setSpec(150); 
				//Malla3D malla = LoadObject.Load("dragon.obj", threads);
				Malla3D malla = LoadObject.Load("C:\\modelos\\teapot2.obj", threads);
				malla.setColor(new Color(74,74,74));
				malla.setReflex(new Color(50,50,50));
				malla.setRefract(new Color(0,0,0));
				malla.setSpec(150);
				malla.setN(10);
				Matriz t = new Matriz();
				t.rotarY(Math.PI-(Math.PI/4));
//				t.rotarX(Math.PI/100);
				malla.rotate(t);		
				malla.applyTransform();
				t = new Matriz();
				t.trasladar(3, 0, 0);
				malla.setTransformacion(t);
				//malla.translate(t);
				malla.applyToWorld();
				//objetos.add(malla);
//				  Matriz t = new Matriz();
//				  t.rotarY(-Math.PI/4);
//				  malla.rotate(t); 
//				  t = new Matriz();
//				  t.trasladar(-5, 0, -5);
//				  malla.rotate(t); 
//				  //t.setZero(); t.rotarY(Math.PI/4); malla.rotate(t);
//				  malla.applyTransform(); 
				  //objetos.add(malla);
	}
}
