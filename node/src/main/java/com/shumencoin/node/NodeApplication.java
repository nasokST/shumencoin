package com.shumencoin.node;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Arrays;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import com.shumencoin.beans.Node;
//import com.shumencoin.convertion.Converter;
//import com.shumencoin.crypto.Crypto;

@SpringBootApplication
@ComponentScan(basePackages = { "com.shumencoin.*" })
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