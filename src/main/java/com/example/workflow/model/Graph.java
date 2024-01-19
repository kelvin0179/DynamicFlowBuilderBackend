package com.example.workflow.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Graph {
    @Id
	@Column(nullable = false)
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    private String name;
    
    @Lob
    @Column(length = 10000)
    private String graph;
}
