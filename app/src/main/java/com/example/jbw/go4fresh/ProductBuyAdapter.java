package com.example.jbw.go4fresh;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductBuyAdapter extends ArrayAdapter<ProductInfo> {
        private Activity contexts;
        private List<ProductInfo> lists;

        ProductBuyAdapter(Activity context, List<ProductInfo> list) {
            super(context, R.layout.list_buy_item, list);
            this.contexts = context;
            this.lists = list;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_buy_item, parent, false);
            }
            TextView t1 = (TextView) listItemView.findViewById(R.id.t1);
            TextView t2 = (TextView) listItemView.findViewById(R.id.t2);

            ProductInfo productInfo = lists.get(position);
            t1.setText(productInfo.getItemName());
            t2.setText(productInfo.getItemType());
            return listItemView;
        }
}


