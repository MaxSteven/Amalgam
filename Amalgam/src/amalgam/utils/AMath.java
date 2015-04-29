package amalgam.utils;

import toxi.geom.Circle;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class AMath {

	public static Vec2D circleInvert(Vec2D v, Circle c) {
		Vec2D inv = new Vec2D(v);
		float d = c.distanceTo(v);
		inv.set(c.x + c.getRadius() * c.getRadius() * (v.x - c.x) / (d * d), c.y + c.getRadius() * c.getRadius() * (v.y - c.y) / (d * d));
		return inv;
	}

	public static Vec3D sphereInvert(Vec3D v, Sphere c) {
		Vec3D inv = new Vec3D(v);
		float d = c.distanceTo(v);
		inv.set(c.x + c.radius * c.radius * (v.x - c.x) / (d * d), c.y + c.radius * c.radius * (v.y - c.y) / (d * d), c.z + c.radius * c.radius * (v.z - c.z) / (d * d));
		return inv;
	}

	public static float normalisedSin(float f, float start, float a) {
		return start + a * 0.5f * (1 + MathUtils.sin(f));
	}

	public static float normalisedSin(float f, float a) {
		return normalisedSin(f, 0, a);
	}

	public static float normalisedSin(float f) {
		return normalisedSin(f, 0, 1);
	}

}
