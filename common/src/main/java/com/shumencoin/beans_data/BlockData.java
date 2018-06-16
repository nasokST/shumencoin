package com.shumencoin.beans_data;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
		transactions = new ArrayList<TransactionData>();
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
	public String getBlockDataHash() {
		return blockDataHash;
	}
	public void setBlockDataHash(String blockDataHash) {
		this.blockDataHash = blockDataHash;
	}
	public List<TransactionData> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionData> transactions) {
		this.transactions = transactions;
	}	
	public String getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	public String getPrevBlockHash() {
		return prevBlockHash;
	}
	public void setPrevBlockHash(String prevBlockHash) {
		this.prevBlockHash = prevBlockHash;
	}

	private BigInteger index;
	private long dificulty;
	private String minedBy;
	private BigInteger nonce;
	private LocalDateTime creationDate;
	private String blockDataHash;
	private List<TransactionData> transactions;	
	private String blockHash;
	private String prevBlockHash;
}
