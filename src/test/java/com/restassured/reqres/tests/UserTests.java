package com.restassured.reqres.tests;

import com.restassured.reqres.models.UserResponse;
import com.restassured.reqres.models.UsersListResponse;
import com.restassured.reqres.utils.TestUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

@Epic("ReqRes API Tests")
@Feature("User Management")
public class UserTests extends BaseTest {

    @Test(description = "GET - Verify Single User Details", groups = "get")
    @Story("User Retrieval")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify getting a single user details by ID")
    public void testGetSingleUser() {
        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "/2", "GET", null);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(configManager.getProperty("users.endpoint") + "/2")
                .then()
                .statusCode(200)
                .extract()
                .response();

        TestUtils.logResponse(response);
        UserResponse userResponse = TestUtils.deserializeResponse(response, UserResponse.class);

        assertNotNull(userResponse.getData());
        assertEquals(userResponse.getData().getId().intValue(), 2);
        assertNotNull(userResponse.getData().getEmail());
        assertNotNull(userResponse.getData().getFirst_name());
        assertNotNull(userResponse.getData().getLast_name());
    }

    @Test(description = "GET - Get List of Users with Pagination", groups = "get")
    @Story("User Listing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify user listing with pagination functionality")
    public void testGetUsersList() {
        int page = 2;
        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "?page=" + page, "GET", null);

        Response response = given()
                .spec(requestSpec)
                .queryParam("page", page)
                .when()
                .get(configManager.getProperty("users.endpoint"))
                .then()
                .statusCode(200)
                .extract()
                .response();

        TestUtils.logResponse(response);
        UsersListResponse usersResponse = TestUtils.deserializeResponse(response, UsersListResponse.class);

        assertNotNull(usersResponse.getData());
        assertFalse(usersResponse.getData().isEmpty());
        assertEquals(usersResponse.getPage().intValue(), page);
        assertTrue(usersResponse.getPer_page() > 0);
        assertNotNull(usersResponse.getTotal());
        assertNotNull(usersResponse.getTotal_pages());
    }

    @Test(description = "GET - Verify User Not Found Response", groups = "get")
    @Story("Error Handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify system handles non-existent user request appropriately")
    public void testGetNonExistentUser() {
        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "/23", "GET", null);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(configManager.getProperty("users.endpoint") + "/23")
                .then()
                .statusCode(404)
                .extract()
                .response();

        TestUtils.logResponse(response);
    }

    @Test(description = "POST - Create New User", groups = "post")
    @Story("User Creation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test to verify new user creation functionality with valid data")
    public void testCreateUser() {
        String requestBody = """
                {
                    "name": "Test Engineer",
                    "job": "QA Lead"
                }
                """;

        TestUtils.logRequest(configManager.getProperty("users.endpoint"), "POST", requestBody);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(configManager.getProperty("users.endpoint"))
                .then()
                .statusCode(201)
                .extract()
                .response();

        TestUtils.logResponse(response);

        assertNotNull(response.jsonPath().getString("id"));
        assertEquals(response.jsonPath().getString("name"), "Test Engineer");
        assertEquals(response.jsonPath().getString("job"), "QA Lead");
        assertNotNull(response.jsonPath().getString("createdAt"));
    }

    @Test(description = "PUT - Update Existing User", groups = "put")
    @Story("User Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify full update of existing user information")
    public void testUpdateUser() {
        String requestBody = """
                {
                    "name": "Updated Engineer",
                    "job": "Senior QA"
                }
                """;

        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "/2", "PUT", requestBody);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(configManager.getProperty("users.endpoint") + "/2")
                .then()
                .statusCode(200)
                .extract()
                .response();

        TestUtils.logResponse(response);

        assertEquals(response.jsonPath().getString("name"), "Updated Engineer");
        assertEquals(response.jsonPath().getString("job"), "Senior QA");
        assertNotNull(response.jsonPath().getString("updatedAt"));
    }

    @Test(description = "PATCH - Partially Update User", groups = "patch")
    @Story("User Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify partial update of user information")
    public void testPartialUpdateUser() {
        String requestBody = """
                {
                    "job": "Team Lead"
                }
                """;

        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "/2", "PATCH", requestBody);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .patch(configManager.getProperty("users.endpoint") + "/2")
                .then()
                .statusCode(200)
                .extract()
                .response();

        TestUtils.logResponse(response);

        assertEquals(response.jsonPath().getString("job"), "Team Lead");
        assertNotNull(response.jsonPath().getString("updatedAt"));
    }

    @Test(description = "DELETE - Remove User", groups = "delete")
    @Story("User Deletion")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify user deletion functionality")
    public void testDeleteUser() {
        TestUtils.logRequest(configManager.getProperty("users.endpoint") + "/2", "DELETE", null);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(configManager.getProperty("users.endpoint") + "/2")
                .then()
                .statusCode(204)
                .extract()
                .response();

        TestUtils.logResponse(response);
    }
}