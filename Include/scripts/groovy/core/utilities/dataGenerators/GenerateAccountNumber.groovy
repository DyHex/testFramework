package core.utilities.dataGenerators

import org.apache.commons.lang.RandomStringUtils

public class GenerateAccountNumber {

	public static String norwegianMod11(){
		return giveMod11CheckDigit(RandomStringUtils.randomNumeric(10))
	}

	private static String giveMod11CheckDigit(String numberSequence) {
		int sum = 0;
		for (int i = 0; i < numberSequence.length(); i++) {
			int weightNumber = calculateWeightNumber(i)
			sum += Character.getNumericValue(numberSequence.charAt(i)) * weightNumber
		}
		int remainder = sum % 11
		int checkDigit
		if (remainder == 0) {
			checkDigit = 0
		} else {
			checkDigit = 11 - remainder
			if(checkDigit == 10){
				// No account number has letters... (See comment block at the end)
				return giveMod11CheckDigit(RandomStringUtils.randomNumeric(10))
			}
		}
		return numberSequence + checkDigit
	}

	private static int calculateWeightNumber(int i) {
		return 7 - ((i + 2) % 6);
	}
}


/*
 * INFO: 
 * If the remainder from the division is 0 or 1, then the subtraction will yield a two digit number of either 10 or 11. 
 * This won't work, so if the check digit is 10, then X is frequently used as the check digit and if the check digit is
 * 11 then 0 is used as the check digit. If X is used, then the field for the check digit has to be defined as character
 * (PIC X) or there will be a numeric problem.
 * 
 * Steps to verify if the check digit is included as part of the number:
 * 
 * The entire number is multiplied by the same weights that were used to calculate and the check digit itself is multiplied by 1.
 * The results of the multiplication are added together.
 * The sum is divided by 11 and if the remainder is 0, the number is correct.
 * PROBLEM: Note that if the check digit is X then 10 is used in the multiplication. Code for this occurrence must be included.
 */
