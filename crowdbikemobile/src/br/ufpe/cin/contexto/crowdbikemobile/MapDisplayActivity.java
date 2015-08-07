package br.ufpe.cin.contexto.crowdbikemobile;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.crowdbikemobile.R;
import android.content.Context;
import android.content.Intent;

public class MapDisplayActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_display_map);
		Intent intent = getIntent();
		ArrayList<String> coordinates = (ArrayList<String>) intent
				.getSerializableExtra("COORDINATES");
		
		String latitude = coordinates.get(0);
		String longitude = coordinates.get(1);
		setSpinner();
	}

	private void setSpinner() {

		Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);

		String[] occurrences = { "CPA", "COVP", "CVM2-3R", "CACC", "CTPO",
				"CTVF", "COVNM", "COFE", "ANDT", "AI" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, occurrences);

		spinner.setAdapter(adapter);

	}

	// Send the actual location of the device to Orion.
	// method is going to be placed onCreate of this Activity
	private void sendMyLocation() {

	}

}
