package br.ufpe.cin.contexto.crowdbikemobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowdbikemobile.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import br.ufpe.cin.br.adapter.crowdbikemobile.Ocorrencia;
import br.ufpe.cin.util.crowdbikemobile.LocationAddress;

public class MapDisplayActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, OnMapLongClickListener{

	public static final String LOCATION = "location";
	private GoogleApiClient mGoogleApiClient2;
	private Location mLastLocation2;
    private GoogleMap googleMap;
	public String latitude;
	public String longitude;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public boolean isLocalChecked = false;
	public TableRow tr1;
	public TableRow tr2;
	public Intent intent;
	public ArrayList<String> coordinates;
	public Spinner spinner;
	public String endereco;



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
		callConnection();

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		// Add a marker in Sydney, Australia, and move the camera.
        this.googleMap = map;
        LatLng location;
        if(mLastLocation2!=null){
		    location = new LatLng(mLastLocation2.getLatitude(),mLastLocation2.getLongitude());
        }else{
            location = new LatLng(-8.054277,-34.881256);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
        map.setOnMapLongClickListener(this);
        try {
            showAllMarkers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	protected OnClickListener report = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showReportDialog();
		}
	};

	public void showReportDialog(){
		String[] occurrences = {"Local de acidente", "Tráfego intenso", "Sinalização Ruim", "Via danificada",
				"Situação de imprudência"};
		AlertDialog.Builder builder = new AlertDialog.Builder(MapDisplayActivity.this);
		// Set the dialog title
		builder.setTitle("Report an issue")
				// Specify the list array, the items to be selected by default (null for none),
				// and the listener through which to receive callbacks when items are selected
//	    	.setMessage("helper message")

				.setSingleChoiceItems(occurrences, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						// Set the action buttons
				.setPositiveButton("Send report", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						sendInformation();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				})
		;

		builder.create().show();
	}


    LatLng latLngLast;
    @Override
    public void onMapLongClick(LatLng latLng) {
        latLngLast = latLng;
        latitude = latLng.latitude+"";
        longitude = latLng.longitude+"";
        showReportDialog();
    }

	//Sets the post button
	private void setButton(){
		Button postButton = (Button) findViewById(R.id.send_issue_btn);
		postButton.setBackgroundResource(R.drawable.green_btn_default_normal_holo_light);
		postButton.setOnClickListener(report);
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
						sendInformation();
					}else {
						Toast.makeText(getApplicationContext(), "Latititude e Longitude obrigatórios!", Toast.LENGTH_LONG).show();
					}

				}else {
					Toast.makeText(getApplicationContext(), "Latititude e Longitude obrigatórios!", Toast.LENGTH_LONG).show();
				}

			}else {
				sendInformation();
			}
		}
	};

	public void sendInformation(){
		if (mLastLocation2 != null) {
			double latitude = mLastLocation2.getLatitude();
			double longitude = mLastLocation2.getLongitude();
			LocationAddress locationAddress = new LocationAddress();
			endereco = locationAddress.getAddressFromLocation(latitude, longitude,	getApplicationContext());
		}

		try {
			if (endereco != null && !endereco.equals("")) {
			    String title = postInfo();
                if(title!=null){ // it means the object was added
                    this.addMarker(latLngLast, title);
                }
			}
			//backToMainPage(v);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String postInfo() throws JSONException {
		String result = null;
		String line = "";
		String id = String.valueOf(generateUniqueId(getApplicationContext()));
        String title = spinner.getSelectedItem().toString();
		Entity entity = new Entity();
		List<Attributes> attributes = new ArrayList<Attributes>();
		attributes.add(new Attributes("title", "String", title, null));
		List<Metadata> metadatas = new ArrayList<Metadata>();
		metadatas.add(new Metadata("location", "String", "WGS84"));
		attributes.add(new Attributes("GPSCoord","coords", latitude + ", " + longitude , metadatas));
		attributes.add(new Attributes("endereco", "String", endereco, null));
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
            if( response.code()==200){
                result = title;
            }

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
      return result;
	}

    public void showAllMarkers() throws Exception {

        List<Ocorrencia> occurrences = getAllOccurrences();

        for(Ocorrencia occurrence: occurrences){

            LatLng latLng = new LatLng(Double.parseDouble(occurrence.getLat()), Double.parseDouble(occurrence.getLng()));
            this.addMarker(latLng, occurrence.getTitle());

        }

    }

    public void addMarker(LatLng latLng, String title){
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon()));
        googleMap.addMarker(markerOptions);
    }

    public List<Ocorrencia> getAllOccurrences() throws Exception {

        ////////////////
        String result = "";
        String line = "";
        Gson gson = new Gson();


        String uri = "http://148.6.80.19:1026/v1/queryContext";
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


    @Override
	protected void onStop(){
		super.onStop();
		mGoogleApiClient2.disconnect();

	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mGoogleApiClient2.disconnect();

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

		spinner = (Spinner) findViewById(R.id.menu_spinner);
		spinner.setBackgroundResource(R.drawable.green_spinner_default_holo_light);
		//spinner.setPopupBackgroundResource(android.R.color.white);

		String[] occurrences = { "Local de acidente", "Tráfego intenso", "Sinalização Ruim", "Via danificada",
				"Situação de imprudência"};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, occurrences);
		spinner.setAdapter(adapter);
	}


	private synchronized void callConnection(){
		Log.i("LOG", "AddressLocationActivity.callConnection()");
		mGoogleApiClient2 = new GoogleApiClient.Builder(this)
				.addOnConnectionFailedListener(this)
				.addConnectionCallbacks(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient2.connect();
	}


	// LISTERNERS
	@Override
	public void onConnected(Bundle bundle) {
		Log.i("LOG", "AddressLocationActivity.onConnected(" + bundle + ")");

		mLastLocation2 = LocationServices
				.FusedLocationApi
				.getLastLocation(mGoogleApiClient2);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i("LOG", "AddressLocationActivity.onConnectionSuspended(" + i + ")");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i("LOG", "AddressLocationActivity.onConnectionFailed(" + connectionResult + ")");
	}

    public Bitmap getBitmapIcon(){
        View markerView = getMarkerView();
        Bitmap markerBmp =  createDrawableFromView(markerView);
        return markerBmp;
    }

    public View getMarkerView(){

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mark_layout, null);
        return marker;

    }

    public Bitmap createDrawableFromView(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}