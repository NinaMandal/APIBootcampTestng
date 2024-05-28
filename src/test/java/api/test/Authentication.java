package api.test;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

public class Authentication {
	
	@Test(priority=1)
	void testBasicAuthentication()
	{
		given()
		.auth().basic("Numpy@gmail.com", "api@123")
		
		.when()
		.get("https://userserviceapi-a54ceee3346a.herokuapp.com/uap/users")
		
		.then()
		.statusCode(200)
		//.body("authenticated",equalTo(true)
		.log().all();
		
		
	}
	
	@Test(priority=2)
	void testDigestAuthentication()
	{
		given()
		.auth().digest("Numpy@gmail.com", "api@123")
		
		.when()
		.get("https://userserviceapi-a54ceee3346a.herokuapp.com/uap/users")
		
		.then()
		.statusCode(200)
		//.body("authenticated",equalTo(true)
		.log().all();
		
		
	}

	
	@Test(priority =3)	void testBasicAuthenticationInvalid()
	{
		 given()
		 .auth().basic("Numpy@gmail.com", "api@12")
		 
		 .when()
		   .get("https://userserviceapi-a54ceee3346a.herokuapp.com/uap/users")
		 .then()
		 .statusCode(401)
		.log().all();
	}

}
