package com.example.workflow.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.ObjRecord;
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

    @SuppressWarnings("unchecked")
	public Object callPost(Object requestBody,String apiUrl,Object globalData){
        RestTemplate restTemplate = new RestTemplate();

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        ((List<Object>)requestBody).add(globalData);
        // Make a POST request to the API endpoint
        return restTemplate.postForObject(apiUrl,requestEntity,Object.class);
    }

    public Object ApiCall(Map<?,?> node,Object globalData){
        if(node.get("apiType").equals("GET")){
            return callGet((String)node.get("parameter"));
        }
        return callPost(node.get("requestBody"), (String)node.get("parameter"),globalData);
    }
}
