package br.ufpe.cin.contexto.bikecidadao.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bikecidadao.R;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.br.adapter.bikecidadao.AdapterOcurrence;
import br.ufpe.cin.br.adapter.bikecidadao.Entity;
import br.ufpe.cin.br.adapter.bikecidadao.Ocorrencia;
import br.ufpe.cin.contexto.bikecidadao.MainActivity;
import br.ufpe.cin.contexto.bikecidadao.pojo.BikePosition;

public class AsyncGetOcurrences extends AsyncTask <String, Void, List<Ocorrencia>> {

	private Context contexto;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
	public AsyncGetOcurrences(Context ctx) {
		this.contexto = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected List<Ocorrencia> doInBackground(String... params) {

        try {
            return getAllOccurrences();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

	@Override
	protected void onPostExecute(List<Ocorrencia> occurrences) {
		Log.v("AsyncGetOcurrences", "Retorno do servidor");
	//	Toast.makeText(contexto, result, Toast.LENGTH_LONG).show();

		super.onPostExecute(occurrences);
		((MainActivity) contexto).showAllMarkers(occurrences);
	}

	public List<Ocorrencia> getAllOccurrences() throws Exception {

		////////////////
		String result = "";
		String line = "";
		Gson gson = new Gson();


		String uri = "http://148.6.80.19:1026/v1/queryContext?limit=500&details=on";
		String getAll = "{\"entities\": [{\"type\": \"Ocurrence\",\"isPattern\": \"true\",\"id\": \".*\"}]}";
		OkHttpClient client = new OkHttpClient();
		try
		{
			RequestBody body = RequestBody.create(JSON, getAll);
			Request request = new Request.Builder()
					.url(uri)
					.post(body)
					.addHeader("Accept","application/json")
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

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		/////////////////////////////////////////

		List<Entity> contextElement = AdapterOcurrence.parseListEntity(result);
		List<Ocorrencia> ocurrences = new ArrayList<Ocorrencia>();
		for (Entity entity : contextElement) {
			ocurrences.add(AdapterOcurrence.toOcurrence(entity));
		}

		// TODO Auto-generated method stub
		return ocurrences;
	}

}
