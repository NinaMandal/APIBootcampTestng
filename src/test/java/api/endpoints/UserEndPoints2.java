package api.endpoints;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

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

public class UserEndPoints2 {
	//methgod created for getting url from property file
	static  ResourceBundle getURL() {
		ResourceBundle routes = ResourceBundle.getBundle("routes");
       return routes;
    }
    
   
	
	
	
	public static Response  createUser(User payload)
	{
		String post_url=getURL().getString("baseURL");
		String post_urlEndpoint=getURL().getString("post_url");
		String fullurl=post_url+post_urlEndpoint;
		
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
		.contentType(ContentType.JSON)
		.body(payload).log().all()
		.when()
		.post(fullurl);
		
		return response;
	}
	
	public static Response  getUserById(int user_id)
	
	{
		
		String getID_url=getURL().getString("baseURL");
		String getID_urlEndpoint=getURL().getString("get_ID_url");
		String fullurl=getID_url+getID_urlEndpoint;
		
		//String getID_url=getURL().getString("baseURL"+"get_ID_url");
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userId", user_id)
		.when()
		.get(fullurl);
		
		return response;
	}
	
	public static Response  getUserByName(String user_first_name)
	{
		
		String getName_url=getURL().getString("baseURL");
		String getName_urlEndpoint=getURL().getString("get_Name_url");
		String fullurl=getName_url+getName_urlEndpoint;
		//String getName_url=getURL().getString("baseURL"+"get_Name_url");
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userFirstName",user_first_name)
		.when()
		.get(fullurl);
		
		return response;
	}
	
	public static Response updateUser(User payload, int user_id)
	{
		
		String putID_url=getURL().getString("baseURL");
		String putID_urlEndpoint=getURL().getString("put_ID_url");
		String fullurl=putID_url+putID_urlEndpoint;
		
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userId", user_id)
		.contentType(ContentType.JSON)
		.body(payload)
		.when()
		.put(fullurl);
		
		return response;
	}
	
	public static Response deleteUser(int user_id)
	{
		String delete_url=getURL().getString("baseURL");
		String deleteID_urlEndpoint=getURL().getString("delete_ID_url");
		String fullurl=delete_url+deleteID_urlEndpoint;
		
		//String deleteIDurl=getURL().getString("baseURL"+"delete_ID_url");
		Response response= given().auth().basic("Numpy@gmail.com", "api@123")
				.pathParam("userId", user_id)
		.when()
		.delete(fullurl);
		
		return response;
	}
	

	
	
	

}
