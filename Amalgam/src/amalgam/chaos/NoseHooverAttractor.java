package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

/**
 * http://jlswbs.blogspot.co.uk/2011/10/symetric-flow.html
 */
public class NoseHooverAttractor extends Attractor {

	private double a;

	public NoseHooverAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

//		this.a = 1.5;
		this.a = 2;
	}

	public NoseHooverAttractor(PApplet p5) {
		this(p5, new Vec3D(1, 0, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
	}

	@Override
	public double dx(double x, double y, double z) {
		return y;
	}

	@Override
	public double dy(double x, double y, double z) {
		return -x + y * z;
	}

	@Override
	public double dz(double x, double y, double z) {
		return a - (y * y);
	}

}