package api.endpoints;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.payload.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

//Created to Perform CRUD operations requests to the user API

public class UserEndPoints {
	
	
	
	
	public static Response  createUser(User payload)
	{
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
		.contentType(ContentType.JSON)
		.body(payload).log().all()
		.when()
		.post(Routes.post_url);
		
		return response;
	}
	
	public static Response  getUserById(int user_id)
	{
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userId", user_id)
		.when()
		.get(Routes.get_ID_url);
		
		return response;
	}
	
	public static Response  getUserByName(String user_first_name)
	{
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userFirstName",user_first_name)
		.when()
		.get(Routes.get_Name_url);
		
		return response;
	}
	
	public static Response updateUser(User payload, int user_id)
	{
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userId", user_id)
		.contentType(ContentType.JSON)
		.body(payload)
		.when()
		.put(Routes.put_ID_url);
		
		return response;
	}
	
	public static Response deleteUser(int user_id)
	{
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userId", user_id)
		.when()
		.delete(Routes.delete_IDl_url);
		
		return response;
	}
	

	
	
	

}
