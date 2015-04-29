package examples.toxi;

import peasy.PeasyCam;
import processing.core.PApplet;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class ToxiTest extends PApplet {
	PeasyCam cam;
	ControlP5 cp5;

	public void setup() {
		size(1280, 720, P3D);

		cam = new PeasyCam(this, 500);
		cp5 = new ControlP5(this);
		cp5.addFrameRate();
		cp5.setAutoDraw(false);
	}

	public void draw() {
		background(0);

		gui();
	}

	public void gui() {
		hint(DISABLE_DEPTH_TEST);
		cam.beginHUD();
		cp5.draw();
		cam.endHUD();
		hint(ENABLE_DEPTH_TEST);
	}

}