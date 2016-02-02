package com.app.traphoria.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import traphoria.com.app.traphoria.R;


/**
 * Created by Harish on 12/16/2015.
 */
public class NotificationAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    ArrayList<String> menuItemList;


    public NotificationAdapter(Activity mActivity) {
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

        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        return position % 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.notification_row_layout, parent, false);
            holder = new ViewHolder();

            holder.notification = (TextView) convertView.findViewById(R.id.member_name);
            holder.more_icon = (ImageView) convertView.findViewById(R.id.select_img);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    public class ViewHolder {
        TextView notification;
        ImageView more_icon;
    }
}
