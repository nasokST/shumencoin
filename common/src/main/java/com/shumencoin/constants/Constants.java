package com.shumencoin.constants;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {

	public static long dificulty = 6;
	public static BigInteger blockReward = new BigInteger("50000");
    public static long minFee = 10;
    public static long maxFee = 1000000;
    public static long maxTransactionToCommit = 3;
    public static BigInteger maxCoinValue = new BigInteger("10000000000000");

	public static String genesisAddress = "0000000000000000000000000000000000000000";
	public static String genesisPublicKey = "00000000000000000000000000000000000000000000000000000000000000000";
	public static byte[] genesisPrevBlockHash = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static byte[][] genesisSignature = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
										 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};

	public static String faucetPrivateKey = "08bc02af150ae70de1be4b6c1039fc70671ef5b13519330bdd9ec77cd79ccfb3";
	public static String faucetPublicKey = "028d69e411cddf2247720ccd27fea2e68fd290f5dd5fe9c780f7027a1b0746c462";
	public static String faucetAddress = "1423d30500657Ed28a39cb6bE5e0354C9117E69C";	

	public static String generateGenesisCreationDate() {
		String str = "2018-05-11T00:00:00+02:00";
		return str;
		//return stringToDateTimeTo(str);
	}
	
	public static String dateTimeToIsoStr(LocalDateTime dateTime) {
		if (null != dateTime) {
			return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);			
		}

		return "";
	}
	
	public static LocalDateTime stringToDateTimeTo(String dateStr) {
		return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
	}	
}
