package examples.chaos;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import amalgam.chaos.Attractor;
import amalgam.chaos.HadleyAttractor;
import amalgam.chaos.HalvorsenAttractor;
import amalgam.chaos.ModifiedLorenzAttractor;
import amalgam.chaos.SprottLinzG_Attractor;
import amalgam.chaos.SymmetricFlowAttractor;
import amalgam.colour.QuickColours;

@SuppressWarnings("serial")
public class Attractor_Example extends PApplet {
	Attractor att;
	ColorList colours;
	PeasyCam cam;

	int MODE = 0;
	int ATT = 0;

	public void setup() {
		size(1280, 720, P3D);
		smooth(8);
		cam = new PeasyCam(this, 50);
		colours = QuickColours.get(500);
		reset();
	}

	public void reset() {

		switch (ATT) {
			case 0 :
				att = new SprottLinzG_Attractor(this);
				break;
			case 1 :
				att = new ModifiedLorenzAttractor(this, new Vec3D(0, 1, 0), 50);
				break;
			case 2 :
				att = new HadleyAttractor(this);
				break;
			case 3 :
				att = new SymmetricFlowAttractor(this, new Vec3D(1, 1, 0), 50);
				ArrayList<Float> params = new ArrayList<>();
				params.add((float) 1.522f);
				att.setParams(params);
				break;
			case 4 :
				att = new HalvorsenAttractor(this, new Vec3D(-5, 0, 0), 50);
				break;
		}
	}

	public void draw() {
		background(0);
		perspective(PI / 3, (float) width / height, 2, 10000);

		for (int i = 0; i < 50; i++) {
			att.update(0.003);
		}

		switch (MODE) {
			case 0 :
				att.draw();
				break;
			case 1 :
				att.draw(colours);
				break;
			case 2 :
				att.drawQuads(colours, 1.05f);
				break;
		}
	}

	public void keyPressed() {
		switch (key) {
			case '1' :
				MODE = (MODE + 1) % 3;
				break;
			case ' ' :
				ATT = (ATT + 1) % 5;
				reset();
				break;
		}
	}
}