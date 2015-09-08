package br.ufpe.cin.contexto.crowdbikemobile;

import br.ufpe.cin.contexto.crowdbikemobile.async.AsyncSendNotification;

import com.example.crowdbikemobile.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ActivitySendNotification extends Activity {
	
	private String latitudeString;
	private String longitudeString;
	private int cor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_send_notification);
		
		Intent it = getIntent();
		
		if(it!=null){
			Bundle params = it.getExtras();
			if(params!=null){
				latitudeString	= params.getString("latitude");
				longitudeString = params.getString("longitude");
				cor 			= params.getInt("cor");
				
				setarCorDeFundo(cor);
				Toast.makeText(getApplicationContext(), "Send " + latitudeString + ", " + longitudeString, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	 /**
     * Este metodo seta a cor de fundo do aplicativo
     * @param cor	C�digo inteiro da cor. A lista de cores dispon�veis est� em res/calues/colors.xml
     */
    public void setarCorDeFundo(int intColor){
    	String stringColor = getResources().getString(intColor);
    	
    	//Setando o fundo do app
    	LinearLayout layoutApp = (LinearLayout)findViewById(R.id.bg_activity_send_notification);
    	layoutApp.setBackgroundColor(Color.parseColor(stringColor));
    	
    	//Setando o fundo do bot�o
		Button botaoSend = (Button)findViewById(R.id.btn_send_notification);
		
		if(intColor == R.color.verde){
			botaoSend.setBackgroundResource(R.drawable.button_shape_verde);
		}else if(intColor == R.color.vermelho){
			botaoSend.setBackgroundResource(R.drawable.button_shape_vermelho);
		}
		
    }
    
    /**
     * Ao do boto SEND
     * @param v
     */
	public void sendNotification(View v){
		int id;
		RadioGroup  radioGroup = (RadioGroup) findViewById(R.id.radio_type);
		id = radioGroup.getCheckedRadioButtonId();
		
		RadioButton radioButton = (RadioButton) findViewById(id);
		
		AsyncSendNotification task = new AsyncSendNotification(this);
		task.execute(latitudeString, longitudeString, radioButton.getText().toString());
		
		//Assumindo que foi enviado com sucesso
		String retorno = "OK";
		
		Bundle bundle = new Bundle();
		bundle.putString("RETURN", retorno);
		
		Intent it = new Intent();
		it.putExtras(bundle);
		
		setResult(RESULT_OK, it);
		finish();
	}

}
