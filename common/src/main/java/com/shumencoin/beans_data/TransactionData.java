package com.shumencoin.beans_data;

import java.io.Serializable;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shumencoin.databind.SignatureDeserializer;
import com.shumencoin.databind.SignatureSerializer;

public class TransactionData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1488991650030440927L;

	private String from; // Sender address: 40 hex digits
    private String to; // Recipient address: 40 hex digits
    private BigInteger value; // Transfer value: integer
    private long fee; // Mining fee: integer
    private String dateCreated; // ISO-8601 string
    private String data; // Optional data (e.g. payload or comments): string
    private String senderPubKey; // 65 hex digits
    private String transactionDataHash;  // 64 hex digits  // Calculate the transaction data hash if it is missing
	@JsonDeserialize(using = SignatureDeserializer.class)
	@JsonSerialize(using = SignatureSerializer.class)	    
    private byte[][] senderSignature; // hex_number[2][64]
    private long minedInBlockIndex; //integer
    private boolean transferSuccessful; // boolean

    public TransactionData() {
    	senderSignature = new byte[32][32];
    }

//	public TransactionData(String from, String to, long value, long fee, String dateCreated, String data,
//			String senderPubKey, String transactionDataHash, String senderSignature, long minedInBlockIndex,
//			boolean transferSuccessful) {
//		super();
//		this.from = from;
//		this.to = to;
//		this.value = value;
//		this.fee = fee;
//		this.dateCreated = dateCreated;
//		this.data = data;
//		this.senderPubKey = senderPubKey;
//		this.transactionDataHash = transactionDataHash;
//		this.senderSignature = senderSignature;
//		this.minedInBlockIndex = minedInBlockIndex;
//		this.transferSuccessful = transferSuccessful;
//	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public BigInteger getValue() {
		return value;
	}
	public void setValue(BigInteger value) {
		this.value = value;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSenderPubKey() {
		return senderPubKey;
	}
	public void setSenderPubKey(String senderPubKey) {
		this.senderPubKey = senderPubKey;
	}
	public String getTransactionDataHash() {
		return transactionDataHash;
	}
	public void setTransactionDataHash(String transactionDataHash) {
		this.transactionDataHash = transactionDataHash;
	}
	public byte[][] getSenderSignature() {
		return senderSignature;
	}

	public void setSenderSignature(byte[][] senderSignature) {
		this.senderSignature = senderSignature;
	}	
	public long getMinedInBlockIndex() {
		return minedInBlockIndex;
	}
	public void setMinedInBlockIndex(long minedInBlockIndex) {
		this.minedInBlockIndex = minedInBlockIndex;
	}
	public boolean isTransferSuccessful() {
		return transferSuccessful;
	}
	public void setTransferSuccessful(boolean transferSuccessful) {
		this.transferSuccessful = transferSuccessful;
	}
}
