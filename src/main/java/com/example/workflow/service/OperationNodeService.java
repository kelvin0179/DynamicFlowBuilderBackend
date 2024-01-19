package com.example.workflow.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationNodeService {

    public <T extends Serializable> T deepCopy(Object data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(data);
            out.flush();
            out.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            @SuppressWarnings("unchecked")
            T copiedObject = (T) in.readObject();
            in.close();

            return copiedObject;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }
    @Autowired
    private KieContainer kieContainer;

    public Object executeWithRule(String dynamicRule,Object incoming,Map<String,Object> globalData) {
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
        kieSession.setGlobal("globalData", globalData);
        Object data=deepCopy(incoming);
        kieSession.insert(data);
        int rules=kieSession.fireAllRules();
        System.out.println(rules);
        kieSession.dispose();
        return data;
    }
}
