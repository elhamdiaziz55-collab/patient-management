import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll // it will run before all the tests
    static void setUp(){
        RestAssured.baseURI = "http://localhost:4004";
    }
    // we should follow three steps (1. Arrange , 2. act , 3. assert)
    @Test
    public void shouldReturnOkWithValidToken(){

        String loginPayload = """
                   {
                       "email"  : "testuser@test.com",
                       "password": "password123"
                   }
                """;


        Response response = given()
                .contentType("application/json")
                .body(loginPayload) // 1.
                .when() // 2.
                .post("/auth/login")
                .then() // 3.
                .statusCode(200) //first assertion
                .body("token",notNullValue()) //second assertion
                .extract().response();
        System.out.println("Generated Token: " + response.jsonPath().getString("token"));
        System.out.println("Real Status Code: " + response.getStatusCode());
    }



    @Test
    public void shouldReturnUnauthorizedWithInvalidToken(){

        String loginPayload = """
                   {
                       "email"  : "invalid_user@test.com",
                       "password": "wrong_password123"
                   }
                """;

        given()
                .contentType("application/json")
                .body(loginPayload) // 1.
                .when() // 2.
                .post("/auth/login")
                .then() // 3.
                .statusCode(401) ;
    }
}
