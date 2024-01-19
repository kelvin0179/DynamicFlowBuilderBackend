package com.example.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

	@Autowired
	OperationNodeService operationNodeService;

	Map<String,Boolean> visited=new HashMap<String,Boolean>();
	Queue<String> queue =new LinkedList<>();

	Object globalData;
	public void singleTraversal(StringBuilder currentNodeId,List<Map<?,?>> edges){
		for(Map<?,?> edge : edges){
			if(edge.get("source").equals(currentNodeId.toString()) && visited.get(edge.get("target"))==false){
				visited.put((String)edge.get("target"), true);
				queue.add((String)edge.get("target"));
			}
		}
	}
	public void multiTraversal(StringBuilder currentNodeId,List<Map<?,?>> edges,int direction){
		for(Map<?,?> edge : edges){
			if(edge.get("source").equals(currentNodeId.toString()) && (int)edge.get("path")==direction && visited.get(edge.get("target"))==false){
				visited.put((String)edge.get("target"), true);
				queue.add((String)edge.get("target"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void executeNode(StringBuilder currentNodeId,StringBuilder parentNodeId,List<String> parentNodeIds,List<Map<?,?>> nodes,List<Map<?,?>> edges) {
		Object parentData=null;
		List<Object> parentDataList=new ArrayList<Object>();
		Map<String,Object> currentNode=new HashMap<>();
		for(Map<?,?> node:nodes) {
			if(node.get("id").equals(currentNodeId.toString())){
				currentNode=(Map<String, Object>) node;
				break;
			}
		}
		if(!currentNodeId.toString().equals("0")) {
			for(Map<?,?> node:nodes) {
				if(parentNodeIds.contains((String)node.get("id"))) {
					parentData=node.get("data");
					List<?> iterableData=(List<?>)node.get("data");
					for(Object nodeData: iterableData){
						parentDataList.add(nodeData);
					}
				}
			}
			// System.out.println(parentDataList);
		}
		else{
			parentDataList=(List<Object>)currentNode.get("requestBody");
		}
		if(currentNode.get("requestBody")==null){
			currentNode.put("requestBody", parentDataList);
		}
		if(currentNode.get("nodeType").equals("api")){
			Object tempList = (Object)apiNodeService.ApiCall(currentNode,globalData);
			List<Object> twoDList = new ArrayList<Object>();
			twoDList.add(tempList);
			currentNode.put("data", twoDList);
			singleTraversal(currentNodeId, edges);
		}
		else if(currentNode.get("nodeType").equals("filter")){
			try {
				Object tempList = (Object)filterNodeService.processJson(currentNode.get("requestBody"),(String)currentNode.get("parameter"));
				List<Object> twoDList = new ArrayList<Object>();
				twoDList.add(tempList);
				currentNode.put("data", twoDList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			singleTraversal(currentNodeId, edges);
		}
		else if(currentNode.get("nodeType").equals("conditional")){
			int path=conditionalNodeService.executeWithRule((String)currentNode.get("parameter"), currentNode.get("requestBody"));
			currentNode.put("path", path);
			currentNode.put("data", currentNode.get("requestBody"));
			multiTraversal(currentNodeId, edges, path);
		}
		else if(currentNode.get("nodeType").equals("operation")){
			Object tempList = (Object)operationNodeService.executeWithRule((String)currentNode.get("parameter"), currentNode.get("requestBody"),(Map<String, Object>)globalData);
			List<Object> twoDList = new ArrayList<Object>();
			twoDList.add(tempList);
			currentNode.put("data", tempList);
			singleTraversal(currentNodeId, edges);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object depthFirstSearch(Object payload,Object globalData) {
		this.globalData = globalData;
		Map<?,?> data=(Map<?, ?>) PayloadConverter.convertToMapOrList(payload);
		List<Map<?,?>> nodes=(List<Map<?, ?>>) PayloadConverter.convertToMapOrList(data.get("nodes"));
		List<Map<?,?>> edges=(List<Map<?, ?>>) PayloadConverter.convertToMapOrList(data.get("edges"));
		String startNodeId=(String)data.get("startNodeId");
		
		for(Map<?,?> node:nodes){
			visited.put((String)node.get("id"), false);
		}
		queue.add(startNodeId);
		visited.put(startNodeId, true);
		String nodeId="";
		List<String> flow=new ArrayList<>();
		while(!queue.isEmpty()) {
			nodeId = (String)queue.poll();
			flow.add(nodeId);
			StringBuilder parentNodeId=new StringBuilder("");
			List<String> parentNodeIds=new ArrayList<>();
			for(Map<?,?> edge: edges) {
				if(edge.get("target").equals(nodeId)) {
					parentNodeId.append(edge.get("source"));
					parentNodeIds.add((String)edge.get("source"));
				}
			}
			StringBuilder currentNodeId=new StringBuilder(nodeId);
			executeNode(currentNodeId,parentNodeId,parentNodeIds,nodes,edges);
		}
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("flow",flow);
		// for(Map<?,?> node:nodes){
		// 	node.remove("requestBody");
		// }
		returnMap.put("nodes",nodes);
		return returnMap;
	}
}
