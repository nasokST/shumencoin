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
import com.shumencoin.beans_data.BlockData;
import com.shumencoin.beans_data.TransactionData;
import com.shumencoin.errors.ShCError;

@RestController
public class NodeRestController {
	
	@Autowired
	Node node;

    @RequestMapping("/node")
    public Node index() {
    	return node;
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
    public ResponseEntity<?>  getBlockByIndex(@PathVariable("index") BigInteger index) {
    	try {
    		BlockData block = node.getBlockchain().getChain().getBlocks().get(Integer.valueOf(index.toString()));
        	return new ResponseEntity<Object>(block, HttpStatus.OK);
    	}
    	catch(Exception e) {
        	return new ResponseEntity<Object>("Invalide block: " + index, HttpStatus.BAD_REQUEST);
    	}
    }

    //  ====== TRANSACTIONS =====
    @RequestMapping("/transactions/pending")
    public List<TransactionData> getAllPendingTransactions() {
    	return node.getBlockchain().getChain().getPendingTransactions();
    }

    @RequestMapping("/transactions/confirmed")
    public ResponseEntity<?> getAllConfirmedTransactions() {
    	// TODO    	
    	return new ResponseEntity<Object>("getAllConfirmedTransactions() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/transactions/{transactionHash}")
    public ResponseEntity<?> getTransactionByHash() {
    	// TODO    	
    	return new ResponseEntity<Object>("getTransactionByHash() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping("/transactions/send")
    public ResponseEntity<?> getTransactionSend() {
    	// TODO    	
    	return new ResponseEntity<Object>("getTransactionSend() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
    }

    // 	====== ADDRESS =====
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

    // ======   PEARS =====
    @RequestMapping("/peers")
    public ResponseEntity<?> getPeers() {
    	return new ResponseEntity<Object>(node.getNode().getPeers(), HttpStatus.OK);
    }

    @PostMapping("/peers/connect")
    public ResponseEntity<?> getPeersConnect() {
    	// TODO    	
    	return new ResponseEntity<Object>("getPeersConnect() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/peers/notify-new-block")
    public ResponseEntity<?> getPeersNotifyForNewBlock() {
    	// TODO 
    	return new ResponseEntity<Object>("getPeersNotifyForNewBlock() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
    }

    // ======   MINING =====
    @GetMapping("/mining/job-request/{minerAddress}")
    public ResponseEntity<?> getMiningAskJob(@PathVariable("minerAddress") String minerAddress) {

    	BlockData miningJob = new BlockData();
    	ShCError error = node.getBlockchain().getNewMiningJob(minerAddress, miningJob); 
    	if (ShCError.NO_ERROR == error) {
    		return new ResponseEntity<Object>(miningJob, HttpStatus.OK);
    	}

    	return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping("/mining/submit-new-block")
    public ResponseEntity<?> getMiningSubmitNewBlock(@RequestBody BlockData minedBlock) {

    	BlockData newBlock = null;
    	ShCError error = node.getBlockchain().submiteMinedBlock(minedBlock, newBlock);
    	if (ShCError.NO_ERROR == error) {
    		return new ResponseEntity<Object>(newBlock, HttpStatus.OK);
    	}

    	return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }    
}