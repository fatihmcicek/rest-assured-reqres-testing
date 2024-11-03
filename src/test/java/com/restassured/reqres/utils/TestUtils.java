package com.restassured.reqres.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestUtils {
    private static final Logger logger = LogManager.getLogger(TestUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Step("Making {method} request to {endpoint}")
    public static void logRequest(String endpoint, String method, String body) {
        logger.info("Request Method: " + method);
        logger.info("Request Endpoint: " + endpoint);
        if (body != null) {
            logger.info("Request Body: " + body);
            saveRequestBody(body);
        }
    }

    @Step("Received response with status code: {response.statusCode}")
    public static void logResponse(Response response) {
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Body: " + response.getBody().asString());
        saveResponseBody(response.getBody().asString());
    }

    @Step("Deserializing response to {clazz.getSimpleName()}")
    public static <T> T deserializeResponse(Response response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.getBody().asString(), clazz);
        } catch (Exception e) {
            logger.error("Error deserializing response: " + e.getMessage());
            saveErrorLog("Deserialization error: " + e.getMessage());
            throw new RuntimeException("Error deserializing response", e);
        }
    }

    @Attachment(value = "Request Body", type = "application/json")
    private static String saveRequestBody(String requestBody) {
        return requestBody;
    }

    @Attachment(value = "Response Body", type = "application/json")
    private static String saveResponseBody(String responseBody) {
        return responseBody;
    }

    @Attachment(value = "Error Log", type = "text/plain")
    private static String saveErrorLog(String message) {
        return message;
    }

    @Step("Validating response contains field: {fieldName}")
    public static void validateResponseField(Response response, String fieldName) {
        String value = response.jsonPath().getString(fieldName);
        logger.info("Validating field: " + fieldName + " with value: " + value);
        if (value == null) {
            saveErrorLog("Field validation failed: " + fieldName + " is null");
            throw new AssertionError("Field " + fieldName + " is missing in response");
        }
    }

    @Step("Validating response status code is: {expectedStatusCode}")
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        logger.info("Validating status code. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
        if (actualStatusCode != expectedStatusCode) {
            saveErrorLog("Status code validation failed. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            throw new AssertionError("Expected status code: " + expectedStatusCode + " but got: " + actualStatusCode);
        }
    }
}