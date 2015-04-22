package amalgam.colour;

import java.util.Iterator;

import toxi.color.ColorList;
import toxi.color.TColor;
import toxi.math.LinearInterpolation;

/**
 * Quickly generate a random ColorList without the annoying boilerplate.
 */
public class QuickColours {
	// add boolean to randomise order?
	public static ColorList get(int size, ColourSet setName, boolean cycle, boolean reflect) {
		ColorList Colours = new ColorList();
		addBaseColours(Colours, setName);

		if (cycle) {
			Colours.add(Colours.get(0));
		}

		WeightedGradient grad = new WeightedGradient(Colours);
		Colours = grad.getGradient(size, new LinearInterpolation());

		if (reflect) {
			ColorList ref = new ColorList(Colours);
			ref.reverse();
			for (TColor clr : ref) {
				Colours.add(clr);
			}
			// resize by removing every other colour
			Iterator<TColor> it = Colours.iterator();
			int i = 0;
			while (it.hasNext()) {
				TColor tc = (TColor) it.next();
				if (i % 2 == 0) {
					it.remove();
				}
				i++;
			}
		}

		return Colours;
	}

	private static void addBaseColours(ColorList Colours, ColourSet setName) {
		switch (setName) {
			case CMYK :
				Colours.add(TColor.newHex("00ffff"));
				Colours.add(TColor.newHex("ff00ff"));
				Colours.add(TColor.newHex("ffff00"));
				Colours.add(TColor.newHex("000000"));
				break;
			case RGB :
				Colours.add(TColor.newHex("ff0000"));
				Colours.add(TColor.newHex("00ff00"));
				Colours.add(TColor.newHex("0000ff"));
				break;
			case CLASSIC :
				Colours.add(TColor.newHex("fac13a"));
				Colours.add(TColor.newHex("dc2338"));
				Colours.add(TColor.newHex("124473"));
				Colours.add(TColor.newHex("fdfefb"));
				Colours.add(TColor.newHex("93caeb"));
				break;
			case ROB_PASTEL :
				Colours.add(TColor.newHex("302e40"));
				Colours.add(TColor.newHex("bf4949"));
				Colours.add(TColor.newHex("f2785c"));
				Colours.add(TColor.newHex("f2c3a7"));
				Colours.add(TColor.newHex("add9d4"));
				break;
			case ROB_PASTEL_2 :
				Colours.add(TColor.newHex("f1f0ec"));
				Colours.add(TColor.newHex("060709"));
				Colours.add(TColor.newHex("e05a35"));
				Colours.add(TColor.newHex("c2433a"));
				Colours.add(TColor.newHex("7d9aaa"));
				Colours.add(TColor.newHex("44797f"));
				break;
			case BLUE_GOLD :
				Colours.add(TColor.newHex("232c35"));
				Colours.add(TColor.newHex("04a1f0"));
				Colours.add(TColor.newHex("d7d8da"));
				Colours.add(TColor.newHex("f5f5f7"));
				Colours.add(TColor.newHex("d4b678"));
				break;
			case BLUE_GOLD_2 :
				Colours.add(TColor.newHex("1c1f25"));
				Colours.add(TColor.newHex("003E89"));
				Colours.add(TColor.newHex("d8bf93"));
				Colours.add(TColor.newHex("ffffff"));
				break;
			case CITRUS :
				Colours.add(TColor.newHex("f41c54"));
				Colours.add(TColor.newHex("ff9f00"));
				Colours.add(TColor.newHex("fbd506"));
				Colours.add(TColor.newHex("a8bf12"));
				Colours.add(TColor.newHex("00aab5"));
				break;
			case ORANGE_BLUE :
				Colours.add(TColor.newHex("ea5930"));
				Colours.add(TColor.newHex("f8af1e"));
				Colours.add(TColor.newHex("f5e5c0"));
				Colours.add(TColor.newHex("187780"));
				Colours.add(TColor.newHex("202930"));
				break;
			case RPYB :
				Colours.add(TColor.newHex("ec1d25"));
				Colours.add(TColor.newHex("7d4199"));
				Colours.add(TColor.newHex("fedb00"));
				Colours.add(TColor.newHex("042446"));
				Colours.add(TColor.newHex("00529b"));
				Colours.add(TColor.newHex("fbfdfb"));
				break;
			case RBOP :
				Colours.add(TColor.newHex("170e21"));
				Colours.add(TColor.newHex("dc2338"));
				Colours.add(TColor.newHex("124473"));
				Colours.add(TColor.newHex("ff6005"));
				Colours.add(TColor.newHex("ffffff"));
				Colours.add(TColor.newHex("9b3a5f"));
				break;
			case FIRE :
				Colours.add(TColor.newHex("281e17"));
				Colours.add(TColor.newHex("9b0e1f"));
				Colours.add(TColor.newHex("db4516"));
				Colours.add(TColor.newHex("ffd255"));
				Colours.add(TColor.newHex("edead1"));
				break;
			case SUNSET_1 :
				Colours.add(TColor.newHex("d95427"));
				Colours.add(TColor.newHex("f2960d"));
				Colours.add(TColor.newHex("f9d567"));
				Colours.add(TColor.newHex("f7e3a6"));
				Colours.add(TColor.newHex("7bafba"));
				Colours.add(TColor.newHex("0185c3"));
				Colours.add(TColor.newHex("00399a"));
				break;
			case SUNSET_2 :
				Colours.add(TColor.newHex("f1f0c0"));
				Colours.add(TColor.newHex("f0c051"));
				Colours.add(TColor.newHex("fe5f0b"));
				Colours.add(TColor.newHex("a14051"));
				Colours.add(TColor.newHex("604071"));
				Colours.add(TColor.newHex("000000"));
				break;
			case FADE_RED :
				Colours.add(TColor.newHex("2d112c"));
				Colours.add(TColor.newHex("530031"));
				Colours.add(TColor.newHex("820233"));
				Colours.add(TColor.newHex("ca293e"));
				Colours.add(TColor.newHex("ef4339"));
				break;
			case FADE_GOLD :
				Colours.add(TColor.newHex("191724"));
				Colours.add(TColor.newHex("4c4547"));
				Colours.add(TColor.newHex("8c594e"));
				Colours.add(TColor.newHex("d18952"));
				Colours.add(TColor.newHex("fdb157"));
				break;
			case FADE_LIME :
				Colours.add(TColor.newHex("122600"));
				Colours.add(TColor.newHex("335a13"));
				Colours.add(TColor.newHex("83a500"));
				Colours.add(TColor.newHex("d2da03"));
				Colours.add(TColor.newHex("f1f2ed"));
				break;
			case FADE_SEA :
				Colours.add(TColor.newHex("081724"));
				Colours.add(TColor.newHex("589494"));
				Colours.add(TColor.newHex("8ebbb4"));
				Colours.add(TColor.newHex("d0dcd0"));
				Colours.add(TColor.newHex("f5eed2"));
				break;
			case FADE_BLUE :
				Colours.add(TColor.newHex("04172e"));
				Colours.add(TColor.newHex("27588f"));
				Colours.add(TColor.newHex("5d97cf"));
				Colours.add(TColor.newHex("97cbff"));
				Colours.add(TColor.newHex("e6ebfc"));
				break;
			case JAZZBERRY :
				Colours.add(TColor.newHex("19b7a9"));
				Colours.add(TColor.newHex("373330"));
				Colours.add(TColor.newHex("e6d9ac"));
				Colours.add(TColor.newHex("b3b302"));
				Colours.add(TColor.newHex("ff7900"));
				break;
			case JAZZBERRY_2 :
				Colours.add(TColor.newHex("373330"));
				Colours.add(TColor.newHex("006d6a"));
				Colours.add(TColor.newHex("fe4529"));
				Colours.add(TColor.newHex("ff7900"));
				Colours.add(TColor.newHex("ffac00"));
				Colours.add(TColor.newHex("e4cf94"));
				break;
			case JAZZBERRY_3 :
				Colours.add(TColor.newHex("da0734"));
				Colours.add(TColor.newHex("f1a20d"));
				Colours.add(TColor.newHex("4aabb1"));
				Colours.add(TColor.newHex("fcf3e7"));
				Colours.add(TColor.newHex("3f1833"));
				break;
			case JAZZBERRY_4 :
				Colours.add(TColor.newHex("047d8f"));
				Colours.add(TColor.newHex("f57305"));
				Colours.add(TColor.newHex("f38808"));
				Colours.add(TColor.newHex("d3c7b6"));
				Colours.add(TColor.newHex("f3ebdb"));
				break;
			case JAZZBERRY_5 :
				Colours.add(TColor.newHex("19b7a9"));
				Colours.add(TColor.newHex("373330"));
				Colours.add(TColor.newHex("e6d9ac"));
				Colours.add(TColor.newHex("b3b302"));
				Colours.add(TColor.newHex("ff7900"));
				break;
			case OGP :
				Colours.add(TColor.newHex("ef643f"));
				Colours.add(TColor.newHex("088899"));
				Colours.add(TColor.newHex("ffffff"));
				Colours.add(TColor.newHex("4c1b3f"));
				Colours.add(TColor.newHex("9b3a5f"));
				break;
			case ORANGE_RED :
				Colours.add(TColor.newHex("382c2c"));
				Colours.add(TColor.newHex("ee4233"));
				Colours.add(TColor.newHex("ece9e4"));
				Colours.add(TColor.newHex("f26f31"));
				break;
			case BLUE_LIME :
				Colours.add(TColor.newHex("282828"));
				Colours.add(TColor.newHex("00404f"));
				Colours.add(TColor.newHex("eeeeee"));
				Colours.add(TColor.newHex("ffffff"));
				Colours.add(TColor.newHex("d9d72b"));
				break;
			case BLUE_LIME_2 :
				Colours.add(TColor.newHex("014E0C"));
				Colours.add(TColor.newHex("FFFF00"));
				Colours.add(TColor.newHex("FFFFFF"));
				Colours.add(TColor.newHex("0166BA"));
				Colours.add(TColor.newHex("00246D"));
				break;
			case BLUE_LIME_3 :
				Colours.add(TColor.newHex("2C6A15"));
				Colours.add(TColor.newHex("70FF05"));
				Colours.add(TColor.newHex("E5FB2C"));
				Colours.add(TColor.newHex("FFFFFF"));
				Colours.add(TColor.newHex("E5FB2C"));
				Colours.add(TColor.newHex("0097A9"));
				break;
			case BLUE_PINK :
				Colours.add(TColor.newHex("315C99"));
				Colours.add(TColor.newHex("2FC4B1"));
				Colours.add(TColor.newHex("46A5D5"));
				Colours.add(TColor.newHex("FF7D9F"));
				Colours.add(TColor.newHex("C1E5D4"));
				break;
			case NVIDIA :
				Colours.add(TColor.newHex("000000"));
				Colours.add(TColor.newHex("92CE00"));
				Colours.add(TColor.newHex("ffffff"));
				break;
			case GRY :
				Colours.add(TColor.newHex("2ab69d"));
				Colours.add(TColor.newHex("343844"));
				Colours.add(TColor.newHex("e65848"));
				Colours.add(TColor.newHex("fdc536"));
				Colours.add(TColor.newHex("fcf2d7"));
				break;
			case BYR :
				Colours.add(TColor.newHex("150146"));
				Colours.add(TColor.newHex("2d71ca"));
				Colours.add(TColor.newHex("f9b350"));
				Colours.add(TColor.newHex("f54945"));
				break;
			case DUST_ORANGE :
				Colours.add(TColor.newHex("2d3032"));
				Colours.add(TColor.newHex("dd5f18"));
				Colours.add(TColor.newHex("fba922"));
				Colours.add(TColor.newHex("f7f7f7"));
				break;
			case BLUE_FLAME :
				Colours.add(TColor.newHex("134578"));
				Colours.add(TColor.newHex("25CCFF"));
				Colours.add(TColor.newHex("FFFFFF"));
				Colours.add(TColor.newHex("FFFE03"));
				Colours.add(TColor.newHex("FFAE01"));
				Colours.add(TColor.newHex("EB0706"));
				break;
			case PASTEL_OG :
				Colours.add(TColor.newHex("312f40"));
				Colours.add(TColor.newHex("49a69c"));
				Colours.add(TColor.newHex("efeac5"));
				Colours.add(TColor.newHex("e89063"));
				Colours.add(TColor.newHex("bf5656"));
			case CYAN_RED :
				Colours.add(TColor.newHex("002849"));
				Colours.add(TColor.newHex("00AAD7"));
				Colours.add(TColor.newHex("00E2D2"));
				Colours.add(TColor.newHex("FFFEFF"));
				Colours.add(TColor.newHex("00E2D2"));
				Colours.add(TColor.newHex("B4000E"));
			case MANILA :
				Colours.add(TColor.newRGB(0, 0, 0));
				Colours.add(TColor.newRGB(1, 1, 1));
				Colours.add(TColor.newRGB(245 / 255f, 147 / 255f, 98 / 255f));
				Colours.add(TColor.newRGB(152 / 255f, 255 / 255f, 219 / 255f));
				Colours.add(TColor.newRGB(212 / 255f, 255 / 255f, 69 / 255f));
				break;
			default :
				break;
		}
	}

	public static ColorList getOriginal(ColourSet setName) {
		ColorList cl = new ColorList();
		addBaseColours(cl, setName);
		return cl;
	}

	public static ColorList get(int size, ColourSet setName) {
		return get(size, setName, true, false);
	}

	public static ColorList get(int size) {
		return get(size, ColourSet.getRandom(), true, false);
	}

	/**
	 * Generates gradient from the given number of randomly created base colours
	 */
	public static ColorList generateRandom(int baseSize, int size, boolean cycle) {
		ColorList Colours = new ColorList();

		for (int i = 0; i < baseSize; i++) {
			Colours.add(TColor.newRandom());
		}

		if (cycle) {
			Colours.add(Colours.get(0));
		}
		WeightedGradient grad = new WeightedGradient(Colours);
		Colours = grad.getGradient(size, new LinearInterpolation());
		return Colours;
	}

	public QuickColours() {
	}

	@Deprecated
	public ColorList getFiery(int size) {
		ColorList clrs = new ColorList();
		clrs.add(TColor.newHex("fac13a"));
		clrs.add(TColor.newHex("dc2338"));
		clrs.add(TColor.newHex("124473"));
		clrs.add(TColor.newHex("fdfefb"));
		clrs.add(TColor.newHex("93caeb"));
		clrs.add(TColor.newHex("fac13a"));
		WeightedGradient grad = new WeightedGradient(clrs);
		clrs = grad.getGradient(size, new LinearInterpolation());
		return clrs;
	}

	@Deprecated
	public ColorList getRandomSet(int size) {
		ColorList clrs = new ColorList();
		int n = 6;

		int a = (int) ((int) n * Math.random());
		switch (a) {
			case 0 :
				clrs.add(TColor.newHex("f41c54"));
				clrs.add(TColor.newHex("ff9f00"));
				clrs.add(TColor.newHex("fbd506"));
				clrs.add(TColor.newHex("a8bf12"));
				clrs.add(TColor.newHex("00aab5"));
				clrs.add(TColor.newHex("f41c54"));
				break;
			case 1 :
				clrs.add(TColor.newHex("f1f0ec"));
				clrs.add(TColor.newHex("060709"));
				clrs.add(TColor.newHex("e05a35"));
				clrs.add(TColor.newHex("c2433a"));
				clrs.add(TColor.newHex("7d9aaa"));
				clrs.add(TColor.newHex("44797f"));
				clrs.add(TColor.newHex("f1f0ec"));
				break;
			case 2 :
				clrs.add(TColor.newHex("fac13a"));
				clrs.add(TColor.newHex("dc2338"));
				clrs.add(TColor.newHex("124473"));
				clrs.add(TColor.newHex("fdfefb"));
				clrs.add(TColor.newHex("93caeb"));
				clrs.add(TColor.newHex("fac13a"));
				break;
			case 3 :
				clrs.add(TColor.newHex("d95427"));
				clrs.add(TColor.newHex("f2960d"));
				clrs.add(TColor.newHex("f9d567"));
				clrs.add(TColor.newHex("f7e3a6"));
				clrs.add(TColor.newHex("7bafba"));
				clrs.add(TColor.newHex("0185c3"));
				clrs.add(TColor.newHex("00399a"));
				clrs.add(TColor.newHex("d95427"));
				break;
			case 4 :
				clrs.add(TColor.newHex("122600"));
				clrs.add(TColor.newHex("335a13"));
				clrs.add(TColor.newHex("83a500"));
				clrs.add(TColor.newHex("d2da03"));
				clrs.add(TColor.newHex("f1f2ed"));
				clrs.add(TColor.newHex("122600"));
				break;
			case 5 :
				clrs.add(TColor.newHex("081724"));
				clrs.add(TColor.newHex("589494"));
				clrs.add(TColor.newHex("8ebbb4"));
				clrs.add(TColor.newHex("d0dcd0"));
				clrs.add(TColor.newHex("f5eed2"));
				clrs.add(TColor.newHex("081724"));
				break;
		}
		WeightedGradient grad = new WeightedGradient(clrs);
		clrs = grad.getGradient(size, new LinearInterpolation());
		return clrs;
	}

	@Deprecated
	public ColorList getRandomSet2(int size) {
		ColorList clrs = new ColorList();
		int n = 4;

		int a = (int) ((int) n * Math.random());
		switch (a) {
			case 0 :
				clrs.add(TColor.newHex("d95427"));
				clrs.add(TColor.newHex("f2960d"));
				clrs.add(TColor.newHex("f9d567"));
				clrs.add(TColor.newHex("f7e3a6"));
				clrs.add(TColor.newHex("7bafba"));
				clrs.add(TColor.newHex("0185c3"));
				clrs.add(TColor.newHex("00399a"));
				clrs.add(TColor.newHex("d95427"));
				break;
			case 1 :
				clrs.add(TColor.newHex("00ffff"));
				clrs.add(TColor.newHex("ff00ff"));
				clrs.add(TColor.newHex("ffff00"));
				clrs.add(TColor.newHex("000000"));
				clrs.add(TColor.newHex("ffffff"));
				clrs.add(TColor.newHex("00ffff"));
				break;
			case 2 :
				clrs.add(TColor.newHex("170e21"));
				clrs.add(TColor.newHex("dc2338"));
				clrs.add(TColor.newHex("124473"));
				clrs.add(TColor.newHex("ff6005"));
				clrs.add(TColor.newHex("ffffff"));
				clrs.add(TColor.newHex("9b3a5f"));
				clrs.add(TColor.newHex("170e21"));
				break;
			case 3 :
				clrs.add(TColor.newHex("2E0927"));
				clrs.add(TColor.newHex("D90000"));
				clrs.add(TColor.newHex("FF2D00"));
				clrs.add(TColor.newHex("FF8C00"));
				clrs.add(TColor.newHex("04756F"));
				clrs.add(TColor.newHex("F1F1F1"));
				clrs.add(TColor.newHex("2E0927"));
				break;
		}
		WeightedGradient grad = new WeightedGradient(clrs);
		clrs = grad.getGradient(size, new LinearInterpolation());
		return clrs;
	}
}