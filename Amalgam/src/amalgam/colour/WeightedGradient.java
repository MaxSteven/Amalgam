package amalgam.colour;

import java.util.ArrayList;

import toxi.color.ColorGradient;
import toxi.color.ColorList;
import toxi.color.TColor;
import toxi.math.InterpolateStrategy;
import toxi.math.LinearInterpolation;

/**
 * Extends the functionality found in the Toxiclibs ColorGradient class to produce a 'smooth' List of colours. Useful for creating continuous colour transitions or blending a small selection of base colours into a large set. The total 'colour distance' of an initial ColorList is used to
 * weight each transition in the gradient and reduce choppiness. As a result an initial ColourList
 * containing {#000000, #000000, #000000, #ffffff} will return exactly the same gradient as one containing only {#000000, #ffffff}.
 * 
 * @author Nick Taylor
 */

public class WeightedGradient {
	private ColorList colours;
	private int totalColours;

	private ArrayList<Float> increments;

	public WeightedGradient(ColorList colours) {
		this.colours = colours;
	}

	/**
	 * Calculates an unweighted gradient
	 */
	public ColorList getUnweighted(int totalColours) {
		ColorGradient grad = new ColorGradient();
		for (int i = 0; i < colours.size(); i++) {
			grad.addColorAt(totalColours * (float) i / (colours.size() - 1), colours.get(i));
		}
		grad.setInterpolator(new LinearInterpolation());
		ColorList finalGradient = grad.calcGradient();
		return finalGradient;
	}

	/**
	 * @param colours
	 *            The base ColorList
	 * @param totalColours
	 *            The number of colours in the returned ColorList
	 * @return The gradiated ColorList
	 */
	public ColorList getGradient(int totalColours, InterpolateStrategy inter) {
		return getGradient(totalColours, inter, false);
	}
	
	public ColorList getGradient(int totalColours, InterpolateStrategy inter, boolean cycle) {
		this.totalColours = totalColours;
		
		if (cycle) {
			colours.add(colours.get(0).copy());
		}
		
		ColorGradient grad = new ColorGradient();
		increments = new ArrayList<>();
		ArrayList<Float> incrementsNonNormalized = new ArrayList<>();
		float totalDist = 0;
		increments.add(0f);

		for (int i = 0; i < colours.size() - 1; i++) {
			TColor clr1 = colours.get(i);
			TColor clr2 = colours.get(i + 1);
			float segment = clr1.distanceToRGB(clr2);
			totalDist += segment;
			incrementsNonNormalized.add(totalDist);
		}

		for (Float seg : incrementsNonNormalized) {
			increments.add(seg / totalDist);
		}

		for (int i = 0; i < colours.size(); i++) {
			grad.addColorAt(increments.get(i) * totalColours, colours.get(i));
		}

		grad.setInterpolator(inter);
		return grad.calcGradient();
	}

	/**
	 * Default LinearInterpolation version
	 */
	public ColorList getGradient(int totalColours) {
		return getGradient(totalColours, new LinearInterpolation());
	}

	/**
	 * Work in progress - flips the current gradient, adds it to the current list, and compresses the new list to its original size
	 */
	public ColorList doubleGradient() {
		ColorGradient grad = new ColorGradient();

		ArrayList<Float> incrementsNew = new ArrayList<>();
		for (int i = 0; i < increments.size(); i++) {
			incrementsNew.add((increments.get(i) / 2));
		}
		for (int i = increments.size() - 1; i >= 0; i--) {
			incrementsNew.add((1 - increments.get(i) / 2));
		}

		for (int i = 0; i < incrementsNew.size(); i++) {
			if (i < incrementsNew.size() / 2) {
				grad.addColorAt(incrementsNew.get(i) * totalColours, colours.get(i));
			} else {
				grad.addColorAt(incrementsNew.get(i) * totalColours, colours.get(incrementsNew.size() - i - 1));
			}
		}
		grad.setInterpolator(new LinearInterpolation());
		return grad.calcGradient();
	}

	public void setColours(ColorList colours) {
		this.colours = colours;
	}

	public ColorList getColours() {
		return colours;
	}
}