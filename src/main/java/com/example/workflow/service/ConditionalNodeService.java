package com.example.workflow.service;

import java.io.IOException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.workflow.model.Person;

@Service
public class ConditionalNodeService {


    @Autowired
    private KieContainer kieContainer;

    public Integer executeWithRule(String dynamicRule,Object data) {
        // Dynamically update the KieContainer with new rules
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Add static rules from resources
       kieFileSystem.delete("src/main/resources/rule1.drl");
       // Add dynamically added rules
       kieFileSystem.write("src/main/resources/rule1.drl", dynamicRule);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        
        // Dispose of the existing KieContainer and create a new one
        kieContainer.dispose();
        kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        
        KieSession kieSession=kieContainer.newKieSession();
        
        kieSession.insert(data);
        return kieSession.fireAllRules();
    }
}
