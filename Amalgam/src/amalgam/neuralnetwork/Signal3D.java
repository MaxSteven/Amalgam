package amalgam.neuralnetwork;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.math.ExponentialInterpolation;

/**
 * Allows the signals to be rendered independently of the arc they move along, picking up the position data each frame
 */
public class Signal3D {
	private final PApplet p5;

	public Arc3D arc;
	public Neuron3D a, b;
	public float v;

	public float sz;
	public Vec3D pos;
	public Vec3D target;

	int age, life;

	public Signal3D(PApplet p5, Arc3D arc, int sigFrames) {
		this.p5 = p5;
		this.arc = arc;
		this.a = arc.a;
		this.b = arc.b;

		this.pos = a.pos;
		this.target = b.pos;
		this.sz = p5.random(1, 5);

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
		p5.strokeWeight(sz);
		p5.stroke(255);
		p5.noFill();
		p5.point(pos.x, pos.y, pos.z);
		p5.strokeWeight(1f);
	}

}