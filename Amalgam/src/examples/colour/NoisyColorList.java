package examples.colour;

import processing.core.PApplet;
import toxi.color.ColorList;
import amalgam.colour.NoiseGradient;
import amalgam.colour.QuickColours;

@SuppressWarnings("serial")
public class NoisyColorList extends PApplet {

	ColorList clrs;
	ColorList clrs2;

	public void setup() {
		size(1280, 720, P2D);
		background(0);
		noStroke();
	}

	public void draw() {
		noLoop();
		background(0);

		noiseSeed((int) random(1000));

		clrs = QuickColours.get(10);

		NoiseGradient ns = new NoiseGradient(this, clrs);
		ns.getGradient(width, 50f);
		ns.render(0);
		ns.getGradientRand(width, 90);
		ns.render(42);
	}

	public void mousePressed() {
		loop();
	}
}
