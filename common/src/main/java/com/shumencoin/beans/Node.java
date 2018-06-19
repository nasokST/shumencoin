package com.shumencoin.beans;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.MiningJobData;
import com.shumencoin.beans_data.NodeData;
import com.shumencoin.beans_data.PeerConnectingInformation;
import com.shumencoin.beans_data.URL;
import com.shumencoin.beans_data.helper.BlockHelper;
import com.shumencoin.convertion.Converter;
import com.shumencoin.crypto.Crypto;
import com.shumencoin.errors.ShCError;

public class Node implements Serializable {

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

	public ShCError validatePearInformation(PeerConnectingInformation peerConnectingInformation) {

		if (getNode().getNodeId().equals(peerConnectingInformation.getNodeId())) {
			return ShCError.SELF_CONNECTION;
		}
		if (!getBlockchain().getChainId().equals(peerConnectingInformation.getChainId())) {
			return ShCError.DIFFERENT_CHAIN_ID;
		}

		return ShCError.NO_ERROR;
	}	

	public ShCError peerConnect(PeerConnectingInformation peerConnectingInformation) {

		ShCError error = validatePearInformation(peerConnectingInformation);
		if (ShCError.NO_ERROR != error) {
			return error;
		}
		
		// TODO searching and remove for existing peerUrl but with different NodeId

		getNode().getPeers().put(peerConnectingInformation.getNodeId(), peerConnectingInformation.getUrl());

		return ShCError.NO_ERROR;
	}
	
	public ShCError synchronizeeBlocksWithPear(List<BlockData> peerBlocks) {
		// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return ShCError.UNKNOWN;
	}

	private String generateNodeId() {
		String rawKey = LocalDateTime.now().toString();

		byte[] randomeKey = new byte[32];
		Crypto.generatePublicKey(randomeKey);

		rawKey += Converter.byteArrayToHexString(randomeKey);

		return Crypto.sha256ToString(rawKey);
	}

	private NodeData node;
	private Blockchain blockchain;
}
