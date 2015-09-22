package br.ufpe.cin.contexto.crowdbikemobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowdbikemobile.R;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.ufpe.cin.br.adapter.crowdbikemobile.AdapterOcurrence;
import br.ufpe.cin.br.adapter.crowdbikemobile.Attributes;
import br.ufpe.cin.br.adapter.crowdbikemobile.Entity;
import br.ufpe.cin.br.adapter.crowdbikemobile.Metadata;

public class MapDisplayActivity extends Activity {

	public String latitude;
	public String longitude;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public boolean isLocalChecked = false;
	public TableRow tr1;
	public TableRow tr2;
	public Intent intent;
	public ArrayList<String> coordinates;

	@Override
	protected void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.activity_display_map);
		intent = getIntent();
		this.latitude = "";
		this.longitude = "";

		coordinates = (ArrayList<String>) intent.getSerializableExtra("COORDINATES");

		setSpinner();
		setButton();
		checkMyLocationRadio();
		EditText latitude_text = (EditText) findViewById(R.id.latitude_text);
		EditText longitude_text = (EditText) findViewById(R.id.longitude_text);
		latitude_text.setBackgroundResource(R.drawable.green_edit_text_holo_light);
		longitude_text.setBackgroundResource(R.drawable.green_edit_text_holo_light);
		if(latitude_text.isActivated())
			latitude_text.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
		if(longitude_text.isActivated())
			longitude_text.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
	}

	//Sets the post button
	private void setButton(){
		Button postButton = (Button) findViewById(R.id.send_issue_btn);
		postButton.setBackgroundResource(R.drawable.green_btn_default_normal_holo_light);
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


		//Inner class to configure the post button

	}

	//Inner class to configure the post button
	public OnClickListener postButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			latitude = coordinates.get(0);
			longitude = coordinates.get(1);
			if(isLocalChecked){
				if(tr1.getVirtualChildAt(1) != null && tr2.getVirtualChildAt(1)!= null) {
					TextView tv1 = (TextView) tr1.getVirtualChildAt(1);
					TextView tv2 = (TextView) tr2.getVirtualChildAt(1);
					if(!tv1.getText().toString().equals("") && !tv2.getText().toString().equals("")){
						latitude = tv1.getText().toString();
						longitude = tv2.getText().toString();
						sendInformation(v);
					}else {
						Toast.makeText(getApplicationContext(), "Latititude e Longitude obrigatórios!", Toast.LENGTH_LONG).show();
					}

				}else {
					Toast.makeText(getApplicationContext(), "Latititude e Longitude obrigatórios!", Toast.LENGTH_LONG).show();
				}

			}else {
				sendInformation(v);
			}
		}
	};

	public void sendInformation(View v){
		try {
			postInfo();
			backToMainPage(v);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void postInfo() throws JSONException {
		String result = "";
		String line = "";
		String id = String.valueOf(generateUniqueId(getApplicationContext()));
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

		Gson gson = new Gson();
		String uri = "http://148.6.80.19:1026/v1/contextEntities/";
		uri += id;

		try
		{
			OkHttpClient client = new OkHttpClient();
			RequestBody body = RequestBody.create(JSON, gson.toJson(entity));
			Request request = new Request.Builder().url(uri).post(body).build();
			Response response;

			int executeCount = 0;
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
//
	}

	public void backToMainPage(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private String generateUniqueId(Context context){
		TelephonyManager mngr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String imei = mngr.getDeviceId();
		Calendar cal = Calendar.getInstance();
		String date =  "" + cal.get(Calendar.SECOND) + "" + cal.get(Calendar.MINUTE) + "" + cal.get(Calendar.HOUR_OF_DAY)
				+ "" + cal.get(Calendar.DAY_OF_MONTH) +  "" + cal.get(Calendar.MONTH) + "" + cal.get(Calendar.YEAR) ;
		return date;
	}

	//Sets the spinner with the desired occurrences

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		View v;
		boolean checked = ((RadioButton) view).isChecked();
		tr1 = (TableRow) findViewById(R.id.latitude_table_row);
		tr2 = (TableRow) findViewById(R.id.longitude_table_row);
		// Check which radio button was clicked
		switch (view.getId()) {
			case R.id.my_loc_radio_btn:
				if (checked) {
					tr1.setVisibility(View.INVISIBLE);
					tr2.setVisibility(View.INVISIBLE);
					v = findViewById(R.id.choose_loc_radio_btn);
					((RadioButton) v).setChecked(false);
					isLocalChecked = false;
					break;
				}
			case R.id.choose_loc_radio_btn:
				if (checked) {
					tr1.setVisibility(View.VISIBLE);
					tr2.setVisibility(View.VISIBLE);
					v = findViewById(R.id.my_loc_radio_btn);
					((RadioButton) v).setChecked(false);
					isLocalChecked = true;
					break;
				}
		}
	}


	private void setSpinner() {

		Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);
		spinner.setBackgroundResource(R.drawable.green_spinner_default_holo_light);
		spinner.setPopupBackgroundResource(android.R.color.white);

		String[] occurrences = { "Local de acidente", "Tráfego intenso", "Sinalização Ruim", "Via danificada",
				"Situação de imprudência"};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, occurrences);
		spinner.setAdapter(adapter);
	}

}