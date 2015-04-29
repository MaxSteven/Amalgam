package amalgam.lattice;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

/**
 *	Interface for creating regular ordered lattices
 */
public interface Lattice2D {

	public float getWidth();

	public float getHeight();

	public Vec2D getCentre();

	public Rect getBounds();

	public int getPointCount();

	public ArrayList<Vec2D> getPoints();
	
	public Tesselation2D toTesselation();
	
	public void drawPoints(PApplet p5, TColor clr);

	public void drawWireframe(PApplet p5, TColor clr);
	
	public void drawAllDetails(PApplet p5, TColor clr);	
}
