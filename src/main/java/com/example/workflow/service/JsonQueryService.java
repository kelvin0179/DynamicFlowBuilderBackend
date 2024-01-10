package com.example.workflow.service;

import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;

@Service
public class JsonQueryService {

    public Object runJsonQuery(String jsonString, String jsonQuery) {
        try {
            return JsonPath.read(jsonString, jsonQuery);
        } catch (Exception e) {
            // Handle exceptions, e.g., invalid JSON or query syntax
            e.printStackTrace();
            return null;
        }
    }
}
