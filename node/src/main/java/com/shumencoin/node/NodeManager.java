package com.shumencoin.node;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.shumencoin.beans.Blockchain;
import com.shumencoin.beans.Node;
import com.shumencoin.beans.URL;
import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;

@Component
public class NodeManager {

	public Node getNode() {
		return node;
	}

	public void initializeNode(String ip, int port) {
		node = new Node();		

		node.setNodeId(generateNodeId());

		URL url = new URL();
		url.setIp(ip);
		url.setPort(port);
		node.setSelfUrl(url);

		Map<String, String> peers = new HashMap<String, String>();

		String chainId = "";

		//Blockchain blockchain = new Blockchain(genesisBlock, startDifficulty);		
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

	private Node node;
}
