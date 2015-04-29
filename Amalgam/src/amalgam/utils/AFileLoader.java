package amalgam.utils;
import geomerative.RG;
import geomerative.RShape;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Loads all Files from the supplied directory into an ArrayList.
 */
public class AFileLoader {
	final PApplet p5;
	String[] filenames;
	String path;

	public AFileLoader(PApplet p5) {
		this.p5 = p5;
	}

	// this doesnt work with app export...internal jar structure is very confusing. Attempt to fix this
	public ArrayList<PImage> loadRasters(String path) {
		ArrayList<PImage> files = new ArrayList<>();

		ArrayList<File> f = getFiles(path, new String[]{"jpg", "png"});
		for (File file : f) {
			files.add(p5.loadImage(file.toString()));
		}

		return files;
	}

	public ArrayList<RShape> loadSVG(String path) {
		// init RG here seems to make more sense
		RG.init(p5);
		ArrayList<RShape> files = new ArrayList<>();

		ArrayList<File> f = getFiles(path, new String[]{"svg"});
		for (File file : f) {
			files.add(RG.loadShape(file.toString()));
		}
		return files;
	}

	public ArrayList<File> getFiles(String path, String[] extensions) {
		ArrayList<File> list = new ArrayList<>();
		File dir = new File(path);
		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		for (File file : files) {
			list.add(file);
		}
		return list;
	}

}