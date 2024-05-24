package api.test;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.module.jsv.JsonSchemaValidator;

public class jsonSchemaValidation {
	

	@Test(priority =1 )
	
	void jsonSchemavalidation()
	{

		 given()
		 .auth().basic("Numpy@gmail.com", "api@123")
		 
		 .when()
		   .get("https://userserviceapi-a54ceee3346a.herokuapp.com/uap/user/1")
		 .then()
		 .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("storejsonschema.json"));
		
	}

}