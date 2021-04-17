package com.example.jbw.go4fresh;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.List;
import java.util.Map;

public class ProductAdapter extends ArrayAdapter<ProductInfo> {
    private Activity contexts;
    private List<ProductInfo> lists;

    ProductAdapter(Activity context, List<ProductInfo> list) {
        super(context, R.layout.list_item, list);
        this.contexts = context;
        this.lists = list;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        TextView t = (TextView) listItemView.findViewById(R.id.t);
        TextView t1 = (TextView) listItemView.findViewById(R.id.t1);
        TextView t2 = (TextView) listItemView.findViewById(R.id.t2);
        TextView t3 = (TextView) listItemView.findViewById(R.id.t3);
        TextView t4 = (TextView) listItemView.findViewById(R.id.t4);
        TextView t5 = (TextView) listItemView.findViewById(R.id.t5);
        TextView t6 = (TextView) listItemView.findViewById(R.id.t6);

        ProductInfo productInfo = lists.get(position);
        t.setText(productInfo.getId());
        t1.setText(productInfo.getItemName());
        t2.setText(productInfo.getItemType());
        t3.setText(productInfo.getItemQuantity()+" kg");
        t4.setText(productInfo.getItemQuality());
        t5.setText("Rs."+productInfo.getMinPrice());
        t6.setText(productInfo.getUserId());
        String productId = productInfo.getId();

        String quantity = productInfo.getItemQuantity();
        if (quantity.equals("0")) {
            deleteProductInfo(productId);
        }
        return listItemView;
    }
    private void deleteProductInfo(final String productId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DELETE_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Invalid Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", productId);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}

