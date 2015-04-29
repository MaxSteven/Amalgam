package examples.chaos;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import amalgam.chaos.Attractor;
import amalgam.chaos.HadleyAttractor;
import amalgam.colour.QuickColours;
import amalgam.utils.AIO;
import controlP5.ControlP5;

/**
 * Main App for rendering chaotic systems
 * 
 * Sort out the Inheritance properly to make creation of new Attractors simpler (and work...)
 */
@SuppressWarnings("serial")
public class AttractorSphere_MAIN extends PApplet {
	Attractor att;

	ColorList colours;
	PeasyCam cam;
	ControlP5 cp5;

	boolean savePDF = false;

	public void setup() {
		size(1280, 720, P3D);
		background(0);

		smooth(8);
		stroke(255);
		strokeWeight(1f);

		cam = new PeasyCam(this, 50);
		colours = new ColorList();
		QuickColours qck = new QuickColours();
		colours = qck.getRandomSet(3000);
		colours = qck.getRandomSet2(2000);

		cp5 = new ControlP5(this);
		cp5.setAutoDraw(false);
		cp5.addFrameRate();

		// att = new LorenzAttractor(this, new Vec3D(0.1f, 0.1f, 0), 50);
		// att = new ModifiedLorenzAttractor(this, new Vec3D(0, 1, 0), 50);
		// att = new SprottLinzL_Attractor(this, new Vec3D(1, 1, 0), 50);
		// att = new SprottLinzG_Attractor(this);
		// att = new NewChaosAttractor(this);
		att = new HadleyAttractor(this);
		// att = new DuffingAttractor(this);
		// att = new NoseHooverAttractor(this, new Vec3D(1, 0, 0), 50);
		// att = new AizawaAttractor(this, new Vec3D(0.1f, 0, 0), 50);
		// att = new HalvorsenAttractor(this, new Vec3D(-5, 0, 0), 50);
	}

	public void draw() {
		background(0);

		for (int i = 0; i < 70; i++) {
			att.update(0.005);
		}

		Vec3D cen = new Vec3D(0, 0, 0);
		// ArrayList<Vec3D> poz = att.getPositions();
		// for (Vec3D v : poz) {
		// Vec3D a = v.sub(cen).normalize().scale(300);
		// point(a.x, a.y, a.z);
		// }

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

	public void gui() {
		hint(DISABLE_DEPTH_TEST);
		cam.beginHUD();
		cp5.draw();
		cam.endHUD();
		hint(ENABLE_DEPTH_TEST);
	}

	public void keyPressed() {
		if (key == 's') {
			savePDF = true;
		}
		if (key == 't') {
			AIO.saveFrameTGA(this);
		}
		if (key == ' ') {
			setup();
		}
	}

}