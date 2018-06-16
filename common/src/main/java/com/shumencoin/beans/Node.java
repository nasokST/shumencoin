package com.shumencoin.beans;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.shumencoin.beans_data.NodeData;
import com.shumencoin.beans_data.URL;
import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;

public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3593737094203707466L;

	public NodeData getNode() {
		return node;
	}

	public Blockchain getBlockchain() {
		return blockchain;
	}	

	public void initializeNode(String ip, int port) {
		node = new NodeData();		

		node.setNodeId(generateNodeId());

		URL url = new URL();
		url.setIp(ip);
		url.setPort(port);
		node.setSelfUrl(url);

		Map<String, String> peers = new HashMap<String, String>();
		node.setPeers(peers);

		blockchain = new Blockchain();
		blockchain.setChainId("1");
	}

	public void resetToGenesisBlock() throws Exception {
		throw new Exception("!!! NOT IMPLEMENTED !!!");
	}

	private String generateNodeId() {
		String rawKey = LocalDateTime.now().toString();

		byte[] randomeKey = new byte[32];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(randomeKey);

		rawKey += Converter.byteArrayToHexString(randomeKey);

		byte[] hash;
		try {
			hash = Crypto.sha256(rawKey);
			return Converter.byteArrayToHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return rawKey;
	}

	private NodeData node;
	private Blockchain blockchain;
}
