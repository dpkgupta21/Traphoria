package com.app.traphoria.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Harish on 12/16/2015.
 */
public class DialogAdapter extends BaseAdapter {


    Activity mActivity;
    LayoutInflater mLayoutInflater;

    List<String> menuItemList;


    public DialogAdapter(Activity mActivity) {
        this.mActivity = mActivity;

        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            menuItemList = Arrays.asList(mActivity.getResources().getStringArray(R.array.menu_list));
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
            convertView = mLayoutInflater.inflate(R.layout.dialog_row_layout, parent, false);
            holder = new ViewHolder();

            holder.textview = (TextView) convertView.findViewById(R.id.textview);
            holder.select_img = (ImageView) convertView.findViewById(R.id.select_img);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    public class ViewHolder {
        TextView textview;
        ImageView select_img;
    }
}
