package examples.twister;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;
import toxi.geom.Vec3D;
import amalgam.colour.ColourSet;
import amalgam.colour.QuickColours;
import amalgam.geom.ConnectionType;
import amalgam.twister.Connection3D;
import amalgam.utils.AIO;
import amalgam.utils.ARnd;
import controlP5.ControlP5;

/**
 * Circular connections. Bezier connections. Simple xy step connections (maybe 45 deg steps?). Something with verlet springs perhaps?.. Constrain the connections according to x/y axis for consistency/better visuals
 */
@SuppressWarnings("serial")
public class Connection3DTest extends PApplet {
	PeasyCam cam;
	ControlP5 cp5;

	boolean savePDF = false;

	float spread = 1000;
	int ptCount = 15;
	Vec3D a; // base
	ArrayList<Connection3D> connections;
	ArrayList<ArrayList<Vec3D>> endpoints;

	ColorList clrs;

	int count = 0;
	int trailSize = 120;

	public void setup() {
		size(1920, 1080, P3D);
		background(0);
		smooth(8);
		strokeCap(ROUND);

		clrs = QuickColours.get(1000, ColourSet.getRandom());

		cam = new PeasyCam(this, 500);
		cp5 = new ControlP5(this);
		cp5.addFrameRate();
		cp5.setAutoDraw(false);
		endpoints = new ArrayList<>();

		a = new Vec3D(random(-300, 300), random(-300, 300), random(-300, 300));
		connections = new ArrayList<>();

		for (int i = 0; i < ptCount; i++) {
			a = new Vec3D(random(-800, 800), random(-800, 800), random(-800, 800));
			Vec3D b = Vec3D.randomVector()
					.scaleSelf(ARnd.fl(spread));
			Connection3D c = new Connection3D(this, a, b.copy(), ConnectionType.SQUARE, random(0.2f, 1.5f));
			c.setStyle(TColor.WHITE.copy(), TColor.CYAN.copy());
			c.setRotation(new Vec3D(random(-0.02f, 0.02f), random(-0.02f, 0.02f), random(-0.02f, 0.02f)));
			connections.add(c);
			for (int j = 0; j < 3; j++) {
				Vec3D bb = b.add(Vec3D.randomVector()
						.scaleSelf(ARnd.fl(spread / 3)));
				Connection3D cc = new Connection3D(this, b.copy(), bb.copy(), ConnectionType.SQUARE_MIDPOINT, random(0.2f, 1f));
				cc.setStyle(TColor.YELLOW.copy(), TColor.YELLOW.copy());
				cc.setRotation(new Vec3D(random(-0.04f, 0.04f), random(-0.04f, 0.04f), random(-0.04f, 0.04f)));
				c.addSubconnection(cc);
				for (int k = 0; k < 3; k++) {
					Vec3D bbb = bb.add(Vec3D.randomVector()
							.scaleSelf(ARnd.fl(spread / 5)));
					Connection3D ccc = new Connection3D(this, bb.copy(), bbb.copy(), ConnectionType.SQUARE_MIDPOINT, random(0.2f, 1f));
					ccc.setStyle(TColor.RED.copy(), TColor.RED.copy());
					ccc.setRotation(new Vec3D(random(-0.04f, 0.04f), random(-0.04f, 0.04f), random(-0.04f, 0.04f)));
					cc.addSubconnection(ccc);
					endpoints.add(new ArrayList<Vec3D>());
					for (int l = 0; l < 1; l++) {
						Vec3D bbbb = bbb.add(Vec3D.randomVector()
								.scaleSelf(ARnd.fl(spread / 6)));
						Connection3D cccc = new Connection3D(this, bbb.copy(), bbbb.copy(), ConnectionType.SQUARE_MIDPOINT, random(0.2f, 1f));
						cccc.setStyle(TColor.BLUE.copy(), TColor.BLUE.copy());
						cccc.setRotation(new Vec3D(random(-0.04f, 0.04f), random(-0.04f, 0.04f), random(-0.04f, 0.04f)));
						// cccc.setRotation(new Vec3D(random(-1f, 1f), random(-1f, 1f), random(-1f, 1f)));
						ccc.addSubconnection(cccc);
						endpoints.add(new ArrayList<Vec3D>());
					}
				}
			}
		}
	}

	public void draw() {
		if (savePDF) {
			AIO.startRawExport(this);
		}
		background(clrs.get(750)
				.toARGB());
		// lights();
		// directionalLight(255, 255, 255, 1, 1, -1);
		// directionalLight(200, 200, 200, -1, -1, 1);

		count = 0; // Rather grim use of global variable to handle tracking in recursive method
		traverse(connections);
		drawPolygons();

		if (savePDF) {
			savePDF = false;
			AIO.endRawExport(this);
		}
		// My3D.drawAxes(this);
	}

	public void traverse(ArrayList<Connection3D> cons) {
		for (Connection3D c : cons) {
			c.update();
			// c.render();
			if (c.getSubconnections()
					.size() > 0) {
				traverse(c.getSubconnections());
			} else {
				endpoints.get(count)
						.add(c.b.copy());
				if (endpoints.get(count)
						.size() > trailSize) {
					endpoints.get(count)
							.remove(0);
				}
				count++;
			}
		}
	}

	public void drawPolygons() {
		noFill();
		stroke(0, 255, 0);
		for (int j = 0; j < endpoints.size(); j++) {
			ArrayList<Vec3D> track = endpoints.get(j);
			beginShape(QUADS);
			for (int i = 0; i < track.size() - 1; i++) {
				Vec3D v = track.get(i);
				Vec3D vv = track.get(i + 1);

				stroke(ARnd.ind(clrs, i + frameCount)
						.toARGB(), 200);
				strokeWeight(0.5f + i / 50f);
				// line(v.x, v.y, v.z, vv.x, vv.y, vv.z);

				float sc = 0.65f + (i / trailSize * 3f);
				float sc2 = 0.65f + ((i + 1) / trailSize * 3f);
				// CosineInterpolation cos = new CosineInterpolation();
				// float sc = cos.interpolate(0, 1, (float) i / track.size());
				// float sc2 = cos.interpolate(0, 1, (float) (i + 1) / track.size());

				noStroke();
				// fill(Rnd.ind(clrs, i + frameCount).toARGB(), 200);
				fill(ARnd.ind(clrs, (int) (1000 * noise(j / 20f) + frameCount))
						.toARGB(), 255);
				vertex(v.x, v.y, v.z);
				vertex(vv.x, vv.y, vv.z);
				vertex(vv.scale(sc2).x, vv.scale(sc2).y, vv.scale(sc2).z);
				vertex(v.scale(sc).x, v.scale(sc).y, v.scale(sc).z);
				// renderQuad(v, vv, v.scale(sc), vv.scale(sc2), 30);
			}
			endShape(CLOSE);
		}
	}

	public void renderQuad(Vec3D a, Vec3D b, Vec3D aa, Vec3D bb, int lerps) {
		for (int i = 0; i < lerps; i++) {
			Vec3D c = a.interpolateTo(aa, (float) i / lerps);
			Vec3D cc = b.interpolateTo(bb, (float) i / lerps);
			Vec3D d = a.interpolateTo(aa, (float) (i + 1) / lerps);
			Vec3D dd = b.interpolateTo(bb, (float) (i + 1) / lerps);

			fill(ARnd.ind(clrs, i + frameCount)
					.toARGB(), 255 * ((float) i / lerps));
			vertex(c.x, c.y, c.z);
			vertex(cc.x, cc.y, cc.z);
			vertex(dd.x, dd.y, dd.z);
			vertex(d.x, d.y, d.z);
		}
	}
	public void keyPressed() {
		switch (key) {
			case 'p' :
				AIO.saveFramePNG(this);
				break;
			case 's' :
				savePDF = true;
				break;
		}
	}
}
