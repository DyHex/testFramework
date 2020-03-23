package core.rest

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import core.HttpRequest
import internal.GlobalVariable

public abstract class PutMethod extends HttpRequest {

	public PutMethod(){
		this.requestObject.setRestRequestMethod('PUT')
		this.requestHeaders.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, GlobalVariable.authToken))
		this.requestHeaders.add(new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json"))
	}
}