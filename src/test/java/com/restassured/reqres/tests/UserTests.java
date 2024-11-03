package com.restassured.reqres.tests;

import com.restassured.reqres.models.User;
import com.restassured.reqres.models.UserResponse;
import com.restassured.reqres.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class UserTests extends BaseTest {

    @Test(description = "Get Single User Test")
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
}