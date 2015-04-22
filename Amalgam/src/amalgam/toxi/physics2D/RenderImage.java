package amalgam.toxi.physics2D;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import toxi.color.ColorList;
import toxi.physics2d.VerletParticle2D;

/**
 * Image-based rendering - i.e for painting
 */
public class RenderImage implements RenderStrategy {

	private final PApplet p5;
	private MyVerletPhysics2D physics;
	private ColorList clrs;

	private ArrayList<PImage> images;

	private boolean shadow;
	private int brushIndex;
	private float maxBrushSize;

	public RenderImage(PApplet p5, MyVerletPhysics2D physics, ColorList clrs, ArrayList<PImage> images, int brush, float maxBrushSize, boolean shadow) {
		this.p5 = p5;
		this.physics = physics;
		this.clrs = clrs;
		this.images = images;

		this.brushIndex = brush;
		this.shadow = shadow;
		this.maxBrushSize = maxBrushSize;
	}

	@Override
	public void render() {

		ArrayList<VerletParticle2D> pts = physics.particles;
		for (int i = 0; i < pts.size() - 1; i++) {
			VerletParticle2D p = pts.get(i);

			if (p.getAge() <= p.getLife()) {
				p5.pushMatrix();
				p5.translate(p.x, p.y);
//				p5.rotate(p5.frameCount * p.getRotation());
				p5.rotate(100 * 0.1f*(p5.noise(1000*p.getRotation()+p5.frameCount/200f)-0.5f));

				// SCALE IS FAST!
				p5.scale((maxBrushSize / 100f) * (p.seedNorm * p.seedNorm) * PApplet.sin(PApplet.TAU * (float) (p.getAge()) / (2f * p.getLife())));

				// SHADOW
				if (shadow) {
					p5.tint(0, 0, 0, physics.getAlpha() / 3);
					// p5.image(images.get(p.seed % images.size()), 6, 0);
					p5.image(images.get(brushIndex % images.size()), 6, 0);
				}

				// MAIN STROKE
				p5.tint(clrs.get((int) ((50 * p.seed) + p.getAge() + p5.frameCount / 3) % clrs.size()).toARGB(), physics.getAlpha());
//				p5.tint(clrs.get((int) (5*p.seed + p.getAge() + p5.frameCount / 3) % clrs.size()).toARGB(), physics.getAlpha());
				p5.image(images.get(p.seed % images.size()), 0, 0);
				// p5.image(images.get(brushIndex % images.size()), 0, 0);

				p5.popMatrix();
			}
		}
	}
}
