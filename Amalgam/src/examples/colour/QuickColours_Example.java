package examples.colour;

import processing.core.PApplet;
import toxi.color.ColorList;
import amalgam.colour.ColourSet;
import amalgam.colour.QuickColours;

@SuppressWarnings("serial")
public class QuickColours_Example extends PApplet {

	ColorList colours;
	int _y = 0;

	public void setup() {
		size(1000, 500, P2D);
		smooth(8);
		noStroke();
		background(0);

		colours = QuickColours.get(100, ColourSet.BLUE_PINK, false, false);
		drawList();

		colours = QuickColours.get(100, ColourSet.BLUE_PINK, true, false);
		drawList();

		colours = QuickColours.get(100, ColourSet.BLUE_PINK, true, true);
		drawList();

		colours = QuickColours.getOriginal(ColourSet.BLUE_PINK);
		drawList();

		colours = QuickColours.generateRandom(10, 100, true);
		drawList();
	}

	public void draw() {

	}

	public void drawList() {
		for (int i = 0; i < colours.size(); i++) {
			int step = width / colours.size();

			fill(colours.get(i).toARGB());
			rect(i * step, _y, step, 100);
		}
		_y += 100;
	}

}
