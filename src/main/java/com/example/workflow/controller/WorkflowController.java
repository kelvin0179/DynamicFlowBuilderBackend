package com.example.workflow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.workflow.model.Graph;
import com.example.workflow.repository.GraphRepository;
import com.example.workflow.service.WorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "*")
public class WorkflowController {
	
	@Autowired
	WorkflowService workflowService;
	
	@Autowired
    private GraphRepository graphRepository;

    @Autowired
    private ObjectMapper objectMapper;
	
	// @PostMapping("/dfs")
	// public Object dfs(@RequestBody Object object) {
	// 	return workflowService.depthFirstSearch(object);
	// }

	@SuppressWarnings("unchecked")
	@PostMapping("/saveGraph")
    public ResponseEntity<String> saveGraph(@RequestBody Object dynamicObject) {
        try {
            // Convert the dynamic object to a JSON string
            String jsonString = objectMapper.writeValueAsString(dynamicObject);
            // Create a new Graph entity and set the 'graph' field
            Graph graph = Graph.builder().graph(jsonString)
            		.name((String)((Map<String, Object>) dynamicObject).get("name"))
            		.build();

            // Save the entity in the database
            graphRepository.save(graph);

            return new ResponseEntity<>("Graph saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving graph", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	@SuppressWarnings("unchecked")
	@PostMapping("/saveGraph/{id}")
    public ResponseEntity<String> saveGraph(@RequestBody Object dynamicObject,@PathVariable("id") Integer id) {
        try {
            // Convert the dynamic object to a JSON string
            String jsonString = objectMapper.writeValueAsString(dynamicObject);
			// System.out.println(jsonString);
            // Create a new Graph entity and set the 'graph' field
			Graph graph=graphRepository.findById(id).get();
			graph.setGraph(jsonString);
			graph.setName((String)((Map<String, Object>) dynamicObject).get("name"));
            // Graph graph = Graph.builder().graph(jsonString)
            // 		.name((String)((Map<String, Object>) dynamicObject).get("name"))
            // 		.build();

            // Save the entity in the database
			graph.setId(id);
            graphRepository.saveAndFlush(graph);

            return new ResponseEntity<>("Graph saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving graph", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@SuppressWarnings("unchecked")
	@PostMapping("/runGraph")
	public ResponseEntity<Object> runGraph(@RequestBody Object object) throws JsonMappingException, JsonProcessingException{
		Map<String,Object> dataObject = (Map<String,Object>)object;
		Object globalData=dataObject.get("data");
		Object graph=objectMapper.readValue((String)((Map<String,Object>)dataObject.get("graph")).get("graph"), Object.class);
		return ResponseEntity.ok(workflowService.depthFirstSearch(graph,globalData));
	}
	

	@GetMapping("/workflowData")
	public ResponseEntity<List<Graph>> getMethodName() {
		return ResponseEntity.ok(graphRepository.findAll());
	}
	
	@GetMapping("/workflowData/{id}")
	public ResponseEntity<Object> getMethodName(@PathVariable("id") Integer id) throws JsonMappingException, JsonProcessingException {
		Object graphData=objectMapper.readValue((String)graphRepository.findById(id).get().getGraph(), Object.class);
		return ResponseEntity.ok(graphData);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/getMaxNodeId/{id}")
	public Integer getMaxNodeId(@PathVariable("id") Integer id) throws JsonMappingException, JsonProcessingException {
		Object graphData=objectMapper.readValue((String)graphRepository.findById(id).get().getGraph(), Object.class);
		List<Map<String, Object>> nodes=(List<Map<String, Object>>) ((Map<String, Object>) graphData).get("nodes");
		int nodeId=0;
		for(Map<String,Object> node:nodes) {
			nodeId=Math.max(nodeId, Integer.parseInt((String)node.get("id")));
		}
		return nodeId+1;
	}
	
	
}
