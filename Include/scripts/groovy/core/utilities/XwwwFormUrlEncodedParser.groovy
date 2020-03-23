package core.utilities

import com.kms.katalon.core.testobject.UrlEncodedBodyParameter
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent

class XwwwFormUrlEncodedParser {

	static HttpTextBodyContent objectToEncodedString(def object) {
		List<UrlEncodedBodyParameter> body = new ArrayList<UrlEncodedBodyParameter>()

		object.properties.each {
			(it.value && it.key != "class") ? body.add(new UrlEncodedBodyParameter("$it.key", "$it.value")) : null
		}

		return new HttpTextBodyContent(new HttpUrlEncodedBodyContent(body).inputStream.getText(), null, "application/x-www-form-urlencoded")
	}
}
