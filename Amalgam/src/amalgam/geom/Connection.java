package amalgam.geom;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Circle;
import toxi.geom.Line2D;
import toxi.geom.Line2D.LineIntersection;
import toxi.geom.Vec2D;

/**
 * Add methods for connection style. Dotted/segmented connections perhaps?
 */
public class Connection {
	static int TYPES = ConnectionType.values().length; // maximum value in switch statement

	public Vec2D a, b, mid;
	public Line2D ab;
	public float dist;
	private ConnectionType t;

	// Style
	private float weight;
	private TColor arcClr, nodeClr;

	public Connection(Vec2D a, Vec2D b, ConnectionType t) {
		this(a, b, t, 1);
	}

	public Connection(Vec2D a, Vec2D b, ConnectionType t, float weight) {
		this.a = a;
		this.b = b;
		this.mid = new Line2D(a, b).getMidPoint();
		this.ab = new Line2D(a, b);
		this.dist = a.distanceTo(b);
		this.weight = weight;

		// Style defaults
		this.arcClr = TColor.RED.copy();
		this.nodeClr = TColor.BLUE.copy();

		this.t = t;
	}

	/**
	 * Draw the connection. Currently very messy
	 */
	public void renderArcs(PApplet p5) {
		p5.ellipseMode(PApplet.RADIUS);
		p5.noFill();
		p5.strokeWeight(weight);

		p5.stroke(arcClr.toARGB());

		switch (t) {
			case LINEAR : // linear
				p5.line(a.x, a.y, b.x, b.y);
				break;
			case SQUARE : // sq edge (rand?)
				p5.beginShape();
				p5.vertex(a.x, a.y);
				// if (Rnd.rn(0.5f)) {
				// p5.vertex(a.x, b.y);
				// } else {
				// p5.vertex(b.x, a.y);
				// }
				p5.vertex(b.x, a.y);
				p5.vertex(b.x, b.y);
				p5.endShape();
				break;
			case SQUARE_MIDPOINT : // sq edge through midpoint (rand?)
				p5.beginShape();
				p5.vertex(a.x, a.y);
				// if (Rnd.rn(0.5f)) {
				// p5.vertex(mid.x, a.y);
				// p5.vertex(mid.x, b.y);
				// } else {
				// p5.vertex(a.x, mid.y);
				// p5.vertex(b.x, mid.y);
				// }
				p5.vertex(a.x, mid.y);
				p5.vertex(b.x, mid.y);
				p5.vertex(b.x, b.y);
				p5.endShape();
				break;
			case BEZIER_1 : // bezier - needs more work
				// Vec2D peak = new Vec2D(mid.x, (a.y + b.y) / 2);
				Vec2D peak = new Vec2D(mid.x, (a.y + b.y) / 2);

				p5.beginShape();
				p5.vertex(a.x, a.y);
				// p5.bezierVertex(peak.x, peak.y, peak.x, peak.y, b.x, b.y);
				p5.bezierVertex(a.x, peak.y, b.x, peak.y, b.x, b.y);
				// p5.bezierVertex(a.x, a.y-dist, b.x, b.y-dist, b.x, b.y);

				p5.endShape();
				break;
			case BEZIER_2 : // bezier 2
				// ========original============
				// Vec2D n = ab.toRay2D().perpendicular().normalize();
				// Vec2D aa = mid.add(n.scale(dist / 2));
				// Vec2D bb = mid.add(n.scale(-dist / 2));
				// p5.stroke(100, 100, 255);
				// p5.beginShape();
				// p5.vertex(a.x, a.y);
				// p5.bezierVertex(aa.x, aa.y, bb.x, bb.y, b.x, b.y);
				// p5.endShape();
				// ===============================

				// //////////////////////////
				// Vec2D pMid = new Vec2D(p5.width / 2f, p5.height / 2f);
				Vec2D pMid = new Vec2D(p5.width / 2f, 0);
				// Vec2D pMid = new Vec2D(0,0);

				Vec2D aCen = a.sub(pMid);
				Vec2D bCen = b.sub(pMid);

				// Vec2D aa = new Vec2D(bCen.magnitude(), 0).rotate(aCen.heading()).addSelf(pMid);
				// Vec2D bb = new Vec2D(aCen.magnitude(), 0).rotate(bCen.heading()).addSelf(pMid);

				float midRad = (aCen.magnitude() + bCen.magnitude()) / 2f;
				// Vec2D aa = new Vec2D(midRad, 0).rotate(aCen.heading()).addSelf(pMid);
				// Vec2D bb = new Vec2D(midRad, 0).rotate(bCen.heading()).addSelf(pMid);

				float aaWt = PApplet.lerp(aCen.magnitude(), bCen.magnitude(), 0.4f);
				float bbWt = PApplet.lerp(aCen.magnitude(), bCen.magnitude(), 0.6f);
				Vec2D aa = new Vec2D(aaWt, 0).rotate(aCen.heading()).addSelf(pMid);
				Vec2D bb = new Vec2D(bbWt, 0).rotate(bCen.heading()).addSelf(pMid);

				p5.beginShape();
				p5.vertex(a.x, a.y);
				p5.bezierVertex(aa.x, aa.y, bb.x, bb.y, b.x, b.y);
				p5.endShape();
				break;
			case ARC_1 : // Spiral
				float r = mid.sub(a).magnitude();

				float ah1 = a.sub(mid).heading() % PApplet.TAU;
				float bh1 = b.sub(mid).heading() % PApplet.TAU;
				if (ah1 > bh1) {
					bh1 += PApplet.TAU;
				}

				// Vec2D lims = arcLimits(mid, a, b);
				p5.arc(mid.x, mid.y, r, r, ah1, bh1);
				break;
			case ARC_2 : // Circ with diam at first point
				float segment = Math.abs(b.sub(a).heading()) % PApplet.TAU;
				Line2D aExt;
				if (segment >= PApplet.PI / 4 && segment < 3 * PApplet.PI / 4) {
					aExt = new Line2D(a.add(-10000, 0), a.add(10000, 0)); // yaxis flat
					// aExt = new Line2D(a.add(0, -10000), a.add(0, 10000)); // inverse
				} else {
					aExt = new Line2D(a.add(0, -10000), a.add(0, 10000)); // xaxis flat
					// aExt = new Line2D(a.add(-10000, 0), a.add(10000, 0)); // inverse
				}

				float rad = a.sub(b).magnitude();
				Circle ca = new Circle(a, rad);
				Circle cb = new Circle(b, rad);
				Vec2D[] ints = ca.intersectsCircle(cb);

				Line2D l = new Line2D(ints[0], ints[1]);

				LineIntersection ii = l.intersectLine(aExt);
				Vec2D centre = ii.getPos();
				if (centre == null) {
					break;
				}
				float radius = centre.sub(a).magnitude();

				// ======== Original Code ============
				// unsymmetric angular behavior - some very large arcs but only on 1 side
				// Vec2D lims2 = arcLimits(centre, a, b);
				// p5.stroke(100, 100, 255);
				// p5.arc(centre.x, centre.y, radius, radius, lims2.x, lims2.y);
				// ===================================

				// ======== New Code ===================
				// // prevents arcs being drawn which span > PI rads
				float ah = a.sub(centre).heading() % PApplet.TAU;
				float bh = b.sub(centre).heading() % PApplet.TAU;
				if (ah > bh) {
					bh += PApplet.TAU;
				}
				if (Math.abs(bh - ah) > PApplet.PI) {
					float temp = ah;
					ah = bh;
					bh = (temp + PApplet.TAU - Math.abs(ah - bh));
				}
				p5.arc(centre.x, centre.y, radius, radius, ah, bh);
				// ===================================

				break;
			case ARC_3 :
				p5.beginShape();
				p5.vertex(a.x, a.y);

				// perfect semi-cirular 'cap' TODO Horribly ugly and can definitely be simplified
				float perpX = Math.abs(a.x - b.x);
				float h = 0.5f * 0.552f * perpX; // relative handle position producing circular arc
				if (a.y > b.y && a.x < b.x) {
					p5.vertex(a.x, b.y);
					p5.bezierVertex(a.x, b.y - h, a.x + perpX / 2 - h, b.y - perpX / 2, a.x + perpX / 2, b.y - perpX / 2);
					p5.bezierVertex(a.x + perpX / 2 + h, b.y - perpX / 2, b.x, b.y - h, b.x, b.y);
				} else if (a.y > b.y && a.x > b.x) {
					p5.vertex(a.x, b.y);
					p5.bezierVertex(a.x, b.y - h, a.x - perpX / 2 + h, b.y - perpX / 2, a.x - perpX / 2, b.y - perpX / 2);
					p5.bezierVertex(a.x - perpX / 2 - h, b.y - perpX / 2, b.x, b.y - h, b.x, b.y);
				} else if (a.y < b.y && a.x > b.x) {
					p5.bezierVertex(a.x, a.y - h, a.x - perpX / 2 + h, a.y - perpX / 2, a.x - perpX / 2, a.y - perpX / 2);
					p5.bezierVertex(a.x - perpX / 2 - h, a.y - perpX / 2, b.x, a.y - h, b.x, a.y);
					p5.vertex(b.x, b.y);
				} else {
					p5.bezierVertex(a.x, a.y - h, a.x + perpX / 2 - h, a.y - perpX / 2, a.x + perpX / 2, a.y - perpX / 2);
					p5.bezierVertex(a.x + perpX / 2 + h, a.y - perpX / 2, b.x, a.y - h, b.x, a.y);
					p5.vertex(b.x, b.y);
				}

				p5.endShape();
				break;
			case CURVE_1 : // Messing with curveVertex()
				Vec2D aac = a.add(Vec2D.randomVector().scaleSelf(100));
				Vec2D bbc = b.add(Vec2D.randomVector().scaleSelf(100));

				p5.beginShape();
				p5.curveVertex(a.x, a.y);
				p5.curveVertex(a.x, a.y);
				p5.curveVertex(aac.x, aac.y);
				p5.curveVertex(bbc.x, bbc.y);
				p5.curveVertex(b.x, b.y);
				p5.curveVertex(b.x, b.y);
				p5.endShape();
				break;
		}
		// renderNodes(p5); // auto-off so nodes always appear above (or below) arcs?
	}

	public void renderNodes(PApplet p5, int type) { // could even add SVGs here? or would using Unfolding Markers really make more sense for that eventually...?
		p5.rectMode(PApplet.CENTER);
		float dotSize = 1.5f * weight;

		p5.noStroke();
		p5.fill(nodeClr.toARGB());

		switch (type) {
			case 0 :
				p5.ellipse(a.x, a.y, dotSize, dotSize);
				p5.ellipse(b.x, b.y, dotSize, dotSize);
				break;
			case 1 :
				p5.rect(a.x, a.y, dotSize, dotSize);
				p5.rect(b.x, b.y, dotSize, dotSize);
				break;
		}
	}

	/**
	 * Convert position of 2 points on outside of the ellipse (centre cen) to arc start and end angles. Deprecated (old arc connectors used this)
	 */
	private Vec2D arcLimits(Vec2D cen, Vec2D a, Vec2D b) {
		float start = PApplet.TAU + a.sub(cen).heading();
		float end = PApplet.TAU + b.sub(cen).heading();
		if (start > end) {
			start = end;
			end = PApplet.TAU + a.sub(cen).heading();
		}

		Vec2D lims = new Vec2D(start, end);
		return lims;
	}

	public void setA(Vec2D a) {
		this.a = a;
	}

	public void setB(Vec2D b) {
		this.a = b;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}

	public void setStyle(TColor arcClr, TColor nodeClr) {
		// TColor contains alpha information so use this.. (-255 to 0)
		this.arcClr = arcClr;
		this.nodeClr = nodeClr;
	}
}