package amalgam.colour;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;

/**
 * Used to create a gradient map effect by thresholding an input colour against a specified ColorList
 */
public class ColourThreshold {
	private final PApplet p5;
	private ColorList Colours;
	private TColor tclr;

	public ColourThreshold(PApplet p5, ColorList colours) {
		this.p5 = p5;
		Colours = colours;
	}

	public void createFill(int inputColour, float variation) {
		this.tclr = TColor.newARGB(inputColour);

		int n = Colours.size();
		// Initial bin
		if (tclr.luminance() >= 0 && tclr.luminance() < 1.0f / n) {
			TColor tempClr = new TColor(Colours.get(0));

			// tempClr.rotateRYB(PApplet.TWO_PI * variation);
			tempClr.rotateRYB(PApplet.TWO_PI * p5.random(-variation, variation));
			// tempClr.adjustRGB(variation, variation, variation);
			// tempClr.adjustRGB(p5.random(-variation, variation), p5.random(-variation, variation), p5.random(-variation, variation));
			p5.fill(tempClr.toARGB());
		}
		// All other bins
		for (int i = 1; i < n; i++) {
			if (tclr.luminance() >= (float) i / n && tclr.luminance() < (float) (i + 1) / n) {
				TColor tempClr = new TColor(Colours.get(i));

				// tempClr.rotateRYB(PApplet.TWO_PI * variation);
				tempClr.rotateRYB(PApplet.TWO_PI * p5.random(-variation, variation));
				// tempClr.adjustRGB(variation, variation, variation);
				// tempClr.adjustRGB(p5.random(-variation, variation), p5.random(-variation, variation), p5.random(-variation, variation));
				p5.fill(tempClr.toARGB());
			}
		}
	}

	public void createFillStroke(int inputColour, float strokeWeight, float variation) {
		this.tclr = TColor.newARGB(inputColour);
		p5.strokeWeight(strokeWeight);
		// p5.strokeJoin(PApplet.ROUND);
		// p5.strokeCap(PApplet.ROUND);

		int n = Colours.size();
		// Initial bin
		if (tclr.luminance() >= 0 && tclr.luminance() < 1.0f / n) {
			TColor tempClr = new TColor(Colours.get(0));

			// tempClr.rotateRYB(PApplet.TWO_PI * variation);
			tempClr.rotateRYB(PApplet.TWO_PI * p5.random(-variation, variation));
			// tempClr.adjustRGB(variation, variation, variation);
			// tempClr.adjustRGB(p5.random(-variation, variation), p5.random(-variation, variation), p5.random(-variation, variation));
			p5.fill(tempClr.toARGB());
			p5.stroke(tempClr.toARGB());
		}
		// All other bins
		for (int i = 1; i < n; i++) {
			if (tclr.luminance() >= (float) i / n && tclr.luminance() < (float) (i + 1) / n) {
				TColor tempClr = new TColor(Colours.get(i));

				// tempClr.rotateRYB(PApplet.TWO_PI * variation);
				tempClr.rotateRYB(PApplet.TWO_PI * p5.random(-variation, variation));
				// tempClr.adjustRGB(variation, variation, variation);
				// tempClr.adjustRGB(p5.random(-variation, variation), p5.random(-variation, variation), p5.random(-variation, variation));
				p5.fill(tempClr.toARGB());
				p5.stroke(tempClr.toARGB());
			}
		}
	}

}