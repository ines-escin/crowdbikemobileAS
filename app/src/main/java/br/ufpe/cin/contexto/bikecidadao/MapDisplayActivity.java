package br.ufpe.cin.contexto.bikecidadao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bikecidadao.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.ufpe.cin.br.adapter.bikecidadao.AdapterOcurrence;
import br.ufpe.cin.br.adapter.bikecidadao.Attributes;
import br.ufpe.cin.br.adapter.bikecidadao.Entity;
import br.ufpe.cin.br.adapter.bikecidadao.Marcador;
import br.ufpe.cin.br.adapter.bikecidadao.Metadata;
import br.ufpe.cin.br.adapter.bikecidadao.Ocorrencia;
import br.ufpe.cin.contexto.bikecidadao.async.AsyncGetOcurrences;
import br.ufpe.cin.util.bikecidadao.ConnectivityUtil;
import br.ufpe.cin.util.bikecidadao.LocationAddress;
import br.ufpe.cin.util.bikecidadao.Constants;
import br.ufpe.cin.util.bikecidadao.OnGetOccurrencesCompletedCallback;

public class MapDisplayActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, OnMapLongClickListener, OnGetOccurrencesCompletedCallback{

	public static final String LOCATION = "location";
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
    private GoogleMap googleMap;

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public Intent intent;
	public String endereco;


	public static final String[] OCCURRENCES = {"Local de acidente", "Tráfego intenso", "Sinalização ruim", "Via danificada"};
	int selectedOccurence;
    private Marker marker;

    //private ClusterManager<Marcador> mClusterManager;

    private HashMap<String,String> markers;

	@Override
	protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
		setContentView(R.layout.activity_display_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

		intent = getIntent();

        setLocationService();
//        setStartButton();
        callConnection();

        markers = new HashMap<String,String>();

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

        if(!ConnectivityUtil.isNetworkAvaiable(this)){
            Toast.makeText(getApplicationContext(), getText(R.string.no_network_avaible), Toast.LENGTH_LONG).show();
        }
	}

//    public void setStartButton(){
//        Button startButton = (Button) findViewById(R.id.start_button);
//        startButton.setOnClickListener(startButtonListener);
//    }
    boolean isStarted = false;
    double stopTime;
    double startTime;
//    public OnClickListener startButtonListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Button startButton = (Button) findViewById(R.id.start_button);//            double seconds = getTimeInSeconds();
//            if(isTracking){ //then stop and show start button
//                ViewCompat.setBackgroundTintList(startButton, ContextCompat.getColorStateList(getApplicationContext(), R.color.green_smooth));
//                startButton.setText(getResources().getText(R.string.start_run));
//                stopTime = seconds;
//            }else{
//                ViewCompat.setBackgroundTintList(startButton, ContextCompat.getColorStateList(getApplicationContext(), R.color.red_smooth));
//                startButton.setText(getResources().getText(R.string.stop_run));
//                startTime = seconds;
//            }
//            isTracking = !isTracking;
//        }
//    };

    @Override
	public void onMapReady(GoogleMap map) {
		// Add a marker in Sydney, Australia, and move the camera.
        this.googleMap = map;

        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            AsyncGetOcurrences asyncGetOcurrences = new AsyncGetOcurrences(MapDisplayActivity.this);
            asyncGetOcurrences.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mClusterManager = new ClusterManager<Marcador>(this, this.googleMap);
       // this.googleMap.setOnCameraChangeListener(mClusterManager);

        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Remove the marker
                // marker.remove();
                showConfirmDialog(marker);

            }
        });

    }

	protected OnClickListener report = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showReportDialog();
		}
	};

	public void showReportDialog(){

        selectedOccurence = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(MapDisplayActivity.this);
        // Set the dialog title
        builder.setTitle(getString(R.string.report_an_issue))
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
//	    	.setMessage("helper message")

                .setSingleChoiceItems(OCCURRENCES, selectedOccurence,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedOccurence = which;
                                //Toast.makeText(getApplicationContext(), OCCURRENCES[selectedOccurence], Toast.LENGTH_LONG).show();

                            }
                        })
                        // Set the action buttons
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        sendInformation();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
        ;

        builder.create().show();
    }


    public void showConfirmDialog(final Marker marker){

        selectedOccurence = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(MapDisplayActivity.this);
        // Set the dialog title
        builder.setTitle("Deseja remover?")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        removeMarker(marker);

                    } catch (Exception e){

                    }
                }
            })
            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
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
        showReportDialog();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }



	public void sendInformation(){
		if (mLastLocation != null) {
			double latitude = latLngLast.latitude;//mLastLocation.getLatitude();
			double longitude = latLngLast.longitude;
			LocationAddress locationAddress = new LocationAddress();
			endereco = locationAddress.getAddressFromLocation(latitude, longitude,	getApplicationContext());
		}

		try {
			if (endereco != null && !endereco.equals("")) {
               Marker marker = this.addMarker(latLngLast, selectedOccurence);
			    String code = postInfo(marker);
                if(code == null){ // it means the object was added
                    marker.remove();
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

	public String postInfo(Marker marker) throws JSONException {
		String result = null;
		String line = "";
		String id = String.valueOf(generateUniqueId(getApplicationContext()));
        String title = OCCURRENCES[selectedOccurence];
        Entity entity = new Entity();
		List<Attributes> attributes = new ArrayList<Attributes>();
		attributes.add(new Attributes("title", "String", title, null));
		List<Metadata> metadatas = new ArrayList<Metadata>();
		metadatas.add(new Metadata("location", "String", "WGS84"));
		attributes.add(new Attributes("GPSCoord","coords", latLngLast.latitude + ", " + latLngLast.longitude , metadatas));
		attributes.add(new Attributes("endereco", "String", endereco, null));
		attributes.add(new Attributes("dataOcorrencia", "String",AdapterOcurrence.df.format(Calendar.getInstance().getTime()),null));
		attributes.add(new Attributes("userId", "String", "1",null));
        attributes.add(new Attributes("occurrenceCode", "String", selectedOccurence+"",null));

		entity.setType("Ocurrence");
		entity.setId(id);
		entity.setAttributes(attributes);

		Gson gson = new Gson();
		String uri = Constants.getFiwareBaseAddress(this)+"/v1/contextEntities/";
		uri += id;

		try
		{
			OkHttpClient client = new OkHttpClient();
			RequestBody body = RequestBody.create(JSON, gson.toJson(entity));
			Request request = new Request.Builder().url(uri).put(body).build();
			Response response;

			int executeCount = 0;
			do
			{
				response = client.newCall(request).execute();
				executeCount++;
			}
			while(response.code() == 408 && executeCount < 5);
            if( response.code()==200){
                result = selectedOccurence+"";
            }

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
      return result;
	}

    public void onGetOccurrencesCompleted(List<Ocorrencia> occurrences) {
        if(occurrences!=null) {
            for (Ocorrencia occurrence : occurrences) {

                LatLng latLng = new LatLng(Double.parseDouble(occurrence.getLat()), Double.parseDouble(occurrence.getLng()));
                Marker marker = this.addMarker(latLng, occurrence.getOccurenceCode());
                markers.put(marker.getId(), String.valueOf(occurrence.getIdOcorrencia()));
            }

          //  List<Marcador> list = new ArrayList<>();;
         //   Iterator<Ocorrencia> it = null;
          //  it = occurrences.iterator();
          /*  while (it.hasNext()) {
                Ocorrencia o = it.next();
                Marcador m = new Marcador(new LatLng(Double.parseDouble(o.getLat()),Double.parseDouble(o.getLng())),
                        OCCURRENCES[o.getOccurenceCode().intValue()],
                        o.getOccurenceCode().intValue(),o.getIdOcorrencia());
                mClusterManager.addItem(m);

            }*/
           // mClusterManager.addItems(list);
            //mClusterManager.setRenderer(new OwnIconRendered(this, this.googleMap, mClusterManager));

        }

    }

    public Marker addMarker(LatLng latLng, int occurenceTypeID){

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(OCCURRENCES[occurenceTypeID]).icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon(occurenceTypeID)));
        markerOptions.snippet("Toque aqui para remover marcador");
        return googleMap.addMarker(markerOptions);
    }

    public List<Ocorrencia> getAllOccurrences() throws Exception {

        ////////////////
        String result = "";
        String line = "";
        Gson gson = new Gson();


        String uri = Constants.getFiwareBaseAddress(this)+"/v1/queryContext?limit=500&details=on";
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
		mGoogleApiClient.disconnect();

	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mGoogleApiClient.disconnect();

	}

	public void backToMainPage(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private String generateUniqueId(Context context){
		Calendar cal = Calendar.getInstance();
		String date =  "" + cal.get(Calendar.SECOND) + "" + cal.get(Calendar.MINUTE) + "" + cal.get(Calendar.HOUR_OF_DAY)
				+ "" + cal.get(Calendar.DAY_OF_MONTH) +  "" + cal.get(Calendar.MONTH) + "" + cal.get(Calendar.YEAR) ;
		return date;
	}


	private synchronized void callConnection() {
        Log.i("LOG", "AddressLocationActivity.callConnection()");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addOnConnectionFailedListener(this)
				.addConnectionCallbacks(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}


	// LISTERNERS
	@Override
	public void onConnected(Bundle bundle) {
		Log.i("LOG", "AddressLocationActivity.onConnected(" + bundle + ")");

		mLastLocation = LocationServices
				.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
        if(mLastLocation !=null){
            LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
        }

        startLocationUpdates();

    }

	@Override
	public void onConnectionSuspended(int i) {
		Log.i("LOG", "AddressLocationActivity.onConnectionSuspended(" + i + ")");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i("LOG", "AddressLocationActivity.onConnectionFailed(" + connectionResult + ")");
	}

    public Bitmap getBitmapIcon(int occurenceTypeID){
        View markerView = getMarkerView(occurenceTypeID);
        Bitmap markerBmp =  createDrawableFromView(markerView);
        return markerBmp;
    }

    public View getMarkerView(int occurenceTypeID){
        //OCCURRENCES = {"Local de acidente", "Tráfego intenso", "Sinalização Ruim", "Via danificada"};

        View marker = null;
        int markerViewTypeID = getMarkViewTypeID(occurenceTypeID);
        marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(markerViewTypeID, null);
        return marker;

    }

    private int getMarkViewTypeID(int occurenceTypeID){
        int markerViewTypeID = R.layout.mark_layout;

        switch (occurenceTypeID){
            case 0:
                markerViewTypeID = R.layout.accident_spot;
                break;
            case 1:
                markerViewTypeID = R.layout.heavy_traffic;
                break;
            case 2:
                markerViewTypeID = R.layout.bad_sinalization;
                break;
            case 3:
                markerViewTypeID = R.layout.rout_damaged;
                break;
        }
        return markerViewTypeID;
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


    // #### Tracking properties

    LocationRequest mLocationRequest;
    String mLastUpdateTime;
    Location mCurrentLocation;
    Polyline polyline;
                                        //mili * sec * minute
                                        //1000 * 60 * 1
    private static final long INTERVAL = 500; // 0,5 seconds
    private static final long FASTEST_INTERVAL = 250; // 0,25 second
 //   private GoogleApiClient mGoogleApiClient;

    private void setLocationService() {
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
//        if(isTracking) {
//            mCurrentLocation = location;
//            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//
//            //Toast.makeText(getApplicationContext(), currentLatLng.toString(), Toast.LENGTH_LONG).show();
//
//            if (polyline == null) {
//                polyline = googleMap.addPolyline(new PolylineOptions().width(8).color(ContextCompat.getColor(getApplicationContext(), R.color.red_smooth)).geodesic(true));
//            } else { //update polyline
//                List points = polyline.getPoints();
//                points.add(currentLatLng);
//                polyline.setPoints(points);
//            }
//
//            getRunStatus();
//        }else{
//            if(polyline !=null) {
//                polyline.remove();
//                polyline=null;
//            }
//        }
    }

//    public String getRunStatus(){
//        String info ="";
//        List<LatLng> points = new ArrayList<>(polyline.getPoints());
//        Double distance = SphericalUtil.computeLength(points);
//        TextView textView = (TextView) findViewById(R.id.run_status);
//        double currentTime = getTimeInSeconds();
//        textView.setText("c Distance: " + roundUp2(distance)+
//                "\nTime: "+roundUp2((currentTime-startTime))+"s" +
//                "\nVm: "+roundUp2((distance/(double)(currentTime-startTime))*3.6)+"km");
//
//        return info;
//    }

    private double roundUp2(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        value = Double.valueOf(df.format(value));
        return value;
    }

    private double getTimeInSeconds(){
        double time = Calendar.getInstance().getTime().getTime();
        time /= 1000;
        return roundUp2(time);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }





    public String removeMarker(Marker marker) throws JSONException {
        String result = null;
        String line = "";
        String id =  markers.get(marker.getId());// pegar id do objeto marcador---------------------------------

        Gson gson = new Gson();
        String uri = Constants.getFiwareBaseAddress(this)+"/v1/contextEntities/";
        uri += id;

        try
        {
            OkHttpClient client = new OkHttpClient();
           // RequestBody body = RequestBody.create(JSON, gson.toJson(entity));
            Request  request = new Request.Builder().url(uri).delete().build();
            Response response;

            int executeCount = 0;
            do
            {
                response = client.newCall(request).execute();
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);
            if( response.code()==200){
               marker.remove(); //remove marcador da tela
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
    /*
    private class OwnIconRendered extends DefaultClusterRenderer<Marcador> {
       private HashMap<Long, Marker> markerOcurrences = new HashMap<>();

        public OwnIconRendered(Context context, GoogleMap map,
                               ClusterManager<Marcador> clusterManager) {
            super(context, map, clusterManager);

        }


        @Override
        protected void onBeforeClusterItemRendered(Marcador item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon(item.getIdTipoMarcador())));
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected void onClusterItemRendered(Marcador clusterItem, Marker marker) {
            markers.put(marker.getId(), String.valueOf(clusterItem.getIdOcurrence()));
            markerOcurrences.put(clusterItem.getIdOcurrence(), marker);
            super.onClusterItemRendered(clusterItem, marker);
        }

        public Marker getMarker(Marcador item) {
            return super.getMarker(item);
        }

        public Marker getMarker(int id) {
            Marker item;
            if (markerOcurrences != null) {
                item = markerOcurrences.get(id);
            } else {
                item = null;
            }
            return item;
        }

    }*/
}