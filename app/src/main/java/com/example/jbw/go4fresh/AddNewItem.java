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
import android.widget.Spinner;
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

public class AddNewItem extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    EditText itemName, itemType, itemQuantity, minPrice;
    Button addItemButton, cancelButton;
    Spinner itemQuality;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);


        itemName = (EditText) findViewById(R.id.itemName);
        itemType = (EditText) findViewById(R.id.itemType);
        itemQuantity = (EditText) findViewById(R.id.itemQuantity);
        //itemQuality = (EditText) findViewById(R.id.itemQuality);
        itemQuality = (Spinner) findViewById(R.id.spinner);
        minPrice = (EditText) findViewById(R.id.minPrice);

        addItemButton = (Button) findViewById(R.id.addItemButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        addItemButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        addItemButton.setOnTouchListener(this);
        cancelButton.setOnTouchListener(this);

    }

    void saveProductInfo() {
        final String item_name = itemName.getText().toString().trim();
        final String item_type = itemType.getText().toString().trim();
        final String item_quantity = itemQuantity.getText().toString();
        final String item_quality = itemQuality.getSelectedItem().toString().trim();
        final String min_price = minPrice.getText().toString().trim();
        int userId = SharedPrefManager.getInstance(this).getUserId();
        final String user_id = String.valueOf(userId);
        int quantity=Integer.valueOf(item_quantity);

        if (item_name.equals("") || item_type.equals("") || item_quantity.equals("") || item_quality.equals("")
                || min_price.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Required fields", Toast.LENGTH_LONG).show();
        } else if (item_quantity.equals("0") || min_price.equals("0")) {
            Toast.makeText(getApplicationContext(), "Product Quantity or Minimum price cannot be zero", Toast.LENGTH_LONG).show();
        }
        else if(quantity<=10){
            Toast.makeText(getApplicationContext(), "Product Quantity is greater than or equal to 10kg", Toast.LENGTH_LONG).show();
            }
        else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.PRODUCT_INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(AddNewItem.this, SellProduct.class));
                            }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_name", item_name);
                    params.put("product_type", item_type);
                    params.put("product_quantity", item_quantity);
                    params.put("product_quality", item_quality);
                    params.put("minprice", min_price);
                    params.put("userId", user_id);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == cancelButton) {
            finish();
            startActivity(new Intent(this, SellProduct.class));
        }
        if (view == addItemButton) {
            saveProductInfo();
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.getBackground().setColorFilter(0xe000994C, PorterDuff.Mode.SRC_ATOP);
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
