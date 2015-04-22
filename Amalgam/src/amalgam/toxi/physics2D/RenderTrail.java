package amalgam.toxi.physics2D;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.physics2d.VerletParticle2D;

/**
 * basic implementation (white fill only)
 */
public class RenderTrail implements RenderStrategy {

	private final PApplet p5;
	private MyVerletPhysics2D physics;
	private ColorList clrs;

	public RenderTrail(PApplet p5, MyVerletPhysics2D physics, ColorList clrs) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
	}

	@Override
	public void render() {
		p5.noFill();

		ArrayList<VerletParticle2D> pts = physics.particles;
		p5.beginShape(PApplet.LINES);
		for (int i = 0; i < pts.size() - 1; i++) {
			VerletParticle2D p = pts.get(i);
			p5.stroke(clrs.get((i + p5.frameCount * 5) % clrs.size()).toARGB(), physics.getAlpha());
			p5.vertex(p.x, p.y);
			p5.vertex(p.getPreviousPosition().x, p.getPreviousPosition().y);
		}
		p5.endShape();
	}
}
