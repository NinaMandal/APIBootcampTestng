package api.test;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class getInvalidTest {
	
	public Logger logger;
	
	@BeforeClass
	public void setupData() throws IOException {
		
		logger=LogManager.getLogger(this.getClass());
		logger.debug("debugging............");


	}

	@Test
	public void testGetUserWithoneInvalidId() {
		logger.info("********* testGetUserWithoneInvalidId*************");
		// Set base URI for the API
		RestAssured.baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap/";

		// Define the invalid user ID
		int invalidUserId = 99000; // assuming this ID is invalid

		given().log().all().auth().basic("Numpy@gmail.com", "api@123")

		// Perform the API request and validate the response

		.pathParam("userId", invalidUserId).when().get("user/{userId}").then().log().all().assertThat()
		.statusCode(404); // Assuming 404 is the expected status code for "not found" scenario
	}

	@DataProvider(name = "invalidUserIds")
	public Object[][] invalidUserIds() {
		logger.info("********* invalidUserIds*************");
		return new Object[][] { { 9999 }, // invalid user ID 1
			{ 123456789 },// invalid user ID 2
			// Add more invalid user IDs here
		};
	}

	@Test(dataProvider = "invalidUserIds")
	public void testGetUserWithInvalidId(int invalidUserId) {
		logger.info("********* testGetUserWithInvalidId*************");
		// Set base URI for the API
		RestAssured.baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap/";

		// Perform the API request and validate the response
		given().log().all().auth().basic("Numpy@gmail.com", "api@123").pathParam("userId", invalidUserId).when()
		.get("/user/{userId}").then().log().all().assertThat().statusCode(404); // Assuming 404 is the expected
		// status code for "not found"
		// scenario
	}

	@DataProvider(name = "invalidUserIdsAN")
	public Object[][] invalidUserIdsAlphaNum() {
		logger.info("********* invalidUserIdsAlphaNum*************");
		return new Object[][] {
			// invalid user ID 1
			{ "Abc" }, { "A-12" },
			// invalid user ID 2
			// Add more invalid user IDs here
		};
	}

	@Test(dataProvider = "invalidUserIdsAN")
	public void testGetUserWithInvalidIdAlphaNum(String invalidUserIdsAlphaNum) {
		logger.info("********* testGetUserWithInvalidIdAlphaNum*************");
		// Set base URI for the API
		RestAssured.baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap/";

		// Perform the API request and validate the response
		given().log().all().auth().basic("Numpy@gmail.com", "api@123").pathParam("userId", invalidUserIdsAlphaNum)
		.when().get("/user/{userId}").then().log().all().assertThat().statusCode(400); // Assuming 404 is the
		// expected status code
		// for "not found"
		// scenario
	}

	// Add user---->Update user----->get user

}
