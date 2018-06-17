package com.shumencoin.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.AsyncBoxView.ChildLocator;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.BlockchainData;
import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.beans_data.helper.BlockHelper;
import com.shumencoin.beans_data.helper.TransactionHelper;
import com.shumencoin.errors.ShCError;

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

	public ShCError getNewMiningJob(String minerAddress, BlockData miningJob) {

		BlockData lastBlock = chain.getBlocks().get(chain.getBlocks().size() - 1);
		long nextBlockIndex = lastBlock.getIndex() + 1;

		List<TransactionData> newBlocktransactions = pendingTransactionsClone();

		// TODO miner transaction

		BlockData nextBlockCandidate = new BlockData(nextBlockIndex, chain.getCurrentDificulty(), minerAddress,
				newBlocktransactions, lastBlock.getBlockHash());

		nextBlockCandidate.setBlockDataHash(BlockHelper.calculateBlockDataHash(nextBlockCandidate));

		chain.getMiningJobs().put(nextBlockCandidate.getBlockDataHash(), nextBlockCandidate);

		miningJob.clone(nextBlockCandidate);
		return ShCError.NO_ERROR;
	}

	public ShCError submiteMinedBlock(BlockData minedBlock, BlockData newBlock) {

		try {
			BlockData nextBlockCandidate = chain.getMiningJobs().get(minedBlock.getBlockDataHash());
			if (null == nextBlockCandidate) {
				return ShCError.MINING_JOB_NOT_FOUND;
			}

			nextBlockCandidate.setCreationDate(minedBlock.getCreationDate());
			nextBlockCandidate.setNonce(minedBlock.getNonce());
			BlockHelper.calculateBlockHash(nextBlockCandidate);

			if (!nextBlockCandidate.getBlockHash().equals(minedBlock.getBlockHash())) {
				return ShCError.INCORRECT_BLOCK_HASH;
			}
			// TODO block difficulty validation

			return addBlockToChain(nextBlockCandidate, newBlock);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ShCError.UNKNOWN;
		}
	}

	private ShCError addBlockToChain(BlockData blockCandidate, BlockData newBlock) {
		BlockData lastBlock = chain.getBlocks().get(chain.getBlocks().size());

		if (blockCandidate.getIndex() != lastBlock.getIndex()) {
			return ShCError.BLOCK_ALREADY_MINED;
		}

		if (!blockCandidate.getPrevBlockHash().equals(lastBlock.getBlockHash())) {
			return ShCError.INCORRECT_BLOCK_HASH;
		}

		chain.getBlocks().add(blockCandidate);
		chain.getMiningJobs().clear();

		pendingTransactionsRemove(blockCandidate.getTransactions());

		newBlock = blockCandidate;
		return ShCError.NO_ERROR;
	}

	private void pendingTransactionsRemove(List<TransactionData> toRemove) {
		// TODO mined Transactions Remove ONLY
		chain.getPendingTransactions().clear();
	}

	private List<TransactionData> pendingTransactionsClone() {

		List<TransactionData> transactions = new LinkedList<TransactionData>();

		for (TransactionData td : chain.getPendingTransactions()) {

			TransactionData newTd = new TransactionData(td);
			TransactionHelper.calculateTransactionDataHash(newTd);

			transactions.add(newTd);
		}

		return transactions;
	}

	private String chainId;
	private BlockchainData chain;
}