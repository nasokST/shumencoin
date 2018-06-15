package com.shumencoin.beans;

public class Transaction {
	private String from; // Sender address: 40 hex digits
    private String to; // Recipient address: 40 hex digits
    private Integer value; // Transfer value: integer
    private Integer fee; // Mining fee: integer
    private String dateCreated; // ISO-8601 string
    private String data; // Optional data (e.g. payload or comments): string
    private String senderPubKey; // 65 hex digits
    private String transactionDataHash;  // 64 hex digits  // Calculate the transaction data hash if it is missing
    private String senderSignature; // hex_number[2][64]
    private Integer minedInBlockIndex; //integer
    private boolean transferSuccessful; // boolean
    
	public Transaction(String from, String to, Integer value, Integer fee, String dateCreated, String data,
			String senderPubKey, String transactionDataHash, String senderSignature, Integer minedInBlockIndex,
			boolean transferSuccessful) {
		super();
		this.from = from;
		this.to = to;
		this.value = value;
		this.fee = fee;
		this.dateCreated = dateCreated;
		this.data = data;
		this.senderPubKey = senderPubKey;
		this.transactionDataHash = transactionDataHash;
		this.senderSignature = senderSignature;
		this.minedInBlockIndex = minedInBlockIndex;
		this.transferSuccessful = transferSuccessful;
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
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
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
	public String getSenderSignature() {
		return senderSignature;
	}
	public void setSenderSignature(String senderSignature) {
		this.senderSignature = senderSignature;
	}
	public Integer getMinedInBlockIndex() {
		return minedInBlockIndex;
	}
	public void setMinedInBlockIndex(Integer minedInBlockIndex) {
		this.minedInBlockIndex = minedInBlockIndex;
	}
	public boolean isTransferSuccessful() {
		return transferSuccessful;
	}
	public void setTransferSuccessful(boolean transferSuccessful) {
		this.transferSuccessful = transferSuccessful;
	}
	
}
