package com.shumencoin.beans;

import java.io.Serializable;
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
		blockchain.initializeChain();
	}

	private String generateNodeId() {
		String rawKey = LocalDateTime.now().toString();

		byte[] randomeKey = new byte[32];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(randomeKey);

		rawKey += Converter.byteArrayToHexString(randomeKey);

		return Crypto.sha256ToString(rawKey);
	}

	private NodeData node;
	private Blockchain blockchain;
}
