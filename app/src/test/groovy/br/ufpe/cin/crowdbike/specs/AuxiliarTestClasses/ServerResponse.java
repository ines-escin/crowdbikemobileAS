package br.ufpe.cin.crowdbike.specs.AuxiliarTestClasses;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by danielmaida on 29/09/15.
 */
public class ServerResponse {
    public Response getResponse(String url)
    {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        try
        {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            response = client.newCall(request).execute();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return response;
    }
}
