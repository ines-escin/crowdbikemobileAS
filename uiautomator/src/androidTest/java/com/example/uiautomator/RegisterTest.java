package com.example.uiautomator;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class RegisterTest extends UiAutomatorTestCase{
    public void test() throws UiObjectNotFoundException {
        make_register("admin@gmail.com", "admin", "admin", "admin");
    }

    public void make_register(String email, String username, String password, String passwordConfirm) throws UiObjectNotFoundException {
        UiObject login_btn = new UiObject(new UiSelector().className("android.widget.Button").instance(1));
        UiObject email_text = new UiObject(new UiSelector().className("android.widget.EditText").instance(0));
        UiObject username_text = new UiObject(new UiSelector().className("android.widget.EditText").instance(1));
        UiObject password_text = new UiObject(new UiSelector().className("android.widget.EditText").instance(2));
        UiObject passwordConfirm_text = new UiObject(new UiSelector().className("android.widget.EditText").instance(3));
        UiObject register_btn = new UiObject(new UiSelector().className("android.widget.Button"));
        login_btn.clickAndWaitForNewWindow();
        email_text.setText(email);
        username_text.setText(username);
        password_text.setText(password);
        passwordConfirm_text.setText(passwordConfirm);
        register_btn.clickAndWaitForNewWindow();
    }
}
