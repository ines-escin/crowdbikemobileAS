package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Before;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SplashScreenTest
        extends ActivityInstrumentationTestCase2<SplashScreenActivity> {

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
        ssActivity = getActivity();
    }


    public void testSplashScreenOpening() throws Exception {
        assertThat(solo.waitForActivity(SplashScreenActivity.class.getName(), 5000), equalTo(true));
    }


}