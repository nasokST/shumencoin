package com.shumencoin.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.shumencoin.convertion.Converter;

public class Crypto {
	
	public static byte[] sha256(String rowText){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
		    String text = "Text to hash, cryptographically.";
	
		    // Change this to UTF-16 if needed
		    md.update( text.getBytes(StandardCharsets.UTF_8) );
	
		    return md.digest();
		    
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}		    
	}
	
	public static String sha256ToString(String rowText) {
		byte[] hash = Crypto.sha256(rowText);

		if (null == hash) {
			return rowText;
		}

		return Converter.byteArrayToHexString(hash);
	}
}
