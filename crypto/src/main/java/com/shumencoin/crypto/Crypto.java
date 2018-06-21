package com.shumencoin.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Arrays;

//import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
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

	/**
	 * https://gist.github.com/nakov/b01f9434df3350bc9b1cbf9b04ddb605
	 *
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

	public static byte[] generateAddress(byte[] publicKeyBytes) {
		try {
			RIPEMD160Digest d = new RIPEMD160Digest();

			d.reset();
			d.update(publicKeyBytes, 1, publicKeyBytes.length - 1);
			byte[] publicShaKeyBytes = new byte[20];
			d.doFinal(publicShaKeyBytes, 0);

			return publicShaKeyBytes;

		} catch (Exception e) {
			return null;
		}
	}

	public static byte[][] getTransactionSignatureData(byte[] msg, byte[] privateKey) throws SignatureException {

		BigInteger privKey = new BigInteger(privateKey);
		BigInteger publicKey = Sign.publicKeyFromPrivate(privKey);

		ECKeyPair keyPair = new ECKeyPair(privKey, publicKey);

		//byte[] msgHash = Hash.sha3(msg);
		Sign.SignatureData signature = Sign.signMessage(msg, keyPair, true);

		byte[][] signatureBts = new byte[3][];
		
		signatureBts[0] = new byte[1];
		signatureBts[1] = new byte[32];
		signatureBts[2] = new byte[32];

		signatureBts[0][0] = signature.getV();
		signatureBts[1] = Arrays.copyOfRange(signature.getR(), 0, signature.getR().length);
		signatureBts[2] = Arrays.copyOfRange(signature.getS(), 0, signature.getS().length);
		
		
		System.out.println("\n --- getTransactionSignatureData ---");
		System.out.println("PK: " + Converter.byteArrayToHexString(keyPair.getPrivateKey().toByteArray()));
		System.out.println("PubKey: " + Converter.byteArrayToHexString(keyPair.getPublicKey().toByteArray()));
		System.out.println("v: " + signature.getV());
		System.out.println("r: " + Converter.byteArrayToHexString(signature.getR()));
		System.out.println("s: " + Converter.byteArrayToHexString(signature.getS()));
		System.out.println(" --- END getTransactionSignatureData --- \n");

		return signatureBts;
	}

	public static boolean verifyTransaction(byte[] address, byte[] msg, byte v, byte[] r, byte[] s) throws SignatureException {

		Sign.SignatureData signature = new Sign.SignatureData(v, r, s);

		BigInteger pubKeyRecovered = Sign.signedMessageToKey(msg, signature);
		byte[] pubKeyRecoveredCompressed = compressPublicKey(pubKeyRecovered.toByteArray());
		byte[] addressRecovered = generateAddress(pubKeyRecoveredCompressed);

		System.out.println("\n --- verifyTransaction---");
		System.out.println("rPK: " + Converter.byteArrayToHexString(pubKeyRecovered.toByteArray()));
		System.out.println("rPKCompress: " + Converter.byteArrayToHexString(pubKeyRecoveredCompressed));
		System.out.println("rAddress: " + Converter.byteArrayToHexString(addressRecovered));
		System.out.println(" --- END verifyTransaction--- \n");
		
		if (Arrays.equals(address, addressRecovered)) {
			return true;
		}

		return false;
	}

	// public static boolean verifyTransaction(String address, byte[] msg, byte v,
	// byte[] r, byte[] s) {
	//
	// Sign.SignatureData signature = new Sign.SignatureData(v, r, s);
	//
	// BigInteger pubKeyRecovered = Sign.signedMessageToKey(msg, signature);
	//
	// System.out.println("Recovered public key: " + pubKeyRecovered.toString(16));
	//
	// boolean validSig = pubKey.equals(pubKeyRecovered);
	//
	// System.out.println("Signature valid? " + validSig);
	// }

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
