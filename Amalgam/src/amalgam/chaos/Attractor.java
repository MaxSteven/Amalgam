package amalgam.chaos;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;

/**
 * http://jlswbs.blogspot.co.uk/
 */
public abstract class Attractor {
	protected final PApplet p5;

	private double x, y, z; // current pos
	private Vec3D o, pos; // original position and current
	private float scale;
	private ArrayList<Vec3D> positions;

	public double a, b, c, d, e, f, g; // chaotic params
	abstract public void setParams(List<Float> params);

	public Attractor(PApplet p5, Vec3D pos, float scale) {
		this.p5 = p5;
		this.o = pos.copy();
		this.pos = pos;
		this.scale = scale;
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;

		positions = new ArrayList<>();
		positions.add(new Vec3D((float) x, (float) y, (float) z));
	}

	public void reset() {
		positions.clear();
		this.pos = o.copy();
		this.x = o.x;
		this.y = o.y;
		this.z = o.z;
	}

	public void setPosition(Vec3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
		this.pos = p;
	}

	// Euler Integration - Perhaps add RK4 at some point?
	public void update(double dt) {
		double xnew = x + dx(x, y, z) * dt;
		double ynew = y + dy(x, y, z) * dt;
		double znew = z + dz(x, y, z) * dt;
		x = xnew;
		y = ynew;
		z = znew;
		pos = new Vec3D((float) x, (float) y, (float) z);
		positions.add(pos);
	}

	public void draw() {
		p5.stroke(255);
//		p5.beginShape(PApplet.POINTS);
		p5.beginShape(PApplet.LINE_STRIP);
		for (int i = 0; i < positions.size(); i++) {
			Vec3D v = positions.get(i);
			v = v.scale(scale);
			p5.vertex(v.x, v.y, v.z);
		}
		p5.endShape();
	}

	public void draw(ColorList clrs) {
		p5.beginShape(PApplet.LINE_STRIP);
		for (int i = 0; i < positions.size(); i++) {
			p5.stroke(clrs.get(i % clrs.size()).toARGB());
			Vec3D v = positions.get(i);
			v = v.scale(scale);
			p5.vertex(v.x, v.y, v.z);
		}
		p5.endShape();

		// p5.stroke(255);
		// p5.beginShape(PApplet.POINTS);
		// for (int i = 0; i < positions.size(); i++) {
		// Vec3D v = positions.get(i);
		// v = v.scale(scale);
		// p5.vertex(v.x, v.y, v.z);
		// }
		// p5.endShape();
	}

	public void drawQuads(ColorList clrs, float scaling) {
		p5.noStroke();
		for (int i = 0; i < positions.size() - 1; i++) {
			Vec3D v = positions.get(i).scale(scale);
			Vec3D w = positions.get(i + 1).scale(scale);

			p5.fill(clrs.get(i % clrs.size()).toARGB());
			// p5.fill(clrs.get((i*i/2) % clrs.size()).toARGB());
			p5.beginShape();
			p5.vertex(v.x, v.y, v.z);
			p5.vertex(v.scale(scaling).x, v.scale(scaling).y,
					v.scale(scaling).z);
			p5.vertex(w.scale(scaling).x, w.scale(scaling).y,
					w.scale(scaling).z);
			p5.vertex(w.x, w.y, w.z);
			p5.endShape();
		}
	}

	public abstract double dx(double x, double y, double z);

	public abstract double dy(double x, double y, double z);

	public abstract double dz(double x, double y, double z);

	public Vec3D getPos() {
		return pos;
	}

	public ArrayList<Vec3D> getPositions() {
		return positions;
	}

}