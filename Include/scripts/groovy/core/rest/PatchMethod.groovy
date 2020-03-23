package core.rest

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import core.HttpRequest
import internal.GlobalVariable

public abstract class PatchMethod extends HttpRequest {

	public PatchMethod(){
		this.requestObject.setRestRequestMethod('PATCH')
		this.requestHeaders.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, GlobalVariable.authToken))
		this.requestHeaders.add(new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json"))
	}
}