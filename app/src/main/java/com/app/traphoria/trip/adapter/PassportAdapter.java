package com.app.traphoria.trip.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.traphoria.R;
import com.app.traphoria.customViews.MyTextView14;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.RelationDTO;

import java.util.List;

public class PassportAdapter extends BaseAdapter {
    private Context context;
    private List<PassportDTO> passportList;


    public PassportAdapter(Context context, List<PassportDTO> passportList) {
        this.context = context;
        this.passportList = passportList;
    }


    @Override
    public int getCount() {
        return passportList.size();
    }

    @Override
    public Object getItem(int position) {
        return passportList.get(position);
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
            mView = (View) mInflater.inflate(R.layout.relation_row_layout,
                    parent, false);
            holder = new ViewHolder();
            holder.txtCountryCode = (MyTextView14) mView.findViewById(R.id.txt_relation_name);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }

        holder.txtCountryCode.setText(passportList.get(position).getCountry());

        return mView;
    }

    private static class ViewHolder {
        private MyTextView14 txtCountryCode;
    }

}


