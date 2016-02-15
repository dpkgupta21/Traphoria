package com.app.traphoria.alert.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        NotificationDTO notificationDTO = notificationList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if (notificationDTO.getType().equalsIgnoreCase("AddMember")) {
                convertView = mLayoutInflater.inflate(R.layout.notification_add_member_type_item,
                        parent, false);
                holder.txtNotificationDetail = (TextView) convertView.findViewById(R.id.txt_notification_detail);
                holder.imgMoreIcon = (ImageView) convertView.findViewById(R.id.img_more_icon);
            } else {
                convertView = mLayoutInflater.inflate(R.layout.notification_add_task_type_item,
                        parent, false);
                holder.txtTaskTitle = (TextView) convertView.findViewById(R.id.txt_task_title);
                holder.txt_task_desc = (TextView) convertView.findViewById(R.id.txt_task_desc);

                holder.imgMoreIcon = (ImageView) convertView.findViewById(R.id.img_more_icon);
            }


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (notificationDTO.getType().equalsIgnoreCase("AddMember")) {
            holder.txtNotificationDetail.setText(notificationList.get(position).getMessage());
        } else {
//            holder.txtNotificationDetail.setText(notificationList.get(position).getMessage());
//            holder.txtNotificationDetail.setText(notificationList.get(position).getMessage());
        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtNotificationDetail;
        ImageView imgMoreIcon;
        TextView txtTaskTitle;
        TextView txt_task_desc;

    }
}
