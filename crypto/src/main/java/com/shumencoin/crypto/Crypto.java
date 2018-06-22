package com.shumencoin.crypto;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Arrays;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
//import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static byte[] generateAddressByPrivateKey(byte[] privateKeyBytes) {
	byte[] pubKey = Crypto.generatePublicKey(privateKeyBytes);
	byte[] pubKeyCompressed = Crypto.compressPublicKey(pubKey);
	
	return Crypto.generateAddress(pubKeyCompressed);
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

    public static String encryptionPrivateKey(String rawPassword, byte[] privateKey, byte[] address)
	    throws DataLengthException, InvalidCipherTextException, JsonProcessingException {

	byte[] password = rawPassword.getBytes();
	byte[] message = privateKey;

	CryptoData cryptoData = new CryptoData();
	
	cryptoData.address = Converter.byteArrayToHexString(address);

	// SCrypt
	SecureRandom secureRandom = new SecureRandom();
	secureRandom.nextBytes(cryptoData.script.salt);

	byte[] cryptHash = generateSCryptHash(password, cryptoData);

	byte[] encryption_key = Arrays.copyOfRange(cryptHash, 0, 32);
	byte[] hmac_key = Arrays.copyOfRange(cryptHash, 32, 64);

	// TwoFish
	secureRandom.nextBytes(cryptoData.iv);

	Twofish_CBC_PKCS7 twofish_CBC_PKCS7 = new Twofish_CBC_PKCS7();
	twofish_CBC_PKCS7.setKey(encryption_key);
	cryptoData.twofish = twofish_CBC_PKCS7.encrypt(message, cryptoData.iv);

	// HMAC
	hmac_hash(new SHA256Digest(), hmac_key, message, cryptoData.mac);

	// JSON
	ObjectMapper ow = new ObjectMapper();
	String json = ow.writeValueAsString(cryptoData);

	return json;
    }

    public static byte[] decryptPrivateKey(CryptoData cryptoData, String rawPassword) throws JsonParseException,
	    JsonMappingException, IOException, DataLengthException, InvalidCipherTextException {

	byte[] password = rawPassword.getBytes(Charset.forName("UTF-8"));

//	// Parse JSON
//	ObjectMapper objectMapper = new ObjectMapper();
//	CryptoData cryptoData = objectMapper.readValue(rawJson, CryptoData.class);

	// SCrypt
	byte[] cryptHash = generateSCryptHash(password, cryptoData);

	byte[] encryption_key = Arrays.copyOfRange(cryptHash, 0, 32);
	byte[] hmac_key = Arrays.copyOfRange(cryptHash, 32, 64);

	// TwoFish
	Twofish_CBC_PKCS7 twofish_CBC_PKCS7 = new Twofish_CBC_PKCS7();

	twofish_CBC_PKCS7.setKey(encryption_key);
	byte[] decryptedMessage = twofish_CBC_PKCS7.decrypt(cryptoData.twofish, cryptoData.iv);

	System.out.println("Decrypted message: " + Converter.byteArrayToHexString(decryptedMessage));

	// HMAC
	byte[] newMac = new byte[32];
	hmac_hash(new SHA256Digest(), hmac_key, decryptedMessage, newMac);

	if (Arrays.equals(newMac, cryptoData.mac)) {
	    System.out.println("mac OK");
	    return decryptedMessage;
	} else {
	    System.out.println("mac NOK");
	    return null;
	}
    }

    static byte[] generateSCryptHash(byte[] password, CryptoData cryptoData) {
	return SCrypt.generate(password, cryptoData.script.salt, cryptoData.script.n, cryptoData.script.r,
		cryptoData.script.p, cryptoData.script.dklen);
    }

    static void hmac_hash(Digest digest, byte[] secret, byte[] seed, byte[] out) {
	HMac mac = new HMac(digest);
	KeyParameter param = new KeyParameter(secret);
	byte[] a = seed;
	int size = digest.getDigestSize();
	int iterations = (out.length + size - 1) / size;
	byte[] buf = new byte[mac.getMacSize()];
	byte[] buf2 = new byte[mac.getMacSize()];
	for (int i = 0; i < iterations; i++) {
	    mac.init(param);
	    mac.update(a, 0, a.length);
	    mac.doFinal(buf, 0);
	    a = buf;
	    mac.init(param);
	    mac.update(a, 0, a.length);
	    mac.update(seed, 0, seed.length);
	    mac.doFinal(buf2, 0);
	    System.arraycopy(buf2, 0, out, (size * i), Math.min(size, out.length - (size * i)));
	}
    }

    public static byte[][] getTransactionSignatureData(byte[] msg, byte[] privateKey) throws SignatureException {

	BigInteger privKey = new BigInteger(privateKey);
	BigInteger publicKey = Sign.publicKeyFromPrivate(privKey);

	ECKeyPair keyPair = new ECKeyPair(privKey, publicKey);

	// byte[] msgHash = Hash.sha3(msg);
	Sign.SignatureData signature = Sign.signMessage(msg, keyPair, true);

	byte[][] signatureBts = new byte[2][];

	signatureBts[0] = new byte[33];
	signatureBts[1] = new byte[32];

	System.arraycopy(signature.getR(), 0, signatureBts[0], 0, signature.getR().length);
	signatureBts[0][32] = signature.getV();
	signatureBts[1] = Arrays.copyOfRange(signature.getS(), 0, signature.getS().length);

	System.out.println("\n --- getTransactionSignatureData ---");
	System.out.println("PK: " + Converter.byteArrayToHexString(keyPair.getPrivateKey().toByteArray()));
	System.out.println("PubKey: " + Converter.byteArrayToHexString(keyPair.getPublicKey().toByteArray()));
	System.out.println("v: " + signature.getV());
	System.out.println("r: " + Converter.byteArrayToHexString(signature.getR()));
	System.out.println("s: " + Converter.byteArrayToHexString(signature.getS()));
	System.out.println(" --- END getTransactionSignatureData --- \n");

	return signatureBts;
    }

    public static boolean verifyTransaction(byte[] address, byte[] msg, byte v, byte[] r, byte[] s)
	    throws SignatureException {

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
