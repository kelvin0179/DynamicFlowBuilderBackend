package com.example.workflow.service;

import java.util.Map;

public class JsonResponse {

    private String message;
    private Map<?, ?> data;

    // Constructors, getters, and setters

    public JsonResponse(String message, Map<?, ?> data) {
        this.message = message;
        this.data = data;
    }

    // Additional constructors, getters, and setters
}
