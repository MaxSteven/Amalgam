package examples.colour;

import processing.core.PApplet;
import toxi.color.TColor;
import amalgam.colour.ColourMatrix;
import amalgam.colour.QuickColours;

@SuppressWarnings("serial")
public class ColourMatrix_Example extends PApplet {

	ColourMatrix m;

	public void setup() {
		size(1400, 1400, P2D);
		background(0);
	}

	public void draw() {
		noLoop();

		m = new ColourMatrix(this, QuickColours.get(5), QuickColours.get(5), width);
		TColor[][] mat = m.matrix;
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				TColor t = mat[i][j];
				set(i, j, t.toARGB());
			}
		}
	}

	public void mousePressed() {
		loop();
	}

}
