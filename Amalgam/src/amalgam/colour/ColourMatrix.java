package amalgam.colour;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;

/**
 * n x m grid of colours to choose from! (Doubly inteprolated ColorList)
 */
public class ColourMatrix {
	ColorList a, b;
	WeightedGradient aa, bb;
	int width;

	public TColor[][] matrix;
	ArrayList<ColorList> list; // hmm?

	public ColourMatrix(ColorList a, ColorList b, int width) {
		this.a = a;
		this.b = b;
		this.width = width;

		this.matrix = new TColor[width][width];

		aa = new WeightedGradient(a);
		ColorList ga = aa.getGradient(width);
		aa = new WeightedGradient(b);
		ColorList gb = aa.getGradient(width);

		for (int i = 0; i < ga.size(); i++) {
			TColor clrA = ga.get(i);
			for (int j = 0; j < gb.size(); j++) {
				TColor clrB = gb.get(j);
				matrix[i][j] = clrA.getBlended(clrB, 0.5f);
				// matrix[i][j] = clrA.getBlended(clrB, (float) i / j);
			}
		}
	}

	public ColourMatrix(PApplet p5, ColorList a, ColorList b, int width) {
		this.a = a;
		this.b = b;
		this.width = width;

		this.matrix = new TColor[width][width];

		aa = new WeightedGradient(a);
		ColorList ga = aa.getGradient(width);
		aa = new WeightedGradient(b);
		ColorList gb = aa.getGradient(width);

		for (int i = 0; i < ga.size(); i++) {
			TColor clrA = ga.get(i);
			for (int j = 0; j < gb.size(); j++) {
				TColor clrB = gb.get(j);
				matrix[i][j] = clrA.getBlended(clrB, 0.5f);
				// matrix[i][j] = clrA.getBlended(clrB, (float)j/gb.size());
				// matrix[i][j] = clrA.getBlended(clrB, (float)Math.random());
				// matrix[i][j] = clrA.getBlended(clrB, p5.noise(i / 400f, j / 400f));
				// matrix[i][j] = clrA.getBlended(clrB, 0.5f*(1+p5.sin((i+j) / 300f)));
				// matrix[i][j] = clrA.getBlended(clrB, (float) i / j);
				// matrix[i][j] = clrA.getBlended(clrB, (float) i % j);
				// matrix[i][j] = clrA.getBlended(clrB, (p5.log((float) i / j) ));
			}
		}
	}
	public TColor[][] create() {
		// TODO
		return null;
	}
}
