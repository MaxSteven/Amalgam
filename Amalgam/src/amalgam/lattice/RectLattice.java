package amalgam.lattice;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Polygon2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

public class RectLattice implements Lattice2D {
	private Vec2D centre;
	private Vec2D topLeft;
	private Rect bounds;

	private float divX, divY;

	private ArrayList<Vec2D> pts;
	private Vec2D[][] ptArray;

	private Rect[][] rectArray;
	private ArrayList<Rect> rects;

	public RectLattice(Vec2D centre, float w, float h, Vec2D divXY) {
		this.centre = centre.copy();
		this.bounds = new Rect(centre.x - w / 2, centre.y - h / 2, w, h);

		// calc offset for TL to maintain exact centre
		float offX = 0.5f * (w % divXY.x);
		float offY = 0.5f * (h % divXY.y);
		this.topLeft = centre.sub((w / 2 - offX), (h / 2 - offY));

		createLattice(centre.subSelf((w / 2 - offX), (h / 2 - offY)), w, h, divXY.x, divXY.y);
	}

	public RectLattice(Vec2D centre, float w, float h, float div) {
		this(centre, w, h, new Vec2D(div, div));
	}

	// reduce this to call the first constructor?
	public RectLattice(Vec2D centre, float w, float h, int segmentsX, int segmentsY) {
		this.centre = centre.copy();
		this.bounds = new Rect(centre.x - w / 2, centre.y - h / 2, w, h);

		// This will need more work TODO
		float offX = 0.5f * (w % divX);
		float offY = 0.5f * (h % divY);
		this.topLeft = centre.sub((w / 2 - offX), (h / 2 - offY));

		createLattice(centre.subSelf(w / 2, h / 2), w, h, divX, divY);
	}

	private void createLattice(Vec2D topLeft, float w, float h, float divX, float divY) {
		this.divX = divX;
		this.divY = divY;
		this.pts = new ArrayList<Vec2D>();
		for (float i = topLeft.x; i <= topLeft.x + w; i += divX) {
			for (float j = topLeft.y; j <= topLeft.y + h; j += divY) {
				pts.add(new Vec2D(i, j));
			}
		}

		this.ptArray = new Vec2D[(int) (w / divX) + 1][(int) (h / divY) + 1]; // +1 is needed on these
		for (int i = 0; i < ptArray.length; i++) {
			for (int j = 0; j < ptArray[0].length; j++) {
				ptArray[i][j] = new Vec2D(topLeft.x + i * divX, topLeft.y + j * divY);
			}
		}
		createRects(topLeft, w, h, divX, divY);
	}

	private void createRects(Vec2D topLeft, float w, float h, float divX, float divY) {
		this.rectArray = new Rect[(int) (w / divX)][(int) (h / divY)];
		this.rects = new ArrayList<>();

		for (int i = 0; i < rectArray.length; i++) {
			for (int j = 0; j < rectArray[0].length; j++) {
				Rect r = new Rect(topLeft.x + i * divX, topLeft.y + j * divY, divX, divY);
				rectArray[i][j] = r;
				rects.add(r);
			}
		}
	}

	public void subdivide() {
		createLattice(topLeft, bounds.width, bounds.height, divX / 2, divY / 2);
	}

	public Tesselation2D toTesselation() {
		ArrayList<Polygon2D> polys = new ArrayList<>();
		for (int i = 0; i < rects.size(); i++) {
			Polygon2D p = rects.get(i).toPolygon2D();
			polys.add(p);
		}
		return new Tesselation2D(polys);
	}

	// Drawing the Lattice
	@Override
	public void drawWireframe(PApplet p5, TColor clr) {
		p5.rectMode(PApplet.CORNER);
		p5.noFill();
		p5.stroke(clr.toARGB());
		for (int i = 0; i < rectArray.length; i++) {
			for (int j = 0; j < rectArray[0].length; j++) {
				Rect r = rectArray[i][j];
				p5.rect(r.x, r.y, r.width, r.height);
			}
		}
	}

	public void drawAllDetails(PApplet p5, TColor clr) {
		drawRects(p5);
		p5.noFill();
		p5.stroke(clr.toARGB());
		for (Vec2D v : pts) {
			p5.point(v.x, v.y);
		}

		p5.stroke(255, 0, 0);
		p5.noFill();
		p5.point(centre.x, centre.y);
		p5.rect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void drawPoints(PApplet p5, TColor clr) {
		p5.noFill();
		p5.stroke(clr.toARGB());
		for (Vec2D v : pts) {
			p5.point(v.x, v.y);
		}
	}

	public void drawRects(PApplet p5) {
		p5.rectMode(PApplet.CORNER);
		p5.noStroke();
		for (int i = 0; i < rectArray.length; i++) {
			for (int j = 0; j < rectArray[0].length; j++) {
				p5.fill((i * j) % 255);
				Rect r = rectArray[i][j];
				p5.rect(r.x, r.y, r.width, r.height);
				p5.fill(0, 0, 255);
				p5.text(i + "," + j, ptArray[i][j].x + 5, ptArray[i][j].y);
			}
		}
	}

	public Rect[][] getRectArray() {
		return rectArray;
	}

	public ArrayList<Rect> getRects() {
		return rects;
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

	public Vec2D[][] getPointArray() {
		return ptArray;
	}
}