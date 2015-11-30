package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;

import com.example.bikecidadao.R;
import com.robotium.solo.Solo;

import org.junit.Before;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

//import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertThat;

public class MainActivityTest
        extends TestActivity<MainActivity> {

    private Solo solo;

    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        solo = new Solo(getInstrumentation());
        mainActivity =  getActivity();
    }



    public void testFirstTimeActivityOpeningWithMutedAlert(){
        //Given The MainActivity is opened for the first time
        //Then the mute button is unchecked(muted)
        onView(withId(R.id.toggle_voice)).check(matches(isNotChecked()));

    }
    public void testUnmuteAlert() throws InterruptedException {
        //Given I'm at MainActivity
        //And the mute button is muted
        Thread.sleep(SHORT_TIME);
        onView(withId(R.id.toggle_voice))

        //When I click the mute alert button
        .perform(click());

        //Then the alert button is checked to unmuted
        onView(withId(R.id.toggle_voice)).check(matches(isChecked()));
    }


    public void testMuteAlert() throws InterruptedException {
        //Given I'm at MainActivity
        //And the mute button was previously unmuted
        Thread.sleep(SHORT_TIME);
        onView(withId(R.id.toggle_voice)).perform(click());

        // When I click the mute alert button
        onView(withId(R.id.toggle_voice)).perform(click());

        //Then the alert button is checked to unmuted
        onView(withId(R.id.toggle_voice)).check(matches(not(isChecked())));
    }

    public void testMapActionItem() {
        onView(withId(R.id.view_map_action)).check(matches(isDisplayed()));
    }

    public void testClickOnMapActionItem() throws InterruptedException {
        Thread.sleep(SHORT_TIME);
        onView(withId(R.id.view_map_action)).perform(click());

        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }


}