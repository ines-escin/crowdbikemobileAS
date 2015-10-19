package br.ufpe.cin.util.bikecidadao;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress {
    private static final String TAG = "LocationAddress";

    public static String getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context) {

        String result = null;
      //  for (int j = 0; j < 10; j++) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            try {
                List<Address> addressList = geocoder.getFromLocation(
                        latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append(" ");
                    }
                        sb.append(address.getLocality()).append(" ");
                        sb.append(address.getPostalCode()).append(" ");
                        sb.append(address.getCountryName());
                    result = sb.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable connect to Geocoder", e);
            } finally {

            }
      //  }
        return  result;
    }

}
