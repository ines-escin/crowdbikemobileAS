package br.ufpe.cin.contexto.crowdbikemobile.async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.ufpe.cin.contexto.crowdbikemobile.MainActivity;
import br.ufpe.cin.contexto.crowdbikemobile.pojo.BikePosition;

import com.example.crowdbikemobile.R;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class AsyncServidor extends AsyncTask <String, Void, String> {

	private Context contexto;
	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");

	public AsyncServidor(Context ctx) {
		this.contexto = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {

		String IMEI 	 = String.valueOf(params[0]);
		String latitude  = String.valueOf(params[1]);
		String longitude = String.valueOf(params[2]);
        
		String result = "false";
		BikePosition posicao;
		String line;
		String resultado = "";


		String uri = "http://" + contexto.getResources().getString(R.string.ip_host) + ":8080/project/rest/trace/";

		int responseCode = 0;

		try {
			try {
				OkHttpClient client = new OkHttpClient();

				posicao = new BikePosition(IMEI, latitude, longitude);

				Gson gson = new Gson();

				RequestBody body = RequestBody.create(JSON, gson.toJson(posicao));
				Request request = new Request.Builder()
						.url(uri)
						.post(body)
						.build();

				int executeCount = 0;
				Response response;

				do
				{
					response = client.newCall(request).execute();
					executeCount++;
				}
				while(response.code() == 408 && executeCount < 5);

				result = response.body().string();

			} catch (Exception e) {
				Log.v("FALHA", "TASK");
				responseCode = 408;
				e.printStackTrace();
			}

		} catch (Exception e) {
			Log.v("FALHA", "TASK");
			responseCode = 408;
			e.printStackTrace();
		}

		return result;

	}

	@Override
	protected void onPostExecute(String result) {
		Log.v("SERVIDOR", "Retorno do servidor");
	//	Toast.makeText(contexto, result, Toast.LENGTH_LONG).show();

		super.onPostExecute(result);
		//((MainActivity) contexto).retornoServidor(result);
	}

}
