package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class SprottLinzG_Attractor extends Attractor {

	private double a;

	public SprottLinzG_Attractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 4;
	}

	public SprottLinzG_Attractor(PApplet p5) {
		this(p5, new Vec3D(1, 0, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
	}

	public double dx(double x, double y, double z) {
		return y * z;
	}

	public double dy(double x, double y, double z) {
		return x * x - y;
	}

	public double dz(double x, double y, double z) {
		return 1 - a * x;
	}
}