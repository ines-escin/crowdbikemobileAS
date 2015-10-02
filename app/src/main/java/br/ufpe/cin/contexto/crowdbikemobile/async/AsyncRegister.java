package br.ufpe.cin.contexto.crowdbikemobile.async;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by danielmaida on 18/09/15.
 */
public class AsyncRegister extends AsyncTask <String, Void, Response> {

    @Override
    protected Response doInBackground(String... params){

        String username = params[0];
        String password = params[1];
        String email = params[2];
        Response response = null;
        int executeCount = 0;


        String url = "http://localhost:8080/project/rest/login?username=" + username
                + "&pass=" + password + "&email=" + email;

        try
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            do
            {
                response = client.newCall(request).execute();
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return response;
    }

}
