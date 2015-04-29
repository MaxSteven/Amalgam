package amalgam.twister;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Line3D;
import toxi.geom.Vec3D;
import toxi.math.noise.SimplexNoise;
import amalgam.geom.ConnectionType;
import amalgam.utils.ARnd;

/**
 * Add methods for connection style. Dotted/segmented connections perhaps? Tweening???
 */
public class Connection3D {
	private final PApplet p5;

	public Vec3D a, b;
	private float diffOrig;
	public Vec3D t; // tweened

	// Style
	private ConnectionType type;
	private float weight;
	private TColor arcClr, nodeClr;

	// Subconnections & movement
	private ArrayList<Connection3D> subconnections; // Is this sensible...?
	private Vec3D rotationXYZ; // try to implement all 3?

	public Connection3D(PApplet p5, Vec3D a, Vec3D b, ConnectionType t) {
		this(p5, a, b, t, 1);
	}

	public Connection3D(PApplet p5, Vec3D a, Vec3D b, ConnectionType t, float weight) {
		this.p5 = p5;

		this.a = a;
		this.b = b;
		this.weight = weight;

		this.diffOrig = a.distanceTo(b);

		// Style defaults
		this.arcClr = TColor.RED.copy();
		this.nodeClr = TColor.BLUE.copy();

		this.type = t;

		subconnections = new ArrayList<>();
		rotationXYZ = new Vec3D();
	}

	public void update() {
		updatePositions();
	}

	private void updatePositions() {
		float mag = (float) (diffOrig * (0.8f + 0.2f * 0.5f * (1 + SimplexNoise.noise(p5.frameCount / 80f, (b.x + b.y + b.z) / 4000f))));
		// float mag = diffOrig;

		// curently does rotation. Handle other movement?
		b.set(a.add(b.sub(a).rotateX(rotationXYZ.x).rotateY(rotationXYZ.y).rotateZ(rotationXYZ.z).normalizeTo(mag)));
		for (Connection3D c : subconnections) {
			Vec3D diff = b.sub(c.a);
			c.a.set(b);
			c.b.addSelf(diff);
		}
	}

	public void render() {
		renderArcs();
		renderNodes();
	}

	/**
	 * Draw the connection.
	 */
	public void renderArcs() {
		p5.ellipseMode(PApplet.RADIUS);
		p5.noFill();
		p5.strokeWeight(2 * weight);
		p5.stroke(arcClr.toARGB());

		Vec3D mid = new Line3D(a, b).getMidPoint();
		Line3D ab = new Line3D(a, b);
		float dist = a.distanceTo(b);

		switch (type) {
			case LINEAR : // linear
				p5.line(a.x, a.y, a.z, b.x, b.y, b.z);
				break;
			case SQUARE : // sq edge (rand?)
				p5.beginShape();
				p5.vertex(a.x, a.y, a.z);
				p5.vertex(b.x, a.y, b.z);
				p5.vertex(b.x, b.y, b.z);
				p5.endShape();
				break;
			case SQUARE_MIDPOINT : // sq edge through midpoint (rand?)
				p5.beginShape();
				if (ARnd.rn(0.0f)) {
					p5.vertex(a.x, a.y, a.z);
					p5.vertex(a.x, mid.y, mid.z);
					p5.vertex(b.x, mid.y, mid.z);
					p5.vertex(b.x, b.y, b.z);
				} else {
					p5.vertex(a.x, a.y, a.z);
					p5.vertex(a.x, mid.y, a.z);
					p5.vertex(b.x, mid.y, b.z);
					p5.vertex(b.x, b.y, b.z);
				}
				p5.endShape();
				break;
			case ARC_2 :

				break;
		}
	}

	public void renderNodes() { // could even add SVGs here? or would using Unfolding Markers really make more sense for that eventually...?
		p5.rectMode(PApplet.CENTER);
		float dotSize = 6;

		p5.noStroke();
		p5.fill(nodeClr.toARGB());

		p5.pushMatrix();
		p5.translate(a.x, a.y, a.z);
		p5.box(dotSize);
		p5.popMatrix();

		p5.pushMatrix();
		p5.translate(b.x, b.y, b.z);
		p5.box(dotSize);
		p5.popMatrix();
	}

	public void setA(Vec3D a) {
		this.a = a;
	}

	public void setB(Vec3D b) {
		this.a = b;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setStyle(TColor arcClr, TColor nodeClr) {
		this.arcClr = arcClr;
		this.nodeClr = nodeClr;
	}

	// Subconnections... How shall I do this nicely?
	public void addSubconnection(Connection3D con) {
		subconnections.add(con);
	}

	public ArrayList<Connection3D> getSubconnections() {
		return subconnections;
	}

	public void setRotation(Vec3D rotationXYZ) {
		this.rotationXYZ = rotationXYZ; // radians
	}

	public void rotateBA(Vec3D rotation) {
		b.set(a.add(b.sub(a).rotateX(rotation.x).rotateY(rotation.y).rotateY(rotation.z)));
		for (Connection3D c : subconnections) {
			Vec3D diff = b.sub(c.a);
			c.a.set(b);
			c.b.addSelf(diff);
		}
	}
}