package amalgam.neuralnetwork;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import toxi.color.TColor;

/**
 * Connects 2 Neuron3D objects
 * 
 * If a neuron fires before the signal has finished traversing the connection, the sender disappears in favour of the new signal. Fix this with a separately acting pulsar?
 */
public class Arc3D {
	private final PApplet p5;

	private ArrayList<Signal3D> signals;
	private boolean sending = false;

	// Store the output for when its time to pass along
	private float output = 0;
	private float fireAlpha = 0;

	public Neuron3D a, b;
	public float w;
	public int sigFrames;
	public int sendingFrame = 0;

	private float alpha = 255;
	private TColor clr = TColor.WHITE.copy();

	public Arc3D(PApplet p5, Neuron3D a, Neuron3D b, float w) {
		this.p5 = p5;
		this.a = a;
		this.b = b;
		this.w = w;

		signals = new ArrayList<>();
		this.sigFrames = 40;
	}

	// Connection active
	public void feedforward(float val) {
		output = val * w;
		sending = true;
		fireAlpha = alpha;

		signals.add(new Signal3D(p5, this, sigFrames));
	}

	public void update() {
		// Remove 'completed' signals.
		Iterator<Signal3D> it = signals.iterator();
		while (it.hasNext()) {
			Signal3D s = (Signal3D) it.next();
			s.update();
			// if (s.pos.distanceTo(s.target) <= 1.5f) {
			if (s.age >= s.life) {
				it.remove();
			}
		}

		if (sending) {
			if (sendingFrame >= sigFrames) {
				b.feedforward(output);
				sendingFrame = 0;
				sending = false;
			}
			sendingFrame++;
		}
	}

	public void render() {
		p5.strokeWeight(1);

		p5.noFill();
		p5.stroke(clr.toARGB(), alpha);
		p5.line(a.pos.x, a.pos.y, a.pos.z, b.pos.x, b.pos.y, b.pos.z);

		// Line glows when firing
		if (fireAlpha > 0) {
			fireAlpha -= alpha / sigFrames;
		}
		p5.stroke(255, fireAlpha);
		p5.line(a.pos.x, a.pos.y, a.pos.z, b.pos.x, b.pos.y, b.pos.z);

		for (Signal3D s : signals) {
			s.render();
		}
	}

	public ArrayList<Signal3D> getSignals() {
		return signals;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setColour(TColor clr) {
		this.clr = clr;
	}
}