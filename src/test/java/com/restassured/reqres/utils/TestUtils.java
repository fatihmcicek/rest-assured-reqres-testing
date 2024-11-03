package com.restassured.reqres.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestUtils {
    private static final Logger logger = LogManager.getLogger(TestUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T deserializeResponse(Response response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.getBody().asString(), clazz);
        } catch (Exception e) {
            logger.error("Error deserializing response: " + e.getMessage());
            throw new RuntimeException("Error deserializing response", e);
        }
    }

    public static void logResponse(Response response) {
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Body: " + response.getBody().asString());
    }

    public static void logRequest(String endpoint, String method, String body) {
        logger.info("Request Method: " + method);
        logger.info("Request Endpoint: " + endpoint);
        if (body != null) {
            logger.info("Request Body: " + body);
        }
    }
}