package com.example.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OperationData {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
	
	@Lob
	@Column(length = 10000)
	private String data;
	
	@OneToOne
	private Graph graph;
}
