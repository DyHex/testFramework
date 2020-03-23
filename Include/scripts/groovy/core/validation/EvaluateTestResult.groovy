package core.validation

import com.kms.katalon.core.util.KeywordUtil

import core.HttpRequest

public class EvaluateTestResult {

	public static void now(HttpRequest restObject){
		if(restObject.skipAutomaticEvaluation) {
			return
		}
		restObject.testStepResults.each{value -> (restObject.resultSummary += "${value}\n")}

		String message = "\n'${restObject.class.getSimpleName()}' TEST-STEP RESULT:\nRestUrl: ${restObject.requestObject.getRestUrl()}"
		restObject.parserInfo ? message += "\nSelectParserInfo: $restObject.parserInfo" : null
		message += "\n$restObject.resultSummary"

		if(restObject.failed){
			if(restObject.stopOnFail){
				KeywordUtil.markFailed(message)
				KeywordUtil.markFailedAndStop()
			}
			else{
				KeywordUtil.markFailed(message)
				restObject.testStepResults.clear()
				restObject.resultSummary = ""
			}
		}
		else{
			KeywordUtil.markPassed(message)
			restObject.testStepResults.clear()
			restObject.resultSummary = ""
		}
	}
}
