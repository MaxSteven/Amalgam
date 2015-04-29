package amalgam.neuralnetwork;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;

/**
 * If a neuron fires before the signal has finished traversing the connection, the sender disappears in favour of the new signal. Fix this with a separately acting pulsar?
 */
public class Arc2D {
	private final PApplet p5;

	private ArrayList<Signal2D> signals;
	private boolean sending = false;

	// Store the output for when its time to pass along
	private float output = 0;
	private float fireAlpha = 0;

	public Neuron2D a, b;
	public float w;
	public int sigFrames;
	public int sendingFrame = 0;

	public Arc2D(PApplet p5, Neuron2D a, Neuron2D b, float w) {
		this.p5 = p5;
		this.a = a;
		this.b = b;
		this.w = w;

		signals = new ArrayList<>();
		this.sigFrames = 30;
	}

	// Connection active
	public void feedforward(float val) {
		output = val * w;
		sending = true;
		fireAlpha = 255;

		signals.add(new Signal2D(p5, this, sigFrames));
	}

	public void update() {
		// Remove 'completed' signals.
		Iterator<Signal2D> it = signals.iterator();
		while (it.hasNext()) {
			Signal2D s = (Signal2D) it.next();
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
		p5.noFill();
		p5.stroke(0xff79bb22, 155);
		p5.line(a.pos.x, a.pos.y, b.pos.x, b.pos.y);

		// Line glows when firing
		if (fireAlpha > 0) {
			fireAlpha -= 255f / sigFrames;
		}
		p5.stroke(255, fireAlpha);
		// p5.stroke(0xff79bb22, fireAlpha);
		p5.line(a.pos.x, a.pos.y, b.pos.x, b.pos.y);

		for (Signal2D s : signals) {
			s.render();
		}
	}

	public ArrayList<Signal2D> getSignals() {
		return signals;
	}
}