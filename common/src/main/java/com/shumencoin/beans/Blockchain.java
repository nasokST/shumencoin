package com.shumencoin.beans;

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
public class Blockchain implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4828564175718932172L;

	private List<Block> blocks; // Block[]
	private List<Transaction> pendingTransactions; // Transaction[]
	private long currentDificulty; // integer
	private Map<String, Block> miningJobs; // map(blockDataHash => Block)
	
	public Blockchain(Block genesisBlock, long startDifficulty) {
		this.blocks = new LinkedList<Block>(Arrays.asList(genesisBlock));
		this.pendingTransactions = new LinkedList<Transaction>();
		this.currentDificulty = startDifficulty;
		this.miningJobs = new HashMap<String, Block>();
	}
	//	constructor(genesisBlock, startDifficulty) {
	//        this.blocks = [genesisBlock]; // Block[]
	//        this.pendingTransactions = []; // Transaction[]
	//        this.currentDifficulty = startDifficulty; // integer
	//        this.miningJobs = {}; // map(blockDataHash => Block)
	//}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Transaction> getPendingTransactions() {
		return pendingTransactions;
	}

	public void setPendingTransactions(List<Transaction> pendingTransactions) {
		this.pendingTransactions = pendingTransactions;
	}

	public long getCurrentDificulty() {
		return currentDificulty;
	}

	public void setCurrentDificulty(long currentDificulty) {
		this.currentDificulty = currentDificulty;
	}

	public Map<String, Block> getMiningJobs() {
		return miningJobs;
	}

	public void setMiningJobs(Map<String, Block> miningJobs) {
		this.miningJobs = miningJobs;
	}
	
}
