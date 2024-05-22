package api.endpoints;

public class Routes {
	
	public static String base_url="https://userserviceapi-a54ceee3346a.herokuapp.com/uap/";
	
	//User module
	
	public static String post_url=base_url+"createusers";
	public static String get_ID_url=base_url+"user/{userId}";
	public static String get_Name_url=base_url+"users/username/{userFirstName}";
	public static String get_All_url=base_url+"users";
	public static String put_ID_url=base_url+"updateuser/{userId}";
	public static String delete_Name_url=base_url+"deleteuser/username/{userfirstname}";
	public static String delete_IDl_url=base_url+"deleteuser/{userId}";
	
	

}
