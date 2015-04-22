package amalgam.toxi;

import processing.core.PApplet;
import toxi.math.InterpolateStrategy;
import AULib.AUMisc;

/**
 * Removed SYM waves and SINE/COSINE (CosineInt), TRIANGLE(Sym linear), SAWTOOTH(linear), BOX (DecimatedInt w/ param = 2)
 */
public class WaveInterpolation implements InterpolateStrategy {

	private int waveType;
	private float adj; // wave adjustment param

	public WaveInterpolation(int waveType, float adj) {
		this.waveType = waveType;
		this.adj = adj;
	}

	@Override
	public double interpolate(double a, double b, double f) {
		return a + (b - a) * wave(waveType, (float) f, adj);
	}

	@Override
	public float interpolate(float a, float b, float f) {
		return a + (b - a) * wave(waveType, f, adj);
	}

	public static final int WAVE_BLOB = 5; // Gaussian bump from 1 to 0
	public static final int WAVE_VAR_BLOB = 6; // controllable blob
	public static final int WAVE_BIAS = 7; // bow down for a<.5, ramp@.5, up for a>.5
	public static final int WAVE_GAIN = 8; // S for a<.5, ramp@.5, inverse-S for a>.5

	private float wave(int _waveType, float _t, float _a) {
		float t = _t;

		if (t >= 0)
			t = (float) (t % 1.);
		else
			t = (float) (1. - (Math.abs(t) % 1.));

		float v = 0;
		float a = PApplet.constrain(_a, 0, 1);

		switch (_waveType) {
			case WAVE_BLOB : // Gaussian bump
				float e2p5 = 0.00193045f; // e^-(2.5^2), which is < .004 (about 1/255)
				float ev = AUMisc.jmap(t, 0, 1, 0, 2.5f);
				v = AUMisc.jmap((float) Math.exp(-AUMisc.jsq(ev)), 1, e2p5, 1, 0);
				break;
			case WAVE_VAR_BLOB : // Baranoski-Rokne controllable blob
				v = (AUMisc.jsq(t - 1) * AUMisc.jsq(t + 1)) / (1 + (a * 50 * AUMisc.jsq(t))); // expand the range of a from [0,1] to [0,50]
				break;
			case WAVE_BIAS :
				// Schlick bias function
				// 0 to 1. a = 0 = flat w/jump at 1, .25=bowed down, .5=flat, .75=bowed up, 1=jump to 1
				if (a == 0) {
					v = (t < 1) ? 0 : 1;
				} else {
					float bias1 = (1.f / a) - 2.f;
					v = t / (1.f + (bias1 * (1.f - t)));
				}
				break;
			case WAVE_GAIN :
				// Schlick gain function. derivatives not 0.
				if (a == 0) {
					v = (t < .5) ? 0 : 1;
				} else {
					float gain1 = ((1.f / a) - 2.f) * (1.f - (2.f * t));
					if (t < .5)
						v = t / (1 + gain1);
					else
						v = (gain1 - t) / (gain1 - 1);
				}
				break;
		}
		return PApplet.constrain(v, 0, 1);
	}

}
