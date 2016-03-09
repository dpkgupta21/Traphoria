package com.app.traphoria.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.traphoria.R;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Harish on 12/16/2015.
 */
public class SideMenuListAdapter extends BaseAdapter {


    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<String> menuItemList;
    private String alertCount;

    public SideMenuListAdapter(Activity mActivity) {
        this.mActivity = mActivity;

        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            menuItemList = Arrays.asList(mActivity.getResources().getStringArray(R.array.menu_list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAlertCount(String alertCount) {
        this.alertCount = alertCount;
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
            convertView = mLayoutInflater.inflate(R.layout.side_menu_row_layout, parent, false);
            holder = new ViewHolder();

            holder.alert_count = (TextView) convertView.findViewById(R.id.alert_count);
            holder.menu_item = (TextView) convertView.findViewById(R.id.menu_item);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (menuItemList != null) {
            holder.menu_item.setText(menuItemList.get(position));
            if (menuItemList.get(position).equals("Alert")) {
                holder.alert_count.setText(alertCount == null ? "" : alertCount);
            }
        }
//        if (position == 1) {
//            holder.alert_count.setText("8");
//        } else {
//            holder.alert_count.setVisibility(View.GONE);
//        }

        return convertView;
    }


    public class ViewHolder {
        TextView menu_item, alert_count;
    }
}
