package br.ufpe.cin.contexto.crowdbikemobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;


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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.content.Intent;
import br.ufpe.cin.br.adapter.crowdbikemobile.AdapterOcurrence;
import br.ufpe.cin.br.adapter.crowdbikemobile.Attributes;
import br.ufpe.cin.br.adapter.crowdbikemobile.Entity;
import br.ufpe.cin.br.adapter.crowdbikemobile.Metadata;
import br.ufpe.cin.util.crowdbikemobile.IdGenerator;

public class MapDisplayActivity extends Activity {

	public String latitude;
	public String longitude;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

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


    //Inner class to configure the post button

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
        String date =  "" + cal.get(Calendar.MILLISECOND);
        return imei + date;
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
}