package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.example.bikecidadao.R;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


//import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertThat;

public class SplashScreenTest
        extends TestActivity<SplashScreenActivity> {

    private Solo solo;

    private SplashScreenActivity ssActivity;

    public SplashScreenTest() {
        super(SplashScreenActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        solo = new Solo(getInstrumentation());
        ssActivity =  getActivity();
    }

    public void testSplashScreenLogoOpening() throws InterruptedException {
        Thread.sleep(SHORT_TIME);
        //Given I open the app
        //Then a logo is displayed
        assertThat(solo.waitForView(R.id.splash), equalTo(true));
    }
    
}