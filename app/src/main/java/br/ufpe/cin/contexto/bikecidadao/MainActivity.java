package br.ufpe.cin.contexto.bikecidadao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bikecidadao.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import br.ufpe.cin.br.adapter.bikecidadao.AdapterOcurrence;
import br.ufpe.cin.br.adapter.bikecidadao.Attributes;
import br.ufpe.cin.br.adapter.bikecidadao.Entity;
import br.ufpe.cin.br.adapter.bikecidadao.Marcador;
import br.ufpe.cin.br.adapter.bikecidadao.Ocorrencia;
import br.ufpe.cin.contexto.bikecidadao.async.AsyncCreateAndWriteFile;
import br.ufpe.cin.contexto.bikecidadao.async.AsyncGetOcurrences;
import br.ufpe.cin.contexto.bikecidadao.async.AsyncTempo;
import br.ufpe.cin.contexto.bikecidadao.pojo.Tempo;
import br.ufpe.cin.db.bikecidadao.LocalRepositoryController;
import br.ufpe.cin.db.bikecidadao.model.GeoLocation;
import br.ufpe.cin.util.bikecidadao.ConnectivityUtil;
import br.ufpe.cin.util.bikecidadao.LocationUtil;
import br.ufpe.cin.util.bikecidadao.Constants;
import br.ufpe.cin.util.bikecidadao.OnGetOccurrencesCompletedCallback;
import br.ufpe.cin.util.bikecidadao.PermissionRequest;


@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        LocationListener, OnClickListener , OnGetOccurrencesCompletedCallback{
    public static final String[] OCCURRENCES = {"Local de acidente", "Tráfego intenso", "Sinalização ruim", "Via danificada"};
    private GoogleMap googleMap;
    private HashMap<String,String> markers;


    private String latitudeString = "";
	private String longitudeString = "";
	private Tempo tempoLocal = new Tempo();
	private int bgColor = 0;
	private TextView txtMensagem;
	private AsyncTempo tempo;
	private TextView txtResultado;
	private long timePosition;
	private String lastLatitudeString;
	private String lastLongitudeString;
	private long timeLastPosition;
	private long anterior;
	private long atual;
	private boolean first = true;   //forecast


	private long anteriorForecast;
	private long atualForecast;
	private boolean firstForecast = true;

	private boolean firstLocation = true;
	private boolean doVoiceAlert;


	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final double EARTH_GRAVITY = 9.81;
	private static final double WEIGHT = 70.0;
	private static final double GRADE = 70.0;
	public static final double W_TO_KGM = 6.12;
	public static final double KGM_TO_KCAL = 1 / 427.0;

	private TextToSpeech TTS;
	// Lumped constant for all frictional losses (tires, bearings, chain).
	private static final double K1 = 0.0053;

	// Lumped constant for aerodynamic drag (kg/m)
	private static final double K2 = 0.185;

	private static double totalCalorias = 0.0;

	private DoSomethingThread randomWork;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 1 meters
	// The minimum startTime between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 0;// 1000 * 60 * 1; // 1
	// minute

	private static final String PREFS_REGISTERED = "Preferences";


	private String registered = "";

    private Intent trackingIntent;


    private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private Location mLastLocation;
	public boolean threadsAlive = false;
	Chronometer chronometer;
	long startTime;
    boolean mBroadcastIsRegistered = false;

	private LocalRepositoryController localRepositoryController;

	private Toolbar toolbar;
	private DrawerLayout mDrawerLayout;
	private NavigationView nvDrawer;
	private ActionBarDrawerToggle mDrawerToggle;

	private ClusterManager<Marcador> mClusterManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		trackingIntent = new Intent(this, LocationTrackerService.class);
		localRepositoryController = new LocalRepositoryController(this);
		setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new HashMap<String,String>();

		txtMensagem = (TextView) findViewById(R.id.txtMensagem);

		TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status){
			}
		});

        setToggleVoiceAlert();

		// Setando a cor de fundo. Padrao: branco
		setarCorDeFundo(R.color.branco);

		callConnection();

		// Necess?rio para usar Runable na activity?
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		final Handler incomingMessageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String message = msg.getData().getString("msg");
				TextView tv = (TextView) findViewById(R.id.txtMensagem);
				Date now = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
				txtMensagem.setText(message);
				// tv.append(ft.format(now) + ' ' + message + '\n');
			}
		};

		if(!ConnectivityUtil.isNetworkAvaiable(this)){
			Toast.makeText(getApplicationContext(), getText(R.string.no_network_avaible), Toast.LENGTH_LONG).show();
		}
		chronometer = (Chronometer) findViewById(R.id.chronometer);
        Button startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(this);



		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = setupDrawerToggle();

		nvDrawer = (NavigationView) findViewById(R.id.nvView);
		setupDrawerContent(nvDrawer);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

        if(!LocationUtil.isGPSEnabled(this)){
            LocationUtil.showEnableGPSDialog(this);
        }

        resumeTrackingStatus();
	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
	}

	public void selectDrawerItem(MenuItem menuItem) {

		switch(menuItem.getItemId()) {
			case R.id.menu_history:
				startActivity(new Intent(this, HistoryActivity.class));
				break;
			case R.id.menu_export:
				openPopUp(findViewById(R.id.drawer_layout));
				break;
			default:
				startActivity(new Intent(this, HistoryActivity.class));
		}



		// Highlight the selected item, update the title, and close the drawer
		menuItem.setChecked(false);
		mDrawerLayout.closeDrawers();
	}
	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
			// Handle presses on the action bar items
		threadsAlive = false;
		if(mGoogleApiClient != null){
			stopLocationUpdate();
		}
        switch (item.getItemId()) {
            case R.id.view_map_action:
                displayMapActivity();
                return true;
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;

        //this.googleMap.setOnMapLongClickListener(this);
        if(PermissionRequest.checkLocationPermission(this)){
            googleMap.setMyLocationEnabled(true);
        }else{
            PermissionRequest.requestLocationPermission(this);
        }
        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

		mClusterManager = new ClusterManager<Marcador>(this, this.googleMap);
		this.googleMap.setOnCameraChangeListener(mClusterManager);

        try {
            AsyncGetOcurrences asyncGetOcurrences = new AsyncGetOcurrences(MainActivity.this);
			asyncGetOcurrences.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	@Override
	protected void onPause() {
		super.onPause();
		mGoogleApiClient.disconnect();

        if (mBroadcastIsRegistered) {
            unregisterReceiver(broadcastReceiver);
            mBroadcastIsRegistered = false;
        }
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGoogleApiClient.connect();
		if(mGoogleApiClient !=null && mGoogleApiClient.isConnected()){
			startLocationUpdate();
		}
		firstForecast = true;

        if (!mBroadcastIsRegistered) {
            registerReceiver(broadcastReceiver, new IntentFilter(
                    LocationTrackerService.BROADCAST_ACTION));
            mBroadcastIsRegistered = true;
        }

        resumeTrackingStatus();
	}


	@Override
	protected void onStop(){
		super.onStop();
	}

	@Override
	protected void onStart(){
		super.onStart();
		threadsAlive = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TTS.stop();
		TTS.shutdown();
	}


	public void setarCorDeFundo(int intColor) {
		setBgColor(intColor);
		//int color = R.color.yellow_smooth;
		String stringColor = getResources().getString(intColor);
		CardView cardView = (CardView) findViewById(R.id.cv);
		cardView.setCardBackgroundColor(Color.parseColor(stringColor));
	}

	public void displayMapActivity(){
		Intent intent = new Intent(this, MapDisplayActivity.class);
		startActivity(intent);
	}


	private void startGenerating() {
		threadsAlive = true;
		randomWork = new DoSomethingThread();
		randomWork.start();
	}

	public void updateResults(String resultado) throws Exception {
		retornoServidorFiware(resultado);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){

		case R.id.start_button:

            boolean isTracking = isTracking();

            if(isTracking){ //then stop and show start button
				stopTrackingService();
			}else{

				if(LocationUtil.isGPSEnabled(this) && getLastLocation()!=null){
                    startTrackingService();
                }else{
                    LocationUtil.showEnableGPSDialog(this);
                }
            }
			break;
		}
	}

	// true if it's tracking, otherwise false
	public void setStartButtonState(boolean isTracking){
		Button startButton = (Button) findViewById(R.id.start_button);
		if(isTracking) {
			ViewCompat.setBackgroundTintList(startButton, ContextCompat.getColorStateList(getApplicationContext(), R.color.red_smooth));
			startButton.setText(getResources().getText(R.string.stop_run));
		}else{
			ViewCompat.setBackgroundTintList(startButton, ContextCompat.getColorStateList(getApplicationContext(), R.color.green_smooth));
			startButton.setText(getResources().getText(R.string.start_run));
		}
	}

    public long getRunElapsedTime(){
		long elapsed = (System.currentTimeMillis()-localRepositoryController.getStartTime());
		return SystemClock.elapsedRealtime() - elapsed;
    }

	private void startTrackingService() {
        Location location = getLastLocation();
        if(location!=null && location.getAccuracy()<30){
            trackingIntent.putExtra("startLocation", location);
            setStartButtonState(true);
            startService(trackingIntent);
            registerReceiver(broadcastReceiver, new IntentFilter(
                    LocationTrackerService.BROADCAST_ACTION));
            mBroadcastIsRegistered = true;
        }else{
            Toast.makeText(this, R.string.low_accuracy_gps, Toast.LENGTH_SHORT).show();
        }
	}

    private void resumeTrackingStatus(){
        if(isTracking()){
            setStartButtonState(true);
        }
    }

	private void stopTrackingService() {
        setStartButtonState(false);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();

        if(polyline !=null) {
            //greenPoint.remove();
            polyline.remove();
            polyline=null;
            ongoingMarker.remove();
            startMarker.remove();
            this.googleMap.setMyLocationEnabled(true);
        }

        trackingIntent.putExtra(Constants.TRACKING_ACTION, Constants.TRACKING_SERVICE_COMMAND_STOP);
        stopService(trackingIntent);


		if (mBroadcastIsRegistered) {
			unregisterReceiver(broadcastReceiver);
			mBroadcastIsRegistered = false;
		}

    }

	private void drawStartPoint(Location startLocation){
        greenPoint = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()))
                .radius(2)
                .strokeColor(ContextCompat.getColor(getApplicationContext(), R.color.green_smooth))
                .fillColor(ContextCompat.getColor(getApplicationContext(), R.color.green_smooth))
                .strokeWidth(16)
                .zIndex(2));
    }

	public class DoSomethingThread extends Thread {

		private static final String TAG = "DoSomethingThread";
		@Override
		public void run() {
			Log.v(TAG, "doing work in Random Number Thread");
			while (true) {
				try {
					publishProgress(fiwareRequest());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void publishProgress(String param) {
			Log.v(TAG, "reporting back from the consumer message Thread");
			final String resultado = param;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						updateResults(resultado);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	public String fiwareRequest() throws Exception {
		int responseCode = 0;
		String json = "";
        String result= "";
		String line = "";

		BufferedReader rd;
		try {
			String uri = Constants.getFiwareBaseAddress(this)+"/v1/queryContext";
            String getAll = "{\"entities\": [{\"type\": \"Ocurrence\",\"isPattern\": \"true\",\"id\": \".*\"}],\"restriction\": " +
                    "{\"scopes\": [{\"type\" : \"FIWARE::Location\",\"value\" : {\"circle\": {\"centerLatitude\": \"" +
                    latitudeString +"\",\"centerLongitude\": \"" +longitudeString +"\",\"radius\": \"30\"}}}]}}";
			OkHttpClient client = new OkHttpClient();
			RequestBody body = RequestBody.create(JSON, getAll);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();

            Response response;

            int executeCount = 0;
            do
            {
                response = client.newCall(request).execute();
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);

            result = response.body().string();
            json = new JSONObject(result).toString();

		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();
		}
		Thread.sleep(500);
		return json;
	}

	public void tarefaParalelaTempo() {
			// Instanciando a asynktask para contato com o servi?o de tempo

		tempo = new AsyncTempo(MainActivity.this);
		tempo.execute(latitudeString, longitudeString);
		if(firstForecast){
            firstForecast = false;
        }

    }




	public void retornoServidorFiware(String retorno) throws Exception {
        if (!retorno.contains("No context element found") && !retorno.equals("")) {
			String distance = getDistanceLocation(retorno);
            Ocorrencia occ = getTipoOcorrencia(retorno);
			String title = occ.getTitle();
			if (distance != null && Double.valueOf(distance) <= 30) {

                setOccurenceCard(occ, distance);

				if (doVoiceAlert) {
					atual = System.nanoTime();
					if ((Double.parseDouble(distance) <= 30.0)) {
						if (first) {
							anterior = atual;
							if(threadsAlive) {
								notificacaoVoz(title, distance);
							}
							first = false;
						} else if (atual - anterior > 30000000000.0f) {
							if(threadsAlive) {
								notificacaoVoz(title, distance);
							}
							anterior = atual;
						}
					}
				}
			}

			Log.v("DIST", distance);
		} else {
			txtMensagem.setText("Nenhum alerta");
            TextView textAlertDetailsView = (TextView) findViewById(R.id.alert_details);
            textAlertDetailsView.setText("");
            ImageView iconWeather = (ImageView) findViewById(R.id.alert_img);
            iconWeather.setImageResource(R.drawable.no_alert);
			setarCorDeFundo(R.color.branco);
			setOccurenceCardTextColor(R.color.branco);
		}

	}

    private void setOccurenceCard(Ocorrencia occ, String distance) {

        int occurenceTypeID = occ.getOccurenceCode();
		int occurenceColor = getOcurrenceColor(occurenceTypeID);

        setarCorDeFundo(occurenceColor);

        ImageView iconWeather = (ImageView) findViewById(R.id.alert_img);
        iconWeather.setImageResource(getImageViewTypeID(occurenceTypeID));

        TextView textMessageView = (TextView) findViewById(R.id.txtMensagem);
        textMessageView.setText(occ.getTitle() + ": " + (int) Double.parseDouble(distance) + "m");

        TextView textAlertDetailsView = (TextView) findViewById(R.id.alert_details);
        textAlertDetailsView.setText("Mantenha-se atento");

		setOccurenceCardTextColor(occurenceColor);

    }

	private void setOccurenceCardTextColor(int color){

		TextView textMessageView = (TextView) findViewById(R.id.txtMensagem);
		TextView alertDetailsView = (TextView) findViewById(R.id.alert_details);

		if(color==R.color.branco){
			textMessageView.setTextColor(ContextCompat.getColor(this, R.color.gray_11));
			alertDetailsView.setTextColor(ContextCompat.getColor(this, R.color.gray_9));
		}else{
			textMessageView.setTextColor(ContextCompat.getColor(this, R.color.branco));
			alertDetailsView.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
		}

	}
    private int getOcurrenceColor(int occurenceTypeID){
        int markColor = R.color.branco;

        switch (occurenceTypeID){
            case 0:
                markColor = R.color.red_smooth;
                break;
            case 1:
                markColor = R.color.yellow_smooth;
                break;
            case 2:
                markColor = R.color.yellow_smooth;
                break;
            case 3:
                markColor = R.color.yellow_smooth;
                break;
        }
        return markColor;
    }

    private int getImageViewTypeID(int occurenceTypeID){
        int markerViewTypeID = R.layout.mark_layout;

        switch (occurenceTypeID){
            case 0:
                markerViewTypeID = R.drawable.mark_accident_spot;
                break;
            case 1:
                markerViewTypeID = R.drawable.mark_heavy_traffic;
                break;
            case 2:
                markerViewTypeID = R.drawable.mark_bad_sinalization;
                break;
            case 3:
                markerViewTypeID = R.drawable.mark_rout_damaged;
                break;
        }
        return markerViewTypeID;
    }

    private void notificacaoVoz(String title, String distance){
        String mensagem = "Alerta: "+title + ((int)Double.parseDouble(distance))
                + "metros";
        //definir scopo de quando mandar a mensagem de voz, como identificar quando mandar.
        TTS.setPitch(1); // Afina??o da Voz
        TTS.setSpeechRate(1);//Velocidade da Voz
        TTS.setLanguage(new Locale("pt", "BR"));
        TTS.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null);
    }

	// xherman

	public String getDistanceLocation(String result) throws Exception {
		List<Entity> listEntity = AdapterOcurrence.parseListEntity(result);
		double minDistance = 0;
		double distance = 0;
		boolean isFirst = true;
		for (Entity entity : listEntity) {
			for (Attributes att : entity.getAttributes()) {
				if (att.getName().equalsIgnoreCase("GPSCoord")) {
					String[] tokensVal = att.getValue().split(",");
					distance = distance(tokensVal[0].trim(),
							tokensVal[1].trim(), latitudeString,
							longitudeString, 'M');
					if (isFirst) {
						minDistance = distance;
						isFirst = false;
					} else {
						if (minDistance < distance) {
							minDistance = distance;
						}
					}
				}
			}
		}
		return String.valueOf(minDistance);
	}

	public Ocorrencia getTipoOcorrencia(String result) throws Exception {
		List<Entity> listEntity = AdapterOcurrence.parseListEntity(result);
		boolean isFirst = true;
        Ocorrencia occ = new Ocorrencia();
		String title = "";
		for (Entity entity : listEntity) {
            occ = AdapterOcurrence.toOcurrence(entity);
		}
		return occ;
	}


	public void setTempoMain(Tempo tempoMain) {
		this.tempoLocal = tempoMain;

		if (tempoMain.getTemperatura() != null) {
			ImageView iconWeather = (ImageView) findViewById(R.id.iconWeather);
			TextView txtTemp = (TextView) findViewById(R.id.temperatura);
			TextView txtDesc = (TextView) findViewById(R.id.previsao);
//			TextView txtUom = (TextView) findViewById(R.id.txt_uom_temp);

			Integer temperatura = Double.valueOf(tempoLocal.getTemperatura())
					.intValue();

			// Exibindo o ?cone
			iconWeather.setImageResource(
                    tempoLocal.getIcone());

			// Exibindo a temperatura
			txtTemp.setText(temperatura.toString()+"\u2103");

			// Exibindo ˚C
			//txtUom.setText("˚C");

			// Exibindo a descricao
			txtDesc.setText(StringUtils.capitalize(tempoLocal.getDescricao().trim()));
		}
	}

	public String getLatitudeString() {
		return latitudeString;
	}

	public String getLongitudeString() {
		return longitudeString;
	}

	/**
	 * Este metodo recebe a resposta da chamada da ActivitySendNotification
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 30) {
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Retorno",
						Toast.LENGTH_LONG).show();
				setarCorDeFundo(R.color.red_smooth);
			}
		}
	}


	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public double calcularCaloria() {


		double distance = distance(latitudeString, longitudeString,
				lastLatitudeString, lastLongitudeString, 'M');

		double durationSec = (timePosition - timeLastPosition) * (1 / 1000.0);
		double durationMin = durationSec * (1 / 1000.0) * (1 / 60.0);

		// Speed in m/s
		// double speed = (lastLocation.getSpeed() + newLocation.getSpeed()) /
		// 2.0;
		double speed = distance / durationSec;

		txtResultado.setText("Dist?ncia: " + distance + "\n Tempo: "
				+ durationSec + "s \n " + speed + "m/s");

		// Duration in min
		// double duration = (double) (newLocation.getTime() -
		// lastLocation.getTime()) * (1/1000.0) * (1/60.0);

		double power = EARTH_GRAVITY * WEIGHT * speed * (K1 + GRADE) + K2
				* (speed * speed * speed);

		// WorkRate in kgm/min
		double workRate = power * W_TO_KGM;

		// VO2 in kgm/min/kg 1.8 = oxygen cost of producing 1 kgm/min of power
		// output. 7 = oxygen cost of unloaded cycling plus resting oxygen
		// consumption
		double vo2 = (1.8 * workRate / WEIGHT) + 7;

		// Calorie in kcal
		totalCalorias = totalCalorias + vo2 * durationMin * WEIGHT
				* KGM_TO_KCAL;
		;

		// Toast.makeText(this, "calorias: " + totalCalorias,
		// Toast.LENGTH_LONG).show();

		// TextView txtCalorias = (TextView) findViewById(R.id.txtResultado);
		// txtCalorias.setText("Calorias:" + totalCalorias);
		txtResultado.setText(String.format("%.4f", totalCalorias) + " Cal");

		return totalCalorias;
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: Esse m?todo calcula a dist?ncia em K, M ou N : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double distance(String latitude1, String longitude1,
			String latitude2, String longitude2, char unit) {

		double lat1 = Double.valueOf(latitude1).doubleValue();
		double lon1 = Double.valueOf(longitude1).doubleValue();
		double lat2 = Double.valueOf(latitude2).doubleValue();
		double lon2 = Double.valueOf(longitude2).doubleValue();
		double dist = 0.0;
		double R = 6372.8; // In kilometers

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));

		dist = R * c;

		if (unit == 'K') {
			dist = dist * 1.609344;

		} else if (unit == 'N') {
			dist = dist * 0.8684;

		} else if (unit == 'M') {
			dist = dist * 1000.0;
		}
		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public void setLatitudeString(String latitudeString) {
		this.latitudeString = latitudeString;
	}

	public void setLongitudeString(String longitudeString) {
		this.longitudeString = longitudeString;
	}

	public void setTimePosition(long timePosition) {
		this.timePosition = timePosition;
	}

	public void setLastLatitudeString(String lastLatitudeString) {
		this.lastLatitudeString = lastLatitudeString;
	}

	public void setLastLongitudeString(String lastLongitudeString) {
		this.lastLongitudeString = lastLongitudeString;
	}

	public String getLastLatitudeString() {
		return lastLatitudeString;
	}

	public String getLastLongitudeString() {
		return lastLongitudeString;
	}

	public void setTimeLastPosition(long timeLastPosition) {
		this.timeLastPosition = timeLastPosition;
	}


	private synchronized void callConnection(){
		Log.i("LOG", "UpdateLocationActivity.callConnection()");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addOnConnectionFailedListener(this)
				.addConnectionCallbacks(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

										//mili * sec * minute
										//1000 * 60 * 1
	private static final long INTERVAL = 1000 * 4;
	private static final long FASTEST_INTERVAL = 1000 * 2;
    private static final long SMALLEST_DISPLACEMENT = 10;

	private void initLocationRequest(){
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); // deslocamento mínimo em metros
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}


	private void startLocationUpdate(){
        if(PermissionRequest.checkLocationPermission(this)){
            initLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            PermissionRequest.requestLocationPermission(this);
        }
	}


	private void stopLocationUpdate(){
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}


	// LISTENERS
	@Override
	public void onConnected(Bundle bundle) {
		Log.i("LOG", "UpdateLocationActivity.onConnected(" + bundle + ")");

		mLastLocation = getLastLocation(); // PARA JÁ TER UMA COORDENADA PARA O UPDATE FEATURE UTILIZAR

        if(mLastLocation !=null){
            LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
		}

		startLocationUpdate();
	}

	private Location getLastLocation(){
		if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
			return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		return null;
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i("LOG", "UpdateLocationActivity.onConnectionSuspended(" + i + ")");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i("LOG", "UpdateLocationActivity.onConnectionFailed(" + connectionResult + ")");
	}


	@Override
	public void onLocationChanged(Location loc) {

		// Do something
		// Atualizando as informacoes do app
		setLatitudeString(String.valueOf(loc.getLatitude()));
		setLongitudeString(String.valueOf(loc.getLongitude()));
		if (firstLocation) {
			startGenerating();
			firstLocation = false;
		}

		// setando o momento da coordenada
		setTimePosition(System.currentTimeMillis());

		// calculandoa caloria do ultimo percurso
		if (getLastLatitudeString() != null) {
			if (getLastLongitudeString() != null) {
				//calcularCaloria();
			}
		}

		// setando a coordenada
		setLastLatitudeString(latitudeString);
		setLastLongitudeString(longitudeString);

		// setando o momento da coordenada
		setTimeLastPosition(timePosition);

        if(!latitudeString.equals("")&& !longitudeString.equals("")){
			atualForecast = System.nanoTime();
			if (firstForecast) {
				anteriorForecast = atualForecast;
				tarefaParalelaTempo();
				firstForecast = false;
				//forecast
			} else if (atualForecast - anteriorForecast > 60000000000.0f) {
				anteriorForecast = atualForecast;
				// forecast
				tarefaParalelaTempo();
			}
		}

		//updateTracking(loc);
	}

	String mLastUpdateTime;
	Polyline polyline;
    Marker startMarker;
    Marker ongoingMarker;
    Circle greenPoint;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent serviceIntent) {

            updateTracking(serviceIntent);
        }
    };

	private void updateTracking(Intent serviceIntent){
		ArrayList<GeoLocation> latLngPoints = serviceIntent.getParcelableArrayListExtra("trackingPoints");
		if(latLngPoints!=null &&!latLngPoints.isEmpty()) {
			ArrayList<LatLng> points = new ArrayList<>(latLngPoints.size() + 1);
			for (GeoLocation point : latLngPoints) {
				points.add(new LatLng(point.getLatitude(), point.getLongitude()));
			}
			if (isTracking()) {
				chronometer.setBase(getRunElapsedTime());
				chronometer.start();
				drawPolyline(points);
			}
		}
	}

    //TODO refactor to a PreferenceManager class
	public boolean isTracking(){
		return localRepositoryController.isTracking();
    }


	private boolean isLocationValid(Location lastLocation, Location nextLocation){
		double accuracy = nextLocation.getAccuracy();
		if(accuracy==0.0) return false; // means there's no accuracy

		double timeDelta = nextLocation.getElapsedRealtimeNanos() - lastLocation.getElapsedRealtimeNanos();
        timeDelta /= 1e9;
		List<LatLng> points = new ArrayList<>();
		points.add( new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
		points.add(new LatLng(nextLocation.getLatitude(), nextLocation.getLongitude()));

		double path = SphericalUtil.computeLength(points);
	//	if((path/timeDelta)>17) return false;

		if(accuracy<15) return true;

		return false;
	}

    public double getAverageSpeed(){
        return ((int)(SphericalUtil.computeLength(polyline.getPoints()) / ((SystemClock.elapsedRealtime()-chronometer.getBase())/1000))) * 3.6;
    }

	private void drawPolyline(ArrayList<LatLng> points){

        if (polyline == null) {
            polyline = googleMap.addPolyline(new PolylineOptions()
                    .width(8)
                    .color(ContextCompat.getColor(getApplicationContext(), R.color.red_smooth))
                    .geodesic(true)
                    .zIndex(1));

            startMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(points.get(0).latitude, points.get(0).longitude))
                    .title("Start")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getMarkerView(R.layout.start_flag_layout))))
                    .anchor(0.5f, 0.5f));
            ongoingMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(points.get(0).latitude, points.get(0).longitude))
                    .title("Ongoing")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getMarkerView(R.layout.ongoing_flag_layout))))
                    .anchor(0.5f, 0.5f));
            this.googleMap.setMyLocationEnabled(false);
        }

        polyline.setPoints(points);
        LatLng lastLatLnt = new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude);
        ongoingMarker.setPosition(lastLatLnt);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLnt, 16));
	}

    private void setToggleVoiceAlert(){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_voice);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				doVoiceAlert = isChecked;
			}
		});
    }

	@Override
    public void onGetOccurrencesCompleted(List<Ocorrencia> occurrences){
        if(occurrences!=null){
            /*for(Ocorrencia occurrence: occurrences){

                LatLng latLng = new LatLng(Double.parseDouble(occurrence.getLat()), Double.parseDouble(occurrence.getLng()));
                Marker marker = this.addMarker(latLng, occurrence.getOccurenceCode());
                markers.put(marker.getId(), String.valueOf(occurrence.getIdOcorrencia()));
            }*/
			List<Marcador> list = new ArrayList<>();;
			Iterator<Ocorrencia> it = null;
			it = occurrences.iterator();
			while (it.hasNext()) {
				Ocorrencia o = it.next();
				Marcador m = new Marcador(new LatLng(Double.parseDouble(o.getLat()),Double.parseDouble(o.getLng())),
						OCCURRENCES[o.getOccurenceCode().intValue()],
						o.getOccurenceCode().intValue(), o.getIdOcorrencia());
				list.add(m);
				//markers.put(String.valueOf(mClusterManager.getMarkerManager().), String.valueOf(o.getIdOcorrencia()));
			}
			mClusterManager.addItems(list);
			mClusterManager.setRenderer(new OwnIconRendered(this, this.googleMap, mClusterManager));

        }
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
    public Marker addMarker(LatLng latLng, int occurenceTypeID){

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(OCCURRENCES[occurenceTypeID]).icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon(occurenceTypeID)));
        return googleMap.addMarker(markerOptions);
    }

    public Bitmap getBitmapIcon(int occurenceTypeID){
        View markerView = getOccurrenceMarkerView(occurenceTypeID);
        Bitmap markerBmp =  createDrawableFromView(markerView);
        return markerBmp;
    }
	public View getMarkerView(int viewId){
		View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(viewId, null);
		return marker;
	}
    public View getOccurrenceMarkerView(int occurenceTypeID){
        //OCCURRENCES = {"Local de acidente", "Tráfego intenso", "Sinalização Ruim", "Via danificada"};
		return getMarkerView(getMarkViewOccurenceTypeID(occurenceTypeID));
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

    private int getMarkViewOccurenceTypeID(int occurenceTypeID){
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

	public void openPopUp(View view){
		Context context = view.getContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Deseja exportar os pontos do mapa?")
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						AsyncCreateAndWriteFile asyncCreateAndWriteFile = new AsyncCreateAndWriteFile(MainActivity.this);
						asyncCreateAndWriteFile.execute();
					}
				}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		builder.create().show();
	}

	private class OwnIconRendered extends DefaultClusterRenderer<Marcador> {

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
	}
}
