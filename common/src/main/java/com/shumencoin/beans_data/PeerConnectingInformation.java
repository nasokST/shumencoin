package com.shumencoin.beans_data;

import java.io.Serializable;

public class PeerConnectingInformation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822068122896169766L;

	public PeerConnectingInformation() {
		
	}

	public PeerConnectingInformation(String url, String nodeId, String chainId) {
		this.setUrl(url);
		this.setNodeId(nodeId);
		this.setChainId(chainId);		
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	private String url;
	private String nodeId;
	private String chainId;

}
