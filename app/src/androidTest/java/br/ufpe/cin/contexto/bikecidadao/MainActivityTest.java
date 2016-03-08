package br.ufpe.cin.contexto.bikecidadao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.HandlerThread;
import android.support.test.InstrumentationRegistry;

import com.example.bikecidadao.R;
import com.robotium.solo.Solo;

import org.junit.Before;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

//import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.matcher.RootMatchers.*;
import static org.junit.Assert.assertThat;

public class MainActivityTest
        extends TestActivity<MainActivity> {

    private Solo solo;

    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        solo = new Solo(getInstrumentation());
        mainActivity =  getActivity();

    }


    public void testStartButton() {

    }

    public void testStopButton() {

    }

    public void testStartTrackingService() {

    }

    public void testStopTrackingService() {

    }

}