package amalgam.twister;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import toxi.math.noise.SimplexNoise;

public class SpinnerStroke extends Spinner {

	private boolean orthogonalLines;

	public SpinnerStroke(PApplet p5, ColorList colours, int pathCount, float vecLength, int opac, float sizeScaling, boolean orthogonalLines) {
		super(p5, colours, pathCount, vecLength, opac, sizeScaling);
		this.orthogonalLines = orthogonalLines;
	}

	@Override
	public void update(int lerps, Vec2D centre, Vec2D centrePrev) {
		this.lerps = lerps;

		// creation, rotation and scaling of the first vector and second vector
		finalPositions.clear();

		for (int i = 0; i < pathCount; i++) {
			// 1st vector
			Vec2D pos1 = new Vec2D(-50 + vecLength * PApplet.sqrt(i), -50 + vecLength * PApplet.sqrt(i));
			float noisePhasor = 20 * i * i;
			float rot = (float) (mainPhasor + PApplet.PI * SimplexNoise.noise(mainPhasor + noisePhasor + p5.frameCount / (150f), mainPhasor + noisePhasor + p5.frameCount / (150f)));
			pos1.rotate(rot);
			pos1.scaleSelf(sizeScaling * (float) SimplexNoise.noise(p5.frameCount / 100f, p5.frameCount / 100f));
			pos1.scaleSelf(PApplet.sin(p5.frameCount / 30f)); // is this needed?

			// 2nd vector (could expand this into a loop to cope with n vector segments)
			// The addition of large SimplexNoise to this rotation causes the staggered rippling effect. To keep the movement smooth, just rotate by a constant value (eg framecount/20f) or smoother noise
			float noisePhasor2 = 10 * i;
			Vec2D pos2 = new Vec2D(vecLength, vecLength).rotate((float) (noisePhasor2 + mainPhasor + SimplexNoise.noise(p5.frameCount / smoothness, p5.frameCount / smoothness)));

			finalPositions.add(new Vec2D(pos1.add(pos2)));
		}

		if (finalPositionsPrev.size() > 0) {
			for (int i = 0; i < pathCount - 1; i++) {
				// do lerps + 1/2/3 etc to add an 'edge' to the rendering (but does add jaggies)

				// use InterpolationStrategy here ?

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

					// STROKE ONLY VERSION
					p5.strokeCap(PApplet.SQUARE);
					if (orthogonalLines) {
						p5.line(a, b, a2, b2);
						p5.line(c, d, c2, d2);
					} else {
						p5.line(a, b, c, d);
						p5.line(a2, b2, c2, d2);
					}
				}
			}
		}
		finalPositionsPrev = new ArrayList<Vec2D>(finalPositions);
	}

}