package examples.audio;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.math.LinearInterpolation;
import amalgam.audio.AudioProcessorMinim;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

@SuppressWarnings("serial")
public class BasicAudio_Example extends PApplet {

	// 120

	Minim minim;
	AudioProcessorMinim proc;
	AudioPlayer song;

	public void setup() {
		size(1280, 720, P2D);
		background(0);
		smooth(8);
		noStroke();

		minim = new Minim(this);
		song = minim.loadFile(getClass().getResource("/nike/arcs/audio/resources/cdw.mp3").toString(), 512);
		proc = new AudioProcessorMinim(this, minim, song);
		proc.start();
	}

	public void draw() {
		background(0);
		proc.update(false);

		translate(width / 2, height / 2);
		// rotate(frameCount / 150f);

		float radBase = 200;
		float W = 250;

		noFill();
		stroke(0, 0, 255);
		int rm = song.bufferSize() % 160;
		int N = song.bufferSize() - rm;

		// Pure buffer values - 160*3 = 480
		for (int i = 0; i < N; i++) {
			if (i % 3 == 0) {
				float th = map(i, 0, N, 0, TAU);
				float w = song.mix.get(i) * W;
				w = constrain(w, 0, radBase * 0.8f);

				Vec2D l = new Vec2D(radBase + w, 0).rotate(th);
				Vec2D ll = new Vec2D(radBase - w, 0).rotate(th);
				line(l.x, l.y, ll.x, ll.y);
			}
		}

		stroke(255, 0, 0);
		line(0, -height, 0, height);
		// Bandvalues from FFT
		for (int i = 0; i < proc.bandValuesLog.length; i++) {
			float f = proc.bandValuesLog[i];

			float th = map(i, 0, proc.bandValuesLog.length, 0, PI);
			float w = f;
//			w = constrain(w, 0, radBase * 0.7f);
			w = map(w, 0, proc.highestBandEnergy, 0, radBase*0.7f);

			Vec2D l = new Vec2D(radBase + w, 0).rotate(th);
			Vec2D ll = new Vec2D(radBase - w, 0).rotate(th);
			line(l.x, l.y, ll.x, ll.y);
			l.reflect(new Vec2D(1, 0));
			ll.reflect(new Vec2D(1, 0));
			line(l.x, l.y, ll.x, ll.y);
		}

		// lin
		stroke(0, 255, 0);
		// Bandvalues from FFT
		for (int i = 0; i < proc.bandValuesLin.length; i++) {
			float f = proc.bandValuesLin[i];

			float th = map(i, 0, proc.bandValuesLin.length, 0, PI);
			float w = f;
			LinearInterpolation li = new LinearInterpolation();
			float adjuster = li.interpolate(0, 50, (float) i / proc.bandValuesLin.length);
			w = adjuster * constrain(w, 0, radBase * 0.7f);

			Vec2D l = new Vec2D(radBase + w, 0).rotate(th);
			Vec2D ll = new Vec2D(radBase - w, 0).rotate(th);
			line(l.x, l.y, ll.x, ll.y);
			l.reflect(new Vec2D(1, 0));
			ll.reflect(new Vec2D(1, 0));
			line(l.x, l.y, ll.x, ll.y);
		}

		System.out.println(proc.totalEnergy);
		
	}

	public void stop() {
		minim.stop();
		super.stop();
	}

}
