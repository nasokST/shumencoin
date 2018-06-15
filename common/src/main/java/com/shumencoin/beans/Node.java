package com.shumencoin.beans;

import java.util.Map;

/**
 * Node bean structure
 * @author dragomir.todorov
 *
 */
public class Node {
	private String nodeId; // the nodeId uniquely identifies the current node
	private String host; // the external host / IP address to connect to this node
	private Integer port; // listening TCP port number
	private String selfUrl; // the external base URL of the REST endpoints
	private Map<String, String> peers; // a map(nodeId --> url) of the peers, connected to this node
	private String chainId; // the unique chain ID (hash of the genesis block)
	private Blockchain blockchain; // the blockchain (blocks, transactions, ...)
	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getSelfUrl() {
		return selfUrl;
	}
	public void setSelfUrl(String selfUrl) {
		this.selfUrl = selfUrl;
	}
	public Map<String, String> getPeers() {
		return peers;
	}
	public void setPeers(Map<String, String> peers) {
		this.peers = peers;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public Blockchain getBlockchain() {
		return blockchain;
	}
	public void setBlockchain(Blockchain blockchain) {
		this.blockchain = blockchain;
	}
	
}
