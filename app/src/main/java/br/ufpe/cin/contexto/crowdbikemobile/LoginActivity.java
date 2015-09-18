package br.ufpe.cin.contexto.crowdbikemobile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.crowdbikemobile.R;
import com.squareup.okhttp.OkHttpClient;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setButtonLogin();
        setEditText();
    }

    private void setEditText(){
        EditText usernameField = (EditText) findViewById(R.id.login_field);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);

        usernameField.setBackgroundResource(R.drawable.green_edit_text_holo_light);
        passwordField.setBackgroundResource(R.drawable.green_edit_text_holo_light);
        if(usernameField.isActivated())
            usernameField.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
        if(passwordField.isActivated())
            passwordField.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
    }

    private void setButtonLogin()
    {
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setBackgroundResource(R.drawable.green_btn_default_normal_holo_light);
        loginButton.setOnClickListener(loginButtonListener);
    }

    public OnClickListener loginButtonListener = new OnClickListener()
    {
        @Override
        public void onClick (View v)
        {
            v.setBackgroundResource(R.drawable.green_btn_default_pressed_holo_light);
            boolean invalidLoginData = false;

            EditText usernameField = (EditText) findViewById(R.id.login_field);
            EditText passwordField = (EditText) findViewById(R.id.passwordField);

            Editable usernameEditable  = usernameField.getText();
            Editable passwordEditable = passwordField.getText();

            String username;
            String password;

            if(usernameEditable == null || passwordEditable == null)
            {
                invalidLoginData = true;
            }
            else
            {
                username = usernameEditable.toString();
                password = passwordEditable.toString();
            }

            loginApproved();
        }
    };

    public void loginApproved()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
