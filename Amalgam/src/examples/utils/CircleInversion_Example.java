package examples.utils;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Vec2D;
import amalgam.utils.AIO;

@SuppressWarnings("serial")
public class CircleInversion_Example extends PApplet {

	ArrayList<Vec2D> pts;
	ArrayList<Vec2D> ptsInv;

	Circle inversion;

	public void setup() {
		size(1280, 720, P2D);
		background(0);
		smooth(8);

		strokeWeight(2);

		inversion = new Circle(width / 2, height / 2, 200);

		pts = new ArrayList<Vec2D>();
		for (int i = 0; i < width; i += 10) {
			for (int j = 0; j < height; j += 10) {
				pts.add(new Vec2D(i, j));
			}
		}
	}

	public void draw() {
		background(0);
		noLoop();
		// inversion.setRadius(inversion.getRadius() * random(0.98f, 1.02f)).jitter(random(-2,2));
		inversion.setRadius(150);

		ptsInv = new ArrayList<Vec2D>();
		for (int i = 0; i < pts.size(); i++) {
			Vec2D v = pts.get(i);
			Vec2D inv = invert(v, inversion);
			ptsInv.add(inv);
		}

		for (int i = 0; i < pts.size(); i++) {
			Vec2D p = pts.get(i);
			Vec2D pp = ptsInv.get(i);

			stroke(0, 0, 255);
			// line(p.x, p.y, pp.x, pp.y);
			stroke(255);
			// point(p.x, p.y);
			stroke(255, 0, 0);
			// point(pp.x, pp.y);

			fill(i % 255, 2 * (i % 255), 5 * (i % 255));
			noStroke();
			ellipse(pp.x, pp.y, 4, 4);
		}

		AIO.saveFramePNG(this);
	}

	public Vec2D invert(Vec2D v, Circle c) {
		Vec2D inv = new Vec2D(v);
		float d = c.distanceTo(v);
		inv.set(c.x + c.getRadius() * c.getRadius() * (v.x - c.x) / (d * d), c.y + c.getRadius() * c.getRadius() * (v.y - c.y) / (d * d));
		return inv;
	}

}
