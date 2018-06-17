package com.shumencoin.crypto;

import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;

import com.shumencoin.convertion.Converter;

public class Crypto {

	public static void generateSalt(byte[] salt) {
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
	}

	/**
	 * https://www.programcreek.com/java-api-examples/index.php?source_dir=cosigner-master/cosigner-common/src/main/java/io/emax/cosigner/common/crypto/Secp256k1.java
	 * 
	 * @return
	 */
	public static byte[] generatePrivateKey() {
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
		} catch (Exception e) {
			secureRandom = new SecureRandom();
		}

		BigInteger privateKeyCheck = BigInteger.ZERO;
		// Bit of magic, move this maybe. This is the max key range.
		BigInteger maxKey = new BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140", 16);

		// Generate the key, skipping as many as desired.
		byte[] privateKeyAttempt = new byte[32];
		secureRandom.nextBytes(privateKeyAttempt);
		privateKeyCheck = new BigInteger(1, privateKeyAttempt);
		while (privateKeyCheck.compareTo(BigInteger.ZERO) == 0 || privateKeyCheck.compareTo(maxKey) == 1) {
			secureRandom.nextBytes(privateKeyAttempt);
			privateKeyCheck = new BigInteger(1, privateKeyAttempt);
		}

		return privateKeyAttempt;
	}

//	public static byte[] generatePublicKey(byte[] privateKey) {
//		try {
//			ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
//			ECPoint pointQ = spec.getG().multiply(new BigInteger(1, privateKey));
//
//			return pointQ.getEncoded(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new byte[0];
//		}
//	}

	 /**
	 * https://gist.github.com/nakov/b01f9434df3350bc9b1cbf9b04ddb605
	 * @param privateKey
	 * @return
	 */
	 public static byte[] generatePublicKey(byte[] privateKey) {
	 BigInteger privKey = new BigInteger(privateKey);
	 BigInteger publicKey = Sign.publicKeyFromPrivate(privKey);
	
	 ECKeyPair keyPair = new ECKeyPair(privKey, publicKey);
	
	 return keyPair.getPublicKey().toByteArray();
	 }

	/**
	 * https://gist.github.com/nakov/b01f9434df3350bc9b1cbf9b04ddb605
	 * 
	 * @param publicKey
	 * @return
	 */
	public static byte[] compressPublicKey(byte[] publicKey) {
		BigInteger pubKey = new BigInteger(publicKey);

		String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
		String pubKeyHex = pubKey.toString(16);
		String pubKeyX = pubKeyHex.substring(0, 64);

		return Converter.HexStringToByteArray(pubKeyYPrefix + pubKeyX);
	}

	public static byte[] generateAddress(byte[] publicKey) {
		return null;
	}

	public static byte[][] signTransaction(String data, byte[] privateKey) {
		return null;
	}

	public static boolean verifyTransaction(String data, byte[] privateKey) {
		return true;
	}

	public static byte[] sha256(String rowText) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Change this to UTF-16 if needed
			md.update(rowText.getBytes(StandardCharsets.UTF_8));

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
