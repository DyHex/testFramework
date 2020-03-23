package core.validation

import core.HttpRequest

public class HttpStatusCode {

	public static void validate(HttpRequest restObject){
		String message
		if(restObject.response.getStatusCode() == restObject.expectedHttpStatusCode){
			message = "Http Status '${restObject.expectedHttpStatusCode}'"
			restObject.pass(message)
		}
		else{
			message = "Expected Http Status code: '${restObject.expectedHttpStatusCode}', but found: '${restObject.response.getStatusCode()}'"
			restObject.fail(message)
		}
	}
}
