package com.shumencoin.constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.shumencoin.beans_data.BlockchainData;

public class Constants {

	public static String chainId;
	public static BlockchainData chain;

	public static String genesisAddress = "0000000000000000000000000000000000000000";
	public static String genesisPublicKey = "00000000000000000000000000000000000000000000000000000000000000000";
	public static byte[] genesisPrevBlockHash = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static byte[][] genesisSignature = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
										 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
	
	public static String faucetPrivateKey = "08bc02af150ae70de1be4b6c1039fc70671ef5b13519330bdd9ec77cd79ccfb3";
	public static String faucetPublicKey = "028d69e411cddf2247720ccd27fea2e68fd290f5dd5fe9c780f7027a1b0746c462";
	public static String faucetAddress = "1423d30500657Ed28a39cb6bE5e0354C9117E69C";	

	public static LocalDateTime generateGenesisCreationDate() {
		String str = "2018-05-11 00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

		return dateTime;
	}	
}
