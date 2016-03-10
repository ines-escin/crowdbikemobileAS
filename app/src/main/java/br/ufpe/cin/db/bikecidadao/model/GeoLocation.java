package br.ufpe.cin.db.bikecidadao.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jal3 on 03/02/2016.
 */
@DatabaseTable(tableName="geo_location")
public class GeoLocation implements Serializable, SafeParcelable {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private double latitude;
    @DatabaseField
    private double longitude;

    @DatabaseField(foreign=true)
    private TrackInfo trackInfo;

    public GeoLocation(){
    }

    public GeoLocation(double latitude, double longitude) {
        this();
        this.latitude = latitude;
        this.longitude= longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }


    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private GeoLocation(Parcel in){
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<GeoLocation> CREATOR = new Parcelable.Creator<GeoLocation>() {

        @Override
        public GeoLocation createFromParcel(Parcel source) {
            return new GeoLocation(source);
        }

        @Override
        public GeoLocation[] newArray(int size) {
            return new GeoLocation[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
