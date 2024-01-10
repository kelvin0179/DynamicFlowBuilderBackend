package com.example.workflow.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleStorageService {

    private final List<String> rules = new ArrayList<>();

    public void addRule(String rule) {
        rules.add(rule);
    }

    public List<String> getRules() {
        return rules;
    }
}

