package com.shumencoin.node;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shumencoin.beans.Node;
//import com.shumencoin.convertion.Converter;
//import com.shumencoin.crypto.Crypto;
import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.MiningJobData;
import com.shumencoin.beans_data.NotificationBaseData;
import com.shumencoin.errors.ShCError;

@SpringBootApplication
@ComponentScan(basePackages = { "com.shumencoin.*" })
@EnableAsync
public class NodeApplication {

	static int port = 8080;

	public static void main(String[] args) {

		Security.addProvider(new BouncyCastleProvider());

		// //byte[] privKey = Crypto.generatePrivateKey();
		// byte[] privKey =
		// Converter.HexStringToByteArray("7e4670ae70c98d24f3662c172dc510a085578b9ccc717e6c2f4e547edd960a34");
		// byte[] pubKey = Crypto.generatePublicKey(privKey);
		// byte[] pubKeyCompressed = Crypto.compressPublicKey(pubKey);
		//
		// System.out.println("PK: " + Converter.byteArrayToHexString(privKey));
		// System.out.println("PubK: " + Converter.byteArrayToHexString(pubKey));
		// System.out.println("PubKCompress: " +
		// Converter.byteArrayToHexString(pubKeyCompressed));

		for (String s : args) {
			switch (s.substring(0, 2)) {
			case "-p":
				port = Integer.valueOf(s.substring(3, s.length()));
				System.out.println(port);
				break;
			}
		}

		SpringApplication.run(NodeApplication.class, args);
	}

	@Bean
	public Node node() {

		Node node = new Node();
		node.initializeNode("127.0.0.1", port);

		return node;
	}

	/**
	 * 
	 * @param currentNode
	 * @param peerConnectingInformation
	 * @return
	 */
	public static ShCError OnPeerAskToConnect(Node currentNode, NotificationBaseData peerConnectingInformation) {

		ShCError error = currentNode.peerConnect(peerConnectingInformation);

		if (ShCError.NO_ERROR == error) {
			error = synchronizeeBlocksWithPear(currentNode, peerConnectingInformation, true);
			if (ShCError.NO_ERROR != error) {
				System.out.println("Error (OnPeerAskToConnect) can not synchronize blocks: " + error);
			}			
			//synchronizeeBlocksWithPear(currentNode, peerConnectingInformation.getUrl());
		}

		// call to peer to connect current node to him
		if (peerConnectingInformation.getNotificationCallBack()) {

			NotificationBaseData currentNodeConnectingInformation = new NotificationBaseData(currentNode, false);

			ObjectMapper om = new ObjectMapper();
			String currentNodeConnectingInformationJson = null;
			try {
				currentNodeConnectingInformationJson = om.writeValueAsString(currentNodeConnectingInformation);
			} catch (JsonProcessingException e) {
				return ShCError.UNKNOWN;
			}

			error = postRequest(peerConnectingInformation.getUrl() + "/peers/connect", currentNodeConnectingInformationJson);
			if (ShCError.NO_ERROR != error) {
				return error;
			}			
		}

		return error;
	}

	/**
	 * This method is call from some another node to tell about new block
	 * 
	 * @return
	 */
	public static ShCError OnNewBlockNotification(Node currentNode, NotificationBaseData peerNotificationData) {

		// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		ShCError error = currentNode.validateNotificationBaseData(peerNotificationData);
		if (ShCError.NO_ERROR != error) {
			return error;
		}

		error = synchronizeeBlocksWithPear(currentNode, peerNotificationData, false);
		if (ShCError.NO_ERROR != error) {
			return error;
		}

		return ShCError.NO_ERROR;

//		Integer numberOfSyncronizedBlocks = new Integer(0);
//		Boolean needOtherPearToBeNotyfied = new Boolean(false);
//
//		error = synchronizeeBlocksWithPear(currentNode, peerNotificationData.getUrl(), numberOfSyncronizedBlocks, needOtherPearToBeNotyfied);
//		if (ShCError.NO_ERROR != error) {
//			return error;
//		}
//
//		String skipNodeId = new String(skipNodeId = peerNotificationData.getNodeId());
//		if (needOtherPearToBeNotyfied) {
//			skipNodeId = "";		
//		
////			NotificationBaseData notificatePearData = new NotificationBaseData(currentNode, false);
////			notifyPear(peerNotificationData.getUrl(), notificatePearData);
//		}
//
//		NotificationBaseData notificatePearData = new NotificationBaseData(currentNode, false);
//		notifyPeersForNewBlock(currentNode, notificatePearData, skipNodeId);
//
//		return ShCError.NOT_IMPLEMENTED;
	}

	/**
	 * this method is call from miner when he mine new block
	 * 
	 * This method trying to submit new block and notify peers on success
	 * 
	 * @param minedBloce
	 * @param newBlock
	 * @return
	 */
	public static ShCError OnSubmitMinedBlock(Node node, MiningJobData minedBlock, BlockData newBlock) {

		// notifyPear("http://127.0.0.1:8080", newBlock);

		ShCError error = node.getBlockchain().submitMinedBlock(minedBlock, newBlock);

		if (ShCError.NO_ERROR == error) {
			NotificationBaseData notificationBaseData = new NotificationBaseData(node, false);

			notifyPeersForNewBlock(node, notificationBaseData, "");
		}

		return error;
	}
	
	@Async
	public static ShCError synchronizeeBlocksWithPear(Node currentNode, NotificationBaseData peerNotificationData, boolean disableCallBackNotification) {

		AtomicInteger numberOfSyncronizedBlocks = new AtomicInteger(0);
		Boolean needOtherPearToBeNotyfied = new Boolean(false);

		ShCError error = synchronizeeBlocksWithPear(currentNode, peerNotificationData.getUrl(), numberOfSyncronizedBlocks, needOtherPearToBeNotyfied);
		if (ShCError.NO_ERROR != error) {
			return error;
		}

		if (numberOfSyncronizedBlocks.get() > 0) {

			String skipNodeId = new String(skipNodeId = peerNotificationData.getNodeId());

			if (needOtherPearToBeNotyfied && (!disableCallBackNotification)) {
				skipNodeId = "";		
			}

			NotificationBaseData notificatePearData = new NotificationBaseData(currentNode, false);
			notifyPeersForNewBlock(currentNode, notificatePearData, skipNodeId);			
		}

		return ShCError.NO_ERROR;
	}	

//	@Async
//	private static ShCError synchronizeeBlocksWithPear(Node currentNode, String otherNodeUrl) {
//		Integer numberOfSyncronizedBlocks = new Integer(0);
//		Boolean needOtherPearToBeNotyfied = new Boolean(false);
//		return synchronizeeBlocksWithPear(currentNode, otherNodeUrl, numberOfSyncronizedBlocks,
//				needOtherPearToBeNotyfied);
//	}

	/**
	 * 
	 * @param currentNode
	 * @param peerConnectingInformation
	 */
	@Async
	private static ShCError synchronizeeBlocksWithPear(Node currentNode, String otherNodeUrl,
			AtomicInteger numberOfSyncronizedBlocks, Boolean needOtherPearToBeNotyfied) {

		try {

			// get pear chain
			String pearChainJson = getRequest(otherNodeUrl + "/blocks");

			ObjectMapper om = new ObjectMapper();
			List<BlockData> peerBlocks = om.readValue(pearChainJson, new TypeReference<List<BlockData>>() {
			});

			return currentNode.synchronizeeBlocksWithPear(peerBlocks, numberOfSyncronizedBlocks,
					needOtherPearToBeNotyfied);

		} catch (ClientProtocolException e) {
			System.out.println("ERROR synchronization with node: " + otherNodeUrl);
		} catch (IOException e) {
			System.out.println("ERROR synchronization with node: " + otherNodeUrl);
		}

		return ShCError.UNKNOWN;
	}

	@Async
	private static void notifyPeersForNewBlock(Node node, NotificationBaseData notificationBaseData, String skipNodeId) {
		for (Map.Entry<String, String> peer : node.getNode().getPeers().entrySet()) {
			if (!peer.getKey().equals(skipNodeId)) {
				notifyPear(peer.getValue(), notificationBaseData);
			}
			// TODO may be implements peer new block confirmation
		}
	}

	/**
	 * 
	 * 
	 * @param peerHost
	 * @param newBlock
	 */
	@Async
	private static void notifyPear(String peerHost, NotificationBaseData notificationBaseData) {

		ObjectMapper ow = new ObjectMapper();
		String newBlockJson = null;
		try {
			newBlockJson = ow.writeValueAsString(notificationBaseData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		postRequest(peerHost + "/peers/notify-new-block", newBlockJson);
	}

	private static ShCError postRequest(String toUrl, String jsonData) {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(toUrl);

		StringEntity entity;
		try {
			entity = new StringEntity(jsonData);
		} catch (UnsupportedEncodingException e) {
			return ShCError.CANNNOT_CONNECT_TO_PEER;
		}

		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		try {
			CloseableHttpResponse response = client.execute(httpPost);
			System.out.println("POST: " + toUrl);
			System.out.println("response: " + response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			return ShCError.CANNNOT_CONNECT_TO_PEER;
		} catch (IOException e) {
			return ShCError.CANNNOT_CONNECT_TO_PEER;
		}

		try {
			client.close();
		} catch (IOException e) {
			return ShCError.CANNNOT_CONNECT_TO_PEER;
		}

		return ShCError.NO_ERROR;
	}

	private static String getRequest(String toUrl) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(toUrl);
		CloseableHttpResponse response = httpclient.execute(httpGet);

		String responseJson = "";
		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			responseJson = EntityUtils.toString(entity);
			EntityUtils.consume(entity);

		} finally {
			response.close();
		}

		return responseJson;
	}
	
}