package br.ufpe.cin.contexto.bikecidadao.cucumber.steps;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import br.ufpe.cin.contexto.bikecidadao.MainActivity;
import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@CucumberOptions(features = "features" )
public class NavigationDrawerSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity mActivity;

    public NavigationDrawerSteps(MainActivity activityClass) {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
    }

    @When("^I click on the navigation drawer button$")
    public void i_click_on_the_navigation_drawer_button() throws Throwable {

    }

    @Then("^the navigation drawer is opened$")
    public void the_navigation_drawer_is_opened() throws Throwable {

    }

    @Then("^all the menu options are properly listed$")
    public void all_the_menu_options_are_properly_listed() throws Throwable {

    }
}
