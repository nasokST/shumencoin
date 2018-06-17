package com.shumencoin.beans_data.helper;

import java.math.BigInteger;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.constants.Constants;
import com.shumencoin.crypto.Crypto;

public class BlockHelper {
	
	public static BlockData generateGenesisBlock() {
		BlockData genesisBlock = new BlockData();

		genesisBlock.setIndex(new BigInteger("0"));
		genesisBlock.setDificulty(0);
		genesisBlock.setMinedBy(Constants.genesisAddress);
		genesisBlock.setNonce(new BigInteger("0"));
		genesisBlock.setCreationDate(Constants.generateGenesisCreationDate());
		genesisBlock.setBlockDataHash(calculateBlockDataHash(genesisBlock));
		genesisBlock.getTransactions().add(TransactionHelper.generateGenesisTransaction());
		genesisBlock.setBlockHash(calculateBlockHash(genesisBlock));
		genesisBlock.setPrevBlockHash(Constants.genesisPrevBlockHash);

		return genesisBlock;
	}

	public static byte[] calculateBlockDataHash(BlockData block) {

		String forHashing = block.getBlockDataHash() + block.getCreationDate().toString() + block.getNonce().toString();

		return Crypto.sha256(forHashing);
	}

	public static byte[] calculateBlockHash(BlockData block) {
		String forHashing = block.getBlockDataHash() + block.getCreationDate().toString() + block.getNonce().toString();

		return Crypto.sha256(forHashing);
	}
}
