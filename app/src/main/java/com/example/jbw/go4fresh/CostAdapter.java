package com.example.jbw.go4fresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class CostAdapter extends ArrayAdapter<CostInfo> {

    private Activity contexts;
    private List<CostInfo> lists;

    CostAdapter(Activity context, List<CostInfo> list) {
        super(context, R.layout.cost_item, list);
        this.contexts = context;
        this.lists = list;
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cost_item, parent, false);
        }
        TextView s1 = listItemView.findViewById(R.id.s1);
        TextView s2 = listItemView.findViewById(R.id.s2);
        TextView s3 = listItemView.findViewById(R.id.s3);
        TextView s4 = listItemView.findViewById(R.id.s4);
        TextView s5 = listItemView.findViewById(R.id.s5);
        final Button sell = listItemView.findViewById(R.id.sell);
        final CostInfo costInfo = lists.get(position);

        s1.setText("Rs."+costInfo.getbPrice()+" per Kg");
        s2.setText("by "+costInfo.getUserName());
        s3.setText("City: "+costInfo.getbCity());
        s4.setText("Your Price: Rs."+costInfo.getsMinPrice());
        s5.setText("Quantity: "+costInfo.getsQuantity()+"Kg");

       int userid= SharedPrefManager.getInstance(getContext()).getUserId();
        final String user_id=String.valueOf(userid);
        final String user_name=SharedPrefManager.getInstance(getContext()).getUsername();

        sell.setOnTouchListener(new View.OnTouchListener() {
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
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_account);
                TextView text=(TextView) dialog.findViewById(R.id.text);
                text.setText(R.string.text);
                dialog.show();
                Window window = dialog.getWindow();
                assert window != null;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button yesButton = (Button) dialog.findViewById(R.id.yes);
                Button noButton = (Button) dialog.findViewById(R.id.no);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneNo = costInfo.getbPhone();
                        String message = "Hello sir/mam , Your request for buying prodoct " + costInfo.getpName() + " of "
                                + costInfo.getsQuantity() + " kg " + costInfo.getbPrice() + "per kg is accepted. Thank You for Buying";
                        String productId = costInfo.getProductId();
                        sendSMS(sell,phoneNo, message,user_id, user_name, costInfo.getpName(), costInfo.getsMinPrice(), costInfo.getbPrice(),
                                costInfo.getQuality(),costInfo.getsQuantity(), costInfo.getsUserId(), productId, costInfo.getUserName());
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
        });

        return listItemView;
    }

    private void clickColor(View view, MotionEvent motionEvent){
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

    private void sendSMS(Button sell,String phoneNo, String msg ,String cUserId,String sUserName,String sProductName,String sMinPrice,String sPrice,
                         String sQuality,String sQuantity, String sUserId,  String sProductId,String uName) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            sell.setEnabled(false);
            saveSellInfo(cUserId,sUserName,sProductName,sMinPrice,sPrice,sQuality,sQuantity,sUserId,sProductId,uName);
            Toast.makeText(getContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
            contexts.startActivity(new Intent(getContext(),SellProduct.class));
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void saveSellInfo(final String cUserId,final String sUserName, final String sProductName, final String sMinPrice, final String sPrice,
                             final String sQuality, final String sQuantity, final String sUserId, final String sProductId, final String uName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.SELL_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            saveBuyInfo(sUserId,uName,sProductName,sMinPrice,sPrice, sQuality, sQuantity,cUserId,sProductId);

                            //Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("cUserId",cUserId );
                params.put("sUserName",sUserName );
                params.put("sProductName", sProductName);
                params.put("sMinPrice",sMinPrice );
                params.put("sPrice", sPrice);
                params.put("sQuality", sQuality);
                params.put("sQuantity", sQuantity);
                params.put("sUserId", sUserId);
                params.put("sProductId",sProductId );

                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void saveBuyInfo(final String CurUserId,final String bUserName, final String bProductName, final String bMinPrice, final String bPrice,
                                 final String bQuality, final String bQuantity, final String bUserId, final String bProductId) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.BUY_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    params.put("CurUserId",CurUserId);
                    params.put("bUserName",bUserName );
                    params.put("bProductName", bProductName);
                    params.put("bMinPrice",bMinPrice );
                    params.put("bPrice", bPrice);
                    params.put("bQuality", bQuality);
                    params.put("bQuantity", bQuantity);
                    params.put("bUserId", bUserId);
                    params.put("bProductId",bProductId );

                    return params;
                }
            };

            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
            deleteCostInfo(bProductId);
        }


    private void deleteCostInfo(final String productId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DELETE_COST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            deleteProductInfo(productId);
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getContext(), "cost deleted", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // startActivity(new Intent(AddNewItem.this,SellActivity.class));
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
                params.put("productId", productId);
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void deleteProductInfo(final String productId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.DELETE_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Product info not deleted", Toast.LENGTH_LONG).show();
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
