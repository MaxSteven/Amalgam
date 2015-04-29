package examples.chaos;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import amalgam.chaos.Attractor;
import amalgam.chaos.HadleyAttractor;
import amalgam.colour.QuickColours;

@SuppressWarnings("serial")
public class AttractorSphere_example extends PApplet {
	Attractor att;
	ColorList colours;
	PeasyCam cam;

	public void setup() {
		size(1280, 720, P3D);
		stroke(255);
		smooth(8);

		cam = new PeasyCam(this, 50);
		colours = QuickColours.get(500);
		att = new HadleyAttractor(this);
	}

	public void draw() {
		background(0);

		for (int i = 0; i < 50; i++) {
			att.update(0.003);
		}

		Vec3D cen = new Vec3D(0, 0, 0);
		ArrayList<Vec3D> poz = att.getPositions();
		for (Vec3D v : poz) {
			float mag = v.distanceTo(cen);
			if (mag >= 5f) {
				mag = 5f;
			}
			Vec3D a = v.sub(cen).normalize().scale(400 * mag / 5f);
			point(a.x, a.y, a.z);
		}

	}

}