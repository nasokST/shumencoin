package com.shumencoin.node;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shumencoin.beans.Node;
//import com.shumencoin.convertion.Converter;
//import com.shumencoin.crypto.Crypto;
import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.MiningJobData;
import com.shumencoin.beans_data.PeerConnectingInformation;
import com.shumencoin.crypto.Crypto;
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
	public static ShCError peerAskToConnect(Node currentNode, PeerConnectingInformation peerConnectingInformation) {

		ShCError error = currentNode.peerConnect(peerConnectingInformation);

		if (ShCError.NO_ERROR == error) {
			synchronizeeBlocksWithPear(currentNode, peerConnectingInformation);
		}

		// call to peer to connect current node to him
		if (peerConnectingInformation.isConnectionCallBack()) {

			PeerConnectingInformation currentNodeConnectingInformation = new PeerConnectingInformation();
	
			currentNodeConnectingInformation.setUrl(currentNode.getNode().getSelfUrl());
			currentNodeConnectingInformation.setChainId(currentNode.getBlockchain().getChainId());
			currentNodeConnectingInformation.setNodeId(currentNode.getNode().getNodeId());
			currentNodeConnectingInformation.setConnectionCallBack(false);
	
			ObjectMapper om = new ObjectMapper();
			String currentNodeConnectingInformationJson = null;
			try {
				currentNodeConnectingInformationJson = om.writeValueAsString(currentNodeConnectingInformation);
			} catch (JsonProcessingException e) {
				return ShCError.UNKNOWN;
			}


			postRequest(peerConnectingInformation.getUrl() + "/peers/connect", currentNodeConnectingInformationJson);			
		}

		return error;
	}

	/**
	 * 
	 * @param currentNode
	 * @param peerConnectingInformation
	 */
	@Async
	public static void synchronizeeBlocksWithPear(Node currentNode,
			PeerConnectingInformation peerConnectingInformation) {

		ShCError error = currentNode.validatePearInformation(peerConnectingInformation);
		if (ShCError.NO_ERROR != error) {
			return;
		}

		try {

			// get pear chain
			String pearChainJson = getRequest(peerConnectingInformation.getUrl() + "/blocks");

			ObjectMapper om = new ObjectMapper();
			List<BlockData> peerBlocks = om.readValue(pearChainJson, new TypeReference<List<BlockData>>() {
			});

			currentNode.synchronizeeBlocksWithPear(peerBlocks);

		} catch (ClientProtocolException e) {
			System.out.println("ERROR synchronization with node: " + peerConnectingInformation.getUrl());
		} catch (IOException e) {
			System.out.println("ERROR synchronization with node: " + peerConnectingInformation.getUrl());
		}

		return;
	}

	/**
	 * tying to submit new block and notify peers on success
	 * 
	 * @param minedBloce
	 * @param newBlock
	 * @return
	 */
	public static ShCError submitMinedBlock(Node node, MiningJobData minedBlock, BlockData newBlock) {

		// notifyPear("http://127.0.0.1:8080", newBlock);

		ShCError error = node.getBlockchain().submitMinedBlock(minedBlock, newBlock);

		if (ShCError.NO_ERROR == error) {
			newBlockNotification(node, newBlock);
		}

		return error;
	}

	private static void newBlockNotification(Node node, BlockData newBlock) {
		for (Map.Entry<String, String> peer : node.getNode().getPeers().entrySet()) {
			notifyPear(peer.getValue(), newBlock);
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
	private static void notifyPear(String peerHost, BlockData newBlock) {

		ObjectMapper ow = new ObjectMapper();
		String newBlockJson = null;
		try {
			newBlockJson = ow.writeValueAsString(newBlock);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		postRequest(peerHost + "/peers/notify-new-block", newBlockJson);
	}

	private static void postRequest(String toUrl, String jsonData) {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(toUrl);

		StringEntity entity;
		try {
			entity = new StringEntity(jsonData);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}

		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		try {
			CloseableHttpResponse response = client.execute(httpPost);
			System.out.println("POST: " + toUrl);
			System.out.println("response: " + response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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