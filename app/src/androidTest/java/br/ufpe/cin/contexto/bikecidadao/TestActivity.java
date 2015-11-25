package br.ufpe.cin.contexto.bikecidadao;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by jal3 on 18/11/2015.
 */
public class TestActivity<T extends Activity> extends ActivityInstrumentationTestCase2{

    public static final int LONG_TIME = 15000;
    public static final int MEDIUM_TIME = 7000;
    public static final int SHORT_TIME = 2000;


    public TestActivity(Class<T> activityClass) {
        super(activityClass);
    }

    // Overriding just to return a generic type, otherwise it will return a class of type 'Activity';
    @Override
    public T getActivity() {
        return (T)super.getActivity();
    }
}

