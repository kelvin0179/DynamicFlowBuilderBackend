package com.example.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workflow.model.OperationData;

public interface OperationDataRepository extends JpaRepository<OperationData, Integer>{

}
