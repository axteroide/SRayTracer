package primitivas.malla;

import java.util.ArrayList;

import primitivas.Color;
import primitivas.Rayo;
import primitivas.Vector;
import primitivas.Matriz;

public class Superficie {
	ArrayList<Poligono> polygon_head = new ArrayList<Poligono>();
	private Color c;
	private Integer n;
	private Color kr;
	private Boolean transparent;
	private Integer spec;
	private Color refract;
	// TriangleMesh local_pos;
	// TriangleMesh world_pos;

	public void setSurface(Poligono v) {
		polygon_head.add(v);
	}

	public Poligono getSurfaceAt(int i) {
		return polygon_head.get(i);
	}
	public void calculateNormals(){
		for(Poligono p:polygon_head)
			p.calculateNormal();
	}

	public ArrayList<Poligono> getPolygon_head() {
		return polygon_head;
	}

	public void setPolygon_head(ArrayList<Poligono> polygon_head) {
		this.polygon_head = polygon_head;
	}
	
	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		this.c = c;
		
	}


	public Integer getN() {
		return n;
	}

	public Integer getSpec() {
		return spec;
	}

	public Boolean isTransparent() {
		return transparent;
	}
	public void setTransparent(boolean t) {
		transparent = t;
	}
	public Color getReflex() {
		return kr;
	}
	public void setReflex(Color c) {
		kr = c;
	}

	public Color getRefract() {
		return refract;
	}
	public void setRefract(Color c) {
		refract = c;
	}

	public Boolean hasReflex() {
		if (kr.getR() > 0 || kr.getG() > 0 || kr.getB() > 0)
			return true;
		return false;
	}

	
	
}
