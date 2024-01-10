package com.example.workflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
public class ApiNodeTestController {
	
	@GetMapping("/callAnotherApi")
	public Object callAnotherApi() {
		RestTemplate restTemplate = new RestTemplate();

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        String apiUrl="http://localhost:8081/api/carriers";
        // Make a POST request to the API endpoint
        return restTemplate.getForObject(apiUrl,Object.class);
	}
}
