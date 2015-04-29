package amalgam.neuralnetwork;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.math.ExponentialInterpolation;

/**
 * Allows the signals to be rendered independently of the arc they move along, picking up the position data each frame
 */
public class Signal2D {
	private final PApplet p5;

	public Arc2D arc;
	public Neuron2D a, b;
	public float v;

	public float sz;
	public Vec2D pos;
	public Vec2D target;

	int age, life;

	public Signal2D(PApplet p5, Arc2D arc, int sigFrames) {
		this.p5 = p5;
		this.arc = arc;
		this.a = arc.a;
		this.b = arc.b;

		this.pos = a.pos;
		this.target = b.pos;
		this.sz = p5.random(3, 7);

		this.age = 0;
		this.life = sigFrames;
	}

	public void update() {
		ExponentialInterpolation exp = new ExponentialInterpolation(0.3f);
		float f = exp.interpolate(0, 1, (float) age / life);
		pos = a.pos.copy().interpolateTo(target, f);

		age++;
	}

	public void render() {
		p5.noStroke();
		p5.fill(255);
		p5.ellipse(pos.x, pos.y, sz, sz);
	}

}