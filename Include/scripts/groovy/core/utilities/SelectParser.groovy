package core.utilities

import com.kms.katalon.core.testobject.ResponseObject

import core.HttpRequest

public class SelectParser {

	public static def Attempt(def responseBody, HttpRequest restObject){

		String contentType = restObject.response.getHeaderField("Content-Type")

		switch(contentType){
			// Selects parser to use based on content-type header
			// If the Content-Type can't be parsed, response body is set to null and a message is set

			case ~/^application\/json.*$/:
				return new JsonParser().jsonToObject(responseBody, restObject.response.getResponseBodyContent())
				break
			// TODO	case ~/^application\/x-www-form-urlencoded.*$/:
			// TODO	case ~/^text\/html.*$/:

			default:
				restObject.parserInfo = "No parser exists for 'Content-Type' header, 'responseBody' set to null"
				return null
		}
	}
}