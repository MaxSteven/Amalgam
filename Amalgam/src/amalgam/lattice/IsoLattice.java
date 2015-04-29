package amalgam.lattice;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Polygon2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import amalgam.toxi.Hexagon2D;
import amalgam.toxi.Triangle2D_Eq;

/**
 * Progressing slowly. Defining the grid from the centre may be a problem - seems to be small offsets - fix this..
 */
public class IsoLattice implements Lattice2D {
	private Vec2D centre;
	private Vec2D topLeft;
	private Rect bounds;

	private float L; // eq tri side length
	private boolean vertical; // orientation - this isn't that nice.

	private ArrayList<Vec2D> pts;
	private Vec2D[][] ptArray;

	private ArrayList<Hexagon2D> hexes;
	private Hexagon2D[][] hexArray;

	private ArrayList<Triangle2D_Eq> tris;
	private Triangle2D_Eq[][] triArray; // TODO

	/**
	 * Base version with TL defined. No attempt to 'properly' centre anything
	 */
	public IsoLattice(float left, float top, float w, float h, float radius, boolean vertical) {
		this.centre = new Vec2D(left + w / 2, top + h / 2);
		this.bounds = new Rect(left, top, w, h);
		this.topLeft = new Vec2D(left, top);
		createLattice(topLeft, w, h, radius, vertical);
	}

	public IsoLattice(Vec2D centre, float w, float h, float radius, boolean vertical) {
		this.centre = centre.copy();
		this.bounds = new Rect(centre.x - w / 2, centre.y - h / 2, w, h);

		// 'Properly' centre the grid. This will misalign multiple grids placed on top of each other though, which isn't ideal? Leave off for now
		float offX = 0;
		float offY = 0;
		if (!vertical) {
			offX = 0.5f * (w % (radius * PApplet.sqrt(3) / 2));
			offY = 0.5f * (h % (radius / 2));
		} else {
			offX = 0.5f * (w % (radius / 2));
			offY = 0.5f * (h % (radius * PApplet.sqrt(3) / 2));
		}

		this.topLeft = centre.sub((w / 2 - offX), (h / 2 - offY));
		createLattice(centre.subSelf((w / 2 - offX), (h / 2 - offY)), w, h, radius, vertical);
	}

	/**
	 * Equilateral length given... This should probably be the default? Can re-engineer for non-eq cases at a later date. The grid is extending outside the bounds for the zigzag part... fix this
	 */
	private void createLattice(Vec2D topLeft, float w, float h, float radius, boolean vertical) {
		this.L = radius;
		this.vertical = vertical;
		this.pts = new ArrayList<Vec2D>();

		float rSin = radius / 2;
		float rCos = radius * (PApplet.sqrt(3) / 2);

		// vertical boolean defines the orientation of the grid
		if (vertical) {
			for (float i = topLeft.x; i <= topLeft.x + w; i += 2 * rSin) {
				for (float j = topLeft.y; j <= topLeft.y + h; j += 2 * rCos) {
					pts.add(new Vec2D(i, j));
				}
			}
			for (float i = topLeft.x + rSin; i <= topLeft.x + w; i += 2 * rSin) {
				for (float j = topLeft.y + rCos; j <= topLeft.y + h; j += 2 * rCos) {
					pts.add(new Vec2D(i, j));
				}
			}

			this.ptArray = new Vec2D[(int) (w / radius) + 1][(int) (h / rCos) + 1];
			for (int i = 0; i < ptArray.length; i++) {
				for (int j = 0; j < ptArray[0].length; j++) {
					ptArray[i][j] = new Vec2D(topLeft.x + i * radius + ((j % 2 == 0) ? 0 : rSin), topLeft.y + j * rCos);
				}
			}
		} else {
			for (float i = topLeft.x; i <= topLeft.x + w; i += 2 * rCos) {
				for (float j = topLeft.y; j <= topLeft.y + h; j += 2 * rSin) {
					pts.add(new Vec2D(i, j));
				}
			}
			for (float i = topLeft.x + rCos; i <= topLeft.x + w; i += 2 * rCos) {
				for (float j = topLeft.y + rSin; j <= topLeft.y + h; j += 2 * rSin) {
					pts.add(new Vec2D(i, j));
				}
			}

			this.ptArray = new Vec2D[(int) (w / rCos) + 1][(int) (h / radius) + 1];
			for (int i = 0; i < ptArray.length; i++) {
				for (int j = 0; j < ptArray[0].length; j++) {
					ptArray[i][j] = new Vec2D(topLeft.x + i * rCos, topLeft.y + j * radius + ((i % 2 == 0) ? 0 : rSin));
				}
			}
		}

		createTriangles();
		createHexagons();
	}

	private void createHexagons() {
		// One hexagon per grid point - fairly simple compared to the tris.
		hexArray = new Hexagon2D[ptArray.length][ptArray[0].length];
		hexes = new ArrayList<>();

		float r = (float) (L * Math.sqrt(3) / 3); // hex radius

		for (int i = 0; i < pts.size(); i++) {
			Vec2D p = pts.get(i);
			hexes.add(new Hexagon2D(p.copy(), r, vertical));
		}

		for (int i = 0; i < ptArray.length; i++) {
			for (int j = 0; j < ptArray[0].length; j++) {
				Vec2D p = ptArray[i][j];
				hexArray[i][j] = new Hexagon2D(p.copy(), r, vertical);
			}
		}
	}

	private void createTriangles() {
		// Blah... TODO
		// int tot = 2 * ((ptArray.length - 1) * (ptArray[0].length - 1));
		// int _y = ptArray[0].length - 1;
		// int _x = tot / _y;
		//
		// System.out.println(_x + ", " + _y);
		// triArray = new Triangle2D_Eq[_x][_y];
		// for (int i = 0; i < ptArray.length / 2 + 1; i++) {
		// for (int j = 0; j < ptArray[0].length - 1; j++) {
		// Vec2D v = ptArray[i][j];
		// Vec2D v2 = ptArray[i + 1][j];
		// Vec2D v3 = ptArray[i + 1][j + 1];
		// Vec2D v4 = ptArray[i][j + 1];
		//
		// try {
		// triArray[i][j] = new Triangle2D_Eq(v, v2, v4, false);
		// triArray[i + 1][j] = new Triangle2D_Eq(v2, v3, v4, true);
		// } catch (Exception e) {
		// // System.out.println("Attempted triangle creation - supplied coordinates do not produce an equilateral triangle");
		// }
		// try {
		// triArray[i][j] = new Triangle2D_Eq(v, v2, v3, false);
		// triArray[i + 1][j] = new Triangle2D_Eq(v, v3, v4, true);
		// } catch (Exception e) {
		// // System.out.println("Attempted triangle creation - supplied coordinates do not produce an equilateral triangle");
		// }
		// }
		// }

		tris = new ArrayList<>();
		for (int i = 0; i < ptArray.length - 1; i++) {
			for (int j = 0; j < ptArray[0].length - 1; j++) {
				Vec2D v = ptArray[i][j];
				Vec2D v2 = ptArray[i + 1][j];
				Vec2D v3 = ptArray[i + 1][j + 1];
				Vec2D v4 = ptArray[i][j + 1];

				// Horribly ugly but works... TODO fix this up
				try {
					tris.add(new Triangle2D_Eq(v, v2, v4, false));
					tris.add(new Triangle2D_Eq(v2, v3, v4, true));
				} catch (Exception e) {
					// System.out.println("Attempted triangle creation - supplied coordinates do not produce an equilateral triangle");
				}
				try {
					tris.add(new Triangle2D_Eq(v, v2, v3, false));
					tris.add(new Triangle2D_Eq(v, v3, v4, true));
				} catch (Exception e) {
					// System.out.println("Attempted triangle creation - supplied coordinates do not produce an equilateral triangle");
				}
			}
		}
	}

	public void subdivide() {
		createLattice(topLeft, bounds.width, bounds.height, L / 2, vertical);
		createTriangles();
		createHexagons();
	}

	@Override
	public Tesselation2D toTesselation() {
		ArrayList<Polygon2D> shapes = new ArrayList<>();
		for (Triangle2D_Eq t : tris) {
			shapes.add(t.toPolygon2D());
		}
		return new Tesselation2D(shapes, bounds);
	}

	public Tesselation2D toHexTesselation() {
		ArrayList<Polygon2D> shapes = new ArrayList<>();
		for (Hexagon2D h : hexes) {
			shapes.add(h.toPolygon2D());
		}
		return new Tesselation2D(shapes, bounds);
	}

	// Drawing Lattice
	public void drawAllDetails(PApplet p5, TColor clr) {
		p5.strokeWeight(1f);

		for (int i = 0; i < tris.size(); i++) {
			Triangle2D_Eq t = tris.get(i);
			p5.noStroke();
			p5.fill((5 * i) % 255);
			p5.triangle(t.a.x, t.a.y, t.b.x, t.b.y, t.c.x, t.c.y);
		}

		p5.stroke(0, 0, 255);
		for (int i = 0; i < ptArray.length; i++) {
			for (int j = 0; j < ptArray[0].length; j++) {
				p5.point(ptArray[i][j].x, ptArray[i][j].y);
				p5.fill(0, 0, 255);
				p5.text(i + "," + j, ptArray[i][j].x + 5, ptArray[i][j].y);
			}
		}
		p5.stroke(255, 0, 0);
		p5.noFill();
		p5.point(centre.x, centre.y);
		p5.rect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void drawWireframe(PApplet p5, TColor clr) {
		p5.noFill();
		p5.stroke(clr.toARGB());

		for (int i = 0; i < tris.size(); i++) {
			Triangle2D_Eq t = tris.get(i);
			p5.triangle(t.a.x, t.a.y, t.b.x, t.b.y, t.c.x, t.c.y);
		}
	}

	@Override
	public void drawPoints(PApplet p5, TColor clr) {
		p5.noFill();
		p5.stroke(clr.toARGB());

		for (int i = 0; i < ptArray.length; i++) {
			for (int j = 0; j < ptArray[0].length; j++) {
				p5.point(ptArray[i][j].x, ptArray[i][j].y);
			}
		}
	}

	// ======= Getters/Setters

	public float getDivisor() {
		return L;
	}

	public Vec2D[][] getPointArray() {
		return ptArray;
	}

	public Triangle2D_Eq[][] getTriArray() {
		return triArray;
	}

	public Hexagon2D[][] getHexArray() {
		return hexArray;
	}

	public ArrayList<Hexagon2D> getHexes() {
		return hexes;
	}

	public ArrayList<Triangle2D_Eq> getTris() {
		return tris;
	}

	@Override
	public float getWidth() {
		return bounds.width;
	}

	@Override
	public float getHeight() {
		return bounds.height;
	}

	@Override
	public Vec2D getCentre() {
		return centre;
	}

	@Override
	public Rect getBounds() {
		return bounds;
	}

	@Override
	public int getPointCount() {
		return pts.size();
	}

	@Override
	public ArrayList<Vec2D> getPoints() {
		return pts;
	}
}