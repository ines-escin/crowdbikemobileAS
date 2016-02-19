package br.ufpe.cin.contexto.bikecidadao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.bikecidadao.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import br.ufpe.cin.contexto.bikecidadao.async.AsyncGetOcurrences;
import br.ufpe.cin.db.bikecidadao.LocalRepositoryController;
import br.ufpe.cin.db.bikecidadao.entity.TrackInfo;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;

    private LocalRepositoryController localRepositoryController;
    private TrackInfo trackInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setActivityEnviroment();

        initVariables();
        loadTrackInfo();
    }


    protected void onStart() {
        if(mGoogleApiClient!=null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        if(mGoogleApiClient!=null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void initVariables(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        localRepositoryController = new LocalRepositoryController(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.action_save_track:
                localRepositoryController.saveTrackingInHistory(trackInfo);
                startHistoryActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.discard_save))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ResultsActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
        ;

        builder.create().show();

    }

    private void startHistoryActivity(){
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        finish();
        startActivity(intent);
    }
    private void setActivityEnviroment() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadTrackInfo() {
        trackInfo = localRepositoryController.getTmpTracking();

        Chronometer chronometerV = (Chronometer) findViewById(R.id.chronometer);
        TextView distanceV = (TextView )findViewById(R.id.distance);
        TextView avgSpeedV = (TextView) findViewById(R.id.avg_speed);

        double distance = trackInfo.getDistance();
        double avgSpeed = (trackInfo.getDistance()/(trackInfo.getElapsedTime()/1000))*3.6;

        chronometerV.setBase(SystemClock.elapsedRealtime() - trackInfo.getElapsedTime());
        distanceV.setText(new DecimalFormat("#.#").format(distance / 1000));
        avgSpeedV.setText(new DecimalFormat("#.#").format(avgSpeed));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMyLocationEnabled(false);
        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

        drawPolyline();
    }


    private void drawPolyline() {
        ArrayList<LatLng> points = trackInfo.getTrackingPoints();
        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .width(8)
                .color(ContextCompat.getColor(getApplicationContext(), R.color.red_smooth))
                .geodesic(true)
                .zIndex(1));
        polyline.setPoints(points);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Location location = getLastLocation();
        LatLng currentLatLng =  new LatLng(location.getLatitude(), location.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));

    }

    private Location getLastLocation(){
        return LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
