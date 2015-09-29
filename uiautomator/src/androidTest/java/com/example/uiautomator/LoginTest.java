package com.example.uiautomator;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LoginTest extends UiAutomatorTestCase{
    public void test() throws UiObjectNotFoundException {
        make_login("admin","admin");
    }
    public void make_login(String loginText, String passwordText) throws UiObjectNotFoundException {
        UiObject login_text = new UiObject(new UiSelector().className("android.widget.EditText").instance(0));
        UiObject password_text = new UiObject(new UiSelector().className("android.widget.EditText").instance(1));
        login_text.setText(loginText);
        password_text.setText(passwordText);
        UiObject login_btn = new UiObject(new UiSelector().className("android.widget.Button"));
        login_btn.clickAndWaitForNewWindow();
    }

}
