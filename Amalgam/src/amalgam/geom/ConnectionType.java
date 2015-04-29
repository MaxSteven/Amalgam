package amalgam.geom;

public enum ConnectionType {
	LINEAR, SQUARE, SQUARE_MIDPOINT, BEZIER_1, BEZIER_2, ARC_1, ARC_2, ARC_3, CURVE_1;

	public static ConnectionType getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
}
