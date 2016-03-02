package br.ufpe.cin.contexto.bikecidadao.cucumber.steps;

import android.app.ActivityManager;
import android.content.Context;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;

@CucumberOptions(features = "features" )
public class TrackingSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    public TrackingSteps(MainActivity activityClass) {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        getActivity();
    }


    @Given("^I just started a new tracking$")
    public void i_just_started_a_new_tracking() throws Throwable {

    }

    @When("^I click to stop tracking$")
    public void i_click_to_stop_tracking() throws Throwable {

    }
    @When("^I click to start tracking$")
    public void i_click_to_start_tracking() throws Throwable {

    }
    @When("^I go to a new location$")
    public void i_go_to_a_new_location() throws Throwable {
    }
    @Then("^the tracking is stopped$")
    public void the_tracking_is_stopped() throws Throwable {

    }

    @Then("^the tracking is started$")
    public void the_tracking_is_started() throws Throwable {

    }
    @Then("^I should be on the results screen$")
    public void i_should_be_on_the_results_screen() throws Throwable {

    }

    @Then("^the chronometer starts counting$")
    public void the_chronometer_starts_counting() throws Throwable {
    }

    @Then("^I can see the tracking details$")
    public void i_can_see_the_tracking_details() throws Throwable {

    }

    @Then("^I should see a line of my path on map$")
    public void i_should_see_a_line_of_my_path_on_map() throws Throwable {

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
