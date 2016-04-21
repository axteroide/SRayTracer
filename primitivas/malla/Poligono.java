package primitivas.malla;

import java.util.ArrayList;

import primitivas.Objeto;
import primitivas.Rayo;
import primitivas.Vector;

public class Poligono {
	ArrayList<Vertice> vertex_list_head = new ArrayList<Vertice>();
	Vector poly_normal;
	boolean poly_visible;
	Objeto base;
	public Poligono(Objeto p){
		base = p;
	}
	
	public Vector getPoly_normal() {
		return poly_normal;
	}
	public void setPoly_normal(Vector poly_normal) {
		this.poly_normal = poly_normal;
	}
	public boolean isPoly_visible() {
		return poly_visible;
	}
	public void setPoly_visible(boolean poly_visible) {
		this.poly_visible = poly_visible;
	}
	public void setVertex(Vertice v){
		vertex_list_head.add(v);
	}
	public Vertice getVertexAt(int i){
		return vertex_list_head.get(i);
	}
	public Vector calculateNormal(){
		Vector v = new Vector(0,0,0);
		Vector w1,w2,w3;
		w1 = vertex_list_head.get(0).getWorld_pos();
		w2 = vertex_list_head.get(1).getWorld_pos();
		w3 = vertex_list_head.get(2).getWorld_pos();
		v.setX((w2.getY() - w1.getY()) * (w3.getZ() - w2.getZ()) - (w2.getZ() - w1.getZ()) * (w3.getY() - w2.getY()));
		v.setY((w2.getZ() - w1.getZ()) * (w3.getX() - w2.getX()) - (w2.getX() - w1.getX()) * (w3.getZ() - w2.getZ()));
		v.setZ((w2.getX() - w1.getX()) * (w3.getY() - w2.getY()) - (w2.getY() - w1.getY()) * (w3.getX() - w2.getX()));
		v.normalizar();
		poly_normal = v;
		return v;
	}
	/**
	 * Intersecta el rayo r con el poligono
	 * @param r
	 * @return
	 */
	public boolean intersecta(Rayo r){
		double dn = r.getD().dotProduct(poly_normal);
		if(dn >= 0){
			return false;
		}
		Vector p1 = vertex_list_head.get(0).getWorld_pos();
		Vector pa = p1.resta(r.getA());
		double pan = pa.dotProduct(poly_normal);
		if(pan == 0){
			return false;
		}
		// lambda intersecta el plano, comprobamos si esta dentro:
		double lambda = pan / dn;
		Vector point = r.getPointAt(lambda);
		Vector p2 = vertex_list_head.get(1).getWorld_pos();
		Vector p3 = vertex_list_head.get(2).getWorld_pos();
		double s1 = poly_normal.dotProduct(p2.resta(p1).vectProduct(point.resta(p1)));
		double s2 = poly_normal.dotProduct(p3.resta(p2).vectProduct(point.resta(p2)));
		double s3 = poly_normal.dotProduct(p1.resta(p3).vectProduct(point.resta(p3)));
		
		if(s1 < 0 && s2 < 0 && s3 < 0 || s1 > 0 && s2 > 0 && s3 > 0){
			if(lambda > 0.0001)
				r.setLambda(lambda, base);
			return true;
		}		
		return false;
	}
}
