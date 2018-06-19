package com.shumencoin.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.NodeData;
import com.shumencoin.beans_data.NotificationBaseData;
import com.shumencoin.beans_data.URL;
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

	public synchronized ShCError validateNotificationBaseData(NotificationBaseData peerConnectingInformation) {

		if (getNode().getNodeId().equals(peerConnectingInformation.getNodeId())) {
			return ShCError.SELF_CONNECTION;
		}
		if (!getBlockchain().getChainId().equals(peerConnectingInformation.getChainId())) {
			return ShCError.DIFFERENT_CHAIN_ID;
		}

		return ShCError.NO_ERROR;
	}

	public synchronized ShCError peerConnect(NotificationBaseData peerConnectingInformation) {

		ShCError error = validateNotificationBaseData(peerConnectingInformation);
		if (ShCError.NO_ERROR != error) {
			return error;
		}

		// TODO searching and remove for existing peerUrl but with different NodeId

		getNode().getPeers().put(peerConnectingInformation.getNodeId(), peerConnectingInformation.getUrl());

		return ShCError.NO_ERROR;
	}

	/**
	 * 
	 * @param peerBlocks
	 * @param numberOfSyncronizedBlocks
	 * @param needOtherPearToBeNotyfied
	 * @return
	 */
	public synchronized ShCError synchronizeeBlocksWithPear(List<BlockData> peerBlocks,
			Integer numberOfSyncronizedBlocks, Boolean needOtherPearToBeNotyfied) {
		
		// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		numberOfSyncronizedBlocks = 0;
		needOtherPearToBeNotyfied = false;
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
