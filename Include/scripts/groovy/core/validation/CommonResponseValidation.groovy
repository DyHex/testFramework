package core.validation

import core.HttpRequest
import core.common.models.CommonResponse
import core.utilities.JsonParser
import core.utilities.SelectParser

public class CommonResponseValidation {
	static CommonResponse commonResponse

	public static void run(HttpRequest restObject){
		if(restObject.expectedHttpStatusCode != restObject.HTTP_STATUS_CODE){
			parseCommonResponse(restObject)
			if(commonResponse != null){
				validateCommonField(restObject, restObject.expectedCommonError, commonResponse.error, "error")
				validateCommonField(restObject, restObject.expectedCommonCode, commonResponse.code, "code")
				validateCommonField(restObject, restObject.expectedCommonMessage, commonResponse.message, "message")
			}
		}
	}

	private static void parseCommonResponse(HttpRequest restObject){
		CommonResponse cr = new CommonResponse()
		cr = SelectParser.Attempt(cr, restObject)
		commonResponse = cr
	}

	private static void validateCommonField(HttpRequest restObject, String expected, String received, String fieldName){
		if(expected != null && received != null){
			if(received == expected){
				restObject.pass("Field '$fieldName': Correct value '$received' receieved")
			}
			else{
				restObject.fail("Field '$fieldName': Expected '$expected', received '$received'")
			}
		}
		else if(expected != null && received == null){
			restObject.fail("Field '$fieldName': Expected '$expected', received 'null'")
		}
	}
}
