package com.example.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.workflow.utility.PayloadConverter;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class WorkflowService {
	
	@Autowired
	ApiNodeService apiNodeService;

	@Autowired
	FilterNodeService filterNodeService;

	@Autowired
	ConditionalNodeService conditionalNodeService;

	public StringBuilder singleTraversal(StringBuilder currentNodeId,List<Map<?,?>> edges){
		for(Map<?,?> edge : edges){
			if(edge.get("source").equals(currentNodeId.toString())){
				currentNodeId=new StringBuilder((String)edge.get("target"));
				break;
			}
		}
		return currentNodeId;
	}
	public StringBuilder multiTraversal(StringBuilder currentNodeId,List<Map<?,?>> edges,int direction){
		for(Map<?,?> edge : edges){
			if(edge.get("source").equals(currentNodeId.toString()) && (int)edge.get("path")==direction){
				currentNodeId=new StringBuilder((String)edge.get("target"));
				break;
			}
		}
		return currentNodeId;
	}

	@SuppressWarnings("unchecked")
	public StringBuilder executeNode(StringBuilder currentNodeId,StringBuilder parentNodeId,List<Map<?,?>> nodes,List<Map<?,?>> edges) {
		Object parentData=null;
		Map<String,Object> currentNode=new HashMap<>();
		for(Map<?,?> node:nodes) {
			if(node.get("id").equals(currentNodeId.toString())){
				currentNode=(Map<String, Object>) node;
				break;
			}
		}
		if(!currentNodeId.toString().equals("0")) {
			for(Map<?,?> node:nodes) {
				if(node.get("id").equals(parentNodeId.toString())) {
					parentData=node.get("data");
					break;
				}
			}
		}
		else{
			parentData=currentNode.get("requestBody");
		}
		currentNode.put("requestBody", parentData);
		if(currentNode.get("nodeType").equals("api")){
			currentNode.put("data", apiNodeService.ApiCall(currentNode));
			currentNodeId=singleTraversal(currentNodeId, edges);
		}
		else if(currentNode.get("nodeType").equals("filter")){
			try {
				currentNode.put("data", filterNodeService.processJson(currentNode.get("requestBody"),(String)currentNode.get("parameter")));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentNodeId=singleTraversal(currentNodeId, edges);
		}
		else if(currentNode.get("nodeType").equals("conditional")){
			int path=conditionalNodeService.executeWithRule((String)currentNode.get("parameter"), currentNode.get("requestBody"));
			currentNode.put("data", currentNode.get("requestBody"));
			currentNodeId=multiTraversal(currentNodeId, edges, path);
		}
		return currentNodeId;
	}
	
	@SuppressWarnings("unchecked")
	public Object depthFirstSearch(Object payload) {
		Map<?,?> data=(Map<?, ?>) PayloadConverter.convertToMapOrList(payload);
		List<Map<?,?>> nodes=(List<Map<?, ?>>) PayloadConverter.convertToMapOrList(data.get("nodes"));
		List<Map<?,?>> edges=(List<Map<?, ?>>) PayloadConverter.convertToMapOrList(data.get("edges"));
		
		String nodeId="0";
		List<String> flow=new ArrayList<>();
		while(true) {
			flow.add(nodeId);
			int childNodes=0;
			StringBuilder parentNodeId=new StringBuilder("");
			for(Map<?,?> edge: edges) {
				if(edge.get("source").equals(nodeId)) {
					childNodes++;
				}
				if(edge.get("target").equals(nodeId)) {
					parentNodeId.append(edge.get("source"));
				}
			}
			if(childNodes==0) {
				break;
			}
			StringBuilder currentNodeId=new StringBuilder(nodeId);
			currentNodeId=executeNode(currentNodeId,parentNodeId,nodes,edges);
			nodeId=currentNodeId.toString();
		}
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("flow",flow);
		returnMap.put("nodes",nodes);
		return returnMap;
	}
}
