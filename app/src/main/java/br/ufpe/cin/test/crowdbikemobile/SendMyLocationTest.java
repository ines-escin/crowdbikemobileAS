package br.ufpe.cin.test.crowdbikemobile;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class SendMyLocationTest extends UiAutomatorTestCase {
	public void test() throws UiObjectNotFoundException {
		//addNewIssue();
		sendIssue("Risco de Acidente");
	}

	private void addNewIssue() throws UiObjectNotFoundException {
		UiObject layoutButton = new UiObject(new UiSelector().className("android.widget.LinearLayout"));
		UiObject newIssueButton = layoutButton.getChild(new UiSelector().className("android.widget.Button"));
		newIssueButton.clickAndWaitForNewWindow();
	}

	private void sendIssue(String spinnerOption) throws UiObjectNotFoundException {

		UiObject newSpinnerType = new UiObject(new UiSelector().className("android.widget.Spinner"));
		newSpinnerType.click();
		UiScrollable issueOptions = new UiScrollable(new UiSelector().scrollable(true));
		UiObject issue = issueOptions.getChildByText(new UiSelector().className("android.widget.CheckedTextView"), spinnerOption);
		issue.click();
		UiObject newSendButton = new UiObject(new UiSelector().className("android.widget.Button"));
		newSendButton.clickAndWaitForNewWindow();
	}
}
