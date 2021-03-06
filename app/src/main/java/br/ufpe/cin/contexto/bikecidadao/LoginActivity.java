package br.ufpe.cin.contexto.bikecidadao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bikecidadao.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Response;

import br.ufpe.cin.contexto.bikecidadao.async.AsyncLogin;

public class LoginActivity extends Activity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(registerButtonListener);
        getWindow().getDecorView().clearFocus();
    }

    public OnClickListener registerButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
        }
    };

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

            String username = "";
            String password = "";


            if(usernameEditable == null || usernameEditable.toString().isEmpty()|| passwordEditable == null || passwordEditable.toString().isEmpty())
            {
                invalidLoginData = true;
            }
            else
            {
                username = usernameEditable.toString();
                password = passwordEditable.toString();
            }

            if(!invalidLoginData) {

                try
                {
                    Response response = new AsyncLogin().execute(username, password).get();
                    if(response != null && response.code() != 408)
                    {
                        loginApproved();
                    }
                    loginApproved();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Nome de usuario ou senha incorretos", Toast.LENGTH_LONG).show();
            }
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
