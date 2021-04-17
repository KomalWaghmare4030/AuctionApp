package com.example.jbw.go4fresh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    EditText item_name, item_type, item_quantity, item_quality, min_price, item_id;
    Button updateButton, deleteButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item);

        item_id = (EditText) findViewById(R.id.productId);
        item_name = (EditText) findViewById(R.id.itemName);
        item_type = (EditText) findViewById(R.id.itemType);
        item_quantity = (EditText) findViewById(R.id.itemQuantity);
        item_quality = (EditText) findViewById(R.id.itemQuality);
        min_price = (EditText) findViewById(R.id.minPrice);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("Name");
        String type = bundle.getString("type");
        String quantity = bundle.getString("quantity");
        String quality = bundle.getString("quality");
        String price = bundle.getString("price");
        String id = bundle.getString("Id");

        item_id.setText(id);
        item_name.setText(name);
        item_type.setText(type);
        item_quantity.setText(quantity);
        item_quality.setText(quality);
        min_price.setText(price);

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        updateButton.setOnTouchListener(this);
        deleteButton.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == updateButton) {
            updateProductInfo();
            Toast.makeText(this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,SellProduct.class));

        }
        if (v == deleteButton) {
            deleteProductInfo();
            Toast.makeText(this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,SellProduct.class));
        }
    }

    void deleteProductInfo() {
        final String product_id = item_id.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.PRODUCT_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("product_id", product_id);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    void updateProductInfo() {
        final String product_id = item_id.getText().toString().trim();
        final String product_name = item_name.getText().toString().trim();
        final String product_type = item_type.getText().toString().trim();
        final String product_quantity = item_quantity.getText().toString();
        final String product_quality = item_quality.getText().toString().trim();
        final String product_price = min_price.getText().toString().trim();

        if (product_name.equals("") || product_type.equals("") || product_quantity.equals("") || product_quality.equals("")
                || product_price.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Required fields", Toast.LENGTH_LONG).show();
        } else if (product_quantity.equals("0") || product_price.equals("0")) {
            Toast.makeText(getApplicationContext(), "Product Quantity or Minimum price cannot be zero", Toast.LENGTH_LONG).show();
        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.PRODUCT_UPDATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                    params.put("product_id", product_id);
                    params.put("product_name", product_name);
                    params.put("product_type", product_type);
                    params.put("product_quantity", product_quantity);
                    params.put("product_quality", product_quality);
                    params.put("minprice", product_price);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.getBackground().setColorFilter(0xe09932CC, PorterDuff.Mode.SRC_ATOP);
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
    @Override
    public void onBackPressed() {
        finish();
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, SellProduct.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchNextActivity);
        super.onBackPressed();
    }
}
