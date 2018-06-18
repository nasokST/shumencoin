package com.shumencoin.beans_data;

import java.io.Serializable;
import java.math.BigInteger;

public class MiningJobData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2635644671322814634L;
	
	public void init(BlockData block) {
		this.setIndex(block.getIndex());
		this.setDificulty(block.getDificulty());
		this.setNonce(block.getNonce());
		this.setCreationDate(block.getCreationDate());
		this.setBlockDataHash(block.getBlockDataHash());
		this.setBlockHash(block.getBlockHash());
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
	private long index;
	private long dificulty;
	private BigInteger nonce;
	private String creationDate;
	private String blockDataHash;
	private String blockHash;
}
