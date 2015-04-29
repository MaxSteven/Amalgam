package amalgam.twister;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import amalgam.audio.SoundData;

/**
 * 3D twister, used for Spacek
 */
public class Twister {
	private final PApplet p5;
	private Vec3D centre;

	private int trailSize;
	public ArrayList<Vec3D> track;

	private Vec3D base;
	private float length, sc;

	private int steps;

	private ColorList clrs;
	private int age;
	private int seed;

	public SoundData sData;

	public Twister(PApplet p5, Vec3D centre, float length, int steps, ColorList clrs) {
		this(p5, centre, length, steps, clrs, 45);
	}

	public Twister(PApplet p5, Vec3D centre, float length, int steps, ColorList clrs, int trailSize) {
		this.p5 = p5;
		this.centre = centre;

		this.length = length;
		this.sc = 1f;
		this.base = new Vec3D(p5.random(0.5f, 1), 0, 0).rotateX(p5.random(PApplet.TAU)).rotateY(p5.random(PApplet.TAU)).normalizeTo(length);

		this.track = new ArrayList<Vec3D>();

		this.steps = steps;

		this.age = 0;
		this.clrs = clrs;
		this.seed = (int) p5.random(10000);

		this.trailSize = trailSize;
	}

	public void update() {
		Vec3D curr = centre.copy();

		for (int i = 0; i < steps; i++) {
			Vec3D temp = base.copy();
			temp.scaleSelf(length * p5.noise((seed + age) / 100f));

			temp.rotateX(1.5f * PConstants.TAU * (p5.noise((age + seed) / 200f)));
			temp.rotateY(1.5f * PConstants.TAU * (p5.noise((seed - age) / 200f)));
			temp.rotateZ(3 * PConstants.TAU * (p5.noise((age + seed) / 150f)));
			curr.addSelf(temp);
		}

		track.add(curr);

		if (track.size() > trailSize) {
			for (int i = 0; i < (track.size() - trailSize); i++) {
				track.remove(0);
			}
		}

		sc = PApplet.constrain((sc - 0.02f), 1f, 2); // ease back the kick
		age++;
	}

	// blocky style
	public void renderBlocky(int ind) { // take index in list too?
		p5.noStroke();
		p5.beginShape(PConstants.QUADS);
		for (int i = 0; i < track.size() - 1; i++) {
			Vec3D v = track.get(i);
			Vec3D vv = track.get(i + 1);

			float r = 1 - PApplet.abs(i - (track.size() / 2f)) / (track.size() / 2f);
			float r2 = 1 - PApplet.abs((i + 1) - (track.size() / 2f)) / (track.size() / 2f);

			float aa = 0.25f;
			if (sData != null) {
				// float aa = 0.0f + sData.energies[i % sData.energies.length] / sData.energyScaling;
				aa = 0.0f + 0.8f * sData.energies[(int) (PApplet.map(i, 0, track.size(), 0, sData.energies.length)) % sData.energies.length] / sData.energyScaling;
			}

			p5.fill(255, 0, 0, 144);
			p5.fill(clrs.get((int) (3 * i + p5.frameCount) % clrs.size()).toARGB(), 255);
			p5.vertex(v.scale(1 + aa * r).x, v.scale(1 + aa * r).y, v.scale(1 + aa * r).z);
			p5.vertex(vv.scale(1 + aa * r2).x, vv.scale(1 + aa * r2).y, vv.scale(1 + aa * r2).z);
			p5.vertex(vv.scale(1 - aa * r2).x, vv.scale(1 - aa * r2).y, vv.scale(1 - aa * r2).z);
			p5.vertex(v.scale(1 - aa * r).x, v.scale(1 - aa * r).y, v.scale(1 - aa * r).z);
		}
		p5.endShape(PConstants.CLOSE);
	}

	// smooth style
	public void render(int ind) { // take index from main list
		p5.noStroke();
		p5.beginShape(PConstants.QUAD_STRIP);
		for (int i = 0; i < track.size() - 1; i++) {
			Vec3D v = track.get(i);
			float r = 1 - PApplet.abs(i - (track.size() / 2f)) / (track.size() / 2f);

			float aa = 0.25f;
			if (sData != null) {
				aa = 0.0f + 0.8f * sData.energies[ind % sData.energies.length] / sData.energyScaling;
			}

			p5.fill(clrs.get((int) (3 * i + p5.frameCount) % clrs.size()).toARGB(), 255);
			p5.fill(255);
			p5.vertex(v.scale(1 + aa * r).x, v.scale(1 + aa * r).y, v.scale(1 + aa * r).z);
			p5.vertex(v.scale(1 - aa * r).x, v.scale(1 - aa * r).y, v.scale(1 - aa * r).z);
		}
		p5.endShape(PConstants.CLOSE);
	}

	public void renderList(int ind) {
		p5.noStroke();
		p5.beginShape(PConstants.QUAD_STRIP);
		for (int i = 0; i < track.size() - 1; i++) {
			Vec3D v = track.get(i);
			float r = 1 - PApplet.abs(i - (track.size() / 2f)) / (track.size() / 2f);

			float aa = 0.2f;
			if (sData != null) {
				aa = 0.15f + 0.3f * sData.energies[ind % sData.energies.length] / sData.energyScaling;
			}

			p5.fill(clrs.get((int) (5 * i + p5.frameCount + 10 * ind) % clrs.size()).toARGB(), 255);
			p5.vertex(v.scale(1 + aa * r).x, v.scale(1 + aa * r).y, v.scale(1 + aa * r).z);
			p5.vertex(v.scale(1 - aa * r).x, v.scale(1 - aa * r).y, v.scale(1 - aa * r).z);
		}
		p5.endShape(PConstants.CLOSE);
	}

	// public void renderList(int ind) {
	// p5.noStroke();
	// p5.beginShape(PConstants.QUAD_STRIP);
	// for (int i = 0; i < track.size() - 1; i++) {
	// Vec3D v = track.get(i);
	// Vec3D vv = track.get(i + 1);
	// float r = 1 - PApplet.abs(i - (track.size() / 2f)) / (track.size() / 2f);
	// float aa = 0.2f + 0.5f * sData.energies[ind % sData.energies.length] / sData.energyScaling;
	// // float aa = 0.2f;
	//
	// Vec3D rail = v.sub(vv).cross(v).normalizeTo(150 * PApplet.sqrt((float) i / track.size()));
	//
	// p5.fill(clrs.get((int) (5 * i + p5.frameCount + 10 * ind) % clrs.size()).toARGB(), 255);
	// p5.vertex(v.add(rail.scale(aa * r)).x, v.add(rail.scale(aa * r)).y, v.add(rail.scale(aa * r)).z);
	// p5.vertex(v.add(rail.scale(-aa * r)).x, v.add(rail.scale(-aa * r)).y, v.add(rail.scale(-aa * r)).z);
	// }
	// p5.endShape(PConstants.CLOSE);
	// }

	public void setSoundData(SoundData s) {
		this.sData = s;
	}
}