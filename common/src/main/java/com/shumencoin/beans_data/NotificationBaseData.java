package com.shumencoin.beans_data;

import java.io.Serializable;

import com.shumencoin.beans.Node;

public class NotificationBaseData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822068122896169766L;
	
	public NotificationBaseData() {
	}	

	public NotificationBaseData(Node node, boolean notificationCallBack) {
		this.setChainId(node.getBlockchain().getChainId());
		this.setNodeId(node.getNode().getNodeId());
		this.setNotificationCallBack(notificationCallBack);
		this.setUrl(node.getNode().getSelfUrl());
	}

	public NotificationBaseData(String url, String nodeId, String chainId, boolean notificationCallBack) {
		this.setUrl(url);
		this.setNodeId(nodeId);
		this.setChainId(chainId);
		this.setNotificationCallBack(notificationCallBack);
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
	public boolean getNotificationCallBack() {
		return notificationCallBack;
	}

	public void setNotificationCallBack(boolean notificationCallBack) {
		this.notificationCallBack = notificationCallBack;
	}
	
	private String url;
	private String nodeId;
	private String chainId;
	private boolean notificationCallBack;
}
