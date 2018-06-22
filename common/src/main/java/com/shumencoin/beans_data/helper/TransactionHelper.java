package com.shumencoin.beans_data.helper;

import java.math.BigInteger;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;

import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.constants.Constants;
import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;
import com.shumencoin.errors.ShCError;

public class TransactionHelper {

    public static void calculateAndSetTransactionDataHash(TransactionData transaction) {

	String forHashing = "{" + transaction.getFrom() + ", " + transaction.getTo() + ", " + transaction.getValue()
		+ ", " + transaction.getFee() + ", " + transaction.getDateCreated().toString() + ", "
		+ transaction.getFrom() + ", " + transaction.getSenderPubKey() + "}";

	transaction.setTransactionDataHash(Crypto.sha256ToString(forHashing));
    }

    public static String calculateOnlyTransactionDataHash(TransactionData transaction) {

	String forHashing = "{" + transaction.getFrom() + ", " + transaction.getTo() + ", " + transaction.getValue()
		+ ", " + transaction.getFee() + ", " + transaction.getDateCreated().toString() + ", "
		+ transaction.getFrom() + ", " + transaction.getSenderPubKey() + "}";

	return Crypto.sha256ToString(forHashing);
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

	calculateAndSetTransactionDataHash(genesisTransaction);

	return genesisTransaction;
    }

    public static TransactionData generateFaucetTransaction(String address) {
	byte[] privateKey = Converter
		.HexStringToByteArray("78a75639dda5267b5a52c3690ed1a13be003c30e70bc86b332a00b123dcec288");
	
	TransactionData genesisTransaction = new TransactionData();
	genesisTransaction.setFrom(Constants.faucetAddress);
	genesisTransaction.setTo(address);
	genesisTransaction.setValue(new BigInteger("1000"));
	genesisTransaction.setFee(15);
	genesisTransaction.setDateCreated(Constants.dateTimeToIsoStr(LocalDateTime.now()));
	genesisTransaction.setData("faucet transaction");
	genesisTransaction.setSenderPubKey(Constants.faucetPublicKey);
	genesisTransaction.setTransferSuccessful(true);
	TransactionHelper.calculateAndSetTransactionDataHash(genesisTransaction);
	genesisTransaction.setTransferSuccessful(true);
	calculateAndSetTransactionDataHash(genesisTransaction);
	TransactionHelper.sign(genesisTransaction, privateKey);
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

	calculateAndSetTransactionDataHash(genesisTransaction);

	return genesisTransaction;
    }

    public static ShCError sign(TransactionData transaction, byte[] privateKey) {
	try {
	    transaction.setSenderSignature(
		    Crypto.getTransactionSignatureData(transaction.getTransactionDataHash().getBytes(), privateKey));
	} catch (SignatureException e) {
	    return ShCError.TRANSACTION_CANNNOT_SIGN;
	}
	return ShCError.NO_ERROR;
    }

    public static ShCError validateSignatute(TransactionData transaction) {

	if (transaction.getSenderPubKey().equals(Constants.genesisAddress)) {
	    return ShCError.NO_ERROR;
	}

	String dataHash = calculateOnlyTransactionDataHash(transaction);

	if (!dataHash.equals(transaction.getTransactionDataHash())) {
	    return ShCError.TRANSACTION_DATA_HASH_IS_NOT_VALID;
	}

	byte[] r = Arrays.copyOfRange(transaction.getSenderSignature()[0], 0, 32);
	try {
	    boolean isValid = Crypto.verifyTransaction(transaction.getFrom().getBytes(), dataHash.getBytes(),
		    transaction.getSenderSignature()[0][32], r, transaction.getSenderSignature()[1]);

	    if (isValid) {
		return ShCError.TRANSACTION_SIGNATURE_IS_NOT_VALID;
	    }
	} catch (SignatureException e) {
	    return ShCError.TRANSACTION_CANNNOT_VALIDATE;
	}

	return ShCError.NO_ERROR;
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

    // public String from;
    // public String to;
    // public long value;
    // public long fee;
    // public String data;
    // public String senderPubKey;
    // public String dateCreated;
}
