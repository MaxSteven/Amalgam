package amalgam.twister;

import processing.core.PApplet;
import toxi.color.ColorList;

public class ConcreteRenderStrategy_Stroke implements RenderingStrategy {
	Spinner spinner;

	ColorList colours;
	PApplet p5;

	public ConcreteRenderStrategy_Stroke(Spinner spinner) {
		this.spinner = spinner;

		this.p5 = spinner.getp5();
		this.colours = new ColorList(spinner.getColours());
	}

	@Override
	public void render(int j, int lerps) {
		PApplet p5 = spinner.getp5();
		int tClr = colours.get((p5.frameCount + j + (int) spinner.getMainPhasor()) % colours.size()).saturate(0.4f * ((float) j / lerps)).toARGB();

		p5.noFill();
		p5.stroke(tClr, spinner.getAlpha());
	}

}