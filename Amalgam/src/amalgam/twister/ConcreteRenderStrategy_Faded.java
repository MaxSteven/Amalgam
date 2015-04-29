package amalgam.twister;

import processing.core.PApplet;
import toxi.color.ColorList;

/**
 * Faded edges
 */
public class ConcreteRenderStrategy_Faded implements RenderingStrategy {
	Spinner spinner;

	ColorList colours;
	PApplet p5;

	public ConcreteRenderStrategy_Faded(Spinner spinner) {
		this.spinner = spinner;

		this.p5 = spinner.getp5();
		this.colours = new ColorList(spinner.getColours());
	}

	@Override
	public void render(int j, int lerps) {
		float diff = 1 - PApplet.abs((float) (lerps / 2 - j)) / (lerps / 2);
		// centre at 0% opacity:
		// float diff = PApplet.abs((float) (lerps / 2 - j)) / (lerps / 2);

		float opac = spinner.getAlpha() * diff;
		// int tClr = colours.get((p5.frameCount + j + spinner.getClrIndex()) % colours.size()).saturate(0.2f * ((float) j / lerps)).toARGB();
		// int tClr = colours.get((5*(p5.frameCount + j) + spinner.getClrIndex()) % colours.size()).toARGB();
		int tClr = colours.get((2 * p5.frameCount + j + spinner.getClrIndex()) % colours.size()).saturate(diff * 0.2f).lighten(0.8f * (float) j / lerps).toARGB();
		// int tClr = colours.get((2 * p5.frameCount + j + spinner.getClrIndex()) % colours.size()).toARGB();
		// int tClr = colours.get((2 * p5.frameCount + j + spinner.getClrIndex()) % colours.size()).analog(0.005f, 0.04f).toARGB();

		p5.noStroke();
		p5.fill(tClr, opac);
	}

	public void render(int j, int lerps, float mag) {
		float diff = 1 - PApplet.abs((float) (lerps / 2 - j)) / (lerps / 2);
		// centre at 0% opacity:
		// diff = PApplet.abs((float) (lerps / 2 - j)) / (lerps / 2);

		float opac = spinner.getAlpha() * diff * (1 - mag / 300f);
		// int tClr = colours.get((p5.frameCount + j + spinner.getClrIndex()) % colours.size()).saturate(0.2f * ((float) j / lerps)).toARGB();
		// int tClr = colours.get((2 * p5.frameCount + j + spinner.getClrIndex()) % colours.size()).saturate(diff / 2f).toARGB();
		int tClr = colours.get(((5 * p5.frameCount + j) + spinner.getClrIndex()) % colours.size()).toARGB();
		// int tClr = colours.get((2 * p5.frameCount + j + spinner.getClrIndex()) % colours.size()).analog(0.005f, 0.04f).toARGB();

		p5.noStroke();
		p5.fill(tClr, opac);
	}
}
