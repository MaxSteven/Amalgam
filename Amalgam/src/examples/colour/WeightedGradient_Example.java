package examples.colour;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;
import amalgam.colour.WeightedGradient;

@SuppressWarnings("serial")
public class WeightedGradient_Example extends PApplet {

	ColorList colours;
	boolean cycle = true; // create cyclic colour set for index incrementing
	int SIZE = 1000; // length of final gradient

	public void setup() {
		size(1280, 720, P2D);
		background(0);
		smooth(8);
		noStroke();
		ellipseMode(CENTER);

		colours = new ColorList();
		colours.add(TColor.newHex("fac13a"));
		colours.add(TColor.newHex("dc2338"));
		colours.add(TColor.newHex("124473"));
		colours.add(TColor.newHex("fdfefb"));
		colours.add(TColor.newHex("93caeb"));
		if (cycle) {
			colours.add(colours.get(0));
		}

		WeightedGradient grad = new WeightedGradient(colours);
		colours = grad.getGradient(SIZE);
	}

	public void draw() {
		fill(colours.get(frameCount % colours.size()).toARGB());
		rect(0, 0, width, height);

		fill(colours.get((2*frameCount) % colours.size()).toARGB());
		ellipse(width/2, height/2, 400, 400);
	}

}
