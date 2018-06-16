package com.shumencoin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shumencoin.beans.Node;
import com.shumencoin.node.NodeManager;

@RestController
public class NodeRestController {
	
	@Autowired
	NodeManager nodeManager;

    @RequestMapping("/node")
    public Node index() {
    	return nodeManager.getNode();
    }
}