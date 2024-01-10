package com.example.workflow.model;

import lombok.Data;

@Data
public class Person {
    private String name;
    private Quality quality;

    @Data
    public static class Quality {
        private String value;
    }
}

