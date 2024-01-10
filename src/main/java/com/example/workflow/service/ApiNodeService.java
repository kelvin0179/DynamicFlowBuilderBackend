package com.example.workflow.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiNodeService {

    public Object callGet(String apiUrl) {
		RestTemplate restTemplate = new RestTemplate();

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
       
        return restTemplate.getForObject(apiUrl,Object.class);
	}

    public Object callPost(Object requestBody,String apiUrl){
        RestTemplate restTemplate = new RestTemplate();

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        
        // Make a POST request to the API endpoint
        return restTemplate.postForObject(apiUrl,requestEntity,Object.class);
    }

    public Object ApiCall(Map<?,?> node){
        if(node.get("apiType").equals("GET")){
            return callGet((String)node.get("parameter"));
        }
        return callPost(node.get("requestBody"), (String)node.get("parameter"));
    }
}
