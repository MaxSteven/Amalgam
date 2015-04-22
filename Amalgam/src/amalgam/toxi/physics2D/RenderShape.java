package amalgam.toxi.physics2D;

import geomerative.RShape;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;

/**
 * TODO - basically just scatter port impl w/ RShape?
 */
public class RenderShape implements RenderStrategy {

	private final PApplet p5;
	private VerletPhysics2D physics;
	private ColorList clrs;
	private ArrayList<RShape> shapes;

	public RenderShape(PApplet p5, VerletPhysics2D physics, ColorList clrs, ArrayList<RShape> images) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
		this.shapes = images;
	}

	@Override
	public void render() {
		// TODO
		ArrayList<VerletParticle2D> pts = physics.particles;
		for (int i = 0; i < pts.size() - 1; i++) {
			VerletParticle2D p = pts.get(i);

			p5.pushMatrix();
			p5.translate(p.x, p.y);
			p5.rotate(p5.frameCount * p.getRotation());

			p5.popMatrix();
		}
	}
}