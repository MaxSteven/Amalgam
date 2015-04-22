package amalgam.toxi.physics2D;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.physics2d.VerletParticle2D;

/**
 * Connects a pair of sequential particles with their previous positions & each other to form a quad. Opacity is controlled by the distance between the 2 particles Includes frameCount based increment of colour.
 */
public class RenderSequentialQuads implements RenderStrategy {

	private final PApplet p5;
	private MyVerletPhysics2D physics;
	private ColorList clrs;

	private float threshold;

	public RenderSequentialQuads(PApplet p5, MyVerletPhysics2D physics, ColorList clrs, float threshold) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
		this.threshold = threshold;
	}

	@Override
	public void render() {
		p5.noStroke();

		ArrayList<VerletParticle2D> pts = physics.particles;
		p5.beginShape(PApplet.QUADS);
		for (int i = 0; i < pts.size() - 1; i++) {
			VerletParticle2D p = pts.get(i);
			VerletParticle2D p2 = pts.get(i + 1);

			float diff = p.sub(p2).magnitude();
			float alphaNew = (threshold - diff) / threshold;

//			p5.fill(clrs.get((i + 5 * p5.frameCount) % clrs.size()).toARGB(), alphaNew * physics.getAlpha());
//			p5.fill(clrs.get((4*i + 3 * p5.frameCount) % clrs.size()).toARGB(), alphaNew * physics.getAlpha());
//			p5.fill(clrs.get((i*i/2 + 3 * p5.frameCount) % clrs.size()).toARGB(), alphaNew * physics.getAlpha());
			p5.fill(clrs.get((i*5 + 3 * p5.frameCount) % clrs.size()).toARGB(), alphaNew * physics.getAlpha());

			if (diff < threshold) {
				if (p.getPreviousPosition() != null && p2.getPreviousPosition() != null) {
					p5.vertex(p.x, p.y);
					p5.vertex(p.getPreviousPosition().x, p.getPreviousPosition().y);
					p5.vertex(p2.getPreviousPosition().x, p2.getPreviousPosition().y);
					p5.vertex(p2.x, p2.y);
				}
			}
		}
		p5.endShape();
	}
}
