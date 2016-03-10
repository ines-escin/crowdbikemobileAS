package br.ufpe.cin.db.bikecidadao.model;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by jal3 on 03/02/2016.
 */
@DatabaseTable(tableName="track_info")
public class TrackInfo implements Serializable {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private long startTime;
    @DatabaseField
    private long endTime;
    @DatabaseField
    private double distance;

    @ForeignCollectionField
    private Collection<GeoLocation> trackingPoints;


    public TrackInfo(){

    }

    public TrackInfo(long startTime, long endTime, double distance) {
        this();
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
    }

    public Collection<GeoLocation> getTrackingPoints() {
        return trackingPoints;
    }

    public void setTrackingPoints(ArrayList<GeoLocation> trackingPoints) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Comparator<TrackInfo> DATE_COMPARATOR = new Comparator<TrackInfo>(){
        @Override
        public int compare(final TrackInfo o1, final TrackInfo o2){
            return (int)(o2.getStartTime()-o1.getStartTime());
        }
    };
}
