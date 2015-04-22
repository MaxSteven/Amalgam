package amalgam.toxi;

import toxi.math.InterpolateStrategy;
import AULib.AUMisc;

/**
 * AUEase -> toxi
 */
public class EaseInterpolation implements InterpolateStrategy {

	private int easeType;

	public EaseInterpolation(int easeType) {
		this.easeType = easeType;
	}

	@Override
	public double interpolate(double a, double b, double f) {
		return a + (b - a) * ease(easeType, (float) f);
	}

	@Override
	public float interpolate(float a, float b, float f) {
		return a + (b - a) * ease(easeType, f);
	}

	// AULib code copy:
	/*************************************************
	 * EASING
	 *************************************************/

	public static final int EASE_LINEAR = 0; // the simplest case, for completeness
	public static final int EASE_IN_CUBIC = 1; // start slow, finish abruptly D:[0, !0] V:[0,1]
	public static final int EASE_OUT_CUBIC = 2; // start abruptly, finish slow D:[!0, 0] V:[0,1]
	public static final int EASE_IN_OUT_CUBIC = 3; // start and end slow D:[0, 0] V:[0,1]
	public static final int EASE_IN_BACK = 4; // start slow, back up, finish abruptly D:[0, !0] V:[-1.1, 1]
	public static final int EASE_OUT_BACK = 5; // start abruptly, overshoot, then end slow D:[!0, 0] V:[0, 1.1]
	public static final int EASE_IN_OUT_BACK = 6; // start slow, undershoot, overshoot, end slow D:[0, 0] V:[-0.05, 1.05]
	public static final int EASE_IN_ELASTIC = 7; // sit there, wiggle a bit, then zoom to end D:[0, !0] V:[-.336, 1]
	public static final int EASE_OUT_ELASTIC = 8; // zoom to overshoot, then slowly wiggle into place D:[!0, 0] V:[0, 1.33]
	public static final int EASE_IN_OUT_ELASTIC = 9; // sit there, wiggle, overshoot end, wiggle and settle D:[0, 0] V:[-.17, 1.17]
	// These are my hybrids
	public static final int EASE_CUBIC_ELASTIC = 10; // start slow, then bounce at the end D=[0,0] V:[0,1.17]
	public static final int EASE_ANTICIPATE_CUBIC = 11; // small negative motion, then ease
	public static final int EASE_ANTICIPATE_ELASTIC = 12; // small backwards motion, then bounce

	private float ease(int easeType, float _t) {
		// This would be so much nicer with function pointers or functions as parameters. But we
		// can't do that in Processing (or in Java), so I just repeat the same in/out/inout format
		// over and over with different functions.
		float t = _t;
		float v = 0;
		float ti = 1 - t; // value for out versions (rather than in)
		float h0 = 2 * t; // t for first half of in/out combos
		float h1 = 2 * ti; // t for second half of in/out combos
		if (t < 0)
			return 0;
		if (t > 1)
			return 1;
		switch (easeType) {
			default :
			case EASE_LINEAR :
				v = t;
				break;
			case EASE_IN_CUBIC : // start slow, finish abruptly [0, !0]
				v = cubic(t);
				break;
			case EASE_OUT_CUBIC : // start abruptly, finish slow [!0, 0]
				v = 1 - cubic(ti);
				break;
			case EASE_IN_OUT_CUBIC : // start and end slow [0, 0]
				if (t < .5) {
					v = .5f * cubic(h0);
				} else {
					v = .5f + (.5f * (1f - cubic(h1)));
				}
				break;
			case EASE_IN_BACK : // start slow, back up, finish abruptly [0, !0]
				v = back(t);
				break;
			case EASE_OUT_BACK : // start abruptly, overshoot, then end slow [!0, 0]
				v = 1 - back(ti);
				break;
			case EASE_IN_OUT_BACK : // start slow, undershoot, overshoot, end slow [0, 0]
				if (t < .5) {
					v = .5f * back(h0);
				} else {
					v = .5f + (.5f * (1f - back(h1)));
				}
				break;
			case EASE_IN_ELASTIC : // sit there, wiggle a bit, then zoom to end [0, !0]
				v = elastic(t);
				break;
			case EASE_OUT_ELASTIC : // zoom to overshoot, then slowly wiggle into place [!0, 0]
				v = 1 - elastic(ti);
				break;
			case EASE_IN_OUT_ELASTIC : // sit there, wiggle, overshoot end, wiggle and settle [0, 0]
				if (t < .5) {
					v = .5f * elastic(h0);
				} else {
					v = .5f + (.5f * (1 - elastic(h1)));
				}
				break;
			case EASE_CUBIC_ELASTIC :
				// My hybrid. Start with a slow cubic ease, then bounces at the end.
				float switchCenter = .6f;
				float blendRadius = .05f;
				float sinHeight = .45f; // biggest value is 1.17 matching elastic
				float blendStart = switchCenter - blendRadius;
				float firstPart = cubic(t / switchCenter);
				if (t < blendStart) {
					v = firstPart;
				} else {
					float t2 = (t - blendStart) / (1 - blendStart);
					float theta = (float) (2 * (2 * Math.PI) * t2);
					float scl = (float) (Math.pow(2, -7 * t2) - 0.0078125); // subtract 2^(-7) so we really go to 0
					float secondPart = (float) (1 + sinHeight * scl * Math.sin(theta));
					float blendEnd = switchCenter + blendRadius;
					if (t > blendEnd) {
						v = secondPart;
					} else {
						float blend = (t - blendStart) / (2 * blendRadius);
						blend = cubicEase(blend);
						v = AUMisc.jlerp(firstPart, secondPart, blend);
					}
				}
				break;
			case EASE_ANTICIPATE_CUBIC :
				// My hybrid. Start with a small backward jump, then eases at the end.
				float aeJumpDuration = .05f;
				float aeJumpSize = .05f;
				if (t < aeJumpDuration) {
					v = -aeJumpSize * t / aeJumpDuration;
				} else {
					float t2 = (t - aeJumpDuration) / (1 - aeJumpDuration);
					v = ease(EASE_OUT_CUBIC, t2);
					v = AUMisc.jlerp(-aeJumpSize, 1, v);
				}
				break;
			case EASE_ANTICIPATE_ELASTIC :
				// My hybrid. Start with a small backward jump, then bounces at the end.
				float abJumpDuration = .05f;
				float abJumpSize = .05f;
				if (t < abJumpDuration) {
					v = -abJumpSize * t / abJumpDuration;
				} else {
					float t2 = (t - abJumpDuration) / (1 - abJumpDuration);
					v = ease(EASE_OUT_ELASTIC, t2);
					v = AUMisc.jlerp(-abJumpSize, 1, v);
				}
				break;
		}
		return v;
	}

	static float cubic(float t) {
		return (t * t * t);
	}

	// This is a little cubic: f(t) = (1+g)t^3 - (g)t^2
	// This is the closed form of a cubic Bezier with values (0, 0, -g/3, 1)
	// The industry standard for g is 1.70158. I don't know where that comes from, or
	// why it's so precise. Anything near 1.7 produces a visually indistinguishable result.
	static float back(float t) {
		float g = 1.70158f; // The industry standard, so stick with it.
		return (t * t) * (((1 + g) * t) - g);
	}

	// We want this to run from [0,1], but it really runs from [0.000976564, 1]
	static float elastic(float t) {
		return (float) (Math.pow(2, 10 * (t - 1)) * Math.cos(6 * Math.PI * t));
	}

	// This version of elastic really runs does from [0,1]. Rarely worth the extra effort.
	static float elastic01(float t) {
		double mins = .000976563; // 2^(-10)
		double scls = .999023; // 1-mins
		double s = Math.pow(2, 10 * (t - 1));
		s = (s - mins) / scls;
		return (float) (s * Math.cos(6 * Math.PI * t));
	}

	/*************************************************
	 * QUICK BLENDS
	 *************************************************/

	public static float cosEase(float _t) {
		if (_t < 0)
			return 0;
		if (_t > 1)
			return 1;
		float cost = (float) Math.cos(Math.PI * _t);
		return AUMisc.jmap(cost, 1, -1, 0, 1);
	}

	// A cubic blend. Derivative is 0 at both ends
	public static float S(float _t) { // f(t) = -2x^3 + 3x^2
		if (_t < 0)
			return 0;
		if (_t > 1)
			return 1;
		return _t * _t * (3.0f - 2.0f * _t);
	}

	// Identical to S(), just another name for the same thing
	public static float cubicEase(float _t) { // f(t) = -2x^3 + 3x^2
		if (_t < 0)
			return 0;
		if (_t > 1)
			return 1;
		return _t * _t * (3.0f - 2.0f * _t);
	}

	// fifth-order blend. Slightly flatter at ends than the cubic.
	public static float S5(float _t) {
		if (_t < 0)
			return 0;
		if (_t > 1)
			return 1;
		return _t * _t * _t * (_t * (_t * 6.0f - 15.0f) + 10.0f);
	}

}
