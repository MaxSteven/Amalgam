package amalgam.colour;

public enum ColourSet {
	CMYK,
	RGB,
	CLASSIC,
	BLUE_GOLD,
	ORANGE_BLUE,
	CITRUS,
	RPYB,
	RBOP,
	FIRE,
	SUNSET_1,
	SUNSET_2,
	FADE_RED,
	FADE_LIME,
	FADE_SEA,
	FADE_BLUE,
	FADE_GOLD,
	ROB_PASTEL,
	ROB_PASTEL_2,
	JAZZBERRY,
	JAZZBERRY_2,
	JAZZBERRY_3,
	JAZZBERRY_4,
	OGP,
	ORANGE_RED,
	BLUE_LIME,
	BLUE_PINK,
	NVIDIA,
	GRY,
	BYR,
	BLUE_GOLD_2,
	JAZZBERRY_5,
	DUST_ORANGE,
	BLUE_FLAME,
	BLUE_LIME_3,
	BLUE_LIME_2,
	PASTEL_OG,
	CYAN_RED,
	MANILA;

	public static ColourSet getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}

	public ColourSet getRandomWarm() {
		return null;
	}

	public ColourSet getRandomCold() {
		return null;
	}

	public ColourSet getRandomDark() {
		return null;
	}

	public ColourSet getRandomLight() {
		return null;
	}

}
