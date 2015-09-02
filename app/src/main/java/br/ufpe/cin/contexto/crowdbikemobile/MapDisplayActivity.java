package br.ufpe.cin.contexto.crowdbikemobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.crowdbikemobile.R;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import br.ufpe.cin.br.adapter.crowdbikemobile.AdapterOcurrence;
import br.ufpe.cin.br.adapter.crowdbikemobile.Attributes;
import br.ufpe.cin.br.adapter.crowdbikemobile.Entity;
import br.ufpe.cin.br.adapter.crowdbikemobile.Metadata;

public class MapDisplayActivity extends Activity {


	public String latitude;
	public String longitude;

	@Override
	protected void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.activity_display_map);
		Intent intent = getIntent();

		ArrayList<String> coordinates = (ArrayList<String>) intent.getSerializableExtra("COORDINATES");

		this.latitude = coordinates.get(0);
		this.longitude = coordinates.get(1);

		setSpinner();
		setButton();
		checkMyLocationRadio();
	}

	//Sets the post button
	private void setButton(){
		Button postButton = (Button) findViewById(R.id.send_issue_btn);
		postButton.setOnClickListener(postButtonListener);
	}

	private void checkMyLocationRadio(){
		RadioButton myLocationRadio = (RadioButton) findViewById(R.id.my_loc_radio_btn);
		myLocationRadio.setChecked(true);
	}

	private String getIMEI(Context context) {

		TelephonyManager mngr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String imei = mngr.getDeviceId();
		return imei;

	}

	//Inner class to configure the post button
	public OnClickListener postButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			try {
				postInfo();
				backToMainPage(v);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public void postInfo() throws JSONException {
		String result = "";
		String line = "";
		String id = String.valueOf("66960489");
		Entity entity = new Entity();
		List<Attributes> attributes = new ArrayList<Attributes>();
		attributes.add(new Attributes("title", "String", "CPA", null));
		List<Metadata> metadatas = new ArrayList<Metadata>();
		metadatas.add(new Metadata("location", "String", "WGS84"));
		attributes.add(new Attributes("GPSCoord","coords", latitude + ", " + longitude ,metadatas));
		attributes.add(new Attributes("endereco", "String", "Endereco qualquer", null));
		attributes.add(new Attributes("dataOcorrencia", "String",AdapterOcurrence.df.format(Calendar.getInstance().getTime()),null));
		attributes.add(new Attributes("userId", "String", "1",null));

		entity.setType("Ocurrence");
		entity.setId(id);
		entity.setAttributes(attributes);

		Gson gson;
		String uri = "http://148.6.80.19:1026/v1/contextEntities";


		int responseCode = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			httppost.setHeader("Accept", "application/json");
			gson = new Gson();
			StringEntity entityPost = new StringEntity(gson.toJson(entity));
			entityPost.setContentType("application/json");


			httppost.setEntity(entityPost);

			int executeCount = 0;
			HttpResponse response;
			do {
				executeCount++;
				//Log.v("TENTATIVA", "tentativa número:" + executeCount);

				// Execute HTTP Post Request
				response = client.execute(httppost);
				responseCode = response.getStatusLine().getStatusCode();

			} while (executeCount < 5 && responseCode == 408);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			while ((line = rd.readLine()) != null){
				result += line.trim();
			}


		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();

		}

//
//        String s = "{ \"type\" : \"Position\",\"isPattern\" : \"false\", " +
//                "\"id\" : \"358972063059834\",\"contextResponses\" : " +
//                "[{\"attributes\" : [{\"name\" : \"latitude\",\"type\" : " +
//                "\"String\",\"value\" : \"\"},{\"name\" : \"longitude\"," +
//                "\"type\" : \"String\",\"value\" : \"\"}],\"statusCode\" : " +
//                "{\"code\" : \"200\",\"reasonPhrase\" : \"OK\"}}]}";
//
//        JSONAssert.assertEquals(s, result, false);
	}

	public void backToMainPage(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	//Sets the spinner with the desired occurrences

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		View v;
		boolean checked = ((RadioButton) view).isChecked();
		TableRow tr1 = (TableRow) findViewById(R.id.latitude_table_row);
		TableRow tr2 = (TableRow) findViewById(R.id.longitude_table_row);
		// Check which radio button was clicked
		switch (view.getId()) {
			case R.id.my_loc_radio_btn:
				if (checked) {
					tr1.setVisibility(View.INVISIBLE);
					tr2.setVisibility(View.INVISIBLE);
					v = findViewById(R.id.choose_loc_radio_btn);
					((RadioButton) v).setChecked(false);
					break;
				}
			case R.id.choose_loc_radio_btn:
				if (checked) {
					tr1.setVisibility(View.VISIBLE);
					tr2.setVisibility(View.VISIBLE);
					v = findViewById(R.id.my_loc_radio_btn);
					((RadioButton) v).setChecked(false);
					break;
				}
		}
	}


	private void setSpinner() {

		Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);

		String[] occurrences = { "CPA", "COVP", "CVM2-3R", "CACC", "CTPO",
				"CTVF", "COVNM", "COFE", "ANDT", "AI" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, occurrences);

		spinner.setAdapter(adapter);

	}

	public void onDestroy(){
		super.onDestroy();
	}

}