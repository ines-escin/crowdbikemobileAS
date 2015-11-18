package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;

import com.robotium.solo.Solo;

import org.junit.Before;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class MainActivityTest
        extends TestActivity<MainActivity> {

    private Solo solo;

    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        solo = new Solo(getInstrumentation());
        mainActivity =  getActivity();
    }

    public void testAlertCardView(){

    }
    public void testMuteAlert(){

    }
    public void testUnmuteAlert(){

    }

}