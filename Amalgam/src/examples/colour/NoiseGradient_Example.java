package examples.colour;

import processing.core.PApplet;
import toxi.color.ColorList;
import amalgam.colour.NoiseGradient;
import amalgam.colour.QuickColours;

@SuppressWarnings("serial")
public class NoiseGradient_Example extends PApplet {

	ColorList clrs, clrs2;

	public void setup() {
		size(1000, 500, P2D);
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
		ns.render(250);
	}

	public void mousePressed() {
		loop();
	}
}
