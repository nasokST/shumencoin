package com.shumencoin.node;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shumencoin.beans.Node;
//import com.shumencoin.convertion.Converter;
//import com.shumencoin.crypto.Crypto;
import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.MiningJobData;
import com.shumencoin.crypto.Crypto;
import com.shumencoin.errors.ShCError;

@SpringBootApplication
@ComponentScan(basePackages = { "com.shumencoin.*" })
@EnableAsync
public class NodeApplication {
	
	static int port = 8080;

	public static void main(String[] args) {

		Security.addProvider(new BouncyCastleProvider());

//		//byte[] privKey = Crypto.generatePrivateKey();
//		byte[] privKey = Converter.HexStringToByteArray("7e4670ae70c98d24f3662c172dc510a085578b9ccc717e6c2f4e547edd960a34");
//		byte[] pubKey = Crypto.generatePublicKey(privKey);
//		byte[] pubKeyCompressed = Crypto.compressPublicKey(pubKey);
//
//		System.out.println("PK: " + Converter.byteArrayToHexString(privKey));
//		System.out.println("PubK: " + Converter.byteArrayToHexString(pubKey));
//		System.out.println("PubKCompress: " + Converter.byteArrayToHexString(pubKeyCompressed));
		
		for (String s : args) {
			switch(s.substring(0, 2)) {
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
	 * tying to submit new block and notify peers on success
	 * 
	 * @param minedBloce
	 * @param newBlock
	 * @return
	 */
	public static ShCError submitMinedBlock(Node node, MiningJobData minedBlock, BlockData newBlock) {

		//notifyPear("http://127.0.0.1:8080", newBlock);

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
	
	private static void getRequest() {
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("http://targethost/homepage");
//		CloseableHttpResponse response1 = httpclient.execute(httpGet);
//		// The underlying HTTP connection is still held by the response object
//		// to allow the response content to be streamed directly from the network socket.
//		// In order to ensure correct deallocation of system resources
//		// the user MUST call CloseableHttpResponse#close() from a finally clause.
//		// Please note that if response content is not fully consumed the underlying
//		// connection cannot be safely re-used and will be shut down and discarded
//		// by the connection manager. 
//		try {
//		    System.out.println(response1.getStatusLine());
//		    HttpEntity entity1 = response1.getEntity();
//		    // do something useful with the response body
//		    // and ensure it is fully consumed
//		    EntityUtils.consume(entity1);
//		} finally {
//		    response1.close();
//		}
//
//		HttpPost httpPost = new HttpPost("http://targethost/login");
//		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//		nvps.add(new BasicNameValuePair("username", "vip"));
//		nvps.add(new BasicNameValuePair("password", "secret"));
//		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//		CloseableHttpResponse response2 = httpclient.execute(httpPost);
//
//		try {
//		    System.out.println(response2.getStatusLine());
//		    HttpEntity entity2 = response2.getEntity();
//		    // do something useful with the response body
//		    // and ensure it is fully consumed
//		    EntityUtils.consume(entity2);
//		} finally {
//		    response2.close();
//		}
	}
}