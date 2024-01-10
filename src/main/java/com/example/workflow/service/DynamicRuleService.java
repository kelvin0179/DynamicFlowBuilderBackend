package com.example.workflow.service;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.definition.rule.Rule;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.example.workflow.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicRuleService {

    @Autowired
    private KieContainer kieContainer;
    
    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    private static List<String> dynamicRules = new ArrayList<>();

    public void addDynamicRule(String rule) {
//    	String rule1 = "import com.example.workflow.model.Person;\n" +
//                "rule 'GoodQualityRule'\n" +
//                "when\n" +
//                "    $person: Person(quality != null, quality.value == 'Good')\n" +
//                "then\n" +
//                "    System.out.println($person.getName() + ' has a Good quality.');\n" +
//                "end";
//    	System.out.println(rule1);
//        dynamicRules.add(rule1);
        reloadKieContainer();
    }

//    public void fireDynamicRules(List<Object> facts) {
//        KieSession kieSession = kieContainer.newKieSession();
//        try {
//            for (Object fact : facts) {
//                kieSession.insert(fact);
//            }
//
//            // Fire the dynamically added rules
//            int rulesFired = kieSession.fireAllRules();
//
//            System.out.println("Dynamic Rules executed. " + rulesFired + " rule(s) fired.");
//        } finally {
//            kieSession.dispose();
//        }
//    }

    private void reloadKieContainer() {
        // Dynamically update the KieContainer with new rules
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Add static rules from resources
        org.springframework.core.io.Resource[] drlResources;
        try {
            drlResources = resourcePatternResolver.getResources("file:C:/Projects/dynamic.drl");
            for (org.springframework.core.io.Resource drlResource : drlResources) {
                String ruleFileName = drlResource.getFilename();
                System.out.println("Adding rule file: " + ruleFileName);

                Resource resource = kieServices.getResources().newFileSystemResource(drlResource.getFile(), "UTF-8");
                kieFileSystem.write(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        kieFileSystem.delete("src/main/resources/dynamicRule.drl");
//        // Add dynamically added rules
//        for (String dynamicRule : dynamicRules) {
//            kieFileSystem.write("src/main/resources/dynamicRule.drl", dynamicRule);
//        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        
        // Dispose of the existing KieContainer and create a new one
        kieContainer.dispose();
        kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        
        KieSession kieSession=kieContainer.newKieSession();
        
        Person person = new Person();
        person.setName("Mohit");

        Person.Quality quality = new Person.Quality();
        quality.setValue("Good");

        person.setQuality(quality);
        
        kieSession.insert(person);
        kieSession.fireAllRules();
        
    }

}
