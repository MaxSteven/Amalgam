package examples.lattice;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import amalgam.lattice.PolygonTiler;

@SuppressWarnings("serial")
public class PolygonTiling_Example extends PApplet {

	PolygonTiler tiler;

	public void setup() {
		size(1280, 720, P2D);
		background(0);

		tiler = PolygonTiler.quickTile(this, new Vec2D(width / 4f, height / 2f), 20, 1, 3);
		tiler.render();
		tiler.setCentre(new Vec2D(3 * width / 4f, height / 2f));
		tiler.renderDual();
	}

	public void draw() {
		noLoop();
	}

}
