package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class HadleyAttractor extends Attractor {

	private double a, b, f, g;

	public HadleyAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 0.2;
		this.b = 4;
		this.f = 8;
		this.g = 1;
	}

	public HadleyAttractor(PApplet p5) {
		this(p5, new Vec3D(0, 0, 1.3f), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.f = params.get(2);
		this.g = params.get(3);
	}

	@Override
	public double dx(double x, double y, double z) {
		return -y * y - z * z - a * x + a * f;
	}

	@Override
	public double dy(double x, double y, double z) {
		return x * y - b * x * z - y + g;
	}

	@Override
	public double dz(double x, double y, double z) {
		return b * x * y + x * z - z;
	}

}