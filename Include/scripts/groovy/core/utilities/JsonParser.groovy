package core.utilities

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent

import core.common.models.CommonResponse
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

public class JsonParser {

	// Takes a dataObject and converts it to Json and returns it as a string so it can be used in a request body
	public static HttpTextBodyContent objectToJson(object){
		return new HttpTextBodyContent(JsonOutput.prettyPrint(new ObjectMapper().writeValueAsString(object)))
	}

	// Takes a Json response body as a string and converts it to the dataObject sent to the method
	public static def jsonToObject(object, jsonResponseText){
		return object = new JsonSlurper().parseText(jsonResponseText)
	}

	// Takes a Json response body as a string and converts it to a CommonResponse object, ignoring fields not contained in CommonResponse.class
	public static CommonResponse jsonToCommonResponseIgnoreUnknownFields(jsonResponseText){

		if(jsonResponseText == null || jsonResponseText == ""){
			return null
		}

		ObjectMapper objectMapper = new ObjectMapper()
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
		return objectMapper.readValue(jsonResponseText, CommonResponse.class)
	}

}