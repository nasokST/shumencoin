package com.shumencoin.beans_data;

import java.io.Serializable;
import java.math.BigInteger;
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

	public BlockData(long index, long dificulty, String minedBy, List<TransactionData>transactions, String prevBlockHash) {
		this.setIndex(index);
		this.setDificulty(dificulty);
		this.setMinedBy(minedBy);
		this.setTransactions(transactions);
		this.setPrevBlockHash(prevBlockHash);
	}
	
	public void clone(BlockData other) {
		this.setIndex(other.getIndex());
		this.setDificulty(other.getDificulty());
		this.setMinedBy(other.getMinedBy());
		this.setNonce(other.getNonce());
		this.setCreationDate(other.getCreationDate());
		this.setBlockDataHash(other.getBlockDataHash());		
		this.setTransactions(other.getTransactions());
		this.setBlockHash(other.getBlockHash());
		this.setPrevBlockHash(other.prevBlockHash);
	}
	
	public boolean equals(BlockData other) {
		return this.getBlockHash().equals(other.getBlockHash());
		
//		return this.getIndex() == other.getIndex() 
//				&&
//				this.getDificulty() == other.getDificulty()
//				&&
//				this.getMinedBy().equals(other.getMinedBy())
//				&&
//				this.getNonce().equals(other.getNonce())
//				&&
//				this.getCreationDate().equals(other.getCreationDate())
//				&&
//				this.getBlockDataHash().equals(other.getBlockDataHash())
//				&&
//				this.getBlockHash().equals(other.getBlockHash())
//				&&
//				this.getPrevBlockHash().equals(other.prevBlockHash);		
	}

	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
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
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public List<TransactionData> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionData> transactions) {
		this.transactions = transactions;
	}
	public String getBlockDataHash() {
		return blockDataHash;
	}
	public void setBlockDataHash(String blockDataHash) {
		this.blockDataHash = blockDataHash;
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

	private long index;
	private long dificulty;
	private String minedBy;
	private BigInteger nonce;
	private String creationDate;
	private String blockDataHash;
	private List<TransactionData> transactions;	
	private String blockHash;
	private String prevBlockHash;
}
