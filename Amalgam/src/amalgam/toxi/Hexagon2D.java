/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package amalgam.toxi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Rect;
import toxi.geom.Shape2D;
import toxi.geom.Vec2D;

/**
 * Hexagon2D (Regular only)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Hexagon2D implements Shape2D {

	public static boolean isClockwise(Vec2D a, Vec2D b, Vec2D c) {
		// TODO
		float determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
		return (determ > 0.0);
	}

	public boolean isVertical() {
		return vertical;
	}

	public void switchOrientation() {
		this.a = centre.sub(a).rotate(PApplet.PI / 2).add(centre).copy();
		this.b = centre.sub(b).rotate(PApplet.PI / 2).add(centre).copy();
		this.c = centre.sub(c).rotate(PApplet.PI / 2).add(centre).copy();
		this.d = centre.sub(d).rotate(PApplet.PI / 2).add(centre).copy();
		this.e = centre.sub(e).rotate(PApplet.PI / 2).add(centre).copy();
		this.f = centre.sub(f).rotate(PApplet.PI / 2).add(centre).copy();

		createTriangles();
		this.vertical ^= vertical; // xor is cool
	}

	@XmlElement(required = true)
	public Vec2D a, b, c, d, e, f;
	public ArrayList<Vec2D> vertices;
	public ArrayList<Triangle2D_Eq> tris;

	public Vec2D centre;
	public float radius;
	public boolean vertical;

	/**
	 * Create hexagon from point list (vertical boolean doesnt apply here)
	 */
	public Hexagon2D(List<Vec2D> points) {
		tris = new ArrayList<>();

		this.a = points.get(0);
		this.b = points.get(1);
		this.c = points.get(2);
		this.d = points.get(3);
		this.e = points.get(4);
		this.f = points.get(5);

		this.centre = a.add(d.sub(a).scale(0.5f));
		this.radius = d.sub(a).scale(0.5f).magnitude();

		vertexList();
		createTriangles();
	}

	public Hexagon2D(Vec2D centre, float radius, boolean vertical) {
		this.centre = centre.copy();
		this.radius = radius;
		this.vertical = vertical;

		tris = new ArrayList<>();

		float fl = (float) (Math.sqrt(3) / 2);
		if (vertical) {
			this.a = centre.add(0, -radius);
			this.b = centre.add(fl * radius, -radius / 2);
			this.c = centre.add(fl * radius, radius / 2);
			this.d = centre.add(0, radius);
			this.e = centre.add(-fl * radius, radius / 2);
			this.f = centre.add(-fl * radius, -radius / 2);
		} else {
			this.a = centre.add(-radius, 0);
			this.b = centre.add(-radius / 2, fl * radius);
			this.c = centre.add(radius / 2, fl * radius);
			this.d = centre.add(radius, 0);
			this.e = centre.add(radius / 2, -fl * radius);
			this.f = centre.add(-radius / 2, -fl * radius);
		}
		vertexList();
		createTriangles();
	}

	private void vertexList() {
		vertices = new ArrayList<>();
		vertices.add(a);
		vertices.add(b);
		vertices.add(c);
		vertices.add(d);
		vertices.add(e);
		vertices.add(f);
	}

	private void createTriangles() {
		tris.clear();
		tris.add(new Triangle2D_Eq(centre, a, b));
		tris.add(new Triangle2D_Eq(centre, b, c));
		tris.add(new Triangle2D_Eq(centre, c, d));
		tris.add(new Triangle2D_Eq(centre, d, e));
		tris.add(new Triangle2D_Eq(centre, e, f));
		tris.add(new Triangle2D_Eq(centre, f, a));
	}

	public Vec2D getCentre() {
		return centre;
	}

	public ArrayList<Triangle2D_Eq> getTris() {
		return tris;
	}

	public boolean containsPoint(ReadonlyVec2D p) {
		for (Triangle2D_Eq t : tris) {
			if (t.containsPoint(p)) {
				return true;
			}
		}
		return false;
	}

	public Hexagon2D copy() {
		return new Hexagon2D(centre, radius, vertical);
	}

	public float getArea() {
		float a = 0;
		for (Triangle2D_Eq t : tris) {
			a += t.getArea();
		}
		return a;
	}

	public Circle getBoundingCircle() {
		return Circle.from2Points(a, d);
	}

	public Rect getBounds() {
		Polygon2D p = this.toPolygon2D();
		return p.getBounds();
	}

	public Circle getCircumCircle() {
		return new Circle(centre, radius);
	}

	public float getCircumference() {
		return a.distanceTo(b) + b.distanceTo(c) + c.distanceTo(d) + d.distanceTo(e) + e.distanceTo(f) + f.distanceTo(a);
	}

	public Vec2D getClosestPointTo(ReadonlyVec2D p) {
		Vec2D closest = null;
		float mag = Float.POSITIVE_INFINITY;
		for (Vec2D v : vertices) {
			float m = p.sub(v).magnitude();
			if (m < mag) {
				m = mag;
				closest = v;
			}
		}
		return closest;
	}

	public List<Line2D> getEdges() {
		return toPolygon2D().getEdges();
	}

	public Vec2D getRandomPoint() {
		int tri = (int) (6 * Math.random());
		return tris.get(tri).getRandomPoint();
	}

	public Vec2D[] getVertexArray() {
		return getVertexArray(null, 0);
	}

	public Vec2D[] getVertexArray(Vec2D[] array, int offset) {
		if (array == null) {
			array = new Vec2D[6];
		}
		array[offset++] = a;
		array[offset++] = b;
		array[offset++] = c;
		array[offset++] = d;
		array[offset++] = e;
		array[offset] = f;
		return array;
	}

	public boolean isClockwise() {
		return Hexagon2D.isClockwise(a, b, c);
	}

	public void set(Vec2D a2, Vec2D b2, Vec2D c2, Vec2D d2, Vec2D e2, Vec2D f2) {
		a = a2;
		b = b2;
		c = c2;
		d = d2;
		e = e2;
		f = f2;
	}

	/**
	 * @return hexagon as polygon
	 */
	public Polygon2D toPolygon2D() {
		Polygon2D poly = new Polygon2D();
		poly.add(a.copy());
		poly.add(b.copy());
		poly.add(c.copy());
		poly.add(d.copy());
		poly.add(e.copy());
		poly.add(f.copy());
		return poly;
	}

	public String toString() {
		return "Hexagon2D: " + a + "," + b + "," + c + "," + d + "," + e + "," + f;
	}
}
