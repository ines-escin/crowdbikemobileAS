package com.example.uiautomator;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class VoiceAlertEnableButton extends UiAutomatorTestCase {
    public void test() throws UiObjectNotFoundException {
        enableVoiceAlert();
    }

    public void enableVoiceAlert() throws UiObjectNotFoundException {
        UiObject voiceButton = new UiObject(new UiSelector().className("android.widget.Button"));
        voiceButton.clickAndWaitForNewWindow();
        UiObject enableVoice = new UiObject(new UiSelector().className("android.widget.CheckBox"));
        if(!enableVoice.isChecked())
            enableVoice.click();
    }
}
