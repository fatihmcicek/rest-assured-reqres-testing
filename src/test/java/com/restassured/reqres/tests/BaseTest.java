package com.restassured.reqres.tests;

import com.restassured.reqres.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected RequestSpecification requestSpec;
    protected ConfigManager configManager;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        configManager = ConfigManager.getInstance();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(configManager.getProperty("base.url"))
                .setContentType(ContentType.JSON)
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}