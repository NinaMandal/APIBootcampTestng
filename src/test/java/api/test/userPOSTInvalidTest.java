package api.test;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import api.utilities.ScreenshotListener;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import org.apache.commons.lang3.exception.ExceptionUtils; // for better exception handling


@Listeners({ScreenshotListener.class})
public class userPOSTInvalidTest {
	
	
	User userPayload;
    int id;
    int expectedStatusCode;
    public Logger logger;
    
	@BeforeClass
	public void setupData() throws IOException {
		
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
	    public void testPOSTInvalidRequests() {
	        try {
	            logger.info("********* Creating POSTInvalidRequests*************");
	            File file = new File(".\\src\\test\\resources\\invalidDataProvider.json");
	            FileReader fr = new FileReader(file);
	            JSONTokener jt = new JSONTokener(fr);
	            JSONObject data = new JSONObject(jt);

	            JSONArray scenarios = data.getJSONArray("scenarios");

	            for (int i = 0; i < scenarios.length(); i++) {
	                JSONObject scenario = scenarios.getJSONObject(i);
	                try {
	                	performPostRequest(scenario);
	                } catch (NumberFormatException | JSONException e) {
	                    logger.error("Error parsing data for scenario " + i + ": " + e.getMessage());
	                }
	            }

	            fr.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private void performPostRequest(JSONObject scenario) {
	    	logger.info("********* performPostRequest*************");
	    	
	    	SoftAssert softAssert = new SoftAssert();
	        JSONObject requestBody = createRequestBody(scenario);

	        userPayload = createPayload(requestBody);

	        expectedStatusCode = scenario.getInt("expectedStatus");

	        Response response = UserEndPoints2.createUser(userPayload);
	        String responseBody = response.asString();

	        response.then().log().all();
	        JsonPath jsonPath = response.jsonPath();

	        int actualStatusCode = response.getStatusCode();

	        softAssert.assertEquals(actualStatusCode, expectedStatusCode,
	                "Expected status to be " + expectedStatusCode + " but found: " + actualStatusCode);
	    }

	    private JSONObject createRequestBody(JSONObject scenario) {
	    	
	    	logger.info("********* createRequestBody*************");
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
	}