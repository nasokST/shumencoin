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
		
		ShCError error = node.getBlockchain().submiteMinedBlock(minedBlock, newBlock);
		
		if (ShCError.NO_ERROR == error) {
			newBlockNotification(node, newBlock);
		}

		return error;
	}
	
	@Async
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
		
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(peerHost + "/peers/notify-new-block");
	 
	    StringEntity entity;
		try {
			entity = new StringEntity(newBlockJson);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}

	    httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	 
	    try {
			CloseableHttpResponse response = client.execute(httpPost);
			System.out.println("POST: " + peerHost + "/notify-new-block");
			System.out.println("response: " + response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }	
}