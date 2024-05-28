package api.test;


import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import api.utilities.ScreenshotListener;
import io.restassured.RestAssured;

@Listeners({ScreenshotListener.class})
public class deleteInvalidTest {

	public Logger logger;
	@BeforeClass
	public void setup() throws IOException {
		RestAssured.baseURI = "https://userserviceapi-a54ceee3346a.herokuapp.com/uap/";
		logger=LogManager.getLogger(this.getClass());
		logger.debug("debugging............");

	}



	@DataProvider(name = "invalidUserIds")
	public Object[][] invalidUserIds() {
		logger.info("********* invalidUserIdsDELETE*************");
		return new Object[][] {
			{ -999654321 }, // invalid user ID 1
			{1234567895},// invalid user ID 2
		};
	}

	@Test(dataProvider = "invalidUserIds")
	public void testGetUserWithInvalidId(int invalidUserId) 
	{
		logger.info("********* testGetUserWithInvalidIdDELETE*************");
		// Perform the API request and validate the response
		given().log().all().auth().basic("Numpy@gmail.com", "api@123")
		.pathParam("userId", invalidUserId)
		.when()

		.delete("deleteuser/{userId}").then().log().all()

		.assertThat()
		.statusCode(404); // Assuming 404 is the expected status code for "not found" scenario
	}


	@DataProvider(name = "invalidUserIdsAN")
	public Object[][] invalidUserIdsAlphaNum() {
		logger.info("********* invalidUserIdsAlphaNumDELETE*************");
		return new Object[][] {
			// invalid user ID 1
			{ "Abc" },
			{"A-12"},
		};
	}

	@Test(dataProvider = "invalidUserIdsAN")
	public void testGetUserWithInvalidIdAlphaNum(String invalidUserIdsAlphaNum) {
		logger.info("********* testGetUserWithInvalidIdAlphaNumDELETE*************");
		given().log().all().auth().basic("Numpy@gmail.com", "api@123")
		.pathParam("userfirstname", invalidUserIdsAlphaNum).
		when()
		.delete("deleteuser/username/{userfirstname}").then().log().all()

		.assertThat()
		.statusCode(404); //
	}

}
