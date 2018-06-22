package com.shumencoin.beans_data.helper;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.constants.Constants;
import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;

public class BlockHelper {
	
	public static BlockData generateGenesisBlock() {
		BlockData genesisBlock = new BlockData();

		genesisBlock.setIndex(0);
		genesisBlock.setDificulty(0);
		genesisBlock.setMinedBy(Constants.genesisAddress);
		genesisBlock.setNonce(new BigInteger("0"));
		genesisBlock.setCreationDate(Constants.generateGenesisCreationDate());
		genesisBlock.setBlockDataHash(calculateBlockDataHash(genesisBlock));
		genesisBlock.getTransactions().add(TransactionHelper.generateGenesisTransaction());
		genesisBlock.setBlockHash(calculateBlockHash(genesisBlock));
		genesisBlock.setPrevBlockHash(Converter.byteArrayToHexString(Constants.genesisPrevBlockHash));

		return genesisBlock;
	}
	
	public static String calculateBlockDataHash(BlockData block) {

		BlockDataHashHelper blockDataHashHelper = new BlockDataHashHelper();
		
		blockDataHashHelper.index = block.getIndex();
		blockDataHashHelper.dificulty = block.getDificulty();
		blockDataHashHelper.minedBy = block.getMinedBy();
		blockDataHashHelper.transactions = block.getTransactions();
		blockDataHashHelper.prevBlockHash = block.getPrevBlockHash();
		
		ObjectMapper ow = new ObjectMapper();
		
		try {
			String forHashing = ow.writeValueAsString(blockDataHashHelper);
			block.setBlockDataHash(Crypto.sha256ToString(forHashing));
			return block.getBlockDataHash();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean validateDificulty(BlockData block) {

		long blockHashDificulty = 0;
		for (char c : block.getBlockHash().toCharArray()) {
			if (c != '0') {
				break;
			}

			blockHashDificulty++;
		}

		if (blockHashDificulty < block.getDificulty()) {
			return false;
		}

		return true;
	}

	public static boolean validateBlock(BlockData block) {

		String hash = calculateBlockHash(block);

		if (hash.equals(block.getBlockHash())) {
			return true;
		}

		return false;
	}	

	public static String calculateBlockHash(BlockData block) {
		String forHashing = block.getBlockDataHash() + block.getCreationDate().toString() + block.getNonce().toString();

		block.setBlockHash(Crypto.sha256ToString(forHashing));
		return block.getBlockHash();
	}
}

class BlockDataHashHelper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7775196132536963596L;
	
	public long index;
	public long dificulty;
	public String minedBy;
	public List<TransactionData> transactions;	
	public String prevBlockHash;
}
