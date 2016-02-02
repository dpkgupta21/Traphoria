package com.app.traphoria.chat;

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
public class ChatAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    ArrayList<String> menuItemList;


    public ChatAdapter(Activity mActivity) {
        this.mActivity = mActivity;

        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            menuItemList = new ArrayList<String>();
            menuItemList.add("Abdul Nasseer (Son)");
            menuItemList.add("Habib (Cook)");
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
            if (position % 2 == 0) {
                convertView = mLayoutInflater.inflate(R.layout.chat_left_row_layout, parent, false);

            } else {
                convertView = mLayoutInflater.inflate(R.layout.chat_right_row_layout, parent, false);

            }
            holder = new ViewHolder();
            holder.msg_tv = (TextView) convertView.findViewById(R.id.msg_tv);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.img_user_image = (ImageView) convertView.findViewById(R.id.img_user_image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    public class ViewHolder {
        TextView msg_tv, date, time;
        ImageView img_user_image;
    }
}
