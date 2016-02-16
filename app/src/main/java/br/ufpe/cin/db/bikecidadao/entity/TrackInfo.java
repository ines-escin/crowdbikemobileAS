package br.ufpe.cin.db.bikecidadao.entity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by jal3 on 03/02/2016.
 */
public class TrackInfo {

    private long id;
    private ArrayList<LatLng> trackingPoints;
    private long startTime;
    private long endTime;
    private double distance;

    public TrackInfo(ArrayList<LatLng> trackingPoints, long startTime, long endTime, double distance) {
        this.trackingPoints = trackingPoints;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
    }

    public ArrayList<LatLng> getTrackingPoints() {
        return trackingPoints;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getDistance() {
        return distance;
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
