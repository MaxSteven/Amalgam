package amalgam.lattice;

/**
 * Strategy used to subdivide a polygon
 */
public enum SubdivisionType {

	CORNERS_TO_CENTRE, CORNERS_TO_RANDOM, MIDPOINTS_TO_CENTRE, HALVE_RANDOM, HALVE_SET, MIDPOINT_POLYGON, MIDTHIRD_POLYGON, BEVEL, BEVEL_FIXED;

	public static SubdivisionType getRandom() {
		int val = (int) (Math.random() * values().length);
		System.out.println(values()[val]);
		return values()[val];
	}
}
