package com.example.workflow.controller;

import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.workflow.model.DataWrapper;
import com.example.workflow.model.Person;
import com.example.workflow.service.DynamicRuleService;

import java.util.List;
import java.util.Map;

@RestController
public class DroolsController {

	@Autowired
    private KieBase kieBase;
	
	@Autowired
    private DynamicRuleService dynamicRuleService;

    @PostMapping("/process-list")
    public String processList(@RequestBody List<Map<?, ?>> dataList) {
        // Create a new KieSession for each request
        KieSession kieSession = kieBase.newKieSession();
        try {
            // Insert the list of maps into the Drools session
            for (Map<?, ?> data : dataList) {
                kieSession.insert(data);
            }

            // Fire all rules
            int rulesFired = kieSession.fireAllRules();

            return "Rules executed. " + rulesFired + " rule(s) fired.";
        }
        finally{
            kieSession.dispose();
        }
        // The kieSession is automatically disposed when try-with-resources block exits
    }
    @PostMapping("/process-persons")
    public String processPersons(@RequestBody List<Map<?,?>> persons) {
        KieSession kieSession = kieBase.newKieSession();
        try {
            // Insert the list of persons into the Drools session
//            for (Person person : persons) {
//                kieSession.insert(person);
//            }
//
//            // Fire all rules
//            int rulesFired = kieSession.fireAllRules();
//
//            return "Rules executed. " + rulesFired + " rule(s) fired.";
        	
        	DataWrapper dataWrapper=DataWrapper.builder().dataList(persons).build();
        	kieSession.insert(persons);
        	int rulesFired = kieSession.fireAllRules();
        	//
        	return "Rules executed. " + rulesFired + " rule(s) fired.";
        	
        } finally {
            // Reset the session to clear inserted facts
            kieSession.dispose();
        }
    }
    @PostMapping("/add-rule")
    public void addRule(@RequestBody String rule) {
//    	System.out.println(rule);
        dynamicRuleService.addDynamicRule(rule);
    }
}
