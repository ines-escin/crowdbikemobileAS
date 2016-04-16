package br.ufpe.cin.contexto.bikecidadao.cucumber.steps;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import br.ufpe.cin.contexto.bikecidadao.MainActivity;
import br.ufpe.cin.contexto.bikecidadao.cucumber.screens.BaseScreen;
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

@CucumberOptions(features = "features")
public class OccurrenceSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    public OccurrenceSteps(MainActivity activityClass) {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
    }


    @Given("^the system has no occurrence added at location \"(.*?)\",\"(.*?)\"$")
    public void the_system_has_no_occurrence_added_at_location(String arg1, String arg2) throws Throwable {
		//TODO
    }    
    @When("^I create a new occurrence at location \"(.*?)\",\"(.*?)\"$")
    public void i_create_a_new_occurrence_at_location(String arg1, String arg2) throws Throwable {
		//TODO
    }
    
    
    @Given("^the system has an occurrence at location \"(.*?)\",\"(.*?)\"$")
    public void the_system_has_an_occurrence_at_location(String arg1, String arg2) throws Throwable {
		//TODO
    }
    @Then("^the occurrence is stored by the system$")
    public void the_occurrence_is_stored_by_the_system() throws Throwable {
        //TODO
    }
    

    @When("^I remove the occurrence$")
    public void i_remove_the_occurrence() throws Throwable {
		//TODO
    }
    @Then("^the occurrence is no longer stored in the system$")
    public void the_occurrence_is_no_longer_stored_in_the_system() throws Throwable {
		//TODO
    }

    @Given("^I am at location \"(.*?)\",\"(.*?)\"$")
    public void i_am_at_location(String arg1, String arg2) throws Throwable {
		//TODO
    }
    @When("^I am under (\\d+) meters from an occurrence at location \"(.*?)\",\"(.*?)\"$")
    public void i_am_under_meters_from_an_occurrence_at_location(int arg1, String arg2, String arg3) throws Throwable {
		//TODO
    }
    @Then("^a occurrence should be received$")
    public void a_occurrence_should_be_received() throws Throwable {
		//TODO
    }
    
    
    @Given("^the system no occurrence near to (\\d+) meters from ocation \"(.*?)\",\"(.*?)\"$")
    public void the_system_no_occurrence_near_to_meters_from_ocation(int arg1, String arg2, String arg3) throws Throwable {
		//TODO
    }
    @Then("^no occurrence should be received$")
    public void no_occurrence_should_be_received() throws Throwable {
		//TODO
    }
    
}
