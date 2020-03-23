package someCompanyName_API_implementation_examples

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import core.HttpRequest
import core.utilities.JsonParser
import core.utilities.SelectParser
import internal.GlobalVariable
import someCompanyName_API_implementation_examples_models.PersonRequest
import someCompanyName_API_implementation_examples_models.PersonResponse

public class CreatePerson extends HttpRequest {

	PersonRequest requestBody
	PersonResponse responseBody

	private CreatePerson(){
		this.requestObject.setRestRequestMethod('POST')
		this.requestHeaders.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, GlobalVariable.authToken))
		this.requestHeaders.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
		this.requestHeaders.add(new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json"))
		this.baseUrl = "https://company-${GlobalVariable.environment}.com"
		this.serviceUrl = "/account"
		this.resourcePathUrl = ""
		this.HTTP_STATUS_CODE = 201
		this.expectedHttpStatusCode = HTTP_STATUS_CODE
	}

	public CreatePerson(PersonRequest body){
		this()
		this.requestBody = body
	}

	@Override
	protected void buildRequestBody() {
		this.requestObject.setBodyContent(JsonParser.objectToJson(requestBody))
	}

	@Override
	protected void parseResponseBody() {
		if(response.getResponseBodyContent() != null){
			this.responseBody = SelectParser.Attempt(responseBody, this)
		}
	}

	@Override
	protected void verifyResponseBody() {
		// TODO: see core.validation.CommonResponseValidation for example
	}
}