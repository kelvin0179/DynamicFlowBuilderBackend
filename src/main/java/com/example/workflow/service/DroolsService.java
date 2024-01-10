package com.example.workflow.service;

import org.kie.api.KieBase;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroolsService {
	
	@Autowired
    private KieBase kieBase;

    public void executeRules(Object yourDataObject) {
        StatelessKieSession kieSession = kieBase.newStatelessKieSession();
        kieSession.execute(yourDataObject);
    }
}

