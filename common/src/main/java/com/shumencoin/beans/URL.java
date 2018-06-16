package com.shumencoin.beans;

import java.io.Serializable;

public class URL implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2691939904909177723L;

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	String getUrl() {
		return "http://" + ip + ":" + port;
	}
	
	private String ip;
	private int port;
}
