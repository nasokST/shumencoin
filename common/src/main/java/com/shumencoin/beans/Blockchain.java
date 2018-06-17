package com.shumencoin.beans;

import java.io.Serializable;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.BlockchainData;
import com.shumencoin.beans_data.helper.BlockHelper;


public class Blockchain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2182266894521524361L;

	public BlockchainData getChain() {
		return chain;
	}

	public void setChain(BlockchainData blockchain) {
		this.chain = blockchain;
	}

	public String getChainId() {
		return chainId;
	}

	public void initializeChain() {

		BlockData genesisBlock = BlockHelper.generateGenesisBlock();

		chain = new BlockchainData(genesisBlock, 1);

		chainId = "1";
	}

	private String chainId;
	private BlockchainData chain;
}