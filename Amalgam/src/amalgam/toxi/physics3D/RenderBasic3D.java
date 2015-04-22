package amalgam.toxi.physics3D;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;

/**
 * basic implementation (point)
 */
public class RenderBasic3D implements RenderStrategy3D {

	private final PApplet p5;
	private VerletPhysics3D physics;

	public RenderBasic3D(PApplet p5, VerletPhysics3D physics) {
		this.p5 = p5;
		this.physics = physics;
	}

	@Override
	public void render() {
		p5.strokeWeight(2);
//		p5.stroke(255, 100, 100, 145);
		p5.stroke(255);
		p5.beginShape(PApplet.POINTS);
		for (VerletParticle3D p : physics.particles) {
			if (p.track) {
				for (int i = 0; i < p.trail.size(); i++) {
					Vec3D v = p.trail.get(i);
					p5.vertex(v.x, v.y, v.z);
				}
			} else {
				p5.vertex(p.x, p.y, p.z);
			}
		}
		p5.endShape(PApplet.CLOSE);
	}
}
