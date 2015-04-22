package amalgam.colour;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;

/**
 * n x m grid of colours to choose from! (Doubly inteprolated ColorList)
 */
public class ColourMatrix3D {
	ColorList a, b, c;
	WeightedGradient aa, bb, cc;
	public int width;

	public TColor[][][] matrix;
	ArrayList<ColorList> list; // hmm?

	public ColourMatrix3D(ColorList a, ColorList b, ColorList c, int width) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.width = width;

		this.matrix = new TColor[width][width][width];

		aa = new WeightedGradient(a);
		ColorList ga = aa.getGradient(width);
		bb = new WeightedGradient(b);
		ColorList gb = bb.getGradient(width);
		cc = new WeightedGradient(c);
		ColorList gc = cc.getGradient(width);

		for (int i = 0; i < ga.size(); i++) {
			TColor clrA = ga.get(i);
			for (int j = 0; j < gb.size(); j++) {
				TColor clrB = gb.get(j);
				for (int k = 0; k < gc.size(); k++) {
					TColor clrC = gb.get(k);
					matrix[i][j][k] = clrA.getBlended(clrB, 0.5f).getBlended(clrC, 0.5f);
					// matrix[i][j] = clrA.getBlended(clrB, (float) i / j);
				}
			}
		}
	}

	public ColourMatrix3D(PApplet p5, ColorList a, ColorList b, ColorList c, int width) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.width = width;

		this.matrix = new TColor[width][width][width];

		aa = new WeightedGradient(a);
		ColorList ga = aa.getGradient(width);
		bb = new WeightedGradient(b);
		ColorList gb = bb.getGradient(width);
		cc = new WeightedGradient(c);
		ColorList gc = cc.getGradient(width);

		for (int i = 0; i < ga.size(); i++) {
			TColor clrA = ga.get(i);
			for (int j = 0; j < gb.size(); j++) {
				TColor clrB = gb.get(j);
				for (int k = 0; k < gc.size(); k++) {
					TColor clrC = gb.get(k);
					matrix[i][j][k] = clrA.getBlended(clrB, 0.5f).getBlended(clrC, 0.5f);
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
	}

	public TColor[][] create() {
		// TODO
		return null;
	}
}
