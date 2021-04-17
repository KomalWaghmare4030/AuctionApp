package com.example.jbw.go4fresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    TextView signInLink;
    Button registerNow;
    EditText firstName, lastName, city, userName, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, OptionPage.class));
            return;
        }

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        city = (EditText) findViewById(R.id.city);
        phone = (EditText) findViewById(R.id.phone);
        userName = (EditText) findViewById(R.id.userNameRegister);
        password = (EditText) findViewById(R.id.passwordRegiter);

        signInLink = (TextView) findViewById(R.id.signInLink);
        registerNow = (Button) findViewById(R.id.registerNow);

        signInLink.setOnClickListener(this);
        registerNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == signInLink) {
            finish();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        }
        if (view == registerNow) {
            Toast.makeText(SignupActivity.this, "Registering User...", Toast.LENGTH_SHORT).show();
            registerUser();

        }
    }

    private void registerUser() {
        final String first_name = firstName.getText().toString().trim();
        final String last_name = lastName.getText().toString().trim();
        final String phone_number = phone.getText().toString();
        final String ci_ty = city.getText().toString().trim();
        final String user_name = userName.getText().toString().trim();
        final String pass_word = password.getText().toString().trim();
        int length=pass_word.length();

        if (first_name.equals("") || last_name.equals("") || phone_number.equals("") || ci_ty.equals("")
                || user_name.equals("") || pass_word.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Required fields", Toast.LENGTH_LONG).show();
        } else
        if(length<=6){
            Toast.makeText(getApplicationContext(), "Password should contain minimum 6 characters", Toast.LENGTH_LONG).show();
        }
        else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Invalid Connection", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_firstName", first_name);
                    params.put("user_lastName", last_name);
                    params.put("user_city", ci_ty);
                    params.put("user_phone", phone_number);
                    params.put("user_name", user_name);
                    params.put("user_password", pass_word);

                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }
   /* @Override
    public void onBackPressed() {
        finish();
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, MainActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchNextActivity);
        super.onBackPressed();
    }*/
}
