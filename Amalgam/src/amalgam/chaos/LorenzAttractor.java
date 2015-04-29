package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class LorenzAttractor extends Attractor {

	private float a, b, c;

	public LorenzAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 10f;
		this.b = 28f;
		this.c = 8.0f / 3;
	}

	public LorenzAttractor(PApplet p5) {
		this(p5, new Vec3D(0.1f, 0.1f, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	public double dx(double x, double y, double z) {
		return -a * (x - y);
	}

	public double dy(double x, double y, double z) {
		return -x * z + b * x - y;
	}

	public double dz(double x, double y, double z) {
		return x * y - c * z;
	}
}