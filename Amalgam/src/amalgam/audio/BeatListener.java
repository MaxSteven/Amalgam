package amalgam.audio;

import ddf.minim.*;
import ddf.minim.analysis.*;

public class BeatListener implements AudioListener {
	private BeatDetect bd;
	private AudioPlayer source;

	public BeatListener(BeatDetect bd, AudioPlayer source) {
		this.source = source;
		this.source.addListener(this);
		this.bd = bd;
	}

	public void samples(float[] samps) {
		bd.detect(source.mix);
	}

	public void samples(float[] sampsL, float[] sampsR) {
		bd.detect(source.mix);
	}
}