package com.example.jbw.go4fresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class CostActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 123;
    ListView costList;
    EditText pId;
    List<CostInfo> costInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        costList = (ListView) findViewById(R.id.costList);
        costInfoList = new ArrayList<>();
        pId = (EditText) findViewById(R.id.pId);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String pid = bundle.getString("productId");
        pId.setText(pid);

        showCost();
    }
    void showCost() {
        final String productId = pId.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.COST_DISPLAY,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                costInfoList.add(new CostInfo(
                                        product.getString("user_firstName"),
                                        product.getString("user_lastName"),
                                        product.getString("user_city"),
                                        product.getString("user_phone"),
                                        product.getString("minprice"),
                                        product.getString("price"),
                                        product.getString("product_name"),
                                        product.getString("product_quantity"),
                                        product.getString("product_id"),
                                        product.getString("cost_id"),
                                        product.getString("user_name"),
                                        product.getString("product_quality"),
                                        product.getString("user_id")
                                ));
                            }
                            CostAdapter adapter = new CostAdapter(CostActivity.this, costInfoList);
                            costList.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CostActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
        ListView list = (ListView) findViewById(R.id.costList);
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
