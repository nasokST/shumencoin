package com.shumencoin.beans_data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

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
    private String transactionDataHash; // 64 hex digits // Calculate the transaction data hash if it is missing
    @JsonDeserialize(using = SignatureDeserializer.class)
    @JsonSerialize(using = SignatureSerializer.class)
    private byte[][] senderSignature;
    private long minedInBlockIndex; // integer
    private boolean transferSuccessful; // boolean

    public TransactionData() {
	senderSignature = new byte[2][];
	senderSignature[0] = new byte[33];
	senderSignature[1] = new byte[32];
    }

    public TransactionData(TransactionData other) {

	this.setFrom(other.getFrom());
	this.setTo(other.getTo());
	this.setValue(other.getValue());
	this.setFee(other.getFee());
	this.setDateCreated(other.getDateCreated());
	this.setData(other.getData());
	this.setSenderPubKey(other.getSenderPubKey());
	this.setTransactionDataHash(other.getTransactionDataHash());

	this.senderSignature = new byte[2][];
	this.senderSignature[0] = new byte[33];
	this.senderSignature[1] = new byte[32];
	this.senderSignature[0] = Arrays.copyOf(other.getSenderSignature()[0], other.getSenderSignature()[0].length);
	this.senderSignature[1] = Arrays.copyOf(other.getSenderSignature()[1], other.getSenderSignature()[1].length);

	this.setMinedInBlockIndex(other.getMinedInBlockIndex());
	this.setTransferSuccessful(other.isTransferSuccessful());
    }

    public boolean equals(Object object) {

	TransactionData other = (TransactionData) object;

	if (this.getTransactionDataHash().equals(other.getTransactionDataHash())) {
	    return true;
	}

	return false;
    }

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
