package amalgam.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import processing.core.PApplet;

/**
 * Convenient access to import/export methods
 */
public class AIO {

	/**
	 * Get timestamp
	 */
	public static String getTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date());
	}

	/**
	 * initialises PDF output
	 */
	public static void startExport(PApplet p5) {
		String f = getTimestamp() + ".pdf";
		p5.beginRecord(PApplet.PDF, f);
	}

	/**
	 * ends PDF output
	 */
	public static void endExport(PApplet p5) {
		p5.endRecord();
	}

	/**
	 * initialises Raw PDF output
	 */
	public static void startRawExport(PApplet p5) {
		String f = getTimestamp() + ".pdf";
		p5.beginRaw(PApplet.PDF, f);
	}

	/**
	 * ends Raw PDF output
	 */
	public static void endRawExport(PApplet p5) {
		p5.endRaw();
	}

	/**
	 * Quick frame saver TGA
	 */
	public static void saveFrameTGA(PApplet p5) {
		String f = getTimestamp() + ".tga";
		p5.saveFrame(f);
	}

	// Check if the folder exists and append the filename with _# if it does to prevent overwriting?
	public static void saveFrameTGASequence(PApplet p5, String folderName) {
		p5.saveFrame(p5.sketchPath + "/" + folderName + "/" + "frame-####.tga");
	}

	// Check if the folder exists and append the filename with _# if it does to prevent overwriting?
	public static void saveFrameTGASequence(PApplet p5, String folderName, int start, int frames) {
		if (p5.frameCount >= start) {
			p5.saveFrame(p5.sketchPath + "/" + folderName + "/" + "frame-####.tga");
		}
		if (p5.frameCount == start + frames) {
			p5.exit();
		}
	}

	/**
	 * Quick frame saver PNG
	 */
	public static void saveFramePNG(PApplet p5) {
		String f = getTimestamp() + ".png";
		p5.saveFrame(f);
	}

	public static void savePNGSequence(PApplet p5, String folderName) {
		p5.saveFrame(p5.sketchPath + "/" + folderName + "/" + "frame-####.png");
	}

	/**
	 * Quick frame saver TIFF
	 */
	public static void saveFrameTIF(PApplet p5) {
		String f = getTimestamp() + ".tif";
		p5.saveFrame(f);
	}

	/**
	 * GIF frames (looping)
	 */
	public static void saveGIFSequence(PApplet p5, int start, int frames) {
		if (p5.frameCount >= start) {
			p5.saveFrame("f###.gif");
		}
		if (p5.frameCount == start + frames) {
			p5.exit();
		}
	}

	/**
	 * GIF frames (looping), from frame 0
	 */
	public static void saveGIFSequence(PApplet p5, int frames) {
		if (p5.frameCount >= 0) {
			p5.saveFrame("f###.gif");
		}
		if (p5.frameCount == frames) {
			p5.exit();
		}
	}
}