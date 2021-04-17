package com.example.jbw.go4fresh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class ViewProduct extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    TextView pName, pType, pQuantity, pQuality, pMinPrice;
    Button buyProductButton,seeRates;
    Bundle bundle;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        pName = (TextView) findViewById(R.id.pName);
        pType = (TextView) findViewById(R.id.pType);
        pQuantity = (TextView) findViewById(R.id.pQuantity);
        pQuality = (TextView) findViewById(R.id.pQuality);
        pMinPrice = (TextView) findViewById(R.id.pMinPrice);
        buyProductButton = (Button) findViewById(R.id.buyProductButton);
        seeRates=(Button) findViewById(R.id.seeRates);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("Name");
        String type = bundle.getString("type");
        String quantity = bundle.getString("quantity");
        String quality = bundle.getString("quality");
        String price = bundle.getString("price");

        pName.setText(" : "+name);
        pType.setText(" : "+type);
        pQuantity.setText(" : "+quantity);
        pQuality.setText(" : "+quality);
        pMinPrice.setText(" : "+price);

        buyProductButton.setOnClickListener(this);
        seeRates.setOnClickListener(this);

        buyProductButton.setOnTouchListener(this);
        seeRates.setOnTouchListener(this);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View v) {
        if (v == buyProductButton) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_price);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            final EditText addCost = (EditText) dialog.findViewById(R.id.addCost);

            Button addButton = (Button) dialog.findViewById(R.id.addButton);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);

            bundle = getIntent().getExtras();
            final String id = bundle.getString("Id");
            String price = bundle.getString("price");
            final int priceInt = Integer.parseInt(price);
            final String sellerId=bundle.getString("userId");

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userId = SharedPrefManager.getInstance(ViewProduct.this).getUserId();
                    final String user_id = String.valueOf(userId);
                    final String add_cost = addCost.getText().toString().trim();



                    if (add_cost.equals("0")) {
                        Toast.makeText(ViewProduct.this, "Price Caanot be Zero", Toast.LENGTH_SHORT).show();
                    } else if (add_cost.equals("")) {
                        Toast.makeText(ViewProduct.this, "Enter Valid Price", Toast.LENGTH_SHORT).show();
                    }else {
                        int costInt = Integer.parseInt(add_cost);
                        if (costInt >= priceInt) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                    Constants.COST_INSERT,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            dialog.dismiss();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("price", add_cost);
                                    params.put("userIdn", user_id);
                                    params.put("productId", id);
                                    params.put("sellerId", sellerId);
                                    return params;
                                }
                            };

                            RequestHandler.getInstance(ViewProduct.this).addToRequestQueue(stringRequest);
                        } else {
                            Toast.makeText(ViewProduct.this, "Please valid values for cost and quantity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            addButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    clickColor(view, motionEvent);
                    return false;
                }
            });
            cancelButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    clickColor(view, motionEvent);
                    return false;
                }
            });

            dialog.show();
        }
        if (v==seeRates)
        {
            finish();
            bundle = getIntent().getExtras();
            final String id = bundle.getString("Id");
            Intent i=new Intent(ViewProduct.this,ProductRateActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("Id", id);
            i.putExtras(bundle1);
            startActivity(i);
        }
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
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
        return false;
    }
}

