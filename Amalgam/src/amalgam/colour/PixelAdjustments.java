package amalgam.colour;

import processing.core.PApplet;

/**
 * Add B&Bs motion blur/chromatic aberration to this?
 */
public class PixelAdjustments {

	public static void fadeScreen(PApplet p5, int r, int g, int b, int fadeLevel) {
		int red, green, blue;
		p5.loadPixels();
		for (int i = 0; i < p5.pixels.length; i++) {
			red = (p5.pixels[i] >> 16) & 0x000000ff;
			green = (p5.pixels[i] >> 8) & 0x000000ff;
			blue = p5.pixels[i] & 0x000000ff;
			p5.pixels[i] = (((red + ((r - red) >> fadeLevel)) << 16) | ((green + ((g - green) >> fadeLevel)) << 8) | (blue + ((b - blue) >> fadeLevel)));
		}
		p5.updatePixels();
	}

	public static void motionBlur(PApplet p5, int samplesPerFrame, int numFrames, float shutterAngle) {
		int w = p5.width;
		int h = p5.height;

		int[][] result = new int[w * h][3];
		float t;

		for (int i = 0; i < w * h; i++)
			for (int a = 0; a < 3; a++)
				result[i][a] = 0;

		for (int sa = 0; sa < samplesPerFrame; sa++) {
			t = PApplet.map(p5.frameCount - 1 + sa * shutterAngle / samplesPerFrame, 0, numFrames, 0, 1);
			// draw_();
			p5.loadPixels();
			for (int i = 0; i < p5.pixels.length; i++) {
				result[i][0] += p5.pixels[i] >> 16 & 0xff;
				result[i][1] += p5.pixels[i] >> 8 & 0xff;
				result[i][2] += p5.pixels[i] & 0xff;
			}
		}

		p5.loadPixels();
		for (int i = 0; i < p5.pixels.length; i++)
			p5.pixels[i] = 0xff << 24 | (result[i][0] / samplesPerFrame) << 16 | (result[i][1] / samplesPerFrame) << 8 | (result[i][2] / samplesPerFrame);
		p5.updatePixels();

	}

}