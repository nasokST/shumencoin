package com.shumencoin.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
	
	public static byte[] sha256(String rowText) throws NoSuchAlgorithmException {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    String text = "Text to hash, cryptographically.";

	    // Change this to UTF-16 if needed
	    md.update( text.getBytes(StandardCharsets.UTF_8) );

	    return md.digest();		
	}
}
