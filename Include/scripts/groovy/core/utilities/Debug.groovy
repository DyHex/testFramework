package core.utilities

import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.KeywordUtil

import core.HttpRequest
import groovy.json.JsonOutput
import internal.GlobalVariable

class Debug {

	public static void logRequestHead(HttpRequest restObject){
		if (GlobalVariable.DEBUG_MODE){
			println("Make a " + restObject.requestObject.getRestRequestMethod() + " request to URL: " + restObject.baseUrl + restObject.serviceUrl + restObject.resourcePathUrl)
			for(TestObjectProperty header : restObject.requestHeaders) {
				println(header.getName() + ": " + header.getValue())
			}
		}
	}

	public static void logRequestBody(HttpRequest restObject){
		if (GlobalVariable.DEBUG_MODE){
			TestObjectProperty header
			String contentType, message

			if(restObject.requestObject.httpHeaderProperties != null){
				header = restObject.requestObject.httpHeaderProperties.find {it -> it.name == "Content-Type"}
				if(header != null){
					contentType = header.value
				} else {
					contentType = "text/plain"
				}
			} else {
				contentType = "text/plain"
			}

			message = "The request body is:\n"

			if(restObject.requestBodyText != null){
				try {
					switch(contentType){
						case ~/^application\/json.*$/:
							println(message + JsonOutput.prettyPrint(restObject.requestBodyText.getText()))
							break
						case ~/^application\/x-www-form-urlencoded.*$/:
							println(message + restObject.requestBodyText.getText())
							break
						case ~/^text\/html.*$/:
							println(message + restObject.requestBodyText.getText())
							break
						default:
							println(message + restObject.requestBodyText.getText())
					}
				} catch (Exception e) {
					println("core.utilities.Debug error: Request Header 'Content-Type' likely does not match actual body content sent")
				};
			}
			else{
				println(message)
			}
		}
	}

	public static void logRequestResponse(HttpRequest restObject){
		if (GlobalVariable.DEBUG_MODE){
			println("The response code is: " + restObject.response.statusCode)
			try {
				String text = restObject.response.getResponseText()
				int size = text.length()

				if(size > 0 && size <= 5000){
					println("The response body is:\n" + JsonOutput.prettyPrint(restObject.response.getResponseText()))
				}
				else if(size > 5000){
					println("The response body is: (Substring: first 5000/$size)\n ${JsonOutput.prettyPrint(text.substring(0, 5000))}\nEnd of substring...")
				}
				else {
					println("The response body is:\n")
				}
			} catch(Exception e) {
				println("Response text: " + restObject.response.getResponseText())
			}
		}
		println("Response time: ${restObject.response.elapsedTime}ms")
	}

	public static void println(String message) {
		if (GlobalVariable.DEBUG_MODE) {
			KeywordUtil.logInfo(message)
		}
	}
}