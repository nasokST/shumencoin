import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

import org.junit.Test;

import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.beans_data.helper.TransactionHelper;
import com.shumencoin.constants.Constants;
import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;
import com.shumencoin.errors.ShCError;

public class CryptoTest {

	@Test
	public void CryptoFuctionsTest() throws SignatureException {
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
		System.out.println("{v: " + transagtionSignatureData[0][32] + " r: "
				+ Converter.byteArrayToHexString(transagtionSignatureData[0]) + " s: "
				+ Converter.byteArrayToHexString(transagtionSignatureData[1]));

		// msgToSign = "ShumenCoi";
		// transagtionSignatureData[0][0] = 28;

		byte[] r = Arrays.copyOfRange(transagtionSignatureData[0], 0, 32);
		try {
			boolean signOk = Crypto.verifyTransaction(address, msgToSign.getBytes(), transagtionSignatureData[0][32], r,
					transagtionSignatureData[1]);

			System.out.println("SIGNATURE: " + signOk);

		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void transactionSignValidationTest() throws SignatureException {
		
		byte[] privateKey = Converter
				.HexStringToByteArray("78a75639dda5267b5a52c3690ed1a13be003c30e70bc86b332a00b123dcec288");
		byte[] pubKey = Crypto.generatePublicKey(privateKey);
		byte[] pubKeyCompressed = Crypto.compressPublicKey(pubKey);
		byte[] address = Crypto.generateAddress(pubKeyCompressed);		

		TransactionData transaction = new TransactionData();
		transaction.setFrom(Converter.byteArrayToHexString(address));
		transaction.setTo(Constants.faucetAddress);
		transaction.setValue(new BigInteger("10000"));
		transaction.setFee(15);
		transaction.setDateCreated(Constants.generateGenesisCreationDate().toString());
		transaction.setData("test transaction");
		transaction.setSenderPubKey(Converter.byteArrayToHexString(pubKeyCompressed));
		transaction.setMinedInBlockIndex(100);
		transaction.setTransferSuccessful(true);
		TransactionHelper.calculateAndSetTransactionDataHash(transaction);

		
		TransactionHelper.sign(transaction, privateKey);
		
		//transaction.setValue(new BigInteger("100000"));
		
		ShCError validTransaction = TransactionHelper.validateSignatute(transaction);
		
	 	assertTrue("Not valid transaction", (validTransaction == ShCError.NO_ERROR));
	}	
}
