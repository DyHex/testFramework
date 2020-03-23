package core.utilities.dataGenerators

import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

import com.kms.katalon.core.util.KeywordUtil

public class GenerateGLN {

	private static final int GLN_LENGTH = 13;

	public static String create(){
		String base = RandomStringUtils.randomNumeric(12)
		String gln = base + EAN13CheckDigit.EAN13_CHECK_DIGIT.calculate(base)
		if(isValidGln(gln)) return gln
		KeywordUtil.markErrorAndStop("Generating a valid GLN failed: $gln")
	}

	private static boolean isValidGln(String gln) {
		if (StringUtils.isEmpty(gln)) return false
		if (!StringUtils.isNumeric(gln)) return false
		if (gln.length() != GLN_LENGTH) return false
		// calculate and check checksum (GTIN-13/GLN)
		return EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(gln)
	}
}
