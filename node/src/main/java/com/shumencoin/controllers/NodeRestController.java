package com.shumencoin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shumencoin.beans.Node;

@RestController
public class NodeRestController {
	
	@Autowired
	Node node;

    @RequestMapping("/node")
    public Node index() {
    	return node;
    }
}