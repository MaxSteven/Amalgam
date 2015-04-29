package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class NewChaosAttractor extends Attractor {

	private double a, b, c;

	public NewChaosAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = -10;
		this.b = -4;
		this.c = 0;
	}

	public NewChaosAttractor(PApplet p5) {
		this(p5, new Vec3D(1, 1, 1), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	@Override
	public double dx(double x, double y, double z) {
		return -((a * b) / (a + b)) * x - y * z + c;
	}

	@Override
	public double dy(double x, double y, double z) {
		return a * y + x * z;
	}

	@Override
	public double dz(double x, double y, double z) {
		return b * z + x * y;
	}

}