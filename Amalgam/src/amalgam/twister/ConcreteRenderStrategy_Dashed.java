package amalgam.twister;

import processing.core.PApplet;
import toxi.color.ColorList;

/**
 * randomised on/off
 */
public class ConcreteRenderStrategy_Dashed implements RenderingStrategy {
	Spinner spinner;

	public ConcreteRenderStrategy_Dashed(Spinner spinner) {
		this.spinner = spinner;
	}

	@Override
	public void render(int j, int lerps) {
		PApplet p5 = spinner.getp5();
		ColorList colours = new ColorList(spinner.getColours());

		int clr = colours.get((p5.frameCount + j + (int) spinner.getMainPhasor()) % colours.size()).darken((0.05f * (float) j / lerps)).toARGB();

		int stopper = (int) p5.random(3);
		if (stopper == 1) {
			p5.fill(clr, spinner.getAlpha());
		} else {
			p5.noFill();
		}

		p5.noStroke();
	}

}
