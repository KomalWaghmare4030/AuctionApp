package com.example.jbw.go4fresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class BuyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView buyList;
    String userId;
    ProductInfo productInfo;
    List<ProductInfo> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        productList = new ArrayList<>();
        buyList=(ListView) findViewById(R.id.buyList);
        buyList.setOnItemClickListener(this);

        int userId=SharedPrefManager.getInstance(this).getUserId();
        String user_id=String.valueOf(userId);
        loadProducts(user_id);
    }

    void loadProducts(final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PRODUCTS,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                productList.add(new ProductInfo(
                                        product.getString("product_id"),
                                        product.getString("product_name"),
                                        product.getString("product_type"),
                                        product.getString("product_quantity"),
                                        product.getString("product_quality"),
                                        product.getString("minprice"),
                                        product.getString("userId")
                                ));
                            }
                            ProductBuyAdapter adapter = new ProductBuyAdapter(BuyActivity.this, productList);
                            buyList.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuyActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        productInfo=(ProductInfo) parent.getItemAtPosition(position);
        Intent i=new Intent(BuyActivity.this,ViewProduct.class);
        Bundle bundle = new Bundle();
        bundle.putString("Id", productInfo.getId());
        bundle.putString("Name", productInfo.getItemName());
        bundle.putString("type", productInfo.getItemType());
        bundle.putString("quantity", productInfo.getItemQuantity());
        bundle.putString("quality", productInfo.getItemQuantity());
        bundle.putString("price", productInfo.getMinPrice());
        bundle.putString("userId", productInfo.getUserId());
        i.putExtras(bundle);
        startActivity(i);
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

    @Override
    public void onContentChanged() {
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.buyList);
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
