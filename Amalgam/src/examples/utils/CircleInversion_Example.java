package examples.utils;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Vec2D;
import amalgam.utils.AMath;

@SuppressWarnings("serial")
public class CircleInversion_Example extends PApplet {

	ArrayList<Vec2D> pts;
	ArrayList<Vec2D> ptsInv;
	Circle inverter;

	public void setup() {
		size(1280, 720, P2D);
		background(0);
		smooth(8);

		inverter = new Circle(width / 2, height / 2, 200);

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
		noStroke();
		inverter.setRadius(150);

		ptsInv = new ArrayList<Vec2D>();
		for (int i = 0; i < pts.size(); i++) {
			Vec2D v = pts.get(i);
			Vec2D inv = AMath.circleInvert(v, inverter);
			ptsInv.add(inv);
		}

		for (int i = 0; i < pts.size(); i++) {
			Vec2D pp = ptsInv.get(i);
			fill(i % 255, 2 * (i % 255), 5 * (i % 255));
			ellipse(pp.x, pp.y, 4, 4);
		}
	}
}
