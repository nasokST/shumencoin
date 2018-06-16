package com.shumencoin.convertion;

import java.math.BigInteger;

import org.bouncycastle.util.encoders.Hex;

public class Converter {
	public static String byteArrayToHexString(byte[] hash) {
		String sha256hex = new String(Hex.encode(hash));
		return sha256hex;
	}

	public static byte[] HexStringToByteArray(String hexString) {
		return Hex.decode(hexString);
	}	
}
