package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class AizawaAttractor extends Attractor {

	private double a, b, c, d, e, f;

	public AizawaAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 0.95;
		this.b = 0.7;
		this.c = 0.6;
		this.d = 3.5;
		this.e = 0.25;
		this.f = 0.1;
	}

	public AizawaAttractor(PApplet p5) {
		this(p5, new Vec3D(0.1f, 0, 0), 50);
	}	
	
	public void setParams(List<Float> params) {
		this.a = params.get(0);
		this.b = params.get(1);
		this.c = params.get(2);
	}

	public double dx(double x, double y, double z) {
		return (z - b) * x - d * y;
	}

	public double dy(double x, double y, double z) {
		return d * x + (z - b) * y;
	}

	public double dz(double x, double y, double z) {
		return c + a * z - (Math.pow(z, 3) / 3) - (Math.pow(x, 2) + Math.pow(y, 2)) * (1 + e * z) + f * z * Math.pow(x, 3);
	}
}