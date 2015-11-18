package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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


    public void testSplashScreenOpening() throws Exception {
        assertThat(solo.waitForActivity(SplashScreenActivity.class.getName(), LONG_TIME_OUT), equalTo(true));

        assertThat(solo.waitForActivity(MainActivity.class.getName(), LONG_TIME_OUT), equalTo(true));
        MainActivity mainActivity = (MainActivity)solo.getCurrentActivity();
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();
    }



}