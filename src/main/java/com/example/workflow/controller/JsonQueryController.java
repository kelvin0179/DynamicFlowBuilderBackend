package com.example.workflow.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.workflow.service.JsonQueryService;
import com.example.workflow.service.JsonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class JsonQueryController {
	
	@Autowired
	private JsonQueryService jsonQueryService;

	@Autowired
	private ObjectMapper objectMapper;
	
    @PostMapping("/run-json-query")
    public Object runJsonQuery(@RequestBody Map<?, ?> jsonRequest) {
        String jsonString = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        String jsonQuery = "$.name"; // Replace with your JSON query

        return jsonQueryService.runJsonQuery(jsonString, jsonQuery);
    }
    
    @PostMapping("/process-nested-json")
    public Object processJson(@RequestBody Object jsonData) throws JsonProcessingException {
        String jsonString;
        String jsonQuery = "$[?(@.quality.value=='Good')]";

        if (jsonData instanceof Map) {
            // Case: Single object
            jsonString = objectMapper.writeValueAsString((Map<?, ?>) jsonData);
        } else if (jsonData instanceof List) {
            // Case: List of objects
            jsonString = objectMapper.writeValueAsString((List<Map<?, ?>>) jsonData);
        } else {
            // Handle other cases or return an appropriate response
            return null;
        }

        return jsonQueryService.runJsonQuery(jsonString, jsonQuery);
    }
}
