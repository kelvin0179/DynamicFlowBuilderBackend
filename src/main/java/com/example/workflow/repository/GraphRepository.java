package com.example.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workflow.model.Graph;

public interface GraphRepository extends JpaRepository<Graph, Integer>{

}
