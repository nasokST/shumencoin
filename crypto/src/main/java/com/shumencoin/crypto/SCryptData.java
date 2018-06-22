package com.shumencoin.crypto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shumencoin.crypto.databind.ByteArrayDeserializer;
import com.shumencoin.crypto.databind.ByteArraySerializer;

public class SCryptData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6514528082860540718L;

	public int dklen;
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	@JsonSerialize(using = ByteArraySerializer.class)
	public byte[] salt;
	public int n;
	public int r;
	public int p;

	SCryptData() {
		dklen = 64;
		salt = new byte[32];
		n = 16384;
		r = 16;
		p = 1;
	}
}