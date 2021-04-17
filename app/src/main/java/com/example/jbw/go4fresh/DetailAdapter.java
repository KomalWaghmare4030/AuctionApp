package com.example.jbw.go4fresh;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JBW on 18/03/2018.
 */

public class DetailAdapter extends ArrayAdapter<CostInfo> {
    private Activity contexts;
    private List<CostInfo> lists;

    DetailAdapter(Activity context, List<CostInfo> list) {
        super(context, R.layout.item_details, list);
        this.contexts = context;
        this.lists = list;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_details, parent, false);
        }
        TextView d1 = (TextView) listItemView.findViewById(R.id.d1);
        TextView d2 = (TextView) listItemView.findViewById(R.id.d2);
        TextView d3 = (TextView) listItemView.findViewById(R.id.d3);
        TextView d4 = (TextView) listItemView.findViewById(R.id.d4);
        TextView d5 = (TextView) listItemView.findViewById(R.id.d5);
        TextView d6 = (TextView) listItemView.findViewById(R.id.d6);
        TextView d7 = (TextView) listItemView.findViewById(R.id.d7);

        final CostInfo costInfo = lists.get(position);

        d1.setText(costInfo.getUserName());
        d2.setText(costInfo.getpName());
        d3.setText(costInfo.getsMinPrice());
        d4.setText(costInfo.getbPrice());
        d5.setText(costInfo.getQuality());
        d6.setText(costInfo.getsQuantity());
        d7.setText(costInfo.getDate());
        return listItemView;
    }
}