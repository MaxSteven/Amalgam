package amalgam.colour;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;

public class NoiseGradient {

	private PApplet p5;
	private ColorList clrs;
	private ColorList finalGrad;

	public NoiseGradient(PApplet p5, ColorList clrs) {
		this.p5 = p5;
		this.clrs = clrs;
	}

	// Noisy gradient
	public ColorList getGradient(int totalColours, float scale) {
		ColorList finalGrad = new ColorList();
		for (int i = 0; i < totalColours; i++) {
			finalGrad.add(clrs.get((int) (clrs.size() * p5.noise((float) i / scale))));
		}
		this.finalGrad = finalGrad;
		return finalGrad;
	}

	// Random segment gradients
	public ColorList getGradientRand(int totalColours, float maxSegment) {
		ColorList finalGrad = new ColorList();

		int ind = 0;
		while (ind < totalColours) {
			int nxt = (int) (maxSegment * p5.random(1));
			ind += nxt;
			TColor c = clrs.get((int) p5.random(clrs.size()));
			for (int i = 0; i < nxt; i++) {
				finalGrad.add(c);
			}
		}
		// this won't give the exact length - could hack it here

		this.finalGrad = finalGrad;
		return finalGrad;
	}

	public void render(int y) {
		if (finalGrad != null) {
			for (int i = 0; i < finalGrad.size(); i++) {
				TColor c = finalGrad.get(i);
				p5.noStroke();
				p5.fill(c.toARGB());
				p5.rect(i, y, 1, 20);
			}
		} else {
			System.out.println("No gradient to render");
		}
	}
}
