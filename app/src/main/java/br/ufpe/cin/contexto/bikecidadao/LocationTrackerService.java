package br.ufpe.cin.contexto.bikecidadao;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.bikecidadao.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.db.bikecidadao.LocalRepositoryController;
import br.ufpe.cin.db.bikecidadao.entity.TrackInfo;
import br.ufpe.cin.util.bikecidadao.Constants;

public class LocationTrackerService extends Service implements LocationListener, ConnectionCallbacks{

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mPreviousLocation;

    private double distance;
    private long startTime;
    private long elapsedTime;

                                        //mili * sec * minute
                                        //1000 * 60 * 1
    private static final long INTERVAL = 1000 * 4;
    private static final long FASTEST_INTERVAL = 1000 * 2;
    private static final long SMALLEST_DISPLACEMENT = 10;
    private boolean isStarted;

    private ArrayList<LatLng> trackingPoints = new ArrayList<>();

    private final Handler handler = new Handler();
    public static final String BROADCAST_ACTION = "br.ufpe.cin.contexto.bikecidadao.runningprogress";
    Intent loggingIntent;

    private LocalRepositoryController localRepositoryController;

    private Gson gson;


    @Override
    public void onCreate() {
        super.onCreate();

        //Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();

        initVariables();
        callGoogleApiConnection();

    }

    private void initVariables(){
        localRepositoryController = new LocalRepositoryController(this);
        gson = new Gson();
        loggingIntent = new Intent(BROADCAST_ACTION);
    }


    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); // deslocamento mínimo em metros
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void callGoogleApiConnection() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();

        mNotificationBuilder = initNotificationBuilder();
        mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        startForeground(Constants.TRACKING_SERVICE_NOTIFICATION_ID, mNotificationBuilder.build());

    }

    private NotificationCompat.Builder initNotificationBuilder() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(LocationTrackerService.this)
                        .setSmallIcon(R.drawable.ic_directions_bike_white_48dp)
                        .setContentTitle(getString(R.string.tracking_notification_title))
                        .setContentText(getString(R.string.tracking_notification_text))
                        .setOngoing(true);


        Intent resultIntent = new Intent(LocationTrackerService.this, MainActivity.class);
//            // The stack builder object will contain an artificial back stack for the
//            // started Activity.
//            // This ensures that navigating backward from the Activity leads out of
//            // your application to the Home screen.
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(LocationTrackerService.this);
//            // Adds the back stack for the Intent (but not the Intent itself)
//            stackBuilder.addParentStack(MainActivity.class);
//            // Adds the Intent that starts the Activity to the top of the stack
//            stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(LocationTrackerService.this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);

        return mBuilder;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void resume() {
        boolean mRequestingLocationUpdates = true;
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();

        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

        mGoogleApiClient.disconnect();

    }

    private void startLocationUpdates() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onLocationChanged(Location location) {

        //validateLocation() TODO create method to filter new location

        updateTracking(location);
        mPreviousLocation = location;

    }



    private void updateTracking(Location location){

        if(mPreviousLocation !=null){
            distance += mPreviousLocation.distanceTo(location);
            elapsedTime = location.getTime() - startTime;
        }

        mPreviousLocation = location;

        saveTrackingLocations(location);
    }


    private void saveTrackingLocations(Location location){
        trackingPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }


    private boolean isLocationValid(Location lastLocation, Location nextLocation) {
        double accuracy = nextLocation.getAccuracy();
        if (accuracy == 0.0) return false; // means there's no accuracy

        double timeDelta = nextLocation.getElapsedRealtimeNanos() - lastLocation.getElapsedRealtimeNanos();
        timeDelta /= 1e9;
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
        points.add(new LatLng(nextLocation.getLatitude(), nextLocation.getLongitude()));

        double path = SphericalUtil.computeLength(points);
        //	if((path/timeDelta)>17) return false;

        if (accuracy < 15) return true;

        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        if (intent != null && intent.hasExtra(Constants.TRACKING_ACTION)) {

            int action = intent.getIntExtra(Constants.TRACKING_ACTION, Constants.TRACKING_SERVICE_COMMAND_START);

            switch (action) {
                case Constants.TRACKING_SERVICE_COMMAND_START:
                    startTracking();
                    break;
                case Constants.TRACKING_SERVICE_COMMAND_STOP:
                    isStarted = false;
                    stopTracking();
                    break;
                default:
                    break;
            }

        }

        setupBroadcastHandler();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        saveTracking();
        stopTracking();
        startHistoryActivity();
    }

    private void setupBroadcastHandler() {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            updateLogging();
            handler.postDelayed(this, 1000); // 2 seconds
        }
    };

    private void updateLogging() {
        loggingIntent.putParcelableArrayListExtra("trackingPoints", trackingPoints);
        sendBroadcast(loggingIntent);
    }

    private void startTracking() {
        //if it's not tracking, then it's the start button was pressed, otherwise it's resuming
        if(!isTracking()){
            setIsTracking(true);
            saveStartTime();
        }
    }

    private void stopTracking() {
        setIsTracking(false);
        stopLocationUpdates();
        stopNotification();
        stopBroadcast();
    }

    public boolean isTracking(){
        return localRepositoryController.isTracking();
    }
    public void setIsTracking(boolean isTracking) {
        localRepositoryController.setIsTracking(isTracking);
    }
    public void saveStartTime(){
        localRepositoryController.saveStartTime(SystemClock.elapsedRealtime());
    }
    public long getStartTime(){
        return localRepositoryController.getStartTime();
    }

    private void stopBroadcast() {
        handler.removeCallbacks(sendUpdatesToUI);
    }

    private void stopNotification() {
        mNotificationManager.cancel(Constants.TRACKING_SERVICE_NOTIFICATION_ID);
        stopForeground(true);
    }

    private void saveTracking() {
        TrackInfo trackInfo = new TrackInfo(trackingPoints, getStartTime(), SystemClock.elapsedRealtime(), distance);
        localRepositoryController.saveTracking(trackInfo);
    }

    private void startHistoryActivity(){
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Location getLastLocation(){
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }


}