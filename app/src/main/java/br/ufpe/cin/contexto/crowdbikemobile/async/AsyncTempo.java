package br.ufpe.cin.contexto.crowdbikemobile.async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufpe.cin.contexto.crowdbikemobile.MainActivity;
import br.ufpe.cin.contexto.crowdbikemobile.pojo.Tempo;

import com.example.crowdbikemobile.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTempo extends AsyncTask <String, Void, Tempo> {
	
	private Context contexto;
	private Map<String, Integer> icone = new HashMap<String, Integer>();
	
	public AsyncTempo(Context ctx) {
		this.contexto = ctx;
	}

	@Override
	protected void onPreExecute() {
		setMapIcone();
	}

	@Override
	protected Tempo doInBackground(String... params) {
		
		/* 
		 * As duas linhas seguintes recebem a atual coordenada geogr�fica da bike 
		 *  
		 */
		String latitude  = params[0];
		String longitude = params[1];
		
		String line;
		String result = "false";
		String resultado = "";
		
		/*
		 * Aqui est� o endere�o do servi�o de tempo
		 * 
		 */
		String uri = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude;
		
		int responseCode = 0;
		
		try {
			OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormEncodingBuilder()
                    .add("latitude", latitude)
                    .add("longitude",longitude)
                    .build();

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
			responseCode = 408;
			e.printStackTrace();
		}

		return parseJson(result);
		
	}

	
	@Override
	protected void onPostExecute(Tempo result) {
		((MainActivity) contexto).setTempoMain(result);
	}

	/**
	 * Este m�todo recebe o resultado do tempo em JSON.
	 * Seu objetivo � fazer um parse do tempo em JSON para um objeto Tempo.
	 * 
	 * @param jsonString	Resultado do webservice em JSON
	 * @return	Resultado do webservice em objeto Tempo
	 */
	private Tempo parseJson(String jsonString) {
		
		Log.v("JSON", jsonString);
		Tempo tempo = new Tempo();
		
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			
			//Tratando a temperatura
				//Buscando a temperatura no json
				JSONObject jsonMain = jsonObject.getJSONObject("main");
				
				//Setando a temperatura no objeto
                String kelvin = jsonMain.getString("temp");
                String celsius = "";

                if(kelvin != null) {
                    celsius = String.valueOf(Double.parseDouble(kelvin) - 273);
                }

				tempo.setTemperatura(celsius);
			
			//Tratando a descri��o
				//Buscando a descri��o no json
				JSONArray jsonArray    = jsonObject.getJSONArray("weather");
				JSONObject jsonWeather = jsonArray.getJSONObject(0);
				
				//Setando a descri��o no objeto
				tempo.setDescricao(jsonWeather.getString("description"));
				
			//Tratando �cone
				//Buscando o c�digo do �cone no json
				tempo.setIcone(getIdIcone(jsonWeather.getString("icon")));
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return tempo;
	}
	
	public void setMapIcone(){
		
		//DIA
		icone.put("01d", R.drawable.icon01d);
		icone.put("02d", R.drawable.icon02d);
		icone.put("03d", R.drawable.icon03d);
		icone.put("04d", R.drawable.icon04d);
		icone.put("09d", R.drawable.icon09d);
		icone.put("10d", R.drawable.icon10d);
		icone.put("11d", R.drawable.icon11d);
		icone.put("13d", R.drawable.icon13d);
		icone.put("50d", R.drawable.icon50d);
		
		//NOITE
		icone.put("01n", R.drawable.icon01n);
		icone.put("02n", R.drawable.icon02n);
		icone.put("03n", R.drawable.icon02n);
		icone.put("04n", R.drawable.icon03n);
		icone.put("09n", R.drawable.icon09n);
		icone.put("10n", R.drawable.icon10n);
		icone.put("11n", R.drawable.icon11n);
		icone.put("13n", R.drawable.icon13n);
		icone.put("50n", R.drawable.icon50n);
	}
		
	public int getIdIcone(String codigo){
		
		int retorno = 0;
		
		if(icone.get(codigo) != null){
			retorno = icone.get(codigo);
		}else{
			retorno = 0;
		}
		return retorno;
	}

}
