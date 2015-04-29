package amalgam.twister;

import processing.core.PApplet;

/**
 * Checkerboard pattern
 */
public class ConcreteRenderStrategy_Checkerboard implements RenderingStrategy {
	Spinner spinner;
	PApplet p5;

	public ConcreteRenderStrategy_Checkerboard(Spinner spinner) {
		this.spinner = spinner;
		this.p5 = spinner.getp5();
	}

	@Override
	public void render(int j, int lerps) {
		if ((j + (int) p5.random(4) + p5.frameCount) % 2 == 0) {
			p5.fill(10 * j, spinner.getAlpha());
		} else {
			p5.fill(255 - 10 * (lerps - j), spinner.getAlpha());
		}

		p5.noStroke();
	}

}
