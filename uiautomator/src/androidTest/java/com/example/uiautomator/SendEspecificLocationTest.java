package br.ufpe.cin.test.crowdbikemobile;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class SendEspecificLocationTest extends UiAutomatorTestCase {

	public void test() throws UiObjectNotFoundException {
		sendIssueWithLL("Via danificada", "-87.231310", "-3.1238913");
	}

	private void sendIssueWithLL(String issue, String latitude, String longitude) throws UiObjectNotFoundException {
		UiObject newSpinnerType = new UiObject(new UiSelector().className("android.widget.Spinner"));
		newSpinnerType.click();
		UiScrollable issueOptions = new UiScrollable(new UiSelector().scrollable(true));
		UiObject choice = issueOptions.getChildByText(new UiSelector().className("android.widget.CheckedTextView"),
				issue);
		choice.click();
		UiObject radiobtn = new UiObject(new UiSelector().className("android.widget.RadioButton").index(2));
		radiobtn.click();
		UiObject tablerow1 = new UiObject(new UiSelector().className("android.widget.TableRow"));
		UiObject tablerow2 = new UiObject(new UiSelector().className("android.widget.TableRow").instance(1));
		UiObject text_lat = tablerow1.getChild(new UiSelector().className("android.widget.EditText"));
		UiObject text_long = tablerow2.getChild(new UiSelector().className("android.widget.EditText"));
		text_lat.setText(latitude);
		text_long.setText(longitude);
		UiObject newSendButton = new UiObject(new UiSelector().className("android.widget.Button"));
		newSendButton.clickAndWaitForNewWindow();
	}

}
