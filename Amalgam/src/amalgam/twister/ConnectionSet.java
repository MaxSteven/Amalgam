package amalgam.twister;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.ColorList;
import toxi.color.TColor;
import toxi.geom.Vec3D;
import amalgam.audio.SoundData;
import amalgam.colour.ColourSet;
import amalgam.colour.QuickColours;
import amalgam.colour.WeightedGradient;
import amalgam.geom.ConnectionType;
import amalgam.utils.ARnd;

public class ConnectionSet {
	private final PApplet p5;

	float spread = 300;
	int ptCount;
	Vec3D a; // base
	ArrayList<Connection3D> connections;
	ArrayList<ArrayList<Vec3D>> endpoints;

	ColorList clrs;

	int count = 0;
	int trailSize = 80;

	SoundData sData;

	public ConnectionSet(PApplet p5, int count, ColorList clrs) {
		this.p5 = p5;

		this.clrs = clrs;
		// initColours();

		this.ptCount = count;
		connections = new ArrayList<>();
		endpoints = new ArrayList<>();

		for (int i = 0; i < ptCount; i++) {
			a = new Vec3D(p5.random(-100, 100), p5.random(-100, 100), p5.random(-150, 50));
			// a = new Vec3D(0, 0, 0);
			Vec3D b = Vec3D.randomVector().scaleSelf(ARnd.fl(spread));
			Connection3D c = new Connection3D(p5, a, b.copy(), ConnectionType.SQUARE, p5.random(0.2f, 1.5f));
			c.setStyle(TColor.WHITE.copy(), TColor.CYAN.copy());
			// c.setRotation(new Vec3D(p5.random(-0.01f, 0.01f), p5.random(-0.01f, 0.01f), p5.random(-0.01f, 0.01f)));
			c.setRotation(new Vec3D(p5.random(-0.06f, 0.06f), p5.random(-0.06f, 0.06f), p5.random(-0.06f, 0.06f)));
			// c.setRotation(new Vec3D(p5.random(-0.2f, 0.2f), p5.random(-0.2f, 0.2f), p5.random(-0.2f, 0.2f)));
			connections.add(c);
			for (int j = 0; j < 1; j++) {
				Vec3D bb = b.add(Vec3D.randomVector().scaleSelf(ARnd.fl(spread / 2)));
				Connection3D cc = new Connection3D(p5, b.copy(), bb.copy(), ConnectionType.SQUARE_MIDPOINT, p5.random(0.2f, 1f));
				cc.setStyle(TColor.YELLOW.copy(), TColor.YELLOW.copy());
				cc.setRotation(new Vec3D(p5.random(-0.06f, 0.06f), p5.random(-0.06f, 0.06f), p5.random(-0.06f, 0.06f)));
				c.addSubconnection(cc);
				for (int k = 0; k < 2; k++) {
					Vec3D bbb = bb.add(Vec3D.randomVector().scaleSelf(ARnd.fl(spread / 3)));
					Connection3D ccc = new Connection3D(p5, bb.copy(), bbb.copy(), ConnectionType.SQUARE_MIDPOINT, p5.random(0.2f, 1f));
					ccc.setStyle(TColor.RED.copy(), TColor.RED.copy());
					ccc.setRotation(new Vec3D(p5.random(-0.06f, 0.06f), p5.random(-0.06f, 0.06f), p5.random(-0.06f, 0.06f)));
					cc.addSubconnection(ccc);
					// endpoints.add(new ArrayList<Vec3D>());
					for (int l = 0; l < 3; l++) {
						Vec3D bbbb = bbb.add(Vec3D.randomVector().scaleSelf(ARnd.fl(spread / 3)));
						Connection3D cccc = new Connection3D(p5, bbb.copy(), bbbb.copy(), ConnectionType.SQUARE_MIDPOINT, p5.random(0.2f, 1f));
						cccc.setStyle(TColor.BLUE.copy(), TColor.BLUE.copy());
						// cccc.setRotation(new Vec3D(p5.random(-0.04f, 0.04f), p5.random(-0.04f, 0.04f), p5.random(-0.04f, 0.04f)));
						cccc.setRotation(new Vec3D(p5.random(-0.01f, 0.01f), p5.random(-0.01f, 0.01f), p5.random(-0.01f, 0.01f)).scaleSelf(10f));
						ccc.addSubconnection(cccc);
						endpoints.add(new ArrayList<Vec3D>());
					}
				}
			}
		}
	}

	public void update() {
		count = 0; // Rather grim use of global variable to handle tracking in recursive method
		traverse(connections);
	}

	public void render() {
		// switch between 2-3 render styles - wireframe? softer? Connection/debug mode
		switch (mode) {
			case 0 :
				drawPolygons();
				break;
			case 1 :
				drawTaperedPolygons();
				break;
			case 2 :
				drawWireframe();
				break;
		}
	}

	private int mode;

	public void setRenderMode(int mode) {
		this.mode = mode;
	}

	public void setSoundData(SoundData sData) {
		this.sData = sData;
	}

	public void drawPolygons() {
		p5.noFill();
		p5.stroke(0, 255, 0);
		for (int j = 0; j < endpoints.size(); j++) {
			ArrayList<Vec3D> track = endpoints.get(j);
			p5.beginShape(PConstants.QUADS);
			for (int i = 0; i < track.size() - 1; i++) {
				Vec3D v = track.get(i);
				Vec3D vv = track.get(i + 1);

				p5.stroke(ARnd.ind(clrs, i + p5.frameCount).toARGB(), 200);
				p5.strokeWeight(0.5f + i / 50f);
				// line(v.x, v.y, v.z, vv.x, vv.y, vv.z);

				float sc = 0.85f + (i / trailSize * 3f);
				float sc2 = 0.85f + ((i + 1) / trailSize * 3f);
				// CosineInterpolation cos = new CosineInterpolation();
				// float sc = cos.interpolate(0, 1, (float) i / track.size());
				// float sc2 = cos.interpolate(0, 1, (float) (i + 1) / track.size());

				p5.noStroke();
				// p5. fill(Rnd.ind(clrs, i + p5.frameCount).toARGB(), p5.frameCount%255);
				// p5.fill(Rnd.ind(clrs, (int) (1000 * p5.noise(j / 20f) + p5.frameCount)).toARGB(), 255);
				p5.fill(clrs.get((int) (10 * j + p5.frameCount) % clrs.size()).toARGB(), 255);
				p5.vertex(v.x, v.y, v.z);
				p5.vertex(vv.x, vv.y, vv.z);
				p5.vertex(vv.scale(sc2).x, vv.scale(sc2).y, vv.scale(sc2).z);
				p5.vertex(v.scale(sc).x, v.scale(sc).y, v.scale(sc).z);
			}
			p5.endShape(PConstants.CLOSE);
		}
	}

	public void drawTaperedPolygons() {
		p5.noFill();
		p5.stroke(0, 255, 0);
		for (int j = 0; j < endpoints.size(); j++) {
			ArrayList<Vec3D> track = endpoints.get(j);
			p5.beginShape(PConstants.QUADS);
			for (int i = 0; i < track.size() - 1; i++) {
				Vec3D v = track.get(i);
				Vec3D vv = track.get(i + 1);

				float r = 1 - PApplet.abs(i - (track.size() / 2f)) / (track.size() / 2f);
				float r2 = 1 - PApplet.abs((i + 1) - (track.size() / 2f)) / (track.size() / 2f);

				r = 1;
				r2 = 1;

				// CosineInterpolation s = new CosineInterpolation();
				// DecimatedInterpolation s = new DecimatedInterpolation(6);
				// r = s.interpolate(0, 1, (float) i / track.size());
				// r2 = s.interpolate(0, 1, (float) (i + 1) / track.size());

				float aa = 0.14f;
				if (sData != null && sData.hat) {
					aa = 0.17f;
				}

				p5.noStroke();
				p5.fill(clrs.get((int) (55 * j + 3 * i + p5.frameCount) % clrs.size()).toARGB(), 255);
				p5.vertex(v.scale(1 + aa * r).x, v.scale(1 + aa * r).y, v.scale(1 + aa * r).z);
				p5.vertex(vv.scale(1 + aa * r2).x, vv.scale(1 + aa * r2).y, vv.scale(1 + aa * r2).z);
				p5.vertex(vv.scale(1 - aa * r2).x, vv.scale(1 - aa * r2).y, vv.scale(1 - aa * r2).z);
				p5.vertex(v.scale(1 - aa * r).x, v.scale(1 - aa * r).y, v.scale(1 - aa * r).z);
			}
			p5.endShape(PConstants.CLOSE);
		}
	}

	private void drawWireframe() {
		for (int j = 0; j < endpoints.size(); j++) {
			ArrayList<Vec3D> track = endpoints.get(j);
			p5.beginShape(PConstants.QUADS);
			for (int i = 0; i < track.size() - 1; i++) {
				Vec3D v = track.get(i);
				Vec3D vv = track.get(i + 1);

				float sc = 0.65f + (i / trailSize * 3f);
				float sc2 = 0.65f + ((i + 1) / trailSize * 3f);

				p5.stroke(255);
				p5.fill(0);
				p5.vertex(v.x, v.y, v.z);
				p5.vertex(vv.x, vv.y, vv.z);
				p5.vertex(vv.scale(sc2).x, vv.scale(sc2).y, vv.scale(sc2).z);
				p5.vertex(v.scale(sc).x, v.scale(sc).y, v.scale(sc).z);
			}
			p5.endShape(PConstants.CLOSE);
		}
	}

	public void traverse(ArrayList<Connection3D> cons) {
		for (Connection3D c : cons) {
			c.update();
			// c.render(); //turn this one for certain mode?
			if (c.getSubconnections().size() > 0) {
				traverse(c.getSubconnections());
			} else {
				endpoints.get(count).add(c.b.copy());
				if (endpoints.get(count).size() > trailSize) {
					endpoints.get(count).remove(0);
				}
				count++;
			}
		}
	}

	public void setColours(ColorList clrs) {
		this.clrs = clrs;
	}

	public void initColours() {
		clrs = QuickColours.get(1000, ColourSet.getRandom());
		clrs = new ColorList();
		clrs.add(TColor.newHex("EE1E3F"));
		clrs.add(TColor.newHex("FAA335"));
		clrs.add(TColor.newHex("F8ED47"));
		clrs.add(TColor.newHex("7EC354"));
		clrs.add(TColor.newHex("EE1E3F"));
		WeightedGradient g = new WeightedGradient(clrs);
		clrs = g.getGradient(500);
	}

}