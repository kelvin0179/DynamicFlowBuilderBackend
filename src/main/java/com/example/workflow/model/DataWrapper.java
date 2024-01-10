package com.example.workflow.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class DataWrapper {
    private List<Map<?, ?>> dataList;
}

