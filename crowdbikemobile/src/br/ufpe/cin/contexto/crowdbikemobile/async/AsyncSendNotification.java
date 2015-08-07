package br.ufpe.cin.contexto.crowdbikemobile.async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.ufpe.cin.contexto.crowdbikemobile.MainActivity;
import br.ufpe.cin.contexto.crowdbikemobile.pojo.BikePosition;

import com.example.crowdbikemobile.R;
import com.google.gson.Gson;

public class AsyncSendNotification extends AsyncTask <String, Void, String> {
	
	private Context contexto;
	
	public AsyncSendNotification(Context ctx) {
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

		/*
		 * Esta linha deve ser modificada.
		 * Aqui deve ser informada a uri do serviço que recebe as coordenadas
		 * geográficas e retorna a situação do local: perigoso, seguro, ...
		 * 
		 */
		String uri = "http://" + contexto.getResources().getString(R.string.ip_host) + ":8080/project/rest/vehicle/";

		int responseCode = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			posicao = new BikePosition(IMEI, latitude, longitude);

			Gson gson = new Gson();

			//nameValuePairs.add(new BasicNameValuePair("latitude",  latitude ));
			//nameValuePairs.add(new BasicNameValuePair("longitude", longitude));

			//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httppost.setEntity(new StringEntity(gson.toJson(posicao)));

			int executeCount = 0;
			HttpResponse response;
			do {
				executeCount++;
				Log.v("TENTATIVA", "tentativa número:" + executeCount);

				// Execute HTTP Post Request
				response = client.execute(httppost);
				responseCode = response.getStatusLine().getStatusCode();						

			} while (executeCount < 5 && responseCode == 408);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((line = rd.readLine()) != null){
				result = line.trim();
			}

			//Neste ponto result já guarda o Json puro
			Log.v("STATUS", result);

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
