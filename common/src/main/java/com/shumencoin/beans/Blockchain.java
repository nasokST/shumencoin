package com.shumencoin.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shumencoin.beans_data.BalanceBean;
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
		chain.getPendingTransactions().removeAll(toRemove);
	}

	public ShCError pendingTransactionsAdd(TransactionData newTransaction) {

		boolean isValidTransactionData = TransactionHelper.isValidTransactionData(newTransaction);
		ShCError isValidTransactionSignature = TransactionHelper.validateSignatute(newTransaction);

		if (isValidTransactionSignature != ShCError.NO_ERROR) {
			return isValidTransactionSignature;
		}

		if (!isValidTransactionData) {
			return ShCError.TRANSACTION_IS_NOT_VALID;
		}

		chain.getPendingTransactions().add(newTransaction);

		return ShCError.NO_ERROR;
	}	

	private List<TransactionData> getNextBlockCandidate(long nextBlockIndex, String minerAddress) {
		TransactionData rewardTransaction = TransactionHelper.generateRewardTransaction(nextBlockIndex, minerAddress);

		List<TransactionData> nextBlocktransactions = new LinkedList<TransactionData>();

		// TODO sort transaction by fee desc

		BigInteger feeSum = new BigInteger("0");
		long commitedTransaction = 0;
		for (TransactionData td : chain.getPendingTransactions()) {

			if (commitedTransaction > Constants.maxTransactionToCommit) {
				break;
			}

			TransactionData newTd = new TransactionData(td);

			// TODO check transaction`s sender balance
			// TODO signature validation

			if (TransactionHelper.isValidTransactionData(newTd)) {
				feeSum.add(newTd.getValue());
				commitedTransaction++;
			} else {
				newTd.setTransferSuccessful(false);
			}

			TransactionHelper.calculateAndSetTransactionDataHash(newTd);
			nextBlocktransactions.add(newTd);
		}

		rewardTransaction.getValue().add(feeSum);
		TransactionHelper.calculateAndSetTransactionDataHash(rewardTransaction);

		nextBlocktransactions.add(rewardTransaction);

		return nextBlocktransactions;
	}

	public List<TransactionData> listConfirmedTransaction() {
		List<TransactionData> confirmedTransactions = new ArrayList<TransactionData>();
		for (BlockData block : chain.getBlocks()) {
			confirmedTransactions.addAll(block.getTransactions());
		}
		return confirmedTransactions;
	}

	public TransactionData getTransactionByHash(String hash) throws Exception {
		for (BlockData block : chain.getBlocks()) {
			for (TransactionData transaction : block.getTransactions()) {
				if (hash.equals(transaction.getTransactionDataHash())) {
					return transaction;
				}
			}
		}
		throw new Exception(hash);
	}

	public List<BalanceBean> getBalances() {

		List<TransactionData> confirmedTransactions = new ArrayList<TransactionData>();
		for (BlockData block : chain.getBlocks()) {
			confirmedTransactions.addAll(block.getTransactions());
		}
		List<BalanceBean> confirmedBalances = calculateBalances(confirmedTransactions);

		List<TransactionData> pendingTransactionsHelper = new ArrayList<TransactionData>();
		pendingTransactionsHelper.addAll(getChain().getPendingTransactions());
		pendingTransactionsHelper.addAll(confirmedTransactions);

		List<BalanceBean> pendingBalances = calculateBalances(pendingTransactionsHelper);

		for (BalanceBean bb : pendingBalances) {
			for (BalanceBean bbc : confirmedBalances) {
				if (bb.getAddress().equals(bbc.getAddress())) {
					bb.setConfirmedBalance(bbc.getPendingBalance());
					break;
				}
			}
		}

		return pendingBalances;
	}

	private List<BalanceBean> calculateBalances(List<TransactionData> transactions) {
		List<BalanceBean> bba = new ArrayList<BalanceBean>();
		Map<String, BigInteger> balanceMap = new HashMap<String, BigInteger>();
		for (TransactionData tr : transactions) {
			if (balanceMap.get(tr.getTo()) == null) {
				balanceMap.put(tr.getTo(), BigInteger.valueOf(0l));
			}
			if (balanceMap.get(tr.getFrom()) == null) {
				balanceMap.put(tr.getFrom(), BigInteger.valueOf(0l));
			}
			balanceMap.put(tr.getTo(), balanceMap.get(tr.getTo()).add(tr.getValue()));
			balanceMap.put(tr.getFrom(),
					balanceMap.get(tr.getFrom()).subtract(tr.getValue()).subtract(BigInteger.valueOf(tr.getFee())));
		}

		for (String key : balanceMap.keySet()) {
			BalanceBean bb = new BalanceBean();
			bb.setAddress(key);
			bb.setPendingBalance(balanceMap.get(key));
			bba.add(bb);
		}
		return bba;
	}

	public TransactionData generateFaucetTransaction(String address) {
		
		TransactionData td = null;

		try {
			td = TransactionHelper.generateFaucetTransaction(address);
			getChain().getPendingTransactions().add(td);
		} catch (Exception e) {
			return null;
		}
		return td;
	}

}