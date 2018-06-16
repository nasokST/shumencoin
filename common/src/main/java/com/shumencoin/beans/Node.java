package com.shumencoin.beans;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Node bean structure
 * @author dragomir.todorov
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5673193831889685431L;

	private String nodeId; // the nodeId uniquely identifies the current node
	URL selfUrl;
	private Map<String, String> peers; // a map(nodeId --> url) of the peers, connected to this node
	private String chainId; // the unique chain ID (hash of the genesis block)
	private Blockchain blockchain; // the blockchain (blocks, transactions, ...)
	
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
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public Blockchain getBlockchain() {
		return blockchain;
	}
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public void setBlockchain(Blockchain blockchain) {
		this.blockchain = blockchain;
	}
}
