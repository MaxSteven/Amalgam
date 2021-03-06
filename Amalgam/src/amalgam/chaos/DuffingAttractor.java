package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class DuffingAttractor extends Attractor {

	private double a, b, c;

	public DuffingAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = -0.1;
		this.b = 0.01;
		this.c = 0.2;
	}

	public DuffingAttractor(PApplet p5) {
		this(p5, new Vec3D(0, 0, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	@Override
	public double dx(double x, double y, double z) {
		return y;
	}

	@Override
	public double dy(double x, double y, double z) {
		return -b * y + x - Math.pow(x, 3) + a * Math.cos(z);
	}

	@Override
	public double dz(double x, double y, double z) {
		return c;
	}

}