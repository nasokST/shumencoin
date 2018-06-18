package com.shumencoin.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.BlockchainData;
import com.shumencoin.beans_data.MiningJobData;
import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.beans_data.helper.BlockHelper;
import com.shumencoin.beans_data.helper.TransactionHelper;
import com.shumencoin.constants.Constants;
import com.shumencoin.errors.ShCError;

public class Blockchain implements Serializable {

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

	public void initializeChain() {

		BlockData genesisBlock = BlockHelper.generateGenesisBlock();

		chain = new BlockchainData(genesisBlock, Constants.dificulty);

		chainId = "1";
	}

	public ShCError getNewMiningJob(String minerAddress, MiningJobData miningJob) {

		BlockData lastBlock = chain.getBlocks().get(chain.getBlocks().size() - 1);
		long nextBlockIndex = lastBlock.getIndex() + 1;

		List<TransactionData> newBlocktransactions = getNextBlockCandidate(nextBlockIndex, minerAddress);

		BlockData nextBlockCandidate = new BlockData(nextBlockIndex, chain.getCurrentDificulty(), minerAddress,
				newBlocktransactions, lastBlock.getBlockHash());

		BlockHelper.calculateBlockDataHash(nextBlockCandidate);

		chain.getMiningJobs().put(nextBlockCandidate.getBlockDataHash(), nextBlockCandidate);

		miningJob.init(nextBlockCandidate);
		return ShCError.NO_ERROR;
	}

	public ShCError submitMinedBlock(MiningJobData minedBlock, BlockData newBlock) {

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
			if (!BlockHelper.validateDificulty(nextBlockCandidate)) {
				return ShCError.INCORRECT_BLOCK_DIFFICULTY;
			}

			return addBlockToChain(nextBlockCandidate, newBlock);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ShCError.UNKNOWN;
		}
	}

	private ShCError addBlockToChain(BlockData blockCandidate, BlockData newBlock) {
		BlockData lastBlock = chain.getBlocks().get(chain.getBlocks().size() - 1);

		if (blockCandidate.getIndex() != (lastBlock.getIndex() + 1)) {
			return ShCError.BLOCK_ALREADY_MINED;
		}

		if (!blockCandidate.getPrevBlockHash().equals(lastBlock.getBlockHash())) {
			return ShCError.INCORRECT_BLOCK_HASH;
		}

		chain.getBlocks().add(blockCandidate);
		chain.getMiningJobs().clear();

		pendingTransactionsRemove(blockCandidate.getTransactions());

		newBlock.clone(blockCandidate);
		return ShCError.NO_ERROR;
	}

	private void pendingTransactionsRemove(List<TransactionData> toRemove) {
		// TODO mined Transactions Remove ONLY
		chain.getPendingTransactions().removeAll(toRemove); 
	}

	private List<TransactionData> getNextBlockCandidate(long nextBlockIndex, String minerAddress) {
		TransactionData rewardTransaction = TransactionHelper.generateRewardTransaction(nextBlockIndex, minerAddress);

		// TODO get fee for miner reward
		List<TransactionData> nextBlocktransactions = new LinkedList<TransactionData>();

		BigInteger feeSum = new BigInteger("0");
		for (TransactionData td : chain.getPendingTransactions()) {

			TransactionData newTd = new TransactionData(td);

			if (newTd.getFee() >= Constants.minFee && newTd.getFee() <= Constants.maxFee) {
				feeSum.add(newTd.getValue());				
			} else {
				newTd.setTransferSuccessful(false);
			}

			TransactionHelper.calculateTransactionDataHash(newTd);			
			nextBlocktransactions.add(newTd);
		}

		rewardTransaction.getValue().add(feeSum);
		TransactionHelper.calculateTransactionDataHash(rewardTransaction);

		nextBlocktransactions.add(rewardTransaction);

		return nextBlocktransactions;
	}
	
	public List<TransactionData> listConfirmedTransaction() {
	    List<TransactionData> confirmedTransactions = new ArrayList<TransactionData>();
	    for(BlockData block : chain.getBlocks()) {
		confirmedTransactions.addAll(block.getTransactions());
	    }
	    return confirmedTransactions;
	}

	public TransactionData getTransactionByHash(String hash) throws Exception {
	    for(BlockData block : chain.getBlocks()) {
		for(TransactionData transaction : block.getTransactions()) {
		    if(hash.equals(transaction.getTransactionDataHash())) {
			return transaction;
		    }
		}
	    }
	    throw new Exception(hash);
	}
}