package amalgam.toxi.physics2D;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;

public class MyVerletPhysics2D extends VerletPhysics2D {

	public MyVerletPhysics2D(PApplet p5) {
		this(p5, null, 50, 0, 1);
	}

	public MyVerletPhysics2D(PApplet p5, Vec2D gravity, int numIterations, float drag, float timeStep) {
		super(gravity, numIterations, drag, timeStep);
		this.p5 = p5;
		setRenderingStrategy(new RenderBasic(p5, this, 1));
	}	
	
	/**
	 * Updates all particle positions
	 */
	protected void updateParticles() {
		for (ParticleBehavior2D b : behaviors) {
			b.update();
			if (index != null && b.supportsSpatialIndex()) {
				b.applyWithIndex(index);
			} else {
				for (VerletParticle2D p : particles) {
					b.apply(p);
				}
			}
		}
		for (VerletParticle2D p : particles) {
			p.scaleVelocity(drag);
			p.update();
		}

		// Polygons - MY ADDITION
		if (polys != null) {
			int tracker = 0;
			for (int i = 0; i < polys.size(); i++) {
				Polygon2D p = polys.get(i);
				int N = p.vertices.size();
				for (int j = 0; j < N; j++) {
					p.vertices.get(j).set(particles.get(tracker));
					tracker += 1;
				}
			}
		}
	}	
	
	// ///////////////////////////////////////////////
	// MY ADDITIONS
	// ///////////////////////////////////////////////
	public PApplet p5;

	public RenderStrategy strat;
	public float alpha = 255;
	
	public void setPApplet(PApplet p5){
		this.p5 = p5;
		this.strat = new RenderBasic(p5, this, 2);		
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	// Polygon attempt..
	public ArrayList<Polygon2D> polys;

	public void addPolygon(Polygon2D p) {
		if (polys == null) {
			polys = new ArrayList<Polygon2D>();
		}
		polys.add(p);
		for (Vec2D v : p) {
			addParticle(new VerletParticle2D(v));
		}
	}

	public void setRenderingStrategy(RenderStrategy strat) {
		this.strat = strat;
	}

	public void render() {
		if (strat == null) {
			throw new NullPointerException("No RenderStrategy defined for the physics system");
		} else {
			strat.render();
		}
	}

	public void removeBehaviors() {
		behaviors.clear();
	}

	public PApplet getp5() {
		return p5;
	}	
	
}
