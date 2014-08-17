package tom.library.shrink.tools;

import java.util.Random;
/**
 * <p>
 * A tool class to deal with random number generation
 * </p>
 * 
 * @author nauval
 *
 */
public class RandomValueGenerator {
	
	/**
	 * <p>
	 * Generates random value between min (inclusive) and max (exclusive)
	 * </p>
	 * @param min
	 * @param max
	 * @return a value between min and max
	 */
	public static int generateRandomFromRange(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min) + min;
	}
	
	/**
	 * <p>
	 * Generates random value between min (inclusive) and max (exclusive)
	 * </p>
	 * @param min
	 * @param max
	 * @return a value between min and max
	 */
	public static float generateRandomFromRange(float min, float max) {
		Random rand = new Random();
		return rand.nextFloat() * (max-min) + min;
	}
	
	/**
	 * <p>
	 * Generates random value between min (inclusive) and max (exclusive)
	 * </p>
	 * @param min
	 * @param max
	 * @return a value between min and max
	 */
	public static double generateRandomFromRange(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble() * (max-min) + min;
	}
}
