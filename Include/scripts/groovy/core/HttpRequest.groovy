package core

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import core.utilities.Debug
import core.validation.CommonResponseValidation
import core.validation.EvaluateTestResult
import core.validation.HttpStatusCode
import core.validation.ResponseHeaders
import internal.GlobalVariable

public abstract class HttpRequest {
	RequestObject requestObject = new RequestObject()								// Katalon.webservice Request Object
	List<TestObjectProperty> requestHeaders = new ArrayList<>()						// Used to populate the request headers for a request
	HttpTextBodyContent requestBodyText = new HttpTextBodyContent("")				// Used to print the JSON body of the request in Console
	String baseUrl = ""																// URL for API, fetched from Profiles to regulate which environment is called, specify in sub-class
	String serviceUrl = ""															// API service end-point to be called, specify in sub-class
	String resourcePathUrl = ""														// End-point specific path, specify in sub-class
	ResponseObject response															// Katalon.webservice Response Object

	Integer HTTP_STATUS_CODE														// Default happy-case Http Status Code for an end-point, must be set in sub-class

	Integer expectedHttpStatusCode = null											// Defines expected HTTP Status Code
	String expectedCommonError = null
	String expectedCommonCode = null
	String expectedCommonMessage = null

	Map expectedResponseHeaders = new HashMap<String, String>()						// Defines expected response headers
	Map expectedResponseHeadersRandomValues = new HashMap<String, String>()			// Defines expected response headers with values we can't evaluate
	Map excludedResponseHeaders = new HashMap<String, String>()						// Defines response headers that should NOT be returned

	boolean stopOnFail = GlobalVariable.stopOnFail									// Decides if a failed test step (request/response) should terminate a test case or not, can be set globally
	boolean failed = false															// Is used by the evaluation code to track if a test step (request/response) as failed or not
	boolean skipAutomaticEvaluation = false											// Turns off all evaluation all together, tests are still run but the failed status is not set (will result in all test steps passing), used in EvaluateTestResult.groovy
	boolean skipAutomaticResponseBodyEvaluation = false								// Turns off custom end-point evaluation, used in end-point classes, is set in test cases

	List<String> testStepResults = new ArrayList<>()								// Used to gather all test results for final evaluation
	String resultSummary = ""														// String used to represent an overview of all test results for a test step, printed in "Log Viewer" for an easy overview per request/response

	String parserInfo																// Info message from core.utilities.SelectParser

	protected abstract void buildRequestBody()										// Required in all end-points, used to parse the requests object model to a JSON body representation. If no request body, implement it as an empty override method
	protected abstract void parseResponseBody()										// Required in all end-points, used to parse the JSON response body to desired object model. If no response body, implement it as an empty override method
	protected abstract void verifyResponseBody()									// Required in all end-points, used to implement end-point specific validation. If no response body, implement it as an empty override method



	// Sets minimum expected headers (override, call super and add additions in end-point class to expand)
	protected void setStandardExpectedHeaders() {
		expectedResponseHeaders.put("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
		expectedResponseHeaders.put("X-XSS-Protection", "1; mode=block")
		excludedResponseHeaders.put("Server", null)

		if(this.response.getHeaderField("Content-Length") != "0"){
			this.expectedResponseHeadersRandomValues.put("Content-Type", null)
		}
	}

	// Called to run test validation for a response
	protected void verifyResponse(){
		HttpStatusCode.validate(this)				// Compares response Http Status Code with expected value
		this.verifyResponseBody()					// Custom end-point validation is called here
		CommonResponseValidation.run(this)			// If negative test, compares CommonResponse fields if expected value is set
		ResponseHeaders.validate(this)				// Compares response headers with expected value
	}

	/** sendRequestVerifyAndEvaluateResponse
	 * 
	 * Call this method in a test case on an end-point object to build, 
	 * send/receive and evaluate the response
	 */
	public sendRequestVerifyAndEvaluateResponse(){
		buildRequest()
		sendRequestAndReceiveResponse()
		evaluateResponse()
	}

	/** buildRequest
	 * 
	 *  Call this method in a test case on an end-point object to build the 
	 *  request without sending it, useful when you want to "manually" edit 
	 *  a request before sending it. 
	 *  
	 *  You'll need to call 'sendRequestAndReceiveResponse' then 'evaluateResponse'
	 *  in order, after calling buildRequest to complete the test step.
	 */
	public buildRequest() {
		if(HTTP_STATUS_CODE == null){
			KeywordUtil.markError("HTTP_STATUS_CODE is 'null', specify default value in subclass-constructor")
		}
		buildRequestBody()
		requestObject.setHttpHeaderProperties(requestHeaders)
		requestObject.setRestUrl(baseUrl + serviceUrl + resourcePathUrl)
		requestBodyText = requestObject.getBodyContent()
	}

	/** sendRequestAndVerifyResponse
	 * 
	 *  Call this method in a test case on an end-point object to send the request,
	 * receive and parse the response. DOES NOT EVALUATE THE RESULTS!
	 * Thus this method can be used to add additional custom tests in a test case
	 * to 'testStepResults' before calling evaluateResponse() for a complete test
	 * evaluation-overview on a test step. Or alter expected values before evaluation.
	 *
	 * This method also logs the request headers, body and response to Console for 
	 * easier trouble shooting, setting DEBUG_MODE in Profiles to 'false' might 
	 * improve test case execution time and will improve load time of console logs.
	 * If DEBUG_MODE is disabled, you'll need to go through the HAR files when 
	 * trouble shooting.
	 */
	public sendRequestAndReceiveResponse() {
		Debug.logRequestHead(this)
		Debug.logRequestBody(this)
		this.response = WS.sendRequest(requestObject)
		Debug.logRequestResponse(this)
		setStandardExpectedHeaders()
		parseResponseBody()
	}

	/** evaluateResponse
	 *  Used to run automated tests and evaluation the
	 *  results after 'sendRequestAndReceiveResponse()'
	 */
	public evaluateResponse(){
		verifyResponse()
		EvaluateTestResult.now(this)
	}

	/* Methods fail(String) and pass(String) are used to set test result for a
	 * single validation test on an end-point object, a request has multiple.
	 * Result is added to the list 'testStepResults'
	 */
	public void fail(String message){
		this.testStepResults.add("! FAILED: " + message)
		this.failed = true
	}
	public void pass(String message){
		this.testStepResults.add("PASSED: " + message)
	}



	// ---------------------- Helper methods ----------------------

	// Sets expected HTTP Status Code. Expand with minimum required fields for validation (common response fields)
	public void setExpectedNegativeOutcome (int httpStatusCode, String error, String code, String message) {
		this.expectedHttpStatusCode = httpStatusCode
		this.expectedCommonError = error
		this.expectedCommonCode = code
		this.expectedCommonMessage = message
	}

	// Enables editing of request headers
	public void addRequestHeader(String name, String value){
		this.requestHeaders.add(new TestObjectProperty(name, ConditionType.EQUALS, value))
	}
	public void updateRequestHeader(String name, String value){
		this.requestHeaders.set(this.requestHeaders.findIndexOf {it.name.equals(name)}, new TestObjectProperty(name, ConditionType.EQUALS, value))
	}
	public void deleteRequestHeader(String name){
		this.requestHeaders.remove(this.requestHeaders.find {it.name.equals(name)})
	}

}
