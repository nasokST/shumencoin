import static org.junit.Assert.*;

import java.security.SignatureException;

import org.junit.Test;

import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;

public class CryptoTest {

	@Test
	public void test() throws SignatureException {
		// byte[] privKey = Crypto.generatePrivateKey();
		byte[] privKey = Converter
				.HexStringToByteArray("78a75639dda5267b5a52c3690ed1a13be003c30e70bc86b332a00b123dcec288");
		byte[] pubKey = Crypto.generatePublicKey(privKey);
		byte[] pubKeyCompressed = Crypto.compressPublicKey(pubKey);
		byte[] address = Crypto.generateAddress(pubKeyCompressed);
		byte[] address1 = Crypto.generateAddress(pubKeyCompressed);

		System.out.println("PK: " + Converter.byteArrayToHexString(privKey));
		System.out.println("PubK: " + Converter.byteArrayToHexString(pubKey));
		System.out.println("PubKCompress: " + Converter.byteArrayToHexString(pubKeyCompressed));
		System.out.println("Address: " + Converter.byteArrayToHexString(address));
		System.out.println("Address1: " + Converter.byteArrayToHexString(address1));

		String msgToSign = "ShumenCoin";

		byte[][] transagtionSignatureData = Crypto.getTransactionSignatureData(msgToSign.getBytes(), privKey);
		System.out.println("{v: " + transagtionSignatureData[0][0] + " r: "
				+ Converter.byteArrayToHexString(transagtionSignatureData[1]) + " s: "
				+ Converter.byteArrayToHexString(transagtionSignatureData[2]));

		//msgToSign = "ShumenCoi";
		//transagtionSignatureData[0][0] = 28;
		
		try {
			boolean signOk = Crypto.verifyTransaction(address, msgToSign.getBytes(), transagtionSignatureData[0][0],
					transagtionSignatureData[1], transagtionSignatureData[2]);
			
			
			System.out.println("SIGNATURE: " + signOk);
			
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
