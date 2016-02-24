package br.ufpe.cin.contexto.bikecidadao.test;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import br.ufpe.cin.contexto.bikecidadao.MainActivity;
import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.example.bikecidadao.R;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;

@CucumberOptions(features = "features")
public class VoiceAlertSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    public VoiceAlertSteps(MainActivity activityClass) {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
    }

    @Given("^I am at the MainActivity$")
    public void i_am_at_the_MainActivity() throws Throwable {
        assertNotNull(getActivity());
    }

    @Then("^the voice alert button is off")
    public void the_sound_button_is_off() throws Throwable {
        onView(withId(R.id.toggle_voice)).check(matches(isNotChecked()));
    }

    @When("^I click the voice alert button$")
    public void i_click_the_voice_alert_button() throws Throwable {
        onView(withId(R.id.toggle_voice)).perform(click());
    }

    @Then("^the voice alert button is on$")
    public void the_voice_alert_button_is_on() throws Throwable {
        onView(withId(R.id.toggle_voice)).check(matches(isChecked()));
    }
    @When("^I turn on voice alert button$")
    public void i_turn_on_voice_alert_button() throws Throwable {
        onView(withId(R.id.toggle_voice)).perform(click());
    }

}
