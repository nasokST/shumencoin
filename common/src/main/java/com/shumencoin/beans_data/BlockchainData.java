package com.shumencoin.beans_data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Blockchain
 * @author dragomir.todorov
 *
 */
public class BlockchainData implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4828564175718932172L;

	private List<BlockData> blocks; // Block[]
	private List<TransactionData> pendingTransactions; // Transaction[]
	private long currentDificulty; // integer
	private Map<String, BlockData> miningJobs; // map(blockDataHash => Block)

	public BlockchainData(BlockData genesisBlock, long startDifficulty) {
		this.blocks = new LinkedList<BlockData>(Arrays.asList(genesisBlock));
		this.pendingTransactions = new LinkedList<TransactionData>();
		this.currentDificulty = startDifficulty;
		this.miningJobs = new HashMap<String, BlockData>();
	}
	//	constructor(genesisBlock, startDifficulty) {
	//        this.blocks = [genesisBlock]; // Block[]
	//        this.pendingTransactions = []; // Transaction[]
	//        this.currentDifficulty = startDifficulty; // integer
	//        this.miningJobs = {}; // map(blockDataHash => Block)
	//}

	public List<BlockData> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<BlockData> blocks) {
		this.blocks = blocks;
	}

	public List<TransactionData> getPendingTransactions() {
		return pendingTransactions;
	}

	public void setPendingTransactions(List<TransactionData> pendingTransactions) {
		this.pendingTransactions = pendingTransactions;
	}

	public long getCurrentDificulty() {
		return currentDificulty;
	}

	public void setCurrentDificulty(long currentDificulty) {
		this.currentDificulty = currentDificulty;
	}

	public Map<String, BlockData> getMiningJobs() {
		return miningJobs;
	}

	public void setMiningJobs(Map<String, BlockData> miningJobs) {
		this.miningJobs = miningJobs;
	}
	
}
