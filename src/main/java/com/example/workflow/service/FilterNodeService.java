package com.example.workflow.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FilterNodeService {
	
	@Autowired
	JsonQueryService jsonQueryService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public Object processJson(@RequestBody Object jsonData,String jsonQuery) throws JsonProcessingException {
        String jsonString;
//        String jsonQuery = "$[?(@.quality.value=='Good')]";

        if (jsonData instanceof Map) {
            // Case: Single object
            jsonString = objectMapper.writeValueAsString((Map<?, ?>) jsonData);
        } else if (jsonData instanceof List) {
            // Case: List of objects
            jsonString = objectMapper.writeValueAsString((List<Map<?, ?>>) jsonData);
        } else {
            // Handle other cases or return an appropriate response
            return jsonData;
        }
        return jsonQueryService.runJsonQuery(jsonString, jsonQuery);
    }
}
