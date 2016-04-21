package primitivas.malla;

import primitivas.Vector;

public class Vertice {
	Vector local_pos;
	Vector world_pos;
	Vector normal;
	public Vector getNormal() {
		return normal;
	}
	public void setNormal(Vector normal) {
		this.normal = normal;
	}
	public Vector getLocal_pos() {
		return local_pos;
	}
	public void setLocal_pos(Vector local_pos) {
		this.local_pos = local_pos;
	}
	public Vector getWorld_pos() {
		return world_pos;
	}
	public void setWorld_pos(Vector world_pos) {
		this.world_pos = world_pos;
	}
	
}
