package amalgam.toxi.physics2D;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Triangle2D;
import toxi.physics2d.VerletParticle2D;

/**
 * Connects 3 sequantial particles to form a triangle. Opacity is based on triangle circumference in relation to the given threshold. includes frameCount based increment of colour.
 */
public class RenderTriangles implements RenderStrategy {

	private final PApplet p5;
	private MyVerletPhysics2D physics;
	private ColorList clrs;
	private float thresh;

	public RenderTriangles(PApplet p5, MyVerletPhysics2D physics, ColorList clrs, float thresh) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
		this.thresh = thresh;
	}

	@Override
	public void render() {
		p5.noStroke();

		ArrayList<VerletParticle2D> pts = physics.particles;
		for (int i = 0; i < pts.size() - 2; i++) {
			VerletParticle2D p = pts.get(i);
			VerletParticle2D p2 = pts.get(i + 1);
			VerletParticle2D p3 = pts.get(i + 2);

			Triangle2D tri = new Triangle2D(p, p2, p3);
			float circ = tri.getCircumference();
			float alphaNew = (thresh - circ) / thresh;

			p5.fill(clrs.get( (int) (5*p5.frameCount +5*i) % clrs.size()).toARGB(), physics.getAlpha() * alphaNew);
			if (circ < thresh) {
				p5.beginShape();
				p5.vertex(p.x, p.y);
				p5.vertex(p2.x, p2.y);
				p5.vertex(p3.x, p3.y);
				p5.endShape(PApplet.CLOSE);
			}
		}
	}
}
