package amalgam.toxi.physics2D;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.physics2d.VerletParticle2D;

/**
 * Basic Dot rendering
 */
public class RenderBasic implements RenderStrategy {

	private final PApplet p5;
	private MyVerletPhysics2D physics;
	private float size;

	private ColorList clrs;

	public RenderBasic(PApplet p5, MyVerletPhysics2D physics, float size) {
		this(p5, physics, size, null);
	}

	public RenderBasic(PApplet p5, MyVerletPhysics2D physics, float size, ColorList clrs) {
		this.p5 = p5;
		this.physics = physics;
		this.size = size;

		this.clrs = clrs;
	}

	@Override
	public void render() {
		p5.noStroke();
		for (VerletParticle2D p : physics.particles) {
			if (clrs == null) {
				p5.fill(255, physics.getAlpha());
			} else {
				// p5.fill(clrs.get((int) (100 * (p.getVelocity().x + p.getVelocity().y)) % clrs.size()).toARGB(), physics.getAlpha());
				p5.fill(clrs.get((int) (500 * (p.getVelocity().x + p.getVelocity().y)) % clrs.size()).toARGB(), physics.getAlpha());
			}

			// p5.rect(p.x, p.y, size, size);
			// p5.rect(p.x, p.y, size - p.getVelocity().magnitude(), size - p.getVelocity().magnitude());
//			p5.ellipse(p.x, p.y, PApplet.map(size + p.getVelocity().magnitude(), 0, 200, size, 20 * size), PApplet.map(size + p.getVelocity().magnitude(), 0, 200, size, 20 * size));
			p5.rect(p.x, p.y, PApplet.map(size + p.getVelocity().magnitude(), 0, 200, size, 20 * size), PApplet.map(size + p.getVelocity().magnitude(), 0, 200, size, 20 * size));
		}
	}
}
