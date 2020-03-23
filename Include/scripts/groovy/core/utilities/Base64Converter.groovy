package core.utilities

public class Base64Converter {
	public static String encode(String toBeEncoded){
		String encoded = toBeEncoded.bytes.encodeBase64().toString()
		return encoded
	}

	public static String decode(String toBeDecoded){
		byte[] decoded = toBeDecoded.decodeBase64()
		return new String(decoded)
	}
}

