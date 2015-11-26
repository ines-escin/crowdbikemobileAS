package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;

import com.example.bikecidadao.R;
import com.robotium.solo.Solo;

import org.junit.Before;


//import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertThat;

public class MapActivityTest
        extends TestActivity<MapDisplayActivity> {

    private Solo solo;

    private MapDisplayActivity mapActivity;

    public MapActivityTest() {
        super(MapDisplayActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        solo = new Solo(getInstrumentation());
        mapActivity =  getActivity();
    }

    public void testLongClickToReport(){
        //Given the MapDisplayActivity is opened
        //When I long click at the map view
        onView(withId(R.id.map)).perform(longClick());

        //Then a report dialog view is displayed
        onView(withText(mapActivity.getString(R.string.report_an_issue))).check(matches(isDisplayed()));
    }

    public void testAddReportOnMap(){
        //TODO
        //Given the Report Dialog is opened
        //When I select a report
        //And click on send report button
        //Then the report is added
    }

}