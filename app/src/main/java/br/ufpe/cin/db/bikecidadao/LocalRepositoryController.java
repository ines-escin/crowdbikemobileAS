package br.ufpe.cin.db.bikecidadao;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufpe.cin.db.bikecidadao.entity.TrackInfo;
import br.ufpe.cin.util.bikecidadao.Constants;

/**
 * Created by jal3 on 12/02/2016.
 */
public class LocalRepositoryController {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences mSharedTrackingHistory;
    private Context context;
    private Gson gson;

    public LocalRepositoryController(Context context){
        this.context = context;
        initVariables();
    }

    private void initVariables(){
        gson = new Gson();
        mSharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                context.MODE_PRIVATE);
        mSharedTrackingHistory = context.getSharedPreferences(Constants.SHARED_TRACKINGS_HISTORY_NAME,
                context.MODE_PRIVATE);
    }


    public void setIsTracking(boolean isTracking){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.TRACKING_SERVICE_STATUS_KEY, isTracking);
        editor.apply();
    }

    public boolean isTracking(){
        return mSharedPreferences.getBoolean(Constants.TRACKING_SERVICE_STATUS_KEY, false);
    }

    public void saveStartTime(long startTime){
        SharedPreferences.Editor editor  = mSharedPreferences.edit();
        editor.putLong(Constants.TRACKING_SERVICE_START_TIME_KEY, startTime);
        editor.apply();
    }

    public long getStartTime(){
        return mSharedPreferences.getLong(Constants.TRACKING_SERVICE_START_TIME_KEY, 0);
    }

    public String getNewTrackId() {
        int count = mSharedPreferences.getInt(Constants.TRACKING_SERVICE_COUNTER_KEY, 0);
        SharedPreferences.Editor editor  = mSharedPreferences.edit();
        editor.putInt(Constants.TRACKING_SERVICE_COUNTER_KEY, ++count);
        editor.remove(Constants.TRACKING_SERVICE_COUNTER_KEY).commit();//TODO take this out to save the id's properly. Now it only saves the last track
        editor.apply();
        return count+"";
    }

    public void saveTmpTracking(TrackInfo trackInfo) {
        SharedPreferences.Editor editor  = mSharedPreferences.edit();
        String trackingPointsStr = gson.toJson(trackInfo);
        editor.putString(Constants.TMP_TRACKING_POINTS_KEY, trackingPointsStr);
        editor.apply();
    }

    public TrackInfo getTmpTracking() {
        String jsonString = mSharedPreferences.getString(Constants.TMP_TRACKING_POINTS_KEY, null);
        TrackInfo trackInfo = gson.fromJson(jsonString, TrackInfo.class);
        return trackInfo;
    }

    public void saveTrackingInHistory(TrackInfo trackInfo) {
        long elapsedTime = SystemClock.elapsedRealtime() - getStartTime();

        //TrackInfo trackInfo = new TrackInfo(trackingPoints, getStartTime(), SystemClock.elapsedRealtime(), distance);
        String trackingPointsStr = gson.toJson(trackInfo);

        String trackId = getNewTrackId();
        SharedPreferences.Editor editor  = mSharedTrackingHistory.edit();
        editor.clear().commit(); //TODO take this out to save all tracks properly
        editor.putString(trackId, trackingPointsStr);
        editor.apply();
    }

    public List<TrackInfo> getAllTrackInfo() {
        List<TrackInfo> tracks = new ArrayList();
        Map<String, ?> keys = mSharedTrackingHistory.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String jsonString = mSharedTrackingHistory.getString(entry.getKey(), null);
            TrackInfo trackInfo = gson.fromJson(jsonString, TrackInfo.class);
            tracks.add(trackInfo);
        }
        return tracks;
    }
}
