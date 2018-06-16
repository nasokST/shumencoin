package com.shumencoin.beans_data;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Node bean structure
 * @author dragomir.todorov
 *
 */
public class NodeData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5673193831889685431L;

	private String nodeId; // the nodeId uniquely identifies the current node
	URL selfUrl;
	private Map<String, String> peers; // a map(nodeId --> url) of the peers, connected to this node

	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getSelfUrl() {
		return selfUrl.getUrl();
	}
	public void setSelfUrl(URL selfUrl) {
		this.selfUrl = selfUrl;
	}	
	public Map<String, String> getPeers() {
		return peers;
	}
	public void setPeers(Map<String, String> peers) {
		this.peers = peers;
	}
}
