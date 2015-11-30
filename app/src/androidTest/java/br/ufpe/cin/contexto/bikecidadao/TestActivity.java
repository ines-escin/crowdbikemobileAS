package br.ufpe.cin.contexto.bikecidadao;

import android.app.Activity;
import android.support.test.espresso.IdlingPolicies;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

/**
 * Created by jal3 on 18/11/2015.
 */
public class TestActivity<T extends Activity> extends ActivityInstrumentationTestCase2{

    public static final int LONG_TIME = 10000;
    public static final int MEDIUM_TIME = 5000;
    public static final int SHORT_TIME = 1000;


    public TestActivity(Class<T> activityClass) {
        super(activityClass);
    }

    // Overriding just to return a generic type, otherwise it will return a class of type 'Activity';
    @Override
    public T getActivity() {
        return (T)super.getActivity();
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();
    }
}

