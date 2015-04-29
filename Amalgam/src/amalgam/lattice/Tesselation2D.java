package amalgam.lattice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import amalgam.utils.ARnd;

/**
 * 2D Shape tesselation and subdivision
 * 
 * TODO: Shape2D/Polygon2D usage? Fix the Bounds Rect which is produced by default. Set a centre offset in here rather than in polygon tilings? (or as well as?). Use Enum for Subdiv type?
 */
public class Tesselation2D {
	private Rect bounds;

	private ArrayList<Polygon2D> shapesOrig;
	public ArrayList<Polygon2D> shapes;
	public ArrayList<Vec2D> points;

	Vec2D centre; // use this rather than offsetting in polygon tilings?

	private float minArea;
	private float bevelWidth;

	public Tesselation2D(ArrayList<Polygon2D> shapes) {
		this(shapes, new Rect(0, 0, 100, 100));
	}

	// TODO
	public Tesselation2D(ArrayList<Polygon2D> shapes, Vec2D centre) {
		this(shapes, new Rect(0, 0, 100, 100));
	}

	public Tesselation2D(ArrayList<Polygon2D> shapes, Rect bounds) {
		this.shapes = shapes;
		this.bounds = bounds;
		this.shapesOrig = new ArrayList<>(shapes);

		this.minArea = 1;
		this.bevelWidth = 10;
	}

	/**
	 * NEW SUBDIVIDOR - INTENDED FOR USE ON ALL POLYGONS
	 */
	private void performSubdivisionPolygon(Polygon2D p, ArrayList<Polygon2D> newShapes, SubdivisionType subdividor) {
		if (Math.abs(p.getArea()) < minArea) { // Only subdivide large shapes
			newShapes.add(p);
		} else {
			Vec2D cen = p.getCentre();
			ArrayList<Vec2D> pts = new ArrayList<>();

			Polygon2D inner;
			Vec2D v, v2, v3, v4;

			switch (subdividor) {
				case CORNERS_TO_CENTRE :
					for (int i = 0; i < p.vertices.size(); i++) {
						v = p.vertices.get(i);
						v2 = p.vertices.get((i + 1) % p.vertices.size());

						// Connect all corners to centre and split
						pts.clear();
						pts.add(v);
						pts.add(cen);
						pts.add(v2);
						newShapes.add(new Polygon2D(pts));
					}
					break;
				case MIDPOINTS_TO_CENTRE :
					for (int i = 0; i < p.vertices.size(); i++) { // fails on first iter
						v = p.vertices.get(i);
						v2 = p.vertices.get((i + 1) % p.vertices.size());
						v3 = p.vertices.get((i + 2) % p.vertices.size());

						// Connect all edge midPoints to centre and split
						Line2D l = new Line2D(v2, v);
						Vec2D mp = l.getMidPoint();
						Line2D l2 = new Line2D(v2, v3);
						Vec2D mp2 = l2.getMidPoint();

						pts.clear();
						pts.add(cen);
						pts.add(mp);
						pts.add(v2);
						pts.add(mp2);
						newShapes.add(new Polygon2D(pts));
					}
					break;
				case HALVE_RANDOM :
					// Currently broken TODO
					if (p.vertices.size() % 2 == 0) {
						// Cut in half (Even)
						// fully randomised (index2 = 0 to switch off)
						int index = ARnd.in(p.vertices.size());

						pts.clear();
						for (int i = 0; i <= p.vertices.size() / 2; i++) {
							pts.add(p.vertices.get((i + index) % p.vertices.size()));
						}
						newShapes.add(new Polygon2D(pts));

						pts.clear();
						for (int i = p.vertices.size() / 2; i <= p.vertices.size(); i++) {
							pts.add(p.vertices.get((i + index) % p.vertices.size()));
						}
						newShapes.add(new Polygon2D(pts));
					} else {
						// Cut in half (Odd)
						// fully randomised (index2 = 0 to switch off)
						int index2 = ARnd.in(p.vertices.size());

						pts.clear();
						v = p.vertices.get((index2 + p.vertices.size() / 2) % p.vertices.size());
						v2 = p.vertices.get((index2 + 1 + p.vertices.size() / 2) % p.vertices.size());
						Vec2D mp = new Line2D(v, v2).getMidPoint();
						for (int i = 0; i <= p.vertices.size() / 2; i++) {
							pts.add(p.vertices.get((i + index2) % p.vertices.size()));
						}
						pts.add(mp);
						newShapes.add(new Polygon2D(pts));

						pts.clear();
						pts.add(mp);
						for (int i = p.vertices.size() / 2 + 1; i <= p.vertices.size(); i++) {
							pts.add(p.vertices.get((i + index2) % p.vertices.size()));
						}
						newShapes.add(new Polygon2D(pts));
					}
					break;
				case HALVE_SET :
					if (p.vertices.size() % 2 == 0) {
						// Cut in half (Even)
						pts.clear();
						int index = 0;

						for (int i = 0; i <= p.vertices.size() / 2; i++) {
							pts.add(p.vertices.get((i + index) % p.vertices.size()));
						}
						newShapes.add(new Polygon2D(pts));
						pts.clear();
						for (int i = p.vertices.size() / 2; i <= p.vertices.size(); i++) {
							pts.add(p.vertices.get((i + index) % p.vertices.size()));
						}
						newShapes.add(new Polygon2D(pts));
					} else {
						// Cut in half (Odd)
						pts.clear();
						int index2 = 0;

						v = p.vertices.get((index2 + p.vertices.size() / 2) % p.vertices.size());
						v2 = p.vertices.get((index2 + 1 + p.vertices.size() / 2) % p.vertices.size());
						Vec2D mp = new Line2D(v, v2).getMidPoint();
						for (int i = 0; i <= p.vertices.size() / 2; i++) {
							pts.add(p.vertices.get((i + index2) % p.vertices.size()));
						}
						pts.add(mp);
						newShapes.add(new Polygon2D(pts));
						pts.clear();
						pts.add(mp);
						for (int i = p.vertices.size() / 2 + 1; i <= p.vertices.size(); i++) {
							pts.add(p.vertices.get((i + index2) % p.vertices.size()));
						}
						newShapes.add(new Polygon2D(pts));
					}
					break;
				case CORNERS_TO_RANDOM :
					// Connect all corners to Random point and split
					Vec2D rnd = p.getRandomPoint();
					for (int i = 0; i < p.vertices.size(); i++) {
						v = p.vertices.get(i);
						v2 = p.vertices.get((i + 1) % p.vertices.size());

						pts.clear();
						pts.add(v);
						pts.add(rnd);
						pts.add(v2);
						newShapes.add(new Polygon2D(pts));
					}
					break;
				case MIDPOINT_POLYGON :
					// Connect Midpoints to new shape, and fill in leftovers
					ArrayList<Vec2D> leftovers = new ArrayList<>();
					pts.clear();
					for (int i = 0; i < p.vertices.size(); i++) {
						v = p.vertices.get(i);
						v2 = p.vertices.get((i + 1) % p.vertices.size());
						v3 = p.vertices.get((i + 2) % p.vertices.size());

						Line2D l = new Line2D(v, v2);
						Vec2D mp1 = l.getMidPoint();
						Line2D l2 = new Line2D(v2, v3);
						Vec2D mp2 = l2.getMidPoint();

						pts.add(mp1);

						leftovers.clear();
						leftovers.add(mp2);
						leftovers.add(v2);
						leftovers.add(mp1);
						newShapes.add(new Polygon2D(leftovers));
					}
					newShapes.add(new Polygon2D(pts));
					break;
				case MIDTHIRD_POLYGON :
					// Connect Midpoint-Thirds to new shape, and fill in leftovers
					ArrayList<Vec2D> miniPts = new ArrayList<>();
					pts.clear();
					for (int i = 0; i < p.vertices.size(); i++) {
						v = p.vertices.get(i);
						v2 = p.vertices.get((i + 1) % p.vertices.size());
						v3 = p.vertices.get((i + 2) % p.vertices.size());

						// Line2D l = new Line2D(v1, v2);
						Vec2D a = v.add(v2.sub(v).scaleSelf(0.3333333f));
						Vec2D b = v.add(v2.sub(v).scaleSelf(0.6666666f));

						// Line2D l2 = new Line2D(v3, v2);
						Vec2D c = v2.add(v3.sub(v2).scaleSelf(0.3333333f));
						Vec2D d = v2.add(v3.sub(v2).scaleSelf(0.6666666f));

						pts.add(a);
						pts.add(b);

						miniPts.clear();
						miniPts.add(b);
						miniPts.add(v2);
						miniPts.add(c);
						newShapes.add(new Polygon2D(miniPts));
					}
					newShapes.add(new Polygon2D(pts));
					break;
				case BEVEL :
					// copies shape and scales by 0.5, then connects to corners. 3D effect achieved by querying heading between vertex(0) and vertex(1) etc
					inner = p.copy();
					inner.scaleSize(0.5f);
					newShapes.add(inner);
					ArrayList<Vec2D> subShapePts = new ArrayList<>();

					for (int i = 0; i < p.vertices.size(); i++) {
						v = p.vertices.get(i);
						v2 = p.vertices.get((i + 1) % p.vertices.size());

						Vec2D b = inner.vertices.get(i);
						Vec2D b2 = inner.vertices.get((i + 1) % p.vertices.size());

						subShapePts.clear();
						subShapePts.add(v);
						subShapePts.add(v2);
						subShapePts.add(b2);
						subShapePts.add(b);
						newShapes.add(new Polygon2D(subShapePts));
					}
					break;
				case BEVEL_FIXED :
					Vec2D centroid = p.getCentroid();

					// midpoints to centre if the bevel width would pass through the shapes centroid (too wide)
					if (p.vertices.get(0).distanceTo(centroid) <= bevelWidth) {
						for (int i = 0; i < p.vertices.size(); i++) {
							v = p.vertices.get(i);
							v2 = p.vertices.get((i + 1) % p.vertices.size());
							// Connect all corners to centre and split
							pts.clear();
							pts.add(v);
							pts.add(cen);
							pts.add(v2);
							newShapes.add(new Polygon2D(pts));
						}
					}
					// or perform the bevel
					else {
						Polygon2D reduced = new Polygon2D();
						for (Vec2D a : p.vertices) {
							Vec2D b = a.copy();
							Vec2D edge = b.sub(centroid).normalize();
							b.subSelf(centroid).subSelf(edge.scale(bevelWidth)).addSelf(centroid);
							reduced.add(b);
						}
						newShapes.add(reduced);
						ArrayList<Vec2D> subShapePts2 = new ArrayList<>();
						for (int i = 0; i < p.vertices.size(); i++) {
							v = p.vertices.get(i);
							v2 = p.vertices.get((i + 1) % p.vertices.size());

							Vec2D b = reduced.vertices.get(i);
							Vec2D b2 = reduced.vertices.get((i + 1) % reduced.vertices.size());

							subShapePts2.clear();
							subShapePts2.add(v);
							subShapePts2.add(v2);
							subShapePts2.add(b2);
							subShapePts2.add(b);
							newShapes.add(new Polygon2D(subShapePts2));
						}
					}
					break;
				default :
					newShapes.add(p);
					break;
			}
		}
	}

	public void setMinShapeArea(float a) {
		this.minArea = a;
	}

	public void setFixedBevelWidth(float a) {
		this.bevelWidth = a;
	}

	public void subdivideRandom(float prob, SubdivisionType subdividor) {
		ArrayList<Polygon2D> newShapes = new ArrayList<>();

		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = (Polygon2D) it.next();
			if (Math.random() < prob) {
				performSubdivisionPolygon(s, newShapes, subdividor);
				it.remove();
			}
		}
		for (Polygon2D sh : newShapes) {
			shapes.add(sh);
		}
	}

	/**
	 * Subdivide at given point with probability
	 */
	public void subdividePosition(Vec2D pos, float prob, SubdivisionType subdividor) {
		ArrayList<Polygon2D> newShapes = new ArrayList<>();

		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = (Polygon2D) it.next();
			if (s.containsPoint(pos)) {
				if (ARnd.rn(prob)) {
					performSubdivisionPolygon(s, newShapes, subdividor);
					it.remove();
				}
			}
		}
		for (Polygon2D sh : newShapes) {
			shapes.add(sh);
		}
	}

	/**
	 * Subdivide given shape
	 */
	public void subdivideShape(Polygon2D s, float prob, SubdivisionType subdividor) {
		ArrayList<Polygon2D> newShapes = new ArrayList<>();

		if (ARnd.rn(prob)) {
			performSubdivisionPolygon(s, newShapes, subdividor);
			shapes.remove(s); // remove orig
		}
		for (Polygon2D sh : newShapes) {
			shapes.add(sh);
		}
	}

	public ArrayList<Polygon2D> getCentralStars(float vertexScale, float midpointScale) {
		ArrayList<Polygon2D> stars = new ArrayList<>();
		for (Polygon2D p : shapes) {
			Vec2D cen = p.getCentroid();
			Polygon2D star = new Polygon2D();

			for (int i = 0; i < p.vertices.size(); i++) {
				Vec2D v = p.vertices.get(i);
				Vec2D v2 = p.vertices.get((i + 1) % p.vertices.size());
				Vec2D mid = new Line2D(v, v2).getMidPoint();

				star.add(cen.add(v.sub(cen).scaleSelf(vertexScale)));
				star.add(cen.add(mid.sub(cen).scaleSelf(midpointScale)));
			}
			stars.add(star);
		}
		return stars;
	}

	public ArrayList<Polygon2D> getCentralStarsFixedOffset(float vertexOffset, float midpointOffset) {
		ArrayList<Polygon2D> stars = new ArrayList<>();
		for (Polygon2D p : shapes) {
			Vec2D cen = p.getCentroid();
			Polygon2D star = new Polygon2D();

			for (int i = 0; i < p.vertices.size(); i++) {
				Vec2D v = p.vertices.get(i);
				Vec2D v2 = p.vertices.get((i + 1) % p.vertices.size());
				Vec2D mid = new Line2D(v, v2).getMidPoint();

				if (vertexOffset >= v.distanceTo(cen)) {
					vertexOffset = 2;
				}
				if (midpointOffset >= mid.distanceTo(cen)) {
					midpointOffset = 2;
				}

				star.add(cen.add(v.sub(cen).subSelf(v.sub(cen).normalizeTo(vertexOffset))));
				star.add(cen.add(mid.sub(cen).subSelf(mid.sub(cen).normalizeTo(midpointOffset))));
			}
			stars.add(star);
		}
		return stars;
	}

	public void shuffle() {
		Collections.shuffle(shapes);
	}

	/**
	 * Intended use - duplicate and then clean for no overlaps?
	 */
	public void duplicate(Vec2D offset) {
		ArrayList<Polygon2D> newShapes = new ArrayList<>();
		for (Polygon2D p : shapes) {
			newShapes.add(p.copy().translate(offset));
		}
		shapes.addAll(newShapes);
	}

	public void clean() {
		// remove polygon duplicate points
		for (Polygon2D p : shapes) {
			p.removeDuplicates(0.1f);
		}

		// remove duplicate polygons
		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D p = it.next();
			Vec2D cen = p.getCentroid();
			for (int i = 0; i < shapes.size(); i++) {
				Polygon2D pp = shapes.get(i);
				if (p != pp && cen.equalsWithTolerance(pp.getCentroid(), 0.05f)) { // quite a high tolerance but seems to be necessary for 'perfect' cleaning?..?
					it.remove();
				}
			}
		}
	}

	public void removeShapesOfVertexCount(int n) {
		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = it.next();
			int N = s.vertices.size();
			if (N == n) {
				it.remove();
			}
		}
	}

	public void onlyShapesOfVertexCount(int n) {
		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = (Polygon2D) it.next();
			int N = s.toPolygon2D().vertices.size();
			if (N != n) {
				it.remove();
			}
		}
	}

	public void removeShapes(float probability) {
		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = it.next();
			if (ARnd.rn(probability)) {
				it.remove();
			}
		}
	}

	/**
	 * Calculates the lattice vertices, removing all duplicates
	 */
	public ArrayList<Vec2D> calculatePoints() {
		points = new ArrayList<>();

		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = it.next();
			List<Vec2D> verts = s.vertices;
			for (Vec2D v : verts) {
				points.add(v.roundTo(0.01f));
			}
		}

		// Reduce
		HashSet<Vec2D> set = new HashSet<>(points);
		ArrayList<Vec2D> pts = new ArrayList<>(set);
		return pts;
	}

	/**
	 * Calculates the polygon centres, removing all duplicates. Accurate for regular polygons only (getCentre() used)
	 */
	public ArrayList<Vec2D> calculateCentrePoints() {
		points = new ArrayList<>();

		Iterator<Polygon2D> it = shapes.iterator();
		while (it.hasNext()) {
			Polygon2D s = it.next();
			points.add(s.getCentre());
		}
		HashSet<Vec2D> set = new HashSet<>(points);
		ArrayList<Vec2D> pts = new ArrayList<>(set);
		return pts;
	}

	public void rotate(float theta, Vec2D centre) {
		for (Polygon2D p : shapes) {
			for (Vec2D v : p) {
				v.set(centre.add(v.sub(centre).rotate(theta)));
			}
		}
	}

	public ArrayList<Vec2D> getPoints() {
		if (points == null) {
			return calculatePoints();
		} else {
			return points;
		}
	}

	public Tesselation2D copy() {
		return new Tesselation2D(shapes);
	}

	public float getWidth() {
		return bounds.width;
	}

	public float getHeight() {
		return bounds.height;
	}

	public Vec2D getCentre() {
		return bounds.getCentroid();
	}

	public int getPointCount() {
		return points.size();
	}

	public ArrayList<Polygon2D> getOriginalShapes() {
		return this.shapesOrig;
	}

	public ArrayList<Polygon2D> getShapes() {
		return this.shapes;
	}

	public void drawAllDetails(PApplet p5, ColorList clrs, boolean stroke, Vec2D translation) {
		if (stroke) {
			p5.stroke(255);
			p5.strokeWeight(0.5f);
		} else {
			p5.noStroke();
		}

		for (int i = 0; i < shapes.size(); i++) {
			Polygon2D sh = shapes.get(i);

			p5.fill(clrs.get(i % clrs.size()).toARGB());
			// p5.fill(clrs.get((int) p5.random(clrs.size())).toARGB());
			p5.beginShape();
			for (Vec2D v : sh.vertices) {
				p5.vertex(v.x + translation.x, v.y + translation.y);
			}
			p5.endShape(PApplet.CLOSE);
		}
	}

	public void drawAllDetails(PApplet p5, ColorList clrs, boolean stroke) {
		drawAllDetails(p5, clrs, stroke, new Vec2D());
	}

	public void draw(PApplet p5, Vec2D translation) {
		// p5.noStroke();
		p5.noFill();
		p5.stroke(255);
		for (int i = 0; i < shapes.size(); i++) {
			Polygon2D sh = shapes.get(i);
			p5.beginShape();
			for (Vec2D v : sh.vertices) {
				p5.vertex(v.x + translation.x, v.y + translation.y);
			}
			p5.endShape(PApplet.CLOSE);
		}
	}

	public void drawLines(PApplet p5, float wt, Vec2D translation) {
		p5.stroke(255);
		p5.strokeWeight(wt);
		p5.noFill();

		for (int i = 0; i < shapes.size(); i++) {
			Polygon2D sh = shapes.get(i);
			p5.beginShape();
			for (Vec2D v : sh.vertices) {
				p5.vertex(v.x + translation.x, v.y + translation.y);
			}
			p5.endShape(PApplet.CLOSE);
		}
	}

	public void drawLines(PApplet p5, float wt) {
		drawLines(p5, wt, new Vec2D());
	}
}