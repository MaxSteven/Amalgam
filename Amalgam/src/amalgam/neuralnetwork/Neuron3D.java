package amalgam.neuralnetwork;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Vec3D;
import amalgam.utils.ARnd;

public class Neuron3D {
	private final PApplet p5;

	private ArrayList<Arc3D> arcs;
	private float sum = 0;
	private float rBase, r;

	private int timesFired = 0;
	private int maxFirings = 3;

	public Vec3D pos, posOrig;

	private float alpha = 255;
	private TColor clr = TColor.WHITE.copy();

	public Neuron3D(PApplet p5, Vec3D pos) {
		this.p5 = p5;
		this.pos = pos.copy();
		this.posOrig = pos.copy();
		arcs = new ArrayList<>();

		this.r = 3; // hmm..?
		this.rBase = 3;
	}

	public void addArc(Arc3D c) {
		arcs.add(c);
	}

	public void feedforward(float input) {
		// Receive an input, accumulate & activate
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

		// move when fired
		pos.jitter(5f);

		for (Arc3D c : arcs) {
			// c.feedforward(sum);

			// forward prop only
			// if (c.b.pos.sub(c.a.pos).x > 0) {
			// c.feedforward(sum);
			// }

			// randomise firing to connected neurons
			if (ARnd.rn(0.4f)) {
				c.feedforward(sum);
			}
		}
	}
	public void render() {
		// update position
		pos.interpolateToSelf(posOrig, 0.15f);
		pos.z = pos.z + 10 * PApplet.sin(p5.frameCount / 50f + pos.x / 100f) * PApplet.cos(p5.frameCount / 50f + pos.y / 100f);

		p5.strokeWeight(r);
		p5.noFill();
		p5.stroke(clr.toARGB(), alpha);
		p5.point(pos.x, pos.y, pos.z);

		if (r > rBase) {
			r -= 0.2f;
		}
		// project();
	}

	private void project() {
		p5.strokeWeight(0.2f);
		p5.noFill();
		p5.stroke(255, 100);
		p5.line(pos.x, pos.y, pos.z, pos.x, pos.y, -500);
	}

	public void setPos(Vec3D pos) {
		this.pos = pos;
	}

	public void reset() {
		this.timesFired = 0;
	}

	public float getRadius() {
		return r;
	}

	public float getBaseRadius() {
		return rBase;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setColour(TColor clr) {
		this.clr = clr;
	}

}