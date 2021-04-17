package com.example.jbw.go4fresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class ProductRateActivity extends AppCompatActivity {

    ListView showPrice;
    List<CostInfo> costInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_rate);

        showPrice = (ListView) findViewById(R.id.showPrice);
        costInfoList = new ArrayList<>();
        Bundle bundle1 = getIntent().getExtras();
        assert bundle1 != null;
        String id = bundle1.getString("Id");

        showCost(id);
    }
    void showCost(final String productId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SHOW_COST,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                costInfoList.add(new CostInfo(
                                        product.getString("user_name"),
                                        product.getString("price")
                                ));
                            }
                            ShowCostAdapter adapter = new ShowCostAdapter(ProductRateActivity.this, costInfoList);
                            showPrice.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductRateActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("productId", productId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
    @Override
    public void onContentChanged() {
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.showPrice);
        list.setEmptyView(empty);
        super.onContentChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.refresh:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}
