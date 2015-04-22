package amalgam.toxi.physics2D;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

/**
 * Connects a pair of sequential particles with their previous positions & each other to form a quad. Opacity is controlled by the distance between the 2 particles Includes frameCount based increment of colour.
 */
public class RenderQuadMesh implements RenderStrategy {

	private final PApplet p5;
	private MyVerletPhysics2D physics;
	private ColorList clrs;

	private float threshold;

	public RenderQuadMesh(PApplet p5, MyVerletPhysics2D physics, ColorList clrs, float threshold) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
		this.threshold = threshold;
	}

	@Override
	public void render() {
		p5.noFill();

		ArrayList<VerletParticle2D> pts = physics.particles;
		for (int i = 0; i < pts.size() - 1; i++) {
			VerletParticle2D p = pts.get(i);
			VerletParticle2D p2 = pts.get(i + 1);
			Vec2D _p = p.getPreviousPosition();
			Vec2D _p2 = p2.getPreviousPosition();

			float diff = p.sub(p2).magnitude();
			float alphaNew = (threshold - diff) / threshold;

			int n = 4;
			if (diff < threshold) {
				for (int j = 0; j < n; j++) {
					p5.stroke(clrs.get((i + j + 5 * p5.frameCount) % clrs.size()).toARGB(), alphaNew * physics.getAlpha());
					Vec2D a = p.interpolateTo(p2, (float) j / n);
					Vec2D a2 = p.interpolateTo(p2, (float) (j + 1) / n);
					Vec2D b = _p.interpolateTo(_p2, (float) j / n);
					Vec2D b2 = _p.interpolateTo(_p2, (float) (j + 1) / n);

					p5.beginShape();
					p5.vertex(b.x, b.y);
					p5.vertex(b2.x, b2.y);
					p5.vertex(a2.x, a2.y);
					p5.vertex(a.x, a.y);
					p5.endShape();
				}
			}

		}
	}
}
