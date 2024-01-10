package com.example.workflow.config;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.example.workflow.service.RuleStorageService;
import org.kie.api.io.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class DroolsConfig {

    // @Autowired
    // private RuleStorageService ruleStorageService;

	@Autowired
    private ResourcePatternResolver resourcePatternResolver;

	@Bean
    public KieContainer kieContainer() throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Scan for DRL files directly in the src/main/resources/ directory
        org.springframework.core.io.Resource[] drlResources = resourcePatternResolver.getResources("classpath*:/*.drl");

        // Add each DRL file to the KieFileSystem
        for (org.springframework.core.io.Resource drlResource : drlResources) {
            String ruleFileName = drlResource.getFilename();
            System.out.println("Adding rule file: " + ruleFileName);

            Resource resource = kieServices.getResources().newFileSystemResource(drlResource.getFile(), "UTF-8");
            kieFileSystem.write(resource);
        }
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Bean
    public KieSession kieSession(KieContainer kieContainer) {
        KieSession kieSession = kieContainer.newKieSession();
        // Create an entry point named "persons"
        return kieSession;
    }
    @Bean
    public KieBase kieBase(KieContainer kieContainer) {
        KieBaseConfiguration kieBaseConfiguration = KieServices.Factory.get().newKieBaseConfiguration();
        return kieContainer.newKieBase(kieBaseConfiguration);
    }
}
