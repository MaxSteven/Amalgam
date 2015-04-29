package amalgam.utils;

import java.util.Iterator;
import java.util.Random;

/**
 * Class with a collection of useful static methods for generating random numbers or items from lists. TODO: Noise? Gaussian? 
 * 
 */
public class ARnd {
	/**
	 * Quick random boolean with weighting (true if random number is < w)
	 */
	public static boolean rn(float w) {
		return (Math.random() < w ? true : false);
	}

	/**
	 * Quick random float between a and n
	 */
	public static float fl(float a, float n) {
		return (float) (a + Math.random() * (n - a));
	}

	public static float fl(float n) {
		return fl(0, n);
	}

	public static float fls(float a) {
		return fl(-a, a);
	}
	/**
	 * Quick random int between a and n
	 */
	public static int in(int a, int n) {
		Random rnd = new Random();
		return rnd.nextInt((n - a) + 1) + a;
	}

	public static int in(int n) {
		return in(0, n);
	}

	public static int ins(int n) {
		return in(-n, n);
	}

	/**
	 * Generic method selecting random item from iterable (e.g. list)
	 */

	// usually to replace colours.get((int)p5.random(colours.size()))
	public static <T> T get(Iterable<T> list) {
		// get list size
		Iterator<T> it = list.iterator();
		int sz = 0;
		while (it.hasNext()) {
			it.next();
			sz++;
		}
		
		// random index
		Random random = new Random();
		int index = random.nextInt(sz);

		// get the item
		T item = null;
		int i = 0;
		Iterator<T> it2 = list.iterator();
		while (it2.hasNext()) {
			item = (T) it2.next();
			if (i == index)
				break;
			i++; // here..?
		}
		return item;
	}

	// Generic method selecting item from list using modulo (e.g. stepping through ColorList with index)
	public static <T> T ind(Iterable<T> list, int ind) {
		// get list size
		Iterator<T> it = list.iterator();
		int sz = 0;
		while (it.hasNext()) {
			it.next();
			sz++;
		}

		int index = (sz+ind) % sz;

		// get the item
		T item = null;
		int i = 0;
		Iterator<T> it2 = list.iterator();
		while (it2.hasNext()) {
			item = (T) it2.next();
			if (i == index)
				break;
			i++;
		}
		return item;
	}

}
