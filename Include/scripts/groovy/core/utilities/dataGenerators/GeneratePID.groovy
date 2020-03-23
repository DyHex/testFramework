package core.utilities.dataGenerators

import org.apache.commons.lang.RandomStringUtils

public class GeneratePID {

	private static Random random = new Random()

	public static String create(){

		List<String> values = [
			numberFixedDigitLength(4),
			// 4
			numberFixedDigitLengthWithinRange(4, 6),
			// 4-6
			numberFixedDigitLengthWithinRange(1, 2),
			// 1-2
			numberFixedDigitLengthWithinRange(1, 38)	// 1-38
		]

		return values.join("-")
	}

	private static String numberFixedDigitLength(int length){
		return RandomStringUtils.randomNumeric(length)
	}

	private static String numberFixedDigitLengthWithinRange(int min, int max){
		if(min >= max){
			throw new IllegalArgumentException("max must be greater than min")
		}

		return numberFixedDigitLength(random.nextInt(max-min)+min)
	}
}