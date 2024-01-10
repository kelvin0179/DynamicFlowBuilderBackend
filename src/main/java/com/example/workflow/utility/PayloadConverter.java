package com.example.workflow.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayloadConverter {

    @SuppressWarnings("unchecked")
    public static Object convertToMapOrList(Object payload) {
        if (payload instanceof List<?>) {
            List<?> payloadList = (List<?>) payload;
            List<Map<?, ?>> convertedList = new ArrayList<>();

            for (Object item : payloadList) {
                if (item instanceof Map<?, ?>) {
                    convertedList.add((Map<?, ?>) item);
                } else {
                    // Handle other types within the list as needed
                    throw new IllegalArgumentException("Unsupported item type in the list: " + item.getClass());
                }
            }

            return convertedList;
        } else if (payload instanceof Map<?, ?>) {
            return (Map<?, ?>) payload;
        } else {
            // Handle other types as needed
            throw new IllegalArgumentException("Unsupported payload type: " + payload.getClass());
        }
    }
}
