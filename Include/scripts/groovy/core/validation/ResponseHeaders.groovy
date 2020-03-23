package core.validation

import core.HttpRequest

public class ResponseHeaders {

	public static void validate(HttpRequest restObject){
		verifyExpectedResponseHeaders(restObject)
		verifyExcludedResponseHeaders(restObject)
		verifyResponseHeadersRandomValues(restObject)
	}

	public static void verifyExpectedResponseHeaders(HttpRequest restObject){
		String message
		restObject.expectedResponseHeaders.each { key, value ->
			if(restObject.response.getHeaderFields().containsKey(key)){
				if(restObject.response.getHeaderFields().get(key).get(0) == value){
					message = "Header is present and contains correct value: '" + key.toString() + "'"
					restObject.pass(message)
				}
				else{
					message = "Header '" + key.toString() + "' expected: '" + value.toString() + "', but found: '" + restObject.response.getHeaderFields().get(key).get(0) + "'"
					restObject.fail(message)
				}
			}
			else{
				message = "Missing header '" + key.toString() + "'"
				restObject.fail(message)
			}
		}
	}

	public static void verifyExcludedResponseHeaders(HttpRequest restObject){
		String message
		restObject.excludedResponseHeaders.each { key, value ->
			if(!restObject.response.getHeaderFields().containsKey(key)){
				message = "Header is not present: '" + key.toString() + "'"
				restObject.pass(message)
			}
			else{
				message = "Header present, that should NOT be there: '" + key.toString() + "'"
				restObject.fail(message)
			}
		}
	}

	public static void verifyResponseHeadersRandomValues(HttpRequest restObject){
		String message
		restObject.expectedResponseHeadersRandomValues.each { key, value ->
			if(restObject.response.getHeaderFields().containsKey(key)){
				message = "Header '${key.toString()}' is present, with unvalidated value: '${restObject.response.getHeaderFields().get(key).get(0)}'"
				restObject.pass(message)
			}
			else{
				message = "Missing header '" + key.toString() + "'"
				restObject.fail(message)
			}
		}
	}
}
