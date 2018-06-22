package com.shumencoin.crypto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shumencoin.crypto.databind.ByteArrayDeserializer;
import com.shumencoin.crypto.databind.ByteArraySerializer;

public class CryptoData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6562117600785066567L;

	public String address;

	public SCryptData script;
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)
	public byte[] twofish;
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)
	public byte[] iv;
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)
	public byte[] mac;

	public CryptoData() {
		script = new SCryptData();
		mac = new byte[32];
		iv = new byte[16];
	}
}