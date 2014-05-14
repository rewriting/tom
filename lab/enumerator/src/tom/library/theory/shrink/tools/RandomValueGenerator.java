package tom.library.theory.shrink.tools;

import java.util.Random;

public class RandomValueGenerator {
	public static int generateRandomFromRange(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min) + min;
	}
}
