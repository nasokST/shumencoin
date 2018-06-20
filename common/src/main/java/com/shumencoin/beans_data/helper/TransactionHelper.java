package com.shumencoin.beans_data.helper;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.constants.Constants;
import com.shumencoin.crypto.Crypto;
import com.shumencoin.errors.ShCError;

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

		calculateTransactionDataHash(genesisTransaction);

		return genesisTransaction;
	}

	public static TransactionData generateRewardTransaction(long nextBlockIndex, String minerAddress) {
		TransactionData genesisTransaction = new TransactionData();

		genesisTransaction.setFrom(Constants.genesisAddress);
		genesisTransaction.setTo(minerAddress);
		genesisTransaction.setValue(Constants.blockReward);
		genesisTransaction.setFee(0);
		genesisTransaction.setDateCreated(Constants.dateTimeToIsoStr(LocalDateTime.now()));
		genesisTransaction.setData("reward transaction");
		genesisTransaction.setSenderPubKey(Constants.genesisPublicKey);
		genesisTransaction.setSenderSignature(Constants.genesisSignature);
		genesisTransaction.setMinedInBlockIndex(nextBlockIndex);
		genesisTransaction.setTransferSuccessful(true);

		calculateTransactionDataHash(genesisTransaction);

		return genesisTransaction;
	}
	
	public static ShCError sign(TransactionData transaction) {
		// TODO sign transaction
		return ShCError.NOT_IMPLEMENTED; 
	}

	public static ShCError validateSignatute(TransactionData transaction) {

		if (transaction.getSenderPubKey().equals(Constants.genesisAddress)) {
			return ShCError.NO_ERROR;
		}

		// TODO sign transaction
		return ShCError.NOT_IMPLEMENTED; 
	}	

	public static boolean isValidTransactionData(TransactionData transaction) {
		
		if (transaction.getFee() < Constants.minFee || transaction.getFee() > Constants.maxFee) {
			return false;
		}
		if (transaction.getValue().min(new BigInteger("0")) == transaction.getValue()) {
			return false;
		}

		// TODO more data validation
		
		return true;
	}
	
//	public String from;
//	public String to;
//	public long value;
//	public long fee;
//	public String data;
//	public String senderPubKey;
//	public String dateCreated;
}
