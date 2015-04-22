package amalgam.toxi.physics3D;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;
import amalgam.audio.SoundData;

/**
 * basic implementation (point)
 */
public class RenderTaperedPolys implements RenderStrategy3D {

	private final PApplet p5;
	private VerletPhysics3D physics;
	private ColorList clrs;

	// Minim beat stuff
	float b = 1;
	SoundData sData;

	int sinceKick = 0;

	public RenderTaperedPolys(PApplet p5, VerletPhysics3D physics, ColorList clrs) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
	}

	@Override
	public void render() {
		// Minim beatDetection data
		if (physics instanceof MyVerletPhysics3D) {
			MyVerletPhysics3D phy = (MyVerletPhysics3D) physics;
			this.sData = phy.sData;
		}

		for (int j = 0; j < physics.particles.size(); j++) {
			VerletParticle3D p = physics.particles.get(j);
			p5.beginShape(PApplet.QUADS);
			if (p.track) {
				for (int i = 0; i < p.trail.size() - 1; i++) {
					Vec3D v = p.trail.get(i);
					Vec3D vv = p.trail.get(i + 1);

					float r = 1 - PApplet.abs(i - (p.trail.size() / 2f)) / (p.trail.size() / 2f);
					float r2 = 1 - PApplet.abs((i + 1) - (p.trail.size() / 2f)) / (p.trail.size() / 2f);
//					float width = 0.02f + sData.totalEnergy / (sData.energyScaling * 20f);
					float width = 0.04f + sData.totalEnergy / (sData.energyScaling * 20f);

					// weird rail
					Vec3D diff = p.cross(p.add(new Vec3D(0, 0, 1))).normalize();

					if (sData.snare == true) {
						p5.fill(clrs.get((int) (i + p.seed) % clrs.size()).copy().lighten(1f).toARGB(), 255);
						sinceKick = 0;
					} else {
						sinceKick++;
//						p5.fill(clrs.get((int) (i + p.seed) % clrs.size()).copy().lighten(PApplet.map(PApplet.constrain(sinceKick, 0, 10), 0, 10, 1f, 0)).toARGB(), 255);
						p5.fill(clrs.get((int) (i + p.seed) % clrs.size()).copy().lighten(PApplet.map(PApplet.constrain(sinceKick, 0, 10), 0, 10, 2f, 0)).saturate(PApplet.map(PApplet.constrain(sinceKick, 0, 10), 0, 10, 2f, 0)).toARGB(), 255);
					}

					// Railed splines
					// float width2 = 10;
					// p5.noStroke();
					// p5.vertex(v.add(diff.scale(width2 * r)).x, v.add(diff.scale(width2 * r)).y, v.add(diff.scale(width2 * r)).z);
					// p5.vertex(vv.add(diff.scale(width2 * r2)).x, vv.add(diff.scale(width2 * r2)).y, vv.add(diff.scale(width2 * r2)).z);
					// p5.vertex(vv.add(diff.scale(-width2 * r2)).x, vv.add(diff.scale(-width2 * r2)).y, vv.add(diff.scale(-width2 * r2)).z);
					// p5.vertex(v.add(diff.scale(-width2 * r)).x, v.add(diff.scale(-width2 * r)).y, v.add(diff.scale(-width2 * r)).z);

					p5.noStroke();
					p5.vertex(v.scale(1 + width * r).x, v.scale(1 + width * r).y, v.scale(1 + width * r).z);
					p5.vertex(vv.scale(1 + width * r2).x, vv.scale(1 + width * r2).y, vv.scale(1 + width * r2).z);
					p5.vertex(vv.scale(1 - width * r2).x, vv.scale(1 - width * r2).y, vv.scale(1 - width * r2).z);
					p5.vertex(v.scale(1 - width * r).x, v.scale(1 - width * r).y, v.scale(1 - width * r).z);
				}
			}
			p5.endShape(PConstants.CLOSE);
		}
	}
}
