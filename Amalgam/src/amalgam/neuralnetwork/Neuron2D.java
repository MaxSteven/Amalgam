package amalgam.neuralnetwork;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import amalgam.toxi.Triangle2D_Eq;
import amalgam.utils.ARnd;

public class Neuron2D {
	private final PApplet p5;

	private ArrayList<Arc2D> arcs;
	private float rBase, r = 5;
	private float sum = 0;
	private float triAlpha = 0;

	private int timesFired = 0; // Use this to set a max
	private int maxFirings = 2;

	// Iso grid specific list for holding bordering triangles (Should pull this behaviour into a subclass)
	private ArrayList<Triangle2D_Eq> tris;
	private ArrayList<Triangle2D_Eq> trisFiring;

	public Vec2D pos;

	public Neuron2D(PApplet p5, Vec2D pos) {
		this.p5 = p5;
		this.pos = pos;
		arcs = new ArrayList<>();
		tris = new ArrayList<>();
		trisFiring = new ArrayList<>();
	}

	public void addArc(Arc2D c) {
		arcs.add(c);
	}

	// Receive an input
	public void feedforward(float input) {
		// Accumulate & Activate(?)
		sum += input;
		if (sum > 1) {
			if (timesFired < maxFirings) {
				fire();
			}
			sum = 0; // Reset the sum to 0 if it fires
		}

		timesFired += 1;
	}

	public void fire() {
		r = 12;
		for (Arc2D c : arcs) {
			c.feedforward(sum);

			// forward prop only
			// if (c.b.pos.sub(c.a.pos).x > 0) {
			// c.feedforward(sum);
			// }
			// randomise firing to connected neurons
			// if (Rnd.rn(0.4f)) {
			// c.feedforward(sum);
			// }
		}

		// light up random adjacent triangle(s) to the neuron
		triAlpha = 255;
		trisFiring.clear();
		for (int i = 0; i < tris.size(); i++) {
			Triangle2D_Eq t = tris.get(i);
			if (ARnd.rn(0.5f)) {
				trisFiring.add(t);
			}
		}

	}

	public void render() {
		// ============================
		// Triangle rendering =========
		if (triAlpha > 0) {
			triAlpha -= 2;
		}

		// randomly selected adjacent tris
		for (int i = 0; i < trisFiring.size(); i++) {
			Triangle2D_Eq t = trisFiring.get(i);
			p5.fill(0xff79bb22, triAlpha);
			p5.noStroke();
			p5.triangle(t.a.x, t.a.y, t.b.x, t.b.y, t.c.x, t.c.y);
		}

		// all adjacent tris
		// for (int i = 0; i < tris.size(); i++) {
		// Triangle2D_Eq t = tris.get(i);
		// p5.fill(0xff79bb22, triAlpha);
		// p5.noStroke();
		// p5.triangle(t.a.x, t.a.y, t.b.x, t.b.y, t.c.x, t.c.y);
		// }
		// ============================
		// ============================

		p5.noStroke();
		p5.fill(255);
		p5.ellipse(pos.x, pos.y, r, r);

		if (r > rBase) {
			r -= 0.2f;
		}
	}

	public void setPos(Vec2D pos) {
		this.pos = pos;
	}

	public void addTriangle(Triangle2D_Eq t) {
		tris.add(t);
	}

	public void reset() {
		this.timesFired = 0;		
//		this.maxFirings = 0;
	}

}