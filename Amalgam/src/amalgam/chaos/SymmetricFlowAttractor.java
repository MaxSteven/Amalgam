package amalgam.chaos;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec3D;

/**
 * http://jlswbs.blogspot.co.uk/2011/10/symetric-flow.html
 */
public class SymmetricFlowAttractor extends Attractor {

	private float a;

	public SymmetricFlowAttractor(PApplet p5, Vec3D pos, float scale) {
		super(p5, pos, scale);

		this.a = 1.27f;
	}

	public SymmetricFlowAttractor(PApplet p5) {
		this(p5, new Vec3D(1, 1, 0), 50);
	}

	public void setParams(List<Float> params) {
		this.a = params.get(0);
	}

	@Override
	public double dx(double x, double y, double z) {
		return -a * x - 4 * y - 4 * z - y * y;
	}

	@Override
	public double dy(double x, double y, double z) {
		return -a * y - 4 * z - 4 * x - z * z;
	}

	@Override
	public double dz(double x, double y, double z) {
		return -a * z - 4 * x - 4 * y - x * x;
	}

}