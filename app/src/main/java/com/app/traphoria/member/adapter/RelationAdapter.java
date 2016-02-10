package com.app.traphoria.member.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.traphoria.R;
import com.app.traphoria.customViews.MyTextView12;
import com.app.traphoria.customViews.MyTextView14;
import com.app.traphoria.model.RelationDTO;

import java.util.List;

public class RelationAdapter extends BaseAdapter {
    private Context context;
    private List<RelationDTO> relationList;


    public RelationAdapter(Context context, List<RelationDTO> relationList) {
        this.context = context;
        this.relationList = relationList;
    }


    @Override
    public int getCount() {
        return relationList.size();
    }

    @Override
    public Object getItem(int position) {
        return relationList.get(position);
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

        holder.txtCountryCode.setText(relationList.get(position).getName());

        return mView;
    }

    private static class ViewHolder {
        private MyTextView14 txtCountryCode;
    }


}

