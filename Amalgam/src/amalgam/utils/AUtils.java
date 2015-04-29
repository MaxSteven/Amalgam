package amalgam.utils;

import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;
import toxi.geom.Vec3D;
import controlP5.ControlP5;

/**
 * Useful snippets for 3D sketches
 */
public class AUtils {

	public static void drawAxes(PApplet p5) {
		p5.strokeWeight(1);
		p5.noFill();
		p5.stroke(255, 0, 0);
		p5.line(0, 0, 0, 500, 0, 0);
		p5.stroke(0, 255, 0);
		p5.line(0, 0, 0, 0, 500, 0);
		p5.stroke(0, 0, 255);
		p5.line(0, 0, 0, 0, 0, 500);

		p5.textSize(30);
		p5.noStroke();
		p5.fill(255, 0, 0);
		p5.text("X", 510, 0, 0);
		p5.fill(0, 255, 0);
		p5.text("Y", 0, 510, 0);
		p5.fill(0, 0, 255);
		p5.text("Z", 0, 0, 510);
	}

	public static void gui(PApplet p5, PeasyCam cam, ControlP5 cp5) {
		p5.hint(PApplet.DISABLE_DEPTH_TEST);
		cam.beginHUD();
		cp5.draw();
		cam.endHUD();
		p5.hint(PApplet.ENABLE_DEPTH_TEST);
	}

	public static Vec3D PV(PVector p) {
		return new Vec3D(p.x, p.y, p.z);
	}

	public static PVector VP(Vec3D p) {
		return new PVector(p.x, p.y, p.z);
	}

}
