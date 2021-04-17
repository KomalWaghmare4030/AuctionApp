package com.example.jbw.go4fresh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button signIn;
    TextView signUpLink;
    EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()) {
            finish();
            startActivity(new Intent(MainActivity.this, OptionPage.class));
            return;
        }

        signIn = (Button) findViewById(R.id.signIn);
        signUpLink = (TextView) findViewById(R.id.signUpLink);

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);

        signIn.setOnClickListener(MainActivity.this);
        signUpLink.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View view) {
        if (view == signIn) {
            Toast.makeText(getApplicationContext(), "Signing In...", Toast.LENGTH_SHORT).show();
            userLogin();
        }
        if (view == signUpLink) {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        }
    }

    private void userLogin() {
        final String user_name = userName.getText().toString().trim();
        final String pass_word = password.getText().toString().trim();

        if (user_name.equals("") || pass_word.equals("")) {
            Toast.makeText(this, "Please enter Username and Password", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    obj.getInt("user_id"),
                                                    obj.getString("user_firstName"),
                                                    obj.getString("user_lastName"),
                                                    obj.getString("user_city"),
                                                    obj.getInt("user_phone"),
                                                    obj.getString("user_name")
                                            );
                                    finish();
                                    //Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), OptionPage.class));

                                } else {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_name", user_name);
                    params.put("user_password", pass_word);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


}
