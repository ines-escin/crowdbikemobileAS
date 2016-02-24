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
    }
    @When("^I click the start button$")
    public void i_click_the_start_button() throws Throwable {

    }
    @Then("^the start button is changed to stop button$")
    public void the_start_button_is_changed_to_stop_button() throws Throwable {

    }
    @Then("^the tracking is started$")
    public void the_tracking_is_started() throws Throwable {
		//TODO
    }
    @Given("^the tracking service is already started$")
    public void the_tracking_service_is_already_started() throws Throwable {

    }
    @When("^I click the stop button$")
    public void i_click_the_stop_button() throws Throwable {

    }
    @Then("^the tracking is stopped$")
    public void the_tracking_is_stopped() throws Throwable {

    }
    @Then("^I am at the ResultsActivity$")
    public void i_Am_at_the_ResultsActivity() throws Throwable {

    }

    @Then("^I can see the distance, time taken and average speed$")
    public void i_can_see_the_distance_time_taken_and_average_speed() throws Throwable {
		//TODO
    }

    @Then("^the chronometer is running$")
    public void the_chronometer_is_running() throws Throwable {
		//TODO
    }

    @Given("^the tracking service is not started$")
    public void the_tracking_service_is_not_started() throws Throwable {

    }
    
    @Then("^the chronometer is in (\\d+)$")
    public void the_chronometer_is_in(int arg1) throws Throwable {
		//TODO
    }
}
