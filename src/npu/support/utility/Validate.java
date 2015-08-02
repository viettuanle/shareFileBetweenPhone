package npu.support.utility;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
/**
 * 
 * @author Tuan Le
 * The purpose of this file is help to validate email address
 */
public class Validate {
	private String content;
	
	static String str_emailformat =  
			 "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//		    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"  
//		            +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"  
//		            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."  
//		            +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"  
//		            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"  
//		            +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
	
	public Validate(String test){
		this.content = test;
	}
	
	public static boolean checkEmailAddressValidate(String emailaddress){
		Matcher matcherObj = Pattern.compile(str_emailformat).matcher(emailaddress); 
		return matcherObj.matches();
	
	}
	/*
	 * Use for name.
	 * cut white space at head, tail The middile has only 1 white space.
	 */
	public static String validateName(String name){
		String temp = name.trim();
		String return_name =  temp.replaceAll("\\s+", " ");
		return return_name;
	
	}
	
	/*
	 * no contain white space
	 */
	public static String validatePhoneNumber(String number){
		String temp_phone = number.replaceAll("\\s+", ""); //cut whitespace
		String return_phone= temp_phone.replaceAll("-", "");
		return return_phone;
	}
}
