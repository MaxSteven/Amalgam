package amalgam.toxi.physics3D;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;

/**
 * basic implementation (point)
 */
public class RenderLines3D implements RenderStrategy3D {

	private final PApplet p5;
	private VerletPhysics3D physics;
	private ColorList clrs;

	public RenderLines3D(PApplet p5, VerletPhysics3D physics, ColorList clrs) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
	}

	@Override
	public void render() {
		p5.noFill();
		// p5.strokeWeight(0.8f);
		p5.strokeWeight(1f);
		// p5.stroke(clrs.get(p5.frameCount % clrs.size()).toARGB());

		for (int i = 0; i < physics.particles.size(); i++) {
			VerletParticle3D p = physics.particles.get(i);
			p5.beginShape(PApplet.LINE_STRIP);
			if (p.track) {
				for (int j = 0; j < p.trail.size(); j++) {
					Vec3D v = p.trail.get(j);
					 p5.stroke(clrs.get((p5.frameCount + i + j) % clrs.size()).toARGB(), 150);
//					p5.stroke(clrs.get((int) ((v.x + v.y + v.z) / 1f) % clrs.size()).toARGB(), 255);
					p5.vertex(v.x, v.y, v.z);
				}
			}
			p5.endShape();
		}
	}
}
