package amalgam.toxi;

import toxi.math.InterpolateStrategy;

/**
 * From Ani library
 */
public class BounceInterpolation implements InterpolateStrategy {
	private int bounceType;

	public BounceInterpolation(int bounceType) {
		this.bounceType = bounceType;
	}

	@Override
	public double interpolate(double a, double b, double f) {
		return interpolate(a, b, (float) f);
	}

	@Override
	public float interpolate(float a, float b, float f) {
		float ff;

		switch (bounceType) {
			default :
			case 0 :
				ff = easeIn(f, 0, 1, 1);
				break;
			case 1 :
				ff = easeOut(f, 0, 1, 1);
				break;
			case 2 :
				ff = easeInOut(f, 0, 1, 1);
				break;
		}
		return a + (b - a) * ff;
	}

	// * @param t the time f
	// * @param b the begin 0
	// * @param c the change 1
	// * @param d the duration 1
	public static final int BOUNCE_IN = 0; // the simplest case, for completeness
	public static final int BOUNCE_OUT = 1; // start slow, finish abruptly D:[0, !0] V:[0,1]
	public static final int BOUNCE_IN_OUT = 2; // start abruptly, finish slow D:[!0, 0] V:[0,1]

	private float easeIn(float t, float b, float c, float d) {
		return c - easeOut(d - t, 0, c, d) + b;
	}

	private float easeOut(float t, float b, float c, float d) {
		if ((t /= d) < (1 / 2.75f)) {
			return c * (7.5625f * t * t) + b;
		} else if (t < (2 / 2.75f)) {
			return c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + b;
		} else if (t < (2.5 / 2.75)) {
			return c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + b;
		} else {
			return c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + b;
		}
	}

	private float easeInOut(float t, float b, float c, float d) {
		if (t < d / 2)
			return easeIn(t * 2, 0, c, d) * .5f + b;
		else
			return easeOut(t * 2 - d, 0, c, d) * .5f + c * .5f + b;
	}

}
