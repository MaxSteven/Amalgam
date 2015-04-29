package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class ModifiedLorenzAttractor extends Attractor {

	private float a, b, c, d;

	public ModifiedLorenzAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 0.1f;
		this.b = 4;
		this.c = 14;
		this.d = 0.08f;
	}

	public ModifiedLorenzAttractor(PApplet p5) {
		this(p5, new Vec3D(0, 1, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	public double dx(double x, double y, double z) {
		return -a * x + (y * y) - (z * z) + a * c;
	}

	public double dy(double x, double y, double z) {
		return x * (y - b * z) + d;
	}

	public double dz(double x, double y, double z) {
		return -z + x * (b * y + z);
	}
}