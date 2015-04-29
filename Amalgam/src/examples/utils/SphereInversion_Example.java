package examples.utils;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;

@SuppressWarnings("serial")
public class SphereInversion_Example extends PApplet {
	ArrayList<Vec3D> pts;
	ArrayList<Vec3D> ptsInv;

	Sphere inversion;

	PeasyCam cam;

	public void setup() {
		size(1280, 720, P3D);
		background(0);

		cam = new PeasyCam(this, 500);

		strokeWeight(2);

		// inversion = new Sphere(new Vec3D(0, 0, 0), 300);
		inversion = new Sphere(new Vec3D(250, 250, 250), 300);

		pts = new ArrayList<Vec3D>();
		for (int i = 0; i < 500; i += 50) {
			for (int j = 0; j < 500; j += 50) {
				for (int k = 0; k < 500; k += 50) {
					pts.add(new Vec3D(i, j, k));
				}
			}
		}
	}

	public void draw() {
		background(0);

		ptsInv = new ArrayList<Vec3D>();
		for (int i = 0; i < pts.size(); i++) {
			Vec3D v = pts.get(i);
			Vec3D inv = invert(v, inversion);
			ptsInv.add(inv);
		}

		for (int i = 0; i < pts.size(); i++) {
			Vec3D p = pts.get(i);
			Vec3D pp = ptsInv.get(i);

			stroke(0, 0, 255);
			// line(p.x, p.y, p.z, pp.x, pp.y, pp.z);
			stroke(255);
			// point(p.x, p.y, p.z);
			stroke(255, 0, 0);
			point(pp.x, pp.y, pp.z);
		}

		// IO.saveFrmPNG(this);
	}

	public Vec3D invert(Vec3D v, Sphere c) {
		Vec3D inv = new Vec3D(v);
		float d = c.distanceTo(v);
		inv.set(c.x + c.radius * c.radius * (v.x - c.x) / (d * d), c.y + c.radius * c.radius * (v.y - c.y) / (d * d), c.z + c.radius * c.radius * (v.z - c.z) / (d * d));
		return inv;
	}

}
