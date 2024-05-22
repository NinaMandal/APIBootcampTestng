package api.test;


import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.endpoints.UserEndPoints;
import api.endpoints.UserEndPoints2;
import api.payload.User;
import api.payload.User.Address;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserTests2 {

	User userPayload;
	int id;
	
	public Logger logger;

	@BeforeClass
	public void setupDataCreate() throws IOException {
		File f = new File(".\\testData\\test_data.json");
		FileReader fr = new FileReader(f);
		JSONTokener jt = new JSONTokener(fr);

		JSONObject data = new JSONObject(jt);
		userPayload= createPayload(data);
		
		logger=LogManager.getLogger(this.getClass());
		logger.debug("debugging............");

		// Create a User object and populate its fields from the JSON data

	}

	public User createPayload( JSONObject data ) {
		userPayload = new User(
				data.getString("user_first_name"),
				data.getString("user_last_name"),
				data.getLong("user_contact_number"),
				data.getString("user_email_id"),
				buildAddressObject(data.getJSONObject("userAddress"))
				);



		return userPayload;

	}
	private User.Address buildAddressObject(JSONObject addressData) {
		return new User.Address(
				addressData.getString("plotNumber"),
				addressData.getString("street"),
				addressData.getString("state"),
				addressData.getString("country"),
				addressData.getLong("zipCode")
				);


	}

	@Test(priority =1)
	public void testCreateUserObject(ITestContext context) {
		logger.info("********* Creating user*************");
		// Use the populated userPayload object in your tests
		Response response = UserEndPoints2.createUser(userPayload);
		String responseBody = response.asString();
		System.out.println("MYYY CODEEEEUU"+ responseBody);
		response.then().log().all();

		// Assertions using TestNG's Assert class
		Assert.assertEquals(response.getStatusCode(), 201, "Unexpected status code"); // Assert status code with custom error message

		// Assert specific fields using JSON path
		JsonPath jsonPath = response.jsonPath();

		JsonPath js = new JsonPath(responseBody);// For parsing json
		id = js.getInt("user_id");// It will extract int from path user id
		String username = js.getString("user_first_name");

		//setting userid and user name to context
		context.setAttribute("user_id", id);
		context.setAttribute("user_first_name", username);
		
		Assert.assertEquals(jsonPath.getString("user_first_name"), userPayload.getUser_first_name(), "First name mismatch");
		Assert.assertEquals(jsonPath.getString("user_last_name"), userPayload.getUser_last_name(), "Last name mismatch");
		Assert.assertEquals(jsonPath.getLong("user_contact_number"), userPayload.getUser_contact_number().longValue(), "contact number mismatchr");
		Assert.assertEquals(jsonPath.getString("user_email_id"), userPayload.getUser_email_id(), "user_email_id");
		Assert.assertEquals(jsonPath.getString("userAddress.plotNumber"), userPayload.getUserAddress().getPlotNumber(), "Plot number mismatch");
		Assert.assertEquals(jsonPath.getString("userAddress.street"), userPayload.getUserAddress().getStreet(), "street mismatch");
		Assert.assertEquals(jsonPath.getString("userAddress.state"), userPayload.getUserAddress().getState(), "state name mismatch");
		Assert.assertEquals(jsonPath.getString("userAddress.country"), userPayload.getUserAddress().getCountry(), "country name mismatch");
		Assert.assertEquals(jsonPath.getLong("userAddress.zipCode"), userPayload.getUserAddress().getZipCode().longValue(), "zipcode mismatch");

		// Log entire response for debugging
		System.out.println("Response body:\n" + responseBody);
	}

	@Test(priority = 2)
	public void testGetById(ITestContext context) throws IOException {
		
		logger.info("********* Get user by ID*************");
		int userId = (int) context.getAttribute("user_id");
		Response response = UserEndPoints2.getUserById(userId);
		response.then().log().all();


	}


	@Test(priority = 3)
	public void testGetUserByUserName(ITestContext context) throws IOException {

		//int userId = (int) context.getAttribute("user_id");
		String username = (String) context.getAttribute("user_first_name");
		Response response = UserEndPoints2.getUserByName(username);

		response.then().log().all();

	}

	@Test(priority =4)
	public void testUpdateUserObject(ITestContext context) throws JsonParseException, JsonMappingException, IOException {

		int userId = (int) context.getAttribute("user_id");

		File f = new File(".\\testData\\update.json");
		FileReader fr = new FileReader(f);
		JSONTokener jt = new JSONTokener(fr);

		JSONObject data = new JSONObject(jt);
		userPayload= createPayload(data);


		Response response = UserEndPoints2.updateUser(userPayload,userId);
		String responseBody = response.asString();


		response
		.then().log().all();
		JsonPath jsonPath = response.jsonPath();

		JsonPath js = new JsonPath(responseBody);// For parsing json

		Assert.assertEquals(jsonPath.getString("user_first_name"), userPayload.getUser_first_name(), "First name mismatch");
		Assert.assertEquals(jsonPath.getString("user_last_name"), userPayload.getUser_last_name(), "Last name mismatch");
		Assert.assertEquals(jsonPath.getLong("user_contact_number"), userPayload.getUser_contact_number().longValue(), "contact number mismatchr");
		Assert.assertEquals(jsonPath.getString("user_email_id"), userPayload.getUser_email_id(), "user_email_id");
		Assert.assertEquals(jsonPath.getString("userAddress.plotNumber"), userPayload.getUserAddress().getPlotNumber(), "Plot number mismatch");
		Assert.assertEquals(jsonPath.getString("userAddress.street"), userPayload.getUserAddress().getStreet(), "street mismatch");
		Assert.assertEquals(jsonPath.getString("userAddress.state"), userPayload.getUserAddress().getState(), "state name mismatch");
		Assert.assertEquals(jsonPath.getString("userAddress.country"), userPayload.getUserAddress().getCountry(), "country name mismatch");
		Assert.assertEquals(jsonPath.getLong("userAddress.zipCode"), userPayload.getUserAddress().getZipCode().longValue(), "zipcode mismatch");
	}

	@Test(priority =5)
	public void testDeleteUserByIDObject(ITestContext context) {
		int userId = (int) context.getAttribute("user_id");
		Response response = UserEndPoints2.deleteUser(userId);
		response.then().log().all();

	}



}