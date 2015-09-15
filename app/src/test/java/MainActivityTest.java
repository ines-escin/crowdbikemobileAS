import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.w3c.dom.Text;

import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import br.ufpe.cin.br.adapter.crowdbikemobile.Attributes;
import br.ufpe.cin.br.adapter.crowdbikemobile.Entity;
import br.ufpe.cin.contexto.crowdbikemobile.MainActivity;

import com.google.gson.Gson;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
		super(MainActivity.class);
    }

	protected void setUp() throws Exception{
		super.setUp();
	}

	@Test
	public void testVoiceAlert() {
		long time = 0;
		long finalTime = 0;
		TextToSpeech TTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
			}
		});

		boolean isSpeaking = false;
		boolean speak = false;

		if (speak) {
			speak = false;
			finalTime = time;
			TTS.setSpeechRate(1);
			TTS.setPitch(1);
			TTS.speak("Alerta: 5,2 metros", TextToSpeech.QUEUE_FLUSH, null);
			isSpeaking = TTS.isSpeaking();
		} else if (time - finalTime > 30000000000.0) {
			TTS.setSpeechRate(1);
			TTS.setPitch(1);
			TTS.speak("Alerta: 5,2 metros", TextToSpeech.QUEUE_FLUSH, null);
			isSpeaking = TTS.isSpeaking();
			finalTime = time;
		}

		assertTrue(isSpeaking);
		assertFalse(isSpeaking);
    }


	public void testRegisterOrionEntity() throws JSONException {

		   String result = "";
			String line = "";
			String IMEI 	 = String.valueOf("358972063059834");
		    Entity entity = new Entity();
			List<Attributes> attributes = new ArrayList<Attributes>();
			Attributes att = new Attributes();
			att.setName("latitude");
			att.setType("String");
			att.setValue("40.418889");
			Attributes att1 = new Attributes();
			att1.setName("longitude");
			att1.setType("String");
			att1.setValue("-3.691944");
			attributes.add(att);
			attributes.add(att1);

			entity.setType("Position");
			entity.setId(IMEI);
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
				fail("Not yet");
			}


	    String s = "{ \"type\" : \"Position\",\"isPattern\" : \"false\", " +
	    		"\"id\" : \"358972063059834\",\"contextResponses\" : " +
	    		"[{\"attributes\" : [{\"name\" : \"latitude\",\"type\" : " +
	    		"\"String\",\"value\" : \"\"},{\"name\" : \"longitude\"," +
	    		"\"type\" : \"String\",\"value\" : \"\"}],\"statusCode\" : " +
	    		"{\"code\" : \"200\",\"reasonPhrase\" : \"OK\"}}]}";

	    JSONAssert.assertEquals(s, result, false);
	}


}
