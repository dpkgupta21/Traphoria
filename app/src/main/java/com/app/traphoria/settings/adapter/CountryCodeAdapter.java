package com.app.traphoria.settings.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.traphoria.R;


import java.util.List;
import java.util.Map;

public class CountryCodeAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;


    public CountryCodeAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            mView = (View) mInflater.inflate(R.layout.layout_county_code_row,
                    parent, false);
            holder = new ViewHolder();
            holder.txtCountryCode = (TextView) mView.findViewById(R.id.txt_contry_code);
            holder.txtCountryName = (TextView) mView.findViewById(R.id.txt_county_name);

            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }

        holder.txtCountryCode.setText(list.get(position).get("dial_code"));
        holder.txtCountryName.setText(list.get(position).get("name"));
        return mView;
    }

    private static class ViewHolder {
        private TextView txtCountryCode;
        private TextView txtCountryName;
    }

}
