package examples.twister;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import amalgam.colour.QuickColours;
import amalgam.twister.Twister;

@SuppressWarnings("serial")
public class TaperedTwister_Example extends PApplet {
	PeasyCam cam;
	ArrayList<Twister> twisters;
	ColorList clrs;

	public void setup() {
		size(1280, 720, P3D);
		smooth(8);
		cam = new PeasyCam(this, 500);
		reset();
	}

	public void reset() {
		clrs = QuickColours.get(2000);
		twisters = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			twisters.add(new Twister(this, new Vec3D(0, 0, 0), 14, (int) random(3, 5), clrs));
		}
	}

	public void draw() {
		background(0);

		for (int i = 0; i < twisters.size(); i++) {
			Twister t = twisters.get(i);
			t.update();
			t.renderList(i);
		}
	}

	public void keyPressed() {
		switch (key) {
			case ' ' :
				reset();
				break;
		}
	}

}