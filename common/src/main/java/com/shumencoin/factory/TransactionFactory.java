package com.shumencoin.factory;

import com.shumencoin.beans.Transaction;
import com.shumencoin.beans.helper.TransactionDataHash;

public class TransactionFactory {

	public Transaction createTransaction(String from, String to, long value, long fee, String dateCreated,
			String data, String senderPubKey, String transactionDataHash, String senderSignature,
			long minedInBlockIndex, boolean transferSuccessful) {
		Transaction transaction = new Transaction(from, to, value, fee, dateCreated, data, senderPubKey,
				transactionDataHash, senderSignature, minedInBlockIndex, transferSuccessful);
		if (transaction.getTransactionDataHash() == null || "".equals(transaction.getTransactionDataHash())) {
			TransactionDataHash hashHelper = new TransactionDataHash();
			hashHelper.from = from;
			hashHelper.to = to;
			hashHelper.value = value;
			hashHelper.fee = fee;
			hashHelper.data = data;
			hashHelper.senderPubKey = senderPubKey;
			hashHelper.dateCreated = dateCreated;
			// String tranDataJson = JSON.stringify(hashHelper); // jackson utils
			// transaction.setTransactionDataHash(CryptoUtils.sha256(tranDataJSON)); //
			// createclass with encryption
		}
		return transaction;
	}

}
