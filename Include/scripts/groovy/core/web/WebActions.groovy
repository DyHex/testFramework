package core.web

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class WebActions {

	/** Info:
	 * The methods contained in this class are just collections of WebUI Keywords.
	 * The intent is to reduce the amount of code needed when creating a test case,
	 * speeding up the process while at the same time ensuring a more robust script.
	 */

	static final int waitTime = 30
	static final int waitTimeShort = 3
	static boolean visible, clickable, inViewport


	/**
	 * Opens browser
	 * @param url
	 */
	static void openBrowser(String url){
		WebUI.openBrowser("", FailureHandling.STOP_ON_FAILURE)
		KeywordUtil.logInfo("Maximizing window...")
		WebUI.maximizeWindow(FailureHandling.STOP_ON_FAILURE)
		if(url){
			KeywordUtil.logInfo("Navigating to url: $url")
			WebUI.navigateToUrl(url, FailureHandling.STOP_ON_FAILURE)
		}
	}

	static void openBrowser(){
		openBrowser("")
	}
	
	static void refreshBrowser() {
		KeywordUtil.logInfo("Refreshing")
		WebDriver webDriver = DriverFactory.getWebDriver()
		webDriver.navigate().refresh()
		KeywordUtil.logInfo("Refresh successfully")
	}

	/**
	 * Close browser
	 */
	static void closeBrowser(){
		WebUI.closeBrowser()
		KeywordUtil.logInfo("Browser closed...")
	}

	/**
	 * Navigate to a given url
	 * @param url
	 */
	static void navigateToUrl(String url){
		KeywordUtil.logInfo("Navigating to url: $url")
		WebUI.navigateToUrl(url, FailureHandling.STOP_ON_FAILURE)
	}

	/**
	 * Click a given element
	 * @param object
	 */
	static void clickElement(TestObject object){
		visible = WebUI.waitForElementVisible(object, waitTimeShort)
		clickable = WebUI.waitForElementClickable(object, waitTimeShort)
		if(visible && clickable){
			KeywordUtil.logInfo("Element is visible & clickable: ${object.getObjectId()}")
			try {
				WebUI.click(object)
				KeywordUtil.markPassed("Element has been clicked: ${object.getObjectId()}")
			} catch (WebElementNotFoundException e) {
				KeywordUtil.markFailed("Element not found: ${object.getObjectId()}")
			} catch (Exception e) {
				KeywordUtil.markFailed("Failed to click on element: ${object.getObjectId()}")
			}
		}
		else{
			KeywordUtil.logInfo("Click '$object.objectId' failed. \nVisible: $visible, \nclickable: $clickable")
		}
	}

	/**
	 * Sets the value on an input field
	 * @param object
	 * @param text
	 */
	static void setText(TestObject object, String text){
		visible = WebUI.verifyElementVisible(object)
		clickable = WebUI.waitForElementClickable(object, waitTimeShort)
		if(visible && clickable){
			KeywordUtil.logInfo("Input field is visible & clickable: ${object.getObjectId()}")
			try {
				WebElement element = WebUiCommonHelper.findWebElement(object, waitTime)
				object = WebUI.convertWebElementToTestObject(element)
				WebUI.setText(object, text)
				KeywordUtil.markPassed("Text has been set in input field: ${object.getObjectId()}")
			} catch (WebElementNotFoundException e) {
				KeywordUtil.markFailed("Input field ${object.getObjectId()} not found...")
			} catch (Exception e) {
				KeywordUtil.markFailed("Failed to set text in input field: ${object.getObjectId()}")
			}
		}
	}

	static void setTextAfterClearingField(TestObject object, String text){
		visible = WebUI.waitForElementVisible(object, waitTime)
		clickable = WebUI.waitForElementClickable(object, waitTimeShort)
		if(visible && clickable){
			KeywordUtil.logInfo("Input field is visible & clickable: ${object.getObjectId()}")
			try {
				WebUI.sendKeys(object, Keys.chord(Keys.CONTROL,'a'))
				WebUI.sendKeys(object, Keys.chord(Keys.BACK_SPACE))
				WebUI.setText(object, text)
				KeywordUtil.markPassed("Text has been set in input field: ${object.getObjectId()}")
			} catch (WebElementNotFoundException e) {
				KeywordUtil.markFailed("Input field ${object.getObjectId()} not found...")
			} catch (Exception e) {
				KeywordUtil.markFailed("Failed to set text in input field: ${object.getObjectId()}")
			}
		}
	}

	/**
	 * Sets the value on an input field from an encrypted string
	 * To encrypt a value in a test case use menu: Help -> Encrypt Text
	 * @param object
	 * @param text
	 */
	static void setEncryptedText(TestObject object, String text){
		visible = WebUI.waitForElementVisible(object, waitTime)
		clickable = WebUI.waitForElementClickable(object, waitTimeShort)
		if(visible && clickable){
			KeywordUtil.logInfo("Input field is visible & clickable: ${object.getObjectId()}")
			try {
				WebUI.setEncryptedText(object, text)
				KeywordUtil.markPassed("Text has been set in input field: ${object.getObjectId()}")
			} catch (WebElementNotFoundException e) {
				KeywordUtil.markFailed("Input field not found: ${object.getObjectId()}")
			} catch (Exception e) {
				KeywordUtil.markFailed("Failed to set text in input field: ${object.getObjectId()}")
			}
		}
	}

	/**
	 * Compare a value with a TestObjects given attributeName.value
	 * @param object
	 * @param attributeName
	 * @param expectedValue
	 * @return boolean
	 */
	static void verifyAttributeValue(TestObject object, String attributeName, String expectedValue){
		WebUI.waitForElementVisible(object, waitTime) ? KeywordUtil.markPassed("${object.getObjectId()} is visible") : KeywordUtil.markFailed("${object.getObjectId()} is not visible")
		String attributeValue = WebUI.getAttribute(object, attributeName)
		if(attributeValue == expectedValue){
			KeywordUtil.markPassed("Attribute is a perfect match.")
		}
		else {
			if(attributeValue.contains(expectedValue)){
				KeywordUtil.markPassed("Attribute contains '$expectedValue' but does not match perfectly.")
			}
			else {
				KeywordUtil.markFailed("Attribute miss match, expected: '$expectedValue' but found '$attributeValue'")
			}
		}
	}

	/**
	 * Creates a new tab, opens it with given url, and returns the new tab index
	 * @param url
	 * @param tabIndex
	 * @return int
	 */
	static int navigateToNewTabAndReturnIndex(String url, int tabIndex){
		WebUI.delay(1)
		WebUI.executeJavaScript('window.open();', [])
		WebUI.switchToWindowIndex(tabIndex)
		WebActions.navigateToUrl(url)
		WebUI.getWindowIndex()
	}

	/**
	 * Open an existing tab
	 * @param tabIndex
	 */
	static void navigateToExistingTab(int tabIndex){
		WebUI.delay(1)
		WebUI.switchToWindowIndex(tabIndex)
	}

	/**
	 * Closes the current tab and opens an existing tab
	 * @param tabIndex
	 */
	static void navigateToExistingTabAndCloseCurrent(int tabIndex){
		WebUI.delay(1)
		WebUI.closeWindowIndex(WebUI.getWindowIndex())
		WebUI.switchToWindowIndex(tabIndex)
	}

	static void verifyElementPresent(TestObject object){
		WebUI.waitForElementVisible(object, waitTime, FailureHandling.CONTINUE_ON_FAILURE) ? KeywordUtil.logInfo("Element visible") : KeywordUtil.logInfo("Element NOT visible")
		WebUI.verifyElementInViewport(object, waitTime, FailureHandling.CONTINUE_ON_FAILURE) ? KeywordUtil.logInfo("Element in viewport") : KeywordUtil.logInfo("Element NOT in viewport")
	}

	static void verifyElementNotPresent(TestObject object){
		WebUI.waitForElementNotPresent(object, waitTime, FailureHandling.CONTINUE_ON_FAILURE) ? KeywordUtil.logInfo("Element visible") : KeywordUtil.logInfo("Element NOT visible")
		WebUI.verifyElementNotInViewport(object, waitTime, FailureHandling.CONTINUE_ON_FAILURE) ? KeywordUtil.logInfo("Element NOT in viewport") : KeywordUtil.logInfo("Element in viewport")
	}
	
	static boolean isElementPresent(TestObject object){
		WebUI.waitForPageLoad(waitTime)
		if(WebUI.verifyElementPresent(object, 1, FailureHandling.OPTIONAL)){
			return true
		}
		return false
	}
}
