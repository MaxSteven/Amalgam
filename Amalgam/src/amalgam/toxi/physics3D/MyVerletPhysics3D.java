package amalgam.toxi.physics3D;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;
import amalgam.audio.SoundData;

public class MyVerletPhysics3D extends VerletPhysics3D {
	private final PApplet p5;

	public MyVerletPhysics3D(PApplet p5) {
		super(null, 50, 0, 1);
		this.p5 = p5;
	}

	public MyVerletPhysics3D(PApplet p5, Vec3D gravity, int numIterations, float drag, float timeStep) {
		super(gravity, numIterations, drag, timeStep);
		this.p5 = p5;
	}
	
	public VerletPhysics3D addParticle(VerletParticle3D p) {
		if (track) {
			p.track(maxTrack);
		}
		particles.add(p);
		return this;
	}

	protected void updateParticles() {
		for (ParticleBehavior3D b : behaviors) {
			b.update();
			for (VerletParticle3D p : particles) {
				b.apply(p);
			}
		}
		for (VerletParticle3D p : particles) {
			p.scaleVelocity(drag);
			p.update();
		}

		// remove inactive (dead) particles - this slows things down a lot? Only do it every few frames or is this dumb?
		// if (p5.frameCount % 12 == 0) {
		// Iterator<VerletParticle3D> pit = particles.iterator();
		// while (pit.hasNext()) {
		// VerletParticle3D p = (VerletParticle3D) pit.next();
		// if (p instanceof VerletParticle3D) {
		// VerletParticle3D pp = (VerletParticle3D) p;
		// if (pp.getAge() > pp.getLife()) {
		// pit.remove();
		// }
		// }
		// }
		// }
	}

	private RenderStrategy3D strat;
	private float alpha;

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setRenderingStrategy(RenderStrategy3D strat) {
		this.strat = strat;
	}
	
	public boolean track = false;
	public int maxTrack = 0;

	public void track(int max) {
		track = true;
		maxTrack = max;
		for (VerletParticle3D p : particles) {
			p.track(max);
		}
	}

	public void render() {
		if (strat == null) {
			setRenderingStrategy(new RenderBasic3D(p5, this));
		}
		strat.render();

		// TColor cl = TColor.newHex("EE1E3F");
		// p5.stroke(255, 0, 0);
		// p5.stroke(cl.toARGB());
		//
		// p5.strokeWeight(0.5f);
		// for (VerletParticle3D p : particles) {
		// p5.beginShape(PApplet.LINE_STRIP);
		// if (p.track) {
		// for (int i = 0; i < p.positions.size(); i++) {
		// Vec3D v = p.positions.get(i);
		// p5.vertex(v.x, v.y, v.z);
		// }
		// }
		// p5.endShape();
		// }

		// p5.beginShape(PApplet.POINTS);
		// for (VerletParticle3D p : particles) {
		// p5.vertex(p.x, p.y, p.z);
		// }
		// p5.endShape();
	}

	// Minim stuff

	public SoundData sData;
	public void setSoundData(SoundData s) {
		this.sData = s;
	}

}
