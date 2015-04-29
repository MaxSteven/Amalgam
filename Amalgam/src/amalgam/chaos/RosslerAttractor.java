package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class RosslerAttractor extends Attractor {

	private float a, b, c;

	public RosslerAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 0.1f;
		this.b = 0.1f;
		this.c = 14;
	}

	public RosslerAttractor(PApplet p5) {
		this(p5, new Vec3D(0.1f, 0.1f, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	@Override
	public double dx(double x, double y, double z) {
		return -y - z;
	}

	@Override
	public double dy(double x, double y, double z) {
		return x + a * y;
	}

	@Override
	public double dz(double x, double y, double z) {
		return b + z * (x - c);
	}

}