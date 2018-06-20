package com.shumencoin.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.NodeData;
import com.shumencoin.beans_data.NotificationBaseData;
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
	 * if peer chain size is great than current chain size
	 * adding blocks from peer chain to current chain 
	 * 
	 * @param peerBlocks
	 * @param numberOfSyncronizedBlocks
	 * @param needOtherPearToBeNotyfied
	 * @return
	 */
	public synchronized ShCError synchronizeeBlocksWithPear(List<BlockData> peerBlocks,
			Integer numberOfSyncronizedBlocks, Boolean needOtherPearToBeNotyfied) {

		numberOfSyncronizedBlocks = 0;
		needOtherPearToBeNotyfied = false;

		List<BlockData> currentChainBlocks = getBlockchain().getChain().getBlocks();

		if (currentChainBlocks.size() > peerBlocks.size()) {
			needOtherPearToBeNotyfied = true;
			return ShCError.NO_ERROR;
		}

		// searching for common block
		AtomicInteger currentChainCommonBlockIndex = new AtomicInteger(-1);
		AtomicInteger peerCommonBlockIndex = new AtomicInteger(-1);

		List<BlockData> elementsToRemoveFromCurrentChain = new LinkedList<BlockData>();
		ShCError error = searchForCommonBlocks(peerBlocks, currentChainCommonBlockIndex, peerCommonBlockIndex, elementsToRemoveFromCurrentChain);
		if (ShCError.NO_ERROR != error) {
			return error;			
		}

		// remove invalid blocks
		currentChainBlocks.removeAll(elementsToRemoveFromCurrentChain);

		// add blocks from peer chain to current chain
		for (int idx = peerCommonBlockIndex.get() + 1; idx < peerBlocks.size(); ++idx) {
			currentChainBlocks.add(peerBlocks.get(idx));
		}

		return ShCError.NO_ERROR;
	}

	/**
	 * 
	 * @param peerBlocks
	 * @param currentChainCommonBlockIndex
	 * @param peerCommonBlockIndex
	 * @param elementsToRemove
	 * @return
	 */
	private ShCError searchForCommonBlocks(List<BlockData> peerBlocks, AtomicInteger currentChainCommonBlockIndex,
			AtomicInteger peerCommonBlockIndex, List<BlockData> elementsToRemove) {

		List<BlockData> currentChainBlocks = getBlockchain().getChain().getBlocks();

		currentChainCommonBlockIndex.set(-1);
		peerCommonBlockIndex.set(-1);
		
		int deltaSize = peerBlocks.size() - currentChainBlocks.size();
		if (deltaSize < 0) {
			return ShCError.UNKNOWN;
		}

		ListIterator currentBlocksIterator = currentChainBlocks.listIterator(currentChainBlocks.size());
		ListIterator peerBlocksIterator = peerBlocks.listIterator(currentChainBlocks.size()); // starting from the same position as currentChainBlocks

		boolean peerBlocksAreValid = true;
		while (peerBlocksIterator.hasPrevious() && currentBlocksIterator.hasPrevious()) {
			
			int tmpCurrentChainCommonBlockIndex = currentBlocksIterator.previousIndex();
			int tmpPeerCommonBlockIndex = peerBlocksIterator.previousIndex();			

			BlockData prevPeerChainBlock = (BlockData) peerBlocksIterator.previous();
			BlockData prevCurrentChainBlock = (BlockData) currentBlocksIterator.previous();

			peerBlocksAreValid = BlockHelper.validateBlock(prevPeerChainBlock);
			if (!peerBlocksAreValid) {
				break;
			}

			if (prevPeerChainBlock.equals(prevCurrentChainBlock)) {
				currentChainCommonBlockIndex.set(tmpCurrentChainCommonBlockIndex);
				peerCommonBlockIndex.set(tmpPeerCommonBlockIndex);
				break;
			}

			elementsToRemove.add(prevCurrentChainBlock);
		}

		if (!peerBlocksAreValid) {
			return ShCError.INCORRECT_PEERS_BLOCKS;
		}

		return ShCError.NO_ERROR;
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
