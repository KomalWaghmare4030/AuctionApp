package com.example.jbw.go4fresh;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JBW on 11/03/2018.
 */

public class ProductSharedManager {
    private static ProductSharedManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref23";
    private static final String KEY_productId = "product_id";
    private static final String KEY_productName = "product_name";
    private static final String KEY_productType = "product_type";
    private static final String KEY_productQuantity = "product_quantity";
    private static final String KEY_productQuality = "product_quality";
    private static final String KEY_minPrice = "minprice";
    private static final String KEY_userId = "userId";


    private ProductSharedManager(Context context) {
        mCtx = context;

    }

    public static synchronized ProductSharedManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ProductSharedManager(context);
        }
        return mInstance;
    }

    public boolean productDisplay(String item_name, String item_type, int item_quantity, String item_quality, int minprice, int userId) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //editor.putInt(KEY_productId, id);
        editor.putString(KEY_productName, item_name);
        editor.putString(KEY_productType, item_type);
        editor.putInt(KEY_productQuantity, item_quantity);
        editor.putString(KEY_productQuality, item_quality);
        editor.putInt(KEY_minPrice, minprice);
        editor.putInt(KEY_userId, userId);


        editor.apply();

        return true;
    }


    public int getProductId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_productId, 0);
    }

    public String getProductName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_productName, null);
    }

    public String getProductType() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_productType, null);
    }

    public int getProductQuantity() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_productQuantity, 0);
    }

    public String getProductQuality() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_productQuality, null);
    }

    public int getMinPrice() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_minPrice, 0);
    }

    public int getUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_userId, 0);
    }

}
