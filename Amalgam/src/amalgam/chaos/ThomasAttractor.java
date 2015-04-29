package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class ThomasAttractor extends Attractor {

	private float b;

	public ThomasAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

//		this.b = 0.09f;
		this.b = 0.18f;
	}

	public ThomasAttractor(PApplet p5) {
		this(p5, new Vec3D(0.1f, 0.1f, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.b = params.get(0);
	}

	public double dx(double x, double y, double z) {
		return Math.sin(y) - b * x;
	}

	public double dy(double x, double y, double z) {
		return Math.sin(z) - b * y;
	}

	public double dz(double x, double y, double z) {
		return Math.sin(x) - b * z;
	}

}