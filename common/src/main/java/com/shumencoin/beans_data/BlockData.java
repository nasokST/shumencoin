package com.shumencoin.beans_data;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shumencoin.databind.ByteArrayDeserializer;
import com.shumencoin.databind.ByteArraySerializer;

/**
 * 
 * @author dragomir.todorov
 *
 */
public class BlockData implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1866556881435507147L;
	
	public BlockData() {
		blockDataHash = new byte[32];		
		transactions = new ArrayList<TransactionData>();
		blockHash = new byte[32];
		prevBlockHash = new byte[32];		
	}


	public BigInteger getIndex() {
		return index;
	}
	public void setIndex(BigInteger index) {
		this.index = index;
	}
	public long getDificulty() {
		return dificulty;
	}
	public void setDificulty(long dificulty) {
		this.dificulty = dificulty;
	}
	public String getMinedBy() {
		return minedBy;
	}
	public void setMinedBy(String minedBy) {
		this.minedBy = minedBy;
	}
	public BigInteger getNonce() {
		return nonce;
	}
	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public byte[] getBlockDataHash() {
		return blockDataHash;
	}
	public void setBlockDataHash(byte[] blockDataHash) {
		this.blockDataHash = blockDataHash;
	}
	public List<TransactionData> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionData> transactions) {
		this.transactions = transactions;
	}
	public byte[] getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(byte[] blockHash) {
		this.blockHash = blockHash;
	}
	public byte[] getPrevBlockHash() {
		return prevBlockHash;
	}
	public void setPrevBlockHash(byte[] prevBlockHash) {
		this.prevBlockHash = prevBlockHash;
	}

	private BigInteger index;
	private long dificulty;
	private String minedBy;
	private BigInteger nonce;
	private LocalDateTime creationDate;
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)	
	private byte[] blockDataHash;
	private List<TransactionData> transactions;	
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)	
	private byte[] blockHash;
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)	
	private byte[] prevBlockHash;
}
