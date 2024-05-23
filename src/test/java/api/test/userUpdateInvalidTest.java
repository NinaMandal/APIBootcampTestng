package api.test;

import java.io.File;
import java.io.FileNotFoundException;

import org.testng.asserts.SoftAssert;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class userUpdateInvalidTest {

    User userPayload;
    int id;
    int expectedStatusCode;
    public Logger logger;

    @BeforeClass
    public void setupData() throws IOException {
        File f = new File(".\\src\\test\\resources\\invalidDataProvider.json");
        FileReader fr = new FileReader(f);
        JSONTokener jt = new JSONTokener(fr);

        JSONObject data = new JSONObject(jt);
        userPayload = createPayload(data);
        logger=LogManager.getLogger(this.getClass());
		logger.debug("debugging............");

    }

    public User createPayload(JSONObject data) {
        try {
            userPayload = new User(
                    data.getString("user_first_name"),
                    data.getString("user_last_name"),
                    data.optLong("user_contact_number",0),
                    data.getString("user_email_id"),
                    buildAddressObject(data.getJSONObject("userAddress"))
            );
        } catch (NumberFormatException | JSONException e) {
            // If parsing fails, set status code to 400
            expectedStatusCode = 400;
        }

        return userPayload;
    }

    private User.Address buildAddressObject(JSONObject addressData) {
        return new User.Address(
                addressData.getString("plotNumber"),
                addressData.getString("street"),
                addressData.getString("state"),
                addressData.getString("country"),
                addressData.optLong("zipCode", 0)
        );
    }

   @Test(priority = 1)
    public void testUsingUserObject(ITestContext context) throws FileNotFoundException {
    	logger.info("********* Creating user*************");
    	
    	
    	File f = new File(".\\testData\\test_data.json");
		FileReader fr = new FileReader(f);
		JSONTokener jt = new JSONTokener(fr);

		JSONObject data = new JSONObject(jt);
		userPayload= createPayload(data);
		
		// Use the populated userPayload object in your tests
		Response response = UserEndPoints2.createUser(userPayload);
		String responseBody = response.asString();
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

    @Test(priority = 1)
    public void testPutRequests(ITestContext context) {
        try {
        	logger.info("********* testPutRequests*************");
            File file = new File(".\\src\\test\\resources\\invalidDataProvider.json");
            FileReader fr = new FileReader(file);
            JSONTokener jt = new JSONTokener(fr);
            JSONObject data = new JSONObject(jt);

            JSONArray scenarios = data.getJSONArray("scenarios");

            id = (int) context.getAttribute("user_id");

            for (int i = 0; i < scenarios.length(); i++) {
                JSONObject scenario = scenarios.getJSONObject(i);
                performPutRequest(scenario,id );
                System.out.println(i);
            }

            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performPutRequest(JSONObject scenario, int id) {
    	logger.info("********* performUpdateRequest*************");
    	SoftAssert softAssert = new SoftAssert();
        JSONObject requestBody = createRequestBody(scenario);

        userPayload = createPayload(requestBody);

        expectedStatusCode = scenario.getInt("expectedStatus");

        Response response = UserEndPoints2.updateUser(userPayload, id);
        String responseBody = response.asString();

        response.then().log().all();
        JsonPath jsonPath = response.jsonPath();

        int actualStatusCode = response.getStatusCode();

        softAssert.assertEquals(actualStatusCode, expectedStatusCode,
                "Expected status to be " + expectedStatusCode + " but found: " + actualStatusCode);
    }

    private JSONObject createRequestBody(JSONObject scenario) {
    	logger.info("********* createRequestBodyUpdateRequest*************");
        JSONObject requestBody = new JSONObject();
        try {
            for (String key : scenario.keySet()) {
                if (!key.equals("scenario_name") && !key.equals("expectedStatus")) {
                    requestBody.put(key, scenario.get(key));
                }
            }
        } catch (NumberFormatException | JSONException e) {
            // If parsing fails, set status code to 400
            expectedStatusCode = 400;
        }
        return requestBody;
    }
    
    

	@Test(priority =2)
	public void testDeleteUserByIDObject(ITestContext context) {
		int userId = (int) context.getAttribute("user_id");
		Response response = UserEndPoints2.deleteUser(userId);
		response.then().log().all();

	}
}