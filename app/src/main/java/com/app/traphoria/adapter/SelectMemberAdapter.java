package com.app.traphoria.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.app.traphoria.R;


/**
 * Created by Harish on 12/16/2015.
 */
public class SelectMemberAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    ArrayList<String> menuItemList;


    public SelectMemberAdapter(Activity mActivity) {
        this.mActivity = mActivity;

        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            menuItemList = new ArrayList<String>();
            menuItemList.add("Abdul Nasseer (Son)");
            menuItemList.add("Habib (Cook)");
            menuItemList.add("Ibtissaam (Wife)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
        if (menuItemList != null) {
            return menuItemList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        if (menuItemList != null) {
            return menuItemList.get(position);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.select_member_row_layout, parent, false);
            holder = new ViewHolder();

            holder.member_name = (TextView) convertView.findViewById(R.id.member_name);
            holder.select_img = (ImageView) convertView.findViewById(R.id.select_img);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (menuItemList != null) {
            holder.member_name.setText(menuItemList.get(position));
        }

        holder.select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return convertView;
    }


    public class ViewHolder {
        TextView member_name;
        ImageView select_img;
    }
}
