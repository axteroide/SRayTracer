package BMP;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import primitivas.Color;
 
public class BMP {
   private static String IMAGE_FILE = "MyImage.bmp";
   private static int WIDTH = 200;
   private static int HEIGHT = 300;
   private BufferedImage image;
 
   public BMP(int w, int h){
	   WIDTH = w;
	   HEIGHT = h;
	   image = new BufferedImage(WIDTH, HEIGHT, 
               BufferedImage.TYPE_INT_RGB);
   }
   
   public BMP(int w, int h, String im){
	   WIDTH = w;
	   HEIGHT = h;
	   IMAGE_FILE = im;
	   image = new BufferedImage(WIDTH, HEIGHT, 
               BufferedImage.TYPE_INT_RGB);
   }
   
   public BMP(){
	   WIDTH = 800;
	   HEIGHT = 600;
	   image = new BufferedImage(WIDTH, HEIGHT, 
               BufferedImage.TYPE_INT_RGB);
   }
   
   public boolean setPixel(int x, int y, Color c){
	   if(x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT ){
		   return false;
	   }
	   
	   image.setRGB(x, y, getIntFromColor(c.getR(), c.getG(), c.getB()));
	   return true;
   }
   
   public void imageClose(){
	      try {
			ImageIO.write(image, "BMP", new File(IMAGE_FILE));
		} catch (IOException e) {
			System.err.println("Error creando la imagen");
		}
   }
   
   private static int getIntFromColor(int Red, int Green, int Blue){
	    Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
	    Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
	    Blue = Blue & 0x000000FF; //Mask out anything not blue.

	    return 0x00000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}
}