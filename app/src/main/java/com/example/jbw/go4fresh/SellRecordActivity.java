package com.example.jbw.go4fresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellRecordActivity extends AppCompatActivity {

    ListView buyDetailList;
    List<CostInfo> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_record);

        buyDetailList=(ListView) findViewById(R.id.buyerDetails);
        productList=new ArrayList<>();
        String user_id=String.valueOf(SharedPrefManager.getInstance(this).getUserId());
        loadSellDetails(user_id);
    }

    void loadSellDetails(final String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SHOW_BUY_DETAILS,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                productList.add(new CostInfo(
                                        product.getString("bUserName"),
                                        product.getString("bProductName"),
                                        product.getString("bMinPrice"),
                                        product.getString("bPrice"),
                                        product.getString("bQuality"),
                                        product.getString("bQuantity"),
                                        product.getString("bDate")
                                ));
                            }
                            DetailAdapter adapter = new DetailAdapter(SellRecordActivity.this, productList);
                            buyDetailList.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SellRecordActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bUserId", userId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
    @Override
    public void onContentChanged() {
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.buyerDetails);
        list.setEmptyView(empty);
        super.onContentChanged();
    }
     @Override
    public void onBackPressed() {
        finish();
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, OptionPage.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchNextActivity);
        super.onBackPressed();
    }
}
