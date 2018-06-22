package com.shumencoin.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shumencoin.beans.Node;
import com.shumencoin.beans_data.BalanceBean;
import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.MiningJobData;
import com.shumencoin.beans_data.NotificationBaseData;
import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.errors.ShCError;
import com.shumencoin.node.NodeApplication;

@RestController
public class NodeRestController {

	@Autowired
	Node node;

	@RequestMapping("/node")
	public Node index() {
		return node;
	}

	@GetMapping("/node/id")
	public ResponseEntity<?> getNodeId() {
		return new ResponseEntity<Object>(node.getNode().getNodeId(), HttpStatus.OK);
	}

	@GetMapping("/node/chain-id")
	public ResponseEntity<?> getChainId() {
		return new ResponseEntity<Object>(node.getBlockchain().getChainId(), HttpStatus.OK);
	}

	@RequestMapping("/balances")
	public ResponseEntity<?> getConfirmedBalances() {
		return new ResponseEntity<Object>("getConfirmedBalances() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
	}

	@RequestMapping("/blocks")
	public List<BlockData> getAllBlocks() {
		return node.getBlockchain().getChain().getBlocks();
	}

	@RequestMapping("/blocks/{index}")
	public ResponseEntity<?> getBlockByIndex(@PathVariable("index") BigInteger index) {
		try {
			BlockData block = node.getBlockchain().getChain().getBlocks().get(Integer.valueOf(index.toString()));
			return new ResponseEntity<Object>(block, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Invalid block: " + index, HttpStatus.BAD_REQUEST);
		}
	}

	// ====== TRANSACTIONS =====
	@RequestMapping("/transactions/pending")
	public List<TransactionData> getAllPendingTransactions() {
		return node.getBlockchain().getChain().getPendingTransactions();
	}

	@RequestMapping("/transactions/confirmed")
	public List<TransactionData> getAllConfirmedTransactions() {
		return node.getBlockchain().listConfirmedTransaction();
	}

	@RequestMapping("/transactions/{transactionHash}")
	public ResponseEntity<?> getTransactionByHash(@PathVariable("transactionHash") String hash) {
		try {
			TransactionData transaction = node.getBlockchain().getTransactionByHash(hash);
			return new ResponseEntity<Object>(transaction, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Invalid transaction hash: " + hash, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/transactions/send")
	public ResponseEntity<?> getTransactionSend(@RequestBody String address) {
	    String addressString = address.substring(0, address.length()-1);
	    if(node.getBlockchain().generateFaucetTransaction(addressString)) {
		return new ResponseEntity<Object>("Success", HttpStatus.OK);
	    } else {
		return new ResponseEntity<Object>("Transaction not created", HttpStatus.BAD_REQUEST);
	    }
	}

	@RequestMapping("/transactions/balances")
	public List<BalanceBean> getBalances() {
	    return node.getBlockchain().getBalances();
	}
	
	// ====== ADDRESS =====
	@RequestMapping("/address/{address}/transactions")
	public ResponseEntity<?> addressGetTransactions() {
		// TODO
		return new ResponseEntity<Object>("addressGetTransactions() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
	}

	@RequestMapping("/address/{address}/balance")
	public ResponseEntity<?> addressGetBalance() {
		// TODO
		return new ResponseEntity<Object>("addressGetBalance() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
	}

	// ====== PEARS =====
	@RequestMapping("/peers")
	public ResponseEntity<?> getPeers() {
		return new ResponseEntity<Object>(node.getNode().getPeers(), HttpStatus.OK);
	}

	@PostMapping("/peers/connect")
	public ResponseEntity<?> peerAskToConnect(@RequestBody NotificationBaseData peerConnectingInformation) {
		ShCError error = NodeApplication.OnPeerAskToConnect(node, peerConnectingInformation);
		if (ShCError.NO_ERROR == error) {
			return new ResponseEntity<Object>(error, HttpStatus.OK);
		}

		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/peers/notify-new-block")
	public ResponseEntity<?> getPeersNotifyForNewBlock(@RequestBody NotificationBaseData notificationData) {
		
		ShCError error = NodeApplication.OnNewBlockNotification(node, notificationData);
		if (ShCError.NO_ERROR == error) {
			return new ResponseEntity<Object>(error, HttpStatus.OK);
		}

		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	// ====== MINING =====
	@GetMapping("/mining/job-request/{minerAddress}")
	public ResponseEntity<?> getMiningAskJob(@PathVariable("minerAddress") String minerAddress) {

		MiningJobData miningJob = new MiningJobData();
		ShCError error = node.getBlockchain().getNewMiningJob(minerAddress, miningJob);
		if (ShCError.NO_ERROR == error) {
			return new ResponseEntity<Object>(miningJob, HttpStatus.OK);
		}

		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/mining/submit-new-block")
	public ResponseEntity<?> getMiningSubmitNewBlock(@RequestBody MiningJobData minedBlock) {

		BlockData newBlock = new BlockData();
		ShCError error = NodeApplication.OnSubmitMinedBlock(node, minedBlock, newBlock);
		if (ShCError.NO_ERROR == error) {
			return new ResponseEntity<Object>(newBlock, HttpStatus.OK);
		}

		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
}