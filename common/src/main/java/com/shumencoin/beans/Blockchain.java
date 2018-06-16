package com.shumencoin.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.BlockchainData;
import com.shumencoin.beans_data.TransactionData;

public class Blockchain implements Serializable{

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

	public void initializeNode() {
		
		BlockData genesisBlock = generateGenesisBlock();
		
		chain = new BlockchainData(genesisBlock, 1);
		
		chainId = "1";
	}

	private BlockData generateGenesisBlock() {
		BlockData genesisBlock = new BlockData();

		genesisBlock.setIndex(new BigInteger("0"));
		genesisBlock.setDificulty(0);
		genesisBlock.setMinedBy("0000000000000000000000000000000000000000");
		genesisBlock.setNonce(new BigInteger("0"));
		genesisBlock.setCreationDate(generateGenesisCreationDate());
		genesisBlock.setBlockDataHash(calculateBlockDataHash(genesisBlock));
		genesisBlock.getTransactions().add(generateGenesisTransaction());
		genesisBlock.setBlockHash("0000000000000000000000000000000000000000000000000000000000000000");
		genesisBlock.setPrevBlockHash("0000000000000000000000000000000000000000000000000000000000000000");

		return genesisBlock;
	}
	
	private String calculateBlockDataHash(BlockData block) {
		String hashString = "";
		return hashString;
	}
	
	private TransactionData generateGenesisTransaction() {
		TransactionData genesisTransaction = new TransactionData();
		
		return genesisTransaction;
	}
	
	private LocalDateTime generateGenesisCreationDate() {
		String str = "2018-05-11 00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
		
		return dateTime;
	}

	private String chainId;	
	private BlockchainData chain;	
}