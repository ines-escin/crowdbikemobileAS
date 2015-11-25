package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.example.bikecidadao.R;
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

    public void testSplashScreenLogoOpening() throws Exception {
        assertThat(solo.waitForView(R.id.splash), equalTo(true));
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();
    }

}