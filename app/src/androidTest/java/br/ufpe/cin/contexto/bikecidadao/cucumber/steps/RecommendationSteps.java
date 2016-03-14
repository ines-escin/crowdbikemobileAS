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

@CucumberOptions(features = "features" )
public class RecommendationSteps extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity mActivity;

    public RecommendationSteps(MainActivity activityClass) {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
    }

    @Given("^there are comfortable routes to go through$")
    public void there_are_comfortable_routes_to_go_through() throws Throwable {

    }
    @Given("^there are routes used by most users to go through$")
    public void there_are_routes_used_by_most_users_to_go_through() throws Throwable {

    }
    @Given("^there are routes with less or no bus on the track to go through$")
    public void there_are_routes_with_less_or_no_bus_on_the_track_to_go_through() throws Throwable {

    }
    @Given("^there are routes with damaged roads to go through$")
    public void there_are_routes_with_damaged_roads_to_go_through() throws Throwable {

    }
    @Given("^there are routes with slow traffic roads to go through$")
    public void there_are_routes_with_slow_traffic_roads_to_go_through() throws Throwable {

    }
    @Then("^I should see a comfortable route recommendation in the recommendation list$")
    public void i_should_see_a_comfortable_route_recommendation_in_the_recommendation_list() throws Throwable {

    }

    @Then("^I should see the most used routes in the recommendation list$")
    public void i_should_see_the_most_used_routes_in_the_recommendation_list() throws Throwable {

    }
    @Then("^I should see routes with less or no bus on the track in the recommendation list$")
    public void i_should_see_routes_with_less_or_no_bus_on_the_track_in_the_recommendation_list() throws Throwable {

    }
    @Then("^I should see routes with damaged roads in the warning list$")
    public void i_should_see_routes_with_damaged_roads_in_the_warning_list() throws Throwable {

    }

    @Then("^I should see routes with slow traffic roads in the warning list$")
    public void i_should_see_routes_with_slow_traffic_roads_in_the_warning_list() throws Throwable {

    }
}
