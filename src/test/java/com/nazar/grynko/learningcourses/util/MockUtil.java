package com.nazar.grynko.learningcourses.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MockUtil {

    public static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
