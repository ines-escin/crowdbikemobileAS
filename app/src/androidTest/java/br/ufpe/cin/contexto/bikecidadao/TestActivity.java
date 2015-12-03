package br.ufpe.cin.contexto.bikecidadao;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.test.espresso.IdlingPolicies;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        setInternetEnabled(super.getActivity(), true);
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();
    }


    public static void setInternetEnabled(Context paramContext, boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

        } catch (Exception e) {
            e.printStackTrace();
        }


        WifiManager wifiManager = (WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

}

