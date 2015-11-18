package br.ufpe.cin.contexto.bikecidadao;

import android.support.test.InstrumentationRegistry;

import com.robotium.solo.Solo;

import org.junit.Before;

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

    }
    public void testAddReportOnMap(){

    }

}