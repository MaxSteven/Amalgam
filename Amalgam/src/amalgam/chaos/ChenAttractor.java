package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class ChenAttractor extends Attractor {

	private double a, b, c;

	public ChenAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 40;
		this.b = 3;
		this.c = 28;
	}

	public ChenAttractor(PApplet p5) {
		this(p5, new Vec3D(-0.1f, 0.5f, -0.6f), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	public double dx(double x, double y, double z) {
		return a * (y - x);
	}

	public double dy(double x, double y, double z) {
		return (c - a) * x - (x * z) + (c * y);
	}

	public double dz(double x, double y, double z) {
		return (x * y) - (b * z);
	}
}