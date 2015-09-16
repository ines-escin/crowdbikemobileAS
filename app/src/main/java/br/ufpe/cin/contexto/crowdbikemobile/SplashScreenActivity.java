package br.ufpe.cin.contexto.crowdbikemobile;

import com.example.crowdbikemobile.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
	private static final int DEFAULT_DELAY_TIME = 3000; // 3 seconds
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
	    new SwitchToMain().execute();
	}

	private class SwitchToMain extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
			ImageView image = (ImageView) findViewById(R.id.splash);
			Animation fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
			fadeIn.setDuration(1000);

			Animation fadeOut = new AlphaAnimation(1, 0);
			fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
			fadeOut.setStartOffset(2000);
			fadeOut.setDuration(1000);

			AnimationSet animation = new AnimationSet(false); //change to false
			animation.addAnimation(fadeIn);
			animation.addAnimation(fadeOut);
			image.setVisibility(View.INVISIBLE);
			image.setAnimation(animation);
			Thread.sleep(DEFAULT_DELAY_TIME + 500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result){

			Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
			startActivity(intent);
			finish();
			super.onPostExecute(result);
		}
		
	}
}
