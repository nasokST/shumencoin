package com.shumencoin.node;

import java.security.Security;
import java.util.Arrays;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.shumencoin.beans.Node;

@SpringBootApplication
@ComponentScan(basePackages = { "com.shumencoin.*" })
public class NodeApplication {

	public static void main(String[] args) {

		Security.addProvider(new BouncyCastleProvider());

		SpringApplication.run(NodeApplication.class, args); 
	}

	@Bean
	public Node nodeManager() {

		Node nodeManager = new Node();
		nodeManager.initializeNode("127.0.0.1", 8080);

		return nodeManager;
	}

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }	
}