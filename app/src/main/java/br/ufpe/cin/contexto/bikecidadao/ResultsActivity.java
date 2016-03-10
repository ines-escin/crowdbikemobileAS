package br.ufpe.cin.contexto.bikecidadao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ufpe.cin.db.bikecidadao.LocalRepositoryController;
import br.ufpe.cin.db.bikecidadao.dao.DatabaseHelper;
import br.ufpe.cin.db.bikecidadao.dao.GeoLocationDao;
import br.ufpe.cin.db.bikecidadao.dao.TrackInfoDao;
import br.ufpe.cin.db.bikecidadao.model.GeoLocation;
import br.ufpe.cin.db.bikecidadao.model.TrackInfo;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;

    private LocalRepositoryController localRepositoryController;
    private TrackInfo trackInfo;

    boolean newTrack = false;

    DatabaseHelper databaseHelper;
    TrackInfoDao trackInfoDao;
    GeoLocationDao geoLocationDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setActivityEnvironment();
        initVariables();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("trackId")) {
            int id = intent.getIntExtra("trackId", 1);
            try {
                trackInfo = trackInfoDao.queryForId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            newTrack = true;
            trackInfo = localRepositoryController.getTmpTracking();
        }
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
        databaseHelper = new DatabaseHelper(this);
        try{
            trackInfoDao = new TrackInfoDao(databaseHelper.getConnectionSource());
            geoLocationDao = new GeoLocationDao(databaseHelper.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        localRepositoryController = new LocalRepositoryController(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(newTrack)
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
                try {
                    trackInfoDao.create(trackInfo);
                    for(GeoLocation d : trackInfo.getTrackingPoints()){
                        d.setTrackInfo(trackInfo);
                        geoLocationDao.create(d);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                startHistoryActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(newTrack){
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
        }else{
            super.onBackPressed();
        }

    }

    private void startHistoryActivity(){
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        finish();
        startActivity(intent);
    }
    private void setActivityEnvironment() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadTrackInfo() {
        Chronometer chronometerV = (Chronometer) findViewById(R.id.chronometer);
        TextView distanceV = (TextView )findViewById(R.id.distance);
        TextView avgSpeedV = (TextView) findViewById(R.id.avg_speed);

        double distance = trackInfo.getDistance();
        double avgSpeed = (trackInfo.getDistance()/(trackInfo.getElapsedTime()/1000.0))*3.6;

        chronometerV.setBase(SystemClock.elapsedRealtime() - trackInfo.getElapsedTime());
        distanceV.setText(new DecimalFormat("#.##").format(distance / 1000.0));
        avgSpeedV.setText(new DecimalFormat("#.##").format(avgSpeed));

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
        Collection<GeoLocation> latLngPoints = trackInfo.getTrackingPoints();
        ArrayList<LatLng> points = new ArrayList<>(latLngPoints.size()+1);
        for (GeoLocation point : latLngPoints){
            points.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }

        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .width(8)
                .color(ContextCompat.getColor(getApplicationContext(), R.color.red_smooth))
                .geodesic(true)
                .zIndex(1));
        polyline.setPoints(points);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(0).latitude, points.get(0).longitude))
                .title("Start")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getMarkerView(R.layout.start_flag_layout))))
                .anchor(0.5f, 0.5f));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(points.get(0).latitude, points.get(0).longitude), 15));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude))
                .title("Stop")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getMarkerView(R.layout.stop_flag_layout))))
                .anchor(0, 1));

    }

    public Bitmap createDrawableFromView(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public View getMarkerView(int viewId){
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(viewId, null);
        return marker;
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        Location location = getLastLocation();
        LatLng currentLatLng =  new LatLng(location.getLatitude(), location.getLongitude());

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        databaseHelper.close();
    }

}
