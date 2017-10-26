package br.ufpe.cin.db.bikecidadao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import br.ufpe.cin.db.bikecidadao.model.TrackInfo;
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

}
