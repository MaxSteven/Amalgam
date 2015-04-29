package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class SprottLinzL_Attractor extends Attractor {

	private float a, b;

	public SprottLinzL_Attractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 3.9f;
		this.b = 0.9f;
	}

	public SprottLinzL_Attractor(PApplet p5) {
		this(p5, new Vec3D(1, 1, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
	}

	public double dx(double x, double y, double z) {
		return y + a * z;
	}

	public double dy(double x, double y, double z) {
		return b * PApplet.pow((float) x, 2) - y;
	}

	public double dz(double x, double y, double z) {
		return 1 - x;
	}
}