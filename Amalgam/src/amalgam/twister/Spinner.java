package amalgam.twister;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import toxi.math.noise.SimplexNoise;

public class Spinner {
	final PApplet p5;
	ColorList colours;
	RenderingStrategy renderingStrategy;

	int pathCount;
	float vecLength, mainPhasor;

	int lerps, clrIndex;
	float alpha, smoothness, sizeScaling;

	ArrayList<Vec2D> finalPositions;
	ArrayList<Vec2D> finalPositionsPrev;

	public Spinner(PApplet p5, ColorList colours, int pathCount, float vecLength, float maxOpac, float sizeScaling) {
		this.p5 = p5;
		this.colours = colours;
		this.pathCount = pathCount;
		this.vecLength = vecLength;
		this.alpha = maxOpac;
		this.mainPhasor = p5.random(-1000, 1000);
		this.finalPositions = new ArrayList<>();
		this.finalPositionsPrev = new ArrayList<>();

		this.sizeScaling = sizeScaling;
		this.clrIndex = (int) p5.random(colours.size()); // offsets starting point in ColorList for rendering

		this.smoothness = 1f; // defaults
		this.renderingStrategy = new ConcreteRenderStrategy_Basic(this);
	}

	// creation, rotation and scaling of the first vector and second vector
	public void update(int lerps, Vec2D centre, Vec2D centrePrev) {
		this.lerps = lerps;
		finalPositions.clear();

		for (int i = 0; i < pathCount; i++) {
			// 1st vector
			Vec2D pos1 = new Vec2D(-50 + vecLength * PApplet.sqrt(i), -50 + vecLength * PApplet.sqrt(i));
			float noisePhasor = 20 * i * i;
			float rot = (float) (mainPhasor + PApplet.PI * SimplexNoise.noise(mainPhasor + noisePhasor + p5.frameCount / (150f), mainPhasor + noisePhasor + p5.frameCount / (150f)));
			pos1.rotate(rot).scaleSelf(sizeScaling * (float) SimplexNoise.noise(mainPhasor / 10f + p5.frameCount / 100f, mainPhasor / 10f + p5.frameCount / 100f));
			pos1.scaleSelf(PApplet.sin(mainPhasor / 10f + p5.frameCount / 30f)); // is this needed? (answer: yes, this is the main source of the scaling behaviour)

			// 2nd vector (could expand this into a loop to cope with n vector segments)
			float noisePhasor2 = 10 * i;
			Vec2D pos2 = new Vec2D(vecLength, vecLength).rotate((float) (noisePhasor2 + mainPhasor + SimplexNoise.noise(p5.frameCount / smoothness, p5.frameCount / smoothness)));
			finalPositions.add(new Vec2D(pos1.add(pos2)));
		}

		p5.beginShape(PApplet.QUADS);
		if (finalPositionsPrev.size() > 0) {
			for (int i = 0; i < pathCount - 1; i++) {
				// do lerps + 1/2/3 etc to add an 'edge' to the rendering (but does add jaggies). Use InterpolationStrategy here ?
				for (int j = 0; j < lerps; j++) {
					float a = PApplet.lerp(finalPositionsPrev.get(i).x + centrePrev.x, finalPositionsPrev.get(i + 1).x + centrePrev.x, (float) j / lerps);
					float b = PApplet.lerp(finalPositionsPrev.get(i).y + centrePrev.y, finalPositionsPrev.get(i + 1).y + centrePrev.y, (float) j / lerps);
					float c = PApplet.lerp(finalPositions.get(i).x + centre.x, finalPositions.get(i + 1).x + centre.x, (float) j / lerps);
					float d = PApplet.lerp(finalPositions.get(i).y + centre.y, finalPositions.get(i + 1).y + centre.y, (float) j / lerps);

					float a2 = PApplet.lerp(finalPositionsPrev.get(i).x + centrePrev.x, finalPositionsPrev.get(i + 1).x + centrePrev.x, (float) (j + 1) / lerps);
					float b2 = PApplet.lerp(finalPositionsPrev.get(i).y + centrePrev.y, finalPositionsPrev.get(i + 1).y + centrePrev.y, (float) (j + 1) / lerps);
					float c2 = PApplet.lerp(finalPositions.get(i).x + centre.x, finalPositions.get(i + 1).x + centre.x, (float) (j + 1) / lerps);
					float d2 = PApplet.lerp(finalPositions.get(i).y + centre.y, finalPositions.get(i + 1).y + centre.y, (float) (j + 1) / lerps);

					// render(j);
					render(j, finalPositions.get(i).distanceTo(finalPositions.get(i + 1)));
					p5.vertex(a, b);
					p5.vertex(a2, b2);
					p5.vertex(c2, d2);
					p5.vertex(c, d);
				}
			}
		}
		p5.endShape(PApplet.CLOSE);

		finalPositionsPrev = new ArrayList<Vec2D>(finalPositions);
	}

	// creation, rotation and scaling of the first vector and second vector
	public void updateOsc(int lerps, Vec2D centre, Vec2D centrePrev) {
		this.lerps = lerps;
		finalPositions.clear();

		for (int i = 0; i < pathCount; i++) {
			// 1st vector
			// Vec2D pos1 = new Vec2D(vecLength, vecLength);
			Vec2D pos1 = new Vec2D(vecLength, vecLength);
			float noisePhasor = (float) (50 * Math.pow(i, 2));
			float rot = (float) (mainPhasor + PApplet.PI * PApplet.sin(mainPhasor + noisePhasor + p5.frameCount / (50f)));
			pos1.rotate(rot);
			pos1.scaleSelf(sizeScaling * (float) PApplet.cos((mainPhasor + p5.frameCount) / 50f));
			pos1.scaleSelf(PApplet.sin((mainPhasor - p5.frameCount) / 50f)); // is this needed? (answer: yes, this is the main source of the scaling behaviour)

			// 2nd vector (could expand this into a loop to cope with n vector segments)
			// The addition of large SimplexNoise to this rotation causes the staggered rippling effect. To keep the movement smooth, just rotate by a constant value (eg framecount/20f) or smoother noise
			float noisePhasor2 = 50 * i;
			Vec2D pos2 = new Vec2D(vecLength, vecLength).rotate((float) (noisePhasor2 + PApplet.sin(p5.frameCount / smoothness)));

			finalPositions.add(new Vec2D(pos1.add(pos2)));
		}

		if (finalPositionsPrev.size() > 0) {
			for (int i = 0; i < pathCount - 1; i++) {
				for (int j = 0; j < lerps; j++) {
					float a = PApplet.lerp(finalPositionsPrev.get(i).x + centrePrev.x, finalPositionsPrev.get(i + 1).x + centrePrev.x, (float) j / lerps);
					float b = PApplet.lerp(finalPositionsPrev.get(i).y + centrePrev.y, finalPositionsPrev.get(i + 1).y + centrePrev.y, (float) j / lerps);
					float c = PApplet.lerp(finalPositions.get(i).x + centre.x, finalPositions.get(i + 1).x + centre.x, (float) j / lerps);
					float d = PApplet.lerp(finalPositions.get(i).y + centre.y, finalPositions.get(i + 1).y + centre.y, (float) j / lerps);

					float a2 = PApplet.lerp(finalPositionsPrev.get(i).x + centrePrev.x, finalPositionsPrev.get(i + 1).x + centrePrev.x, (float) (j + 1) / lerps);
					float b2 = PApplet.lerp(finalPositionsPrev.get(i).y + centrePrev.y, finalPositionsPrev.get(i + 1).y + centrePrev.y, (float) (j + 1) / lerps);
					float c2 = PApplet.lerp(finalPositions.get(i).x + centre.x, finalPositions.get(i + 1).x + centre.x, (float) (j + 1) / lerps);
					float d2 = PApplet.lerp(finalPositions.get(i).y + centre.y, finalPositions.get(i + 1).y + centre.y, (float) (j + 1) / lerps);

					render(j);

					p5.beginShape();
					p5.vertex(a, b);
					p5.vertex(a2, b2);
					p5.vertex(c2, d2);
					p5.vertex(c, d);
					p5.endShape(PApplet.CLOSE);
				}
			}
		}
		finalPositionsPrev = new ArrayList<Vec2D>(finalPositions);
	}

	// creation, rotation and scaling of the first vector and second vector
	public void updateOsc2(int lerps, Vec2D centre, Vec2D centrePrev) {
		this.lerps = lerps;
		finalPositions.clear();

		for (int i = 0; i < pathCount; i++) {
			float noisePhasor = (float) Math.pow(i + 1, 3);
			Vec2D pos1 = new Vec2D(vecLength, 0).rotate(mainPhasor - noisePhasor + (mainPhasor / 500f) * p5.frameCount / smoothness).scaleSelf(sizeScaling * PApplet.cos(mainPhasor + p5.frameCount / smoothness));

			// float noisePhasor2 = (float) Math.pow(i, 2);
			// Vec2D pos2 = new Vec2D(-vecLength, 0).rotate(noisePhasor2 - 2*i * p5.frameCount / smoothness).scaleSelf(sizeScaling * PApplet.sin(mainPhasor - noisePhasor2 + p5.frameCount / smoothness));

			// finalPositions.add(pos1.add(pos2));
			finalPositions.add(pos1);
		}

		if (finalPositionsPrev.size() > 0) {
			for (int i = 0; i < pathCount - 1; i++) {
				for (int j = 0; j < lerps; j++) {
					float a = PApplet.lerp(finalPositionsPrev.get(i).x + centrePrev.x, finalPositionsPrev.get(i + 1).x + centrePrev.x, (float) j / lerps);
					float b = PApplet.lerp(finalPositionsPrev.get(i).y + centrePrev.y, finalPositionsPrev.get(i + 1).y + centrePrev.y, (float) j / lerps);
					float c = PApplet.lerp(finalPositions.get(i).x + centre.x, finalPositions.get(i + 1).x + centre.x, (float) j / lerps);
					float d = PApplet.lerp(finalPositions.get(i).y + centre.y, finalPositions.get(i + 1).y + centre.y, (float) j / lerps);

					float a2 = PApplet.lerp(finalPositionsPrev.get(i).x + centrePrev.x, finalPositionsPrev.get(i + 1).x + centrePrev.x, (float) (j + 1) / lerps);
					float b2 = PApplet.lerp(finalPositionsPrev.get(i).y + centrePrev.y, finalPositionsPrev.get(i + 1).y + centrePrev.y, (float) (j + 1) / lerps);
					float c2 = PApplet.lerp(finalPositions.get(i).x + centre.x, finalPositions.get(i + 1).x + centre.x, (float) (j + 1) / lerps);
					float d2 = PApplet.lerp(finalPositions.get(i).y + centre.y, finalPositions.get(i + 1).y + centre.y, (float) (j + 1) / lerps);

					render(j);

					p5.beginShape();
					p5.vertex(a, b);
					p5.vertex(a2, b2);
					p5.vertex(c2, d2);
					p5.vertex(c, d);
					p5.endShape(PApplet.CLOSE);
				}
			}
		}
		finalPositionsPrev = new ArrayList<Vec2D>(finalPositions);
	}

	public void render(int j) {
		renderingStrategy.render(j, lerps);
	}

	public void render(int j, float mag) {
		if (renderingStrategy instanceof ConcreteRenderStrategy_Faded) {
			ConcreteRenderStrategy_Faded cr = (ConcreteRenderStrategy_Faded) renderingStrategy;
			cr.render(j, lerps, mag);
		} else {
			renderingStrategy.render(j, lerps);
		}
	}

	// GETTERS AND SETTERS

	public float getVecLength() {
		return vecLength;
	}

	public void setVecLength(float vecLength) {
		this.vecLength = vecLength;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float opac) {
		this.alpha = opac;
	}

	public int getLerps() {
		return lerps;
	}

	public void setLerps(int lerps) {
		this.lerps = lerps;
	}

	public float getSmoothness() {
		return smoothness;
	}

	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}

	public float getSizeScaling() {
		return sizeScaling;
	}

	public void setSizeScaling(float sizeScaling) {
		this.sizeScaling = sizeScaling;
	}

	public RenderingStrategy getRenderingStrategy() {
		return renderingStrategy;
	}

	public void setRenderingStrategy(RenderingStrategy renderingStrategy) {
		this.renderingStrategy = renderingStrategy;
	}

	public PApplet getp5() {
		return p5;
	}

	public ColorList getColours() {
		return colours;
	}

	public int getPathCount() {
		return pathCount;
	}

	public float getMainPhasor() {
		return mainPhasor;
	}

	public ArrayList<Vec2D> getFinalPositions() {
		return finalPositions;
	}

	public ArrayList<Vec2D> getFinalPositionsPrev() {
		return finalPositionsPrev;
	}

	public int getClrIndex() {
		return clrIndex;
	}

}