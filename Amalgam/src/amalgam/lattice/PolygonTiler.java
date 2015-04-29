package amalgam.lattice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.ColorList;
import toxi.color.TColor;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

/**
 * Offset away from 0,0 of the original shape messes up repeats - fixed by using a global offset when rendering. equalsWithTolerance() is very slow. Still getting shape repeats?
 */
public class PolygonTiler {
	private final PApplet p5;

	private ArrayList<Polygon2D> duplicatedPolygons; // list of depth 0 poly set
	private ArrayList<Polygon2D> duals;
	private HashMap<Vec2D, Polygon2D> polys; // full poly list

	private Vec2D centre;
	private ColorList clrs;

	public PolygonTiler(PApplet p5, Vec2D centre) {
		this.p5 = p5;
		this.centre = centre;
		duplicatedPolygons = new ArrayList<>();
		polys = new HashMap<>();

		clrs = new ColorList();
		clrs.add(TColor.RED.copy());
	}

	public PolygonTiler(PApplet p5, Vec2D centre, ColorList clrs) {
		this.p5 = p5;
		this.centre = centre;
		this.clrs = clrs;
		duplicatedPolygons = new ArrayList<>();
		polys = new HashMap<>();
	}

	// =================================================
	// Factory method for quick tiling creation
	// =================================================
	public static PolygonTiler quickTile(PApplet p5, Vec2D centre, float size, int type, int reps) {
		PolygonTiler tiling = new PolygonTiler(p5, new Vec2D(p5.width / 2f, p5.height / 2f));
		int[] a, b, c, d;

		switch (type) {
			case 0 :
				// 3.3.3.3.3.3
				tiling.append(Polygon2D.fromEdgeLength(size, 3).rotate(PApplet.PI / 6));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2}, 3);
				b = tiling.add(a, new int[]{1, 2}, 3);
				tiling.repeat(b, reps);
				break;
			case 1 :
				// 4.6.12
				tiling.append(Polygon2D.fromEdgeLength(size, 12).rotate(PApplet.PI / 12));
				a = tiling.add(new int[]{0}, new int[]{0, 2, 4, 6, 8, 10}, 6);
				b = tiling.add(new int[]{0}, new int[]{1, 3, 5, 7, 9, 11}, 4);
				c = tiling.add(b, new int[]{2}, 12);
				tiling.repeat(c, reps);
				break;
			case 2 :
				// 6.6.6
				tiling.append(Polygon2D.fromEdgeLength(size, 6));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2, 3, 4, 5}, 6);
				b = tiling.add(a, new int[]{2, 3}, 6);
				tiling.repeat(b, reps);
				break;
			case 3 :
				// 4.4.4.4
				tiling.append(Polygon2D.fromEdgeLength(size, 4));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2, 3}, 4);
				b = tiling.add(a, new int[]{1, 2}, 4);
				tiling.repeat(b, reps);
				break;
			case 4 :
				// 3.6.3.6
				tiling.append(Polygon2D.fromEdgeLength(size, 6));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2, 3, 4, 5}, 3);
				b = tiling.add(a, new int[]{1}, 6);
				tiling.repeat(b, reps);
				break;
			case 5 :
				// 3.3.4.3.4
				tiling.append(Polygon2D.fromEdgeLength(size, 4).rotate(PApplet.PI / 4));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2, 3}, 3);
				b = tiling.add(a, new int[]{1}, 4);
				c = tiling.add(b, new int[]{2, 3}, 3);
				d = tiling.add(c, new int[]{2}, 4);
				tiling.repeat(d, reps);
				break;
			case 6 :
				// 3.3.3.3.6
				tiling.append(Polygon2D.fromEdgeLength(size, 6));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2, 3, 4, 5}, 3);
				b = tiling.add(a, new int[]{1}, 3);
				c = tiling.add(a, new int[]{2}, 3);
				d = tiling.add(c, new int[]{1}, 6);
				tiling.repeat(d, reps);
				break;
			case 7 :
				// 4.8.8
				tiling.append(Polygon2D.fromEdgeLength(size, 8).rotate(PApplet.PI / 8));
				a = tiling.add(new int[]{0}, new int[]{1, 3, 5, 7}, 4);
				b = tiling.add(a, new int[]{1}, 8);
				tiling.repeat(b, reps);
				break;
			case 8 :
				// 3.3.4.4
				tiling.append(Polygon2D.fromEdgeLength(size, 4).rotate(PApplet.PI / 4));
				a = tiling.add(new int[]{0}, new int[]{0, 2}, 4);
				a = Arrays.copyOf(a, a.length + 1);
				b = tiling.add(a, new int[]{1, 3}, 3);
				c = tiling.add(b, new int[]{1}, 3);
				d = tiling.add(c, new int[]{2}, 4);
				tiling.repeat(d, reps);
				break;
			case 9 :
				// 3.4.6.4
				tiling.append(Polygon2D.fromEdgeLength(size, 6));
				a = tiling.add(new int[]{0}, new int[]{0, 1, 2, 3, 4, 5}, 4);
				b = tiling.add(a, new int[]{1}, 3);
				c = tiling.add(a, new int[]{2}, 6);
				tiling.repeat(c, reps);
				break;
			case 10 :
				// 3.12.12
				tiling.append(Polygon2D.fromEdgeLength(size, 12).rotate(PApplet.PI / 12));
				a = tiling.add(new int[]{0}, new int[]{0, 2, 4, 6, 8, 10}, 3);
				b = tiling.add(new int[]{0}, new int[]{1, 3, 5, 7, 9, 11}, 12);
				tiling.repeat(b, reps);
				break;
			default :
				// 4.8.8
				tiling.append(Polygon2D.fromEdgeLength(size, 8).rotate(PApplet.PI / 8));
				a = tiling.add(new int[]{0}, new int[]{1, 3, 5, 7}, 4);
				b = tiling.add(a, new int[]{1}, 8);
				tiling.repeat(b, reps);
				break;
		}
		return tiling;
	}

	// =================================================
	// Creation
	// =================================================
	// intialise the tesselation
	public void append(Polygon2D sh) {
		append(sh, TColor.WHITE.copy());
	}

	public void append(Polygon2D sh, TColor clr) {
		sh.center();
		sh.setColour(clr);
		duplicatedPolygons.add(sh);
	}

	int phase = 1; // used for for colours?
	// add new phase
	public int[] add(int[] addTo, int[] edgesAttached, int newShapeSides) {
		return add(addTo, edgesAttached, newShapeSides, TColor.WHITE.copy());
	}

	public int[] add(int[] addTo, int[] edgesAttached, int newShapeSides, TColor clr) {
		int start = duplicatedPolygons.size();
		for (int i : addTo) {
			for (int j : edgesAttached) {
				_add(i, j, newShapeSides, clr);
			}
		}
		int end = duplicatedPolygons.size();

		int[] range = new int[end - start];
		int t = start;
		for (int i = 0; i < range.length; i++) {
			range[i] = t;
			t++;
		}
		phase++;
		return range;
	}

	// add each new poly - now with colour setting
	Polygon2D parent, poly;
	Line2D line;
	private void _add(int index, int edge, int sides, TColor clr) {
		parent = duplicatedPolygons.get(index);
		line = parent.getEdges().get(edge);
		if (parent.isClockwise()) {
			poly = Polygon2D.fromBaseEdge(line.a, line.b, sides);
		} else {
			poly = Polygon2D.fromBaseEdge(line.b, line.a, sides);
		}
		poly.setColour(clr);
		duplicatedPolygons.add(poly);
		polys.put(poly.getCentroid(), poly); // put poly in lookup (with centroid?)
	}

	// =================================================
	// Repetition
	// =================================================
	// Python code repeats by copying shapes to new centers?

	public void repeat(int[] indexes, int d) {
		HashMap<Vec2D, Integer> memo = new HashMap<>();
		int depth = 0;
		while (depth < d) {
			_repeat(indexes, new Vec2D(0, 0), depth, memo);
			depth += 1;
		}
	}

	// default, 8 repeats
	public void repeat(int[] indexes) {
		repeat(indexes, 8);
	}

	private void _repeat(int[] indexes, Vec2D xy, int depth, HashMap<Vec2D, Integer> memo) {
		if (depth < 0) {
			return;
		}
		Vec2D key = xy;
		int previousDepth = (memo.get(key) != null) ? memo.get(key) : -1;
		if (previousDepth >= depth) {
			return;
		}
		memo.put(key, depth);
		if (previousDepth == -1) {
			addRepeats(xy);
		}
		for (int in : indexes) {
			Polygon2D poly = duplicatedPolygons.get(in);
			_repeat(indexes, xy.add(poly.getCentre()), depth - 1, memo);
		}
	}

	private void addRepeats(Vec2D xy) {
		boolean addRepeat;

		for (Polygon2D poly : duplicatedPolygons) {
			Vec2D key = xy.add(poly.getCentre());
			addRepeat = true;
//			if (polys.containsKey(key)) {
//				addRepeat = false;
//			}
			for (Vec2D k : polys.keySet()) {
				if (k.equalsWithTolerance(key, 0.001f)) { // suitable tolerance for new shapes. Slow but accurate - prevents overlaps.
					addRepeat = false;
				}
			}
			if (addRepeat) {
				Polygon2D pp = poly.copy().setColour(poly.getColour()).translate(xy.x, xy.y);
				polys.put(key, pp); // patterns work fine provided the tesselation starts at (0,0)
			}
		}
	}

	// =================================================
	// Dualling
	// =================================================
	public void dual() {
		HashMap<Vec2D, ArrayList<Polygon2D>> vertices = new HashMap<>();

		// Takes each poly vertex. Checks vertex hashmap to see if the vertex already exists. If so, add the current poly to the vertex poly list. If not, create a new entry with the current poly.
		for (Polygon2D poly : polys.values()) {
			for (int i = 0; i < poly.vertices.size(); i++) {

				Vec2D key = poly.vertices.get(i);
				Vec2D dupeKey = null;
				boolean keyIsUnique = true;

				for (Vec2D k : vertices.keySet()) {
					if (key.equalsWithTolerance(k, 0.001f)) { // suitable tolerance for new shapes
						keyIsUnique = false;
						dupeKey = k;
						break;
					}
				}

				if (keyIsUnique) {
					ArrayList<Polygon2D> initialise = new ArrayList<>();
					initialise.add(poly);
					vertices.put(key, initialise);
				} else {
					vertices.get(dupeKey).add(poly);
				}
			}
		}

		// Take each vertex-poly pair. Sort list of polygon centers according to angle from vertex. Create new polygon from these centers.
		duals = new ArrayList<>();
		for (Map.Entry<Vec2D, ArrayList<Polygon2D>> entry : vertices.entrySet()) {
			final Vec2D key = entry.getKey(); // apparently needs to be final?
			ArrayList<Polygon2D> polys = entry.getValue();

			if (polys.size() > 2) {
				ArrayList<Vec2D> polyCentres = new ArrayList<Vec2D>();
				for (Polygon2D poly : polys) {
					polyCentres.add(poly.getCentre());
				}
				// Sort according to angle between vertex and poly centre
				polyCentres.sort(new Comparator<Vec2D>() {
					public int compare(Vec2D o1, Vec2D o2) {
						float a = o1.sub(key).heading();
						if (a < 0) {
							a += PApplet.TAU;
						}
						float b = o2.sub(key).heading();
						if (b < 0) {
							b += PApplet.TAU;
						}
						return Float.compare(a, b);
					}
				});
				duals.add(new Polygon2D(polyCentres).setColour(TColor.newRandom()).removeDuplicates(0.001f)); // how costly is this? TODO
			}
		}
	}

	// =================================================
	// Rendering
	// =================================================
	public void render() {
		p5.pushMatrix();
		p5.translate(centre.x, centre.y);

		p5.stroke(255);
		p5.noStroke();

		Collection<Polygon2D> values = polys.values();
		for (Polygon2D p : values) {
			p5.fill(p.getColour().toARGB());
			p5.beginShape();
			for (int j = 0; j < p.vertices.size(); j++) {
				Vec2D a = p.vertices.get(j);
				p5.vertex(a.x, a.y);
			}
			p5.endShape(PConstants.CLOSE);
		}
		p5.popMatrix();
	}

	public void renderDual() {
		p5.pushMatrix();
		p5.translate(centre.x, centre.y);

		p5.stroke(255);
		p5.noStroke();

		for (int i = 0; i < duals.size(); i++) {
			Polygon2D d = duals.get(i);
			p5.fill(d.getColour().toARGB());
			p5.beginShape();
			for (int j = 0; j < d.vertices.size(); j++) {
				Vec2D a = d.vertices.get(j);
				p5.vertex(a.x, a.y);
			}
			p5.endShape(PConstants.CLOSE);

		}
		p5.popMatrix();
	}

	// =================================================
	// Get/Set
	// =================================================
	public PolygonTiler setColours(ColorList clrs) {
		this.clrs = clrs;
		return this;
	}

	public PolygonTiler setCentre(Vec2D centre) {
		this.centre = centre;
		return this;
	}

	public Vec2D getCentre() {
		return centre;
	}

	public int getPolyCount() {
		return polys.size();
	}

	public ArrayList<Polygon2D> getPolygons() {
		ArrayList<Polygon2D> pp = new ArrayList<>();
		for (Polygon2D p : polys.values()) {
			pp.add(p);
		}
		return pp;
	}

	public ArrayList<Polygon2D> getDualPolygons() {
		dual();
		return duals;
	}

	public void constrictTo(Rect bounds) {
		Iterator<Vec2D> it = polys.keySet().iterator();
		while (it.hasNext()) {
			Vec2D c = it.next();
			if (!bounds.containsPoint(c.add(centre))) {
				it.remove();
			}
		}
	}

	public Tesselation2D toTesselation() {
		ArrayList<Polygon2D> shapes = new ArrayList<>();

		for (Polygon2D p : polys.values()) {
			// centre immediately after creation? Or leave this up to the Tesselation instead?
			Polygon2D pp = p.copy();
			for (Vec2D v : pp) {
				v.addSelf(centre);
			}
			shapes.add(pp);
		}
		Tesselation2D tess = new Tesselation2D(shapes);
		return tess;
	}

	public Tesselation2D toDualTesselation() {
		dual();
		ArrayList<Polygon2D> shapes = new ArrayList<>();

		for (Polygon2D p : duals) {
			// centre immediately after creation?
			Polygon2D pp = p.copy();
			for (Vec2D v : pp) {
				v.addSelf(centre);
			}
			shapes.add(pp);
		}
		Tesselation2D tess = new Tesselation2D(shapes);
		return tess;
	}

	// Clear for reuse
	public void clear() {
		setCentre(new Vec2D(0, 0));
		duplicatedPolygons.clear();
		polys.clear();
		if (duals != null) {
			duals.clear();
		}
	}
}
