package com.shumencoin.beans;

import java.io.Serializable;

import com.shumencoin.beans_data.BlockchainData;

public class Blockchain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2182266894521524361L;

	private String chainId;	
	private BlockchainData chain;

	public BlockchainData getChain() {
		return chain;
	}

	public void setChain(BlockchainData blockchain) {
		this.chain = blockchain;
	}

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

}