package amalgam.audio;

import processing.core.PApplet;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;

/**
 * Abstracted Audio processing class. Currently uses Minim. Add switch for using beads (or sonia?) for beat detection. Add getters/setters etc
 */
public class AudioProcessorMinim implements AudioProcessor {
	private final PApplet p5;

	public AudioPlayer song;
	public Minim minim;
	public ddf.minim.analysis.FFT fftLog;

	// Beat detection
	// Minim
	public BeatListener beatListener;
	public BeatDetect beatDetect;
	public boolean kick;
	public boolean snare;
	public boolean hat;

	// Processed values
	public float energyScaling = 1000f; // Maybe needed to scale sound energies up/down for line-in?
	public float[] bandValues;
	public float totalEnergy;

	public SoundData sData; // contains all of the above values for convenience
	public boolean audioDebug = true; // true for audio related debug
	// ==============================

	public AudioProcessorMinim(PApplet p5, Minim minim, AudioPlayer song) {
		this.p5 = p5;
		this.sData = new SoundData();
		this.minim = minim;
		this.song = song;
		setupMinim();
	}

	public void start() {
		song.loop();
	}

	public void update(boolean draw) {
		resetSoundValues();

		if (draw) {
			// Spectrum
			p5.noFill();
			p5.stroke(100);
			for (int i = 0; i < song.bufferSize() - 1; i++) {
				float x1 = PApplet.map(i, 0, song.bufferSize(), 0, p5.width);
				float x2 = PApplet.map(i + 1, 0, song.bufferSize(), 0, p5.width);
				p5.line(x1, p5.height / 4 + song.left.get(i) * 50, x2, p5.height / 4 + song.left.get(i + 1) * 50);
				p5.line(x1, p5.height / 2 + song.right.get(i) * 50, x2, p5.height / 2 + song.right.get(i + 1) * 50);
				p5.line(x1, 3 * p5.height / 4 + song.mix.get(i) * 50, x2, 3 * p5.height / 4 + song.mix.get(i + 1) * 50);
			}
		}
		p5.noStroke();

		// Beat detection
		p5.fill(0, 255, 0);
		if (beatDetect.isHat()) {
			hat = true;
			if (draw) {
				p5.ellipse(40, 300, 30, 30);
			}
		}
		if (beatDetect.isKick()) {
			kick = true;
			if (draw) {
				p5.ellipse(80, 300, 30, 30);
			}
		}
		if (beatDetect.isSnare()) {
			snare = true;
			if (draw) {
				p5.ellipse(120, 300, 30, 30);
			}
		}

		// Spectral energy log
		totalEnergy = 0;
		fftLog.forward(song.mix);
		for (int i = 0; i < fftLog.avgSize(); i++) {
			float av = fftLog.getAvg(i);
			float band = fftLog.getBand(i);

			// bandValues[i] = band;
			bandValues[i] = av;
			totalEnergy += av;

			// if (av > 50f && draw) {
			if (band > 30f && draw) {
				p5.fill(0, 0, 255);
				p5.ellipse(50 + i * 30, 30, 30, 30);
			}
		}
		if (totalEnergy > 1200f && draw) {
			p5.fill(255);
			p5.ellipse(100, 200, 80, 80);
		}
		sData.update(energyScaling, bandValues, totalEnergy, kick, hat, snare);
	}

	public void setupMinim() {
		beatDetect = new BeatDetect(song.bufferSize(), song.sampleRate());
		beatDetect.setSensitivity(50);
		beatListener = new BeatListener(beatDetect, song);
		fftLog = new ddf.minim.analysis.FFT(song.bufferSize(), song.sampleRate());
		// fftLog.logAverages(22, 3); // 30 averages
		fftLog.logAverages(11, 5); // 30 averages
		bandValues = new float[fftLog.avgSize()];
	}

	public void resetSoundValues() {
		kick = false;
		hat = false;
		snare = false;
		totalEnergy = 0;
	}

	public void stop() {
		minim.stop();
		p5.stop();
	}
}
