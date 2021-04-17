package com.example.jbw.go4fresh;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ShowCostAdapter extends ArrayAdapter<CostInfo> {

        private Activity contexts;
        private List<CostInfo> lists;

        ShowCostAdapter(Activity context, List<CostInfo> list) {
            super(context,R.layout.cost_item ,list);
            this.contexts = context;
            this.lists = list;
        }
        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.show_cost_item, parent, false);
            }
            TextView s1 = (TextView) listItemView.findViewById(R.id.s1);
            TextView s2 = (TextView) listItemView.findViewById(R.id.s2);

            final CostInfo costInfo = lists.get(position);
            s1.setText("Rs."+costInfo.getbPrice()+" per Kg");
            s2.setText("By "+costInfo.getUserName());

            return listItemView;
        }
    }
