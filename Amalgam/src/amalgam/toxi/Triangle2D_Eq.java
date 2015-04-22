package amalgam.toxi;

import processing.core.PApplet;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;

/**
 * Extension for equilateral triangles on a grid (up/down)
 */
public class Triangle2D_Eq extends Triangle2D {

	private boolean isUp;
	private Vec2D centre;
	private float sideLength;

	// Equilateral only
	public Triangle2D_Eq(Vec2D centre, float sideLength, boolean isUp) {
		this.centre = centre;
		this.sideLength = sideLength;

		if (isUp) {
			this.a = new Vec2D(centre.x, centre.y - sideLength * PApplet.sqrt(3) / 4f);
			this.b = new Vec2D(centre.x - sideLength / 2f, centre.y + sideLength * PApplet.sqrt(3) / 4f);
			this.c = new Vec2D(centre.x + sideLength / 2f, centre.y + sideLength * PApplet.sqrt(3) / 4f);
		} else {
			this.a = new Vec2D(centre.x, centre.y + sideLength * PApplet.sqrt(3) / 4f);
			this.b = new Vec2D(centre.x - sideLength / 2f, centre.y - sideLength * PApplet.sqrt(3) / 4f);
			this.c = new Vec2D(centre.x + sideLength / 2f, centre.y - sideLength * PApplet.sqrt(3) / 4f);
		}
		this.isUp = isUp;
	}

	// Ugly from point version. Redo. TODO
	public Triangle2D_Eq(Vec2D point, float sideLength, int dir) {
		this.sideLength = sideLength;

		switch (dir) {
			case 0 :
				this.centre = point.add(0, sideLength * PApplet.sqrt(3) / 4f);
				this.isUp = true;

				this.a = new Vec2D(point.x, point.y);
				this.b = new Vec2D(point.x - sideLength / 2f, point.y + sideLength * PApplet.sqrt(3) / 2f);
				this.c = new Vec2D(centre.x + sideLength / 2f, point.y + sideLength * PApplet.sqrt(3) / 2f);
				break;
			case 1 :
				this.centre = point.add(0, -sideLength * PApplet.sqrt(3) / 4f);
				this.isUp = true;

				this.a = new Vec2D(point.x, point.y);
				this.b = new Vec2D(point.x - sideLength / 2f, point.y - sideLength * PApplet.sqrt(3) / 2f);
				this.c = new Vec2D(centre.x + sideLength / 2f, point.y - sideLength * PApplet.sqrt(3) / 2f);
				break;
		}

	}

	// Checks for eq, throw exception if not? Pretty nasty but it works...
	public Triangle2D_Eq(Vec2D a, Vec2D b, Vec2D c) {
		if (Math.abs(a.sub(b).magnitude() - b.sub(c).magnitude()) < 0.01f && Math.abs(b.sub(c).magnitude() - c.sub(a).magnitude()) < 0.01f) { // sometimes these aren't totally exact so use threshold?
			this.a = a.copy();
			this.b = b.copy();
			this.c = c.copy();
			this.centre = a.add(b).addSelf(c).scaleSelf(1f / 3);
			this.sideLength = a.sub(b).magnitude();
		} else {
			throw new UnsupportedOperationException("The points provided do not form an equilateral triangle");
		}
	}

	// Constructor with isUp defined
	public Triangle2D_Eq(Vec2D a, Vec2D b, Vec2D c, boolean isUp) {
		if (Math.abs(a.sub(b).magnitude() - b.sub(c).magnitude()) < 0.01f && Math.abs(b.sub(c).magnitude() - c.sub(a).magnitude()) < 0.01f) { // sometimes these aren't totally exact so use threshold?
			this.a = a.copy();
			this.b = b.copy();
			this.c = c.copy();
			this.centre = a.add(b).addSelf(c).scaleSelf(1f / 3);
			this.isUp = isUp;

			this.sideLength = a.sub(b).magnitude();
			if (isUp) {
				this.centre = a.add(0, sideLength * PApplet.sqrt(3) / 4);
			} else {
				this.centre = a.add(0, -sideLength * PApplet.sqrt(3) / 4);
			}
		} else {
			throw new UnsupportedOperationException("The points provided do not form an equilateral triangle");
		}
	}
	
	public float getSideLength() {
		return sideLength;
	}

	public boolean isUp() {
		return isUp;
	}

	public Vec2D getCentre() {
		return centre;
	}

	@Override
	public String toString() {
		return "Triangle2D_Eq: " + a + "," + b + "," + c + "," + isUp;
	}

}
