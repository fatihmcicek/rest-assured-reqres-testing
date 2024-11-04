package com.restassured.reqres.tests;

import com.restassured.reqres.utils.TestUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

@Epic("ReqRes API Tests")
@Feature("Negative User Management Tests")
public class NegativeUserTests extends BaseTest {

    @Test(description = "GET - Verify Non-Existent User Returns 404", groups = "negative")
    @Story("Error Handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify system properly handles requests for non-existent users")
    public void testGetNonExistentUser() {
        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "/999", "GET", null);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(configManager.getProperty("users.endpoint") + "/999")
                .then()
                .statusCode(404)
                .extract()
                .response();

        TestUtils.logResponse(response);
    }

    @Test(description = "POST - Verify User Creation With Invalid Data", groups = "negative")
    @Story("Error Handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify system properly handles user creation with invalid data")
    public void testCreateUserWithInvalidData() {
        String requestBody = """
                {
                    "name": "",
                    "job": ""
                }
                """;

        TestUtils.logRequest(configManager.getProperty("users.endpoint"), "POST", requestBody);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(configManager.getProperty("users.endpoint"))
                .then()
                .statusCode(400)
                .extract()
                .response();

        TestUtils.logResponse(response);
    }

    @Test(description = "POST - Verify Login With Invalid Credentials", groups = "negative")
    @Story("Error Handling")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify system properly handles login attempts with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        String requestBody = """
                {
                    "email": "invalid@email.com",
                    "password": "wrongpassword"
                }
                """;

        TestUtils.logRequest(configManager.getProperty("login.endpoint"), "POST", requestBody);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(configManager.getProperty("login.endpoint"))
                .then()
                .statusCode(400)
                .extract()
                .response();

        TestUtils.logResponse(response);
        assertEquals(response.jsonPath().getString("error"), "user not found");
    }
}