package com.app.traphoria.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.app.traphoria.R;
import com.app.traphoria.model.NotificationDTO;

public class NotificationAdapter extends BaseAdapter {


    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<NotificationDTO> notificationList;

    public NotificationAdapter(Activity mActivity, List<NotificationDTO> notificationList) {
        this.mActivity = mActivity;
        this.notificationList = notificationList;
        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {

        return notificationList.size();

    }

    @Override
    public Object getItem(int position) {


        return notificationList.get(position);

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

        return 0;
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


        holder.notification.setText(notificationList.get(position).getMessage());

        return convertView;
    }


    public class ViewHolder {
        TextView notification;
        ImageView more_icon;
    }
}
