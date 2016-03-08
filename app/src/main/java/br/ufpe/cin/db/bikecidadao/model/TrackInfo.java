package br.ufpe.cin.db.bikecidadao.model;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by jal3 on 03/02/2016.
 */
public class TrackInfo extends SugarRecord implements Serializable {

    private ArrayList<LatLng> trackingPoints;
    private long startTime;
    private long endTime;
    private double distance;

    public TrackInfo(){
        trackingPoints= new ArrayList<LatLng>();
    }

    public TrackInfo(long startTime, long endTime, double distance) {
        this();
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
    }

    public ArrayList<LatLng> getTrackingPoints() {
        return trackingPoints;
    }

    public void setTrackingPoints(ArrayList<LatLng> trackingPoints) {
        this.trackingPoints = trackingPoints;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getElapsedTime(){
        return endTime - startTime;
    }

    public static Comparator<TrackInfo> DATE_COMPARATOR = new Comparator<TrackInfo>(){
        @Override
        public int compare(final TrackInfo o1, final TrackInfo o2){
            return (int)(o2.getStartTime()-o1.getStartTime());
        }
    };
}
