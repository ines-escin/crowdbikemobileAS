package br.ufpe.cin.contexto.bikecidadao.cucumber.steps;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import br.ufpe.cin.contexto.bikecidadao.MainActivity;
import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


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

@CucumberOptions(features = "features" )
public class WeatherSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    public WeatherSteps(MainActivity activityClass) {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        getActivity();
    }

    @Then("^I can see the weather details$")
    public void i_can_see_the_weather_details() throws Throwable {

    }

}
