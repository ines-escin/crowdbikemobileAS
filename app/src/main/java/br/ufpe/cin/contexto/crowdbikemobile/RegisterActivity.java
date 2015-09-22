package br.ufpe.cin.contexto.crowdbikemobile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.crowdbikemobile.R;
import com.squareup.okhttp.Response;

import br.ufpe.cin.contexto.crowdbikemobile.async.AsyncLogin;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().getDecorView().clearFocus();
        setButton();
        setEditTexts();
    }

    private void setButton(){
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setBackgroundResource(R.drawable.green_btn_default_normal_holo_light);
        registerButton.setOnClickListener(registerButtonListener);
    }

    public View.OnClickListener registerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setBackgroundResource(R.drawable.green_btn_default_pressed_holo_light);

            String username = ((EditText) findViewById(R.id.loginRegisterField)).getText().toString();
            String email = ((EditText) findViewById(R.id.emailRegister)).getText().toString();
            String password= ((EditText) findViewById(R.id.passwordRegisterField)).getText().toString();
            String confirmPassword = ((EditText) findViewById(R.id.RepeatPasswordRegister)).getText().toString();

            if(validateFields(username,email,password,confirmPassword))
            {
                try
                {
                    Response response = new AsyncLogin().execute(username, password, email).get();
                    if(response.code() != 408)
                    {
                        registrationSuccessfull();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    public void registrationSuccessfull()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private boolean validateFields (String username, String email, String password, String confirmPassword){

        if(username == null || username.isEmpty())
        {
            return false;
        }
        if(email == null || email.isEmpty())
        {
            return false;
        }
        else
        {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);

            if(!m.matches())
            {
                return false;
            }
        }

        if(password == null || password.isEmpty()){
            return false;
        }

        if(confirmPassword == null || confirmPassword.isEmpty())
        {
            return false;
        }
        else
        {
            if(!confirmPassword.equals(password))
            {
                return false;
            }
        }

        return  true;
    }

    private void setEditTexts(){

        EditText emailField = (EditText) findViewById(R.id.emailRegister);
        EditText usernameField = (EditText) findViewById(R.id.loginRegisterField);
        EditText passwordField = (EditText) findViewById(R.id.passwordRegisterField);
        EditText repeatPasswordField = (EditText) findViewById(R.id.RepeatPasswordRegister);

        usernameField.setBackgroundResource(R.drawable.green_edit_text_holo_light);
        passwordField.setBackgroundResource(R.drawable.green_edit_text_holo_light);
        emailField.setBackgroundResource(R.drawable.green_edit_text_holo_light);
        repeatPasswordField.setBackgroundResource(R.drawable.green_edit_text_holo_light);
        if(usernameField.isActivated())
            usernameField.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
        if(passwordField.isActivated())
            passwordField.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
        if(emailField.isActivated())
            passwordField.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
        if(repeatPasswordField.isActivated())
            passwordField.setBackgroundResource(R.drawable.green_textfield_activated_holo_light);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
