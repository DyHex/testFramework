package core.utilities.dataGenerators

class GenerateSSN {
	static Random r = new Random()

	static String norwegian(){
		String ssn = ""
		ssn += (r.nextInt(31)+1).toString().padLeft(2, '0')
		ssn += (r.nextInt(12)+1).toString().padLeft(2, '0')
		ssn += r.nextInt(99).toString().padLeft(2, '0')
		ssn += r.nextInt(999).toString().padLeft(3, '0')

		ArrayList idNumberArray = ssn.collect {it as int}

		calculateCheckDigit(idNumberArray, [3, 7, 6, 1, 8, 9, 4, 5, 2, 1])
		calculateCheckDigit(idNumberArray, [5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1])

		if(idNumberArray.size != 11){
			// If incomplete, generate new
			return norwegian()
		}

		ssn = idNumberArray.toString().replaceAll(/\D++/, '')	// return
	}

	private static void calculateCheckDigit(ArrayList idNumberArray, ArrayList weightNumbers){
		int tmp = 0
		[idNumberArray, weightNumbers].transpose().each {a, b -> tmp += a*b}
		int remainder = tmp % 11
		int checkDigit
		if (remainder == 0) {
			checkDigit = 0
		} else {
			checkDigit = 11 - remainder
			if(checkDigit == 10){
				// If remainder is 10, SSN must be throw'n
				return
			}
		}
		idNumberArray.add(checkDigit)
	}
}
