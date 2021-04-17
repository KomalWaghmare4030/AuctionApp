package com.example.jbw.go4fresh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

public class OptionPage extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_READ_PHONE_STATE = 1;
    CardView sellCardView,buyCardView,infoCardView;
    ImageView sellImageView,buyImageView,infoImageView;
    TextView sellTextView,buyTextView,infoTextView;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);

        sellCardView=(CardView) findViewById(R.id.sellCardView);
        buyCardView=(CardView) findViewById(R.id.buyCardView);
        infoCardView=(CardView) findViewById(R.id.infoCardView);

        sellImageView=(ImageView) findViewById(R.id.sellImageView);
        buyImageView=(ImageView) findViewById(R.id.buyImageView);
        infoImageView=(ImageView) findViewById(R.id.infoImageView);

        sellTextView=(TextView) findViewById(R.id.sellTextView);
        buyTextView=(TextView) findViewById(R.id.buyTextView);
        infoTextView=(TextView) findViewById(R.id.infoTextView);

        sellCardView.setOnClickListener(this);
        sellImageView.setOnClickListener(this);
        sellTextView.setOnClickListener(this);

        buyCardView.setOnClickListener(this);
        buyImageView.setOnClickListener(this);
        buyTextView.setOnClickListener(this);

        infoCardView.setOnClickListener(this);
        infoImageView.setOnClickListener(this);
        infoTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.sellCardView:
            case R.id.sellImageView:
            case R.id.sellTextView:
                startActivity(new Intent(this,SellProduct.class));
                break;

            case R.id.buyCardView:
            case R.id.buyImageView:
            case R.id.buyTextView:
                startActivity(new Intent(this,BuyActivity.class));
                break;

            case R.id.infoCardView:
            case R.id.infoImageView:
            case R.id.infoTextView:
                //Toast.makeText(this,"Information Activity",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,InfoActivity.class));
                break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.uname).setTitle("User : " +SharedPrefManager.getInstance(this).getUsername());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.uname:
                Toast.makeText(this,"User : " +SharedPrefManager.getInstance(this).getUsername(),Toast.LENGTH_SHORT).show();
                break;
                case R.id.sellHistory:
                finish();
                startActivity(new Intent(this, SellRecordActivity.class));
                break;

            case R.id.buyHistory:
                finish();
                startActivity(new Intent(this, BuyRecordActivity.class));
                break;

            case R.id.logOut :
                finish();
                SharedPrefManager.getInstance(this).logout();
                Toast.makeText(this,"Signing Out...",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.delete_account:
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    deleteUser();
                }
                else {
                    Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                Toast.makeText(getApplication(),"Please select valid option",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    @SuppressLint("ClickableViewAccessibility")
    void deleteUser()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_account);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button yesButton = (Button) dialog.findViewById(R.id.yes);
        Button noButton = (Button) dialog.findViewById(R.id.no);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OptionPage.this,"Deleting account...",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                deleteCostInfo();
                SharedPrefManager.getInstance(OptionPage.this).logout();
                finish();
                startActivity(new Intent(OptionPage.this, MainActivity.class));
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yesButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickColor(view, motionEvent);
                return false;
            }
        });
        noButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickColor(view, motionEvent);
                return false;
            }
        });

    }

    void clickColor(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.getBackground().setColorFilter(0xe000994C	, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                view.getBackground().clearColorFilter();
                view.invalidate();
                break;
            }
        }
    }
    private void deleteCostInfo() {
        final String userId=String.valueOf(SharedPrefManager.getInstance(this).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DELETE_COST_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            deleteProductInfo(userId);
                            Toast.makeText(getApplicationContext(), "cost deleted 1", Toast.LENGTH_SHORT).show();
                           // JSONObject jsonObject = new JSONObject(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error while deleteing cost", Toast.LENGTH_LONG).show();
                        }
                        // startActivity(new Intent(AddNewItem.this,SellActivity.class));
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
                params.put("sellerId", userId);
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void deleteProductInfo(final String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DELETE_PRODUCT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            deleteUserInfo(userId);
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), "Product deleted", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // startActivity(new Intent(AddNewItem.this,SellActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Product info not deleted", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void deleteUserInfo(final String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DELETE_USER_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), "Product deleted", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // startActivity(new Intent(AddNewItem.this,SellActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Product info not deleted", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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
    @Override
    protected void onStart() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_READ_PHONE_STATE);
        super.onStart();
    }

}
