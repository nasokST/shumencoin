package com.shumencoin.beans_data.helper;

import java.math.BigInteger;

import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.constants.Constants;
import com.shumencoin.crypto.Crypto;

public class TransactionHelper {
	
	public static String calculateTransactionDataHash(TransactionData transaction) {

		String forHashing = "{" + 
							transaction.getFrom() + ", " +
							transaction.getTo() + ", " +
							transaction.getValue() + ", " +
							transaction.getFee() + ", " +
							transaction.getDateCreated().toString() + ", " +
							transaction.getFrom() + ", " + 
							transaction.getSenderPubKey() + "}"; 

		transaction.setTransactionDataHash(Crypto.sha256ToString(forHashing));

		return transaction.getTransactionDataHash();
	}	

	public static TransactionData generateGenesisTransaction() {
		TransactionData genesisTransaction = new TransactionData();

		genesisTransaction.setFrom(Constants.genesisAddress);
		genesisTransaction.setTo(Constants.faucetAddress);
		genesisTransaction.setValue(new BigInteger("1000000000000"));
		genesisTransaction.setFee(0);
		genesisTransaction.setDateCreated(Constants.generateGenesisCreationDate().toString());
		genesisTransaction.setData("genesis transaction");
		genesisTransaction.setSenderPubKey(Constants.faucetPublicKey);
		genesisTransaction.setSenderSignature(Constants.genesisSignature);
		genesisTransaction.setMinedInBlockIndex(0);
		genesisTransaction.setTransferSuccessful(true);

		genesisTransaction.setTransactionDataHash(calculateTransactionDataHash(genesisTransaction));

		return genesisTransaction;
	}	
	
//	public String from;
//	public String to;
//	public long value;
//	public long fee;
//	public String data;
//	public String senderPubKey;
//	public String dateCreated;
}
