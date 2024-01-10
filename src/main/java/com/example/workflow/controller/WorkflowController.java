package com.example.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.workflow.service.WorkflowService;

@RestController
public class WorkflowController {
	
	@Autowired
	WorkflowService workflowService;
	
	@PostMapping("/dfs")
	public Object dfs(@RequestBody Object object) {
		return workflowService.depthFirstSearch(object);
	}
}
