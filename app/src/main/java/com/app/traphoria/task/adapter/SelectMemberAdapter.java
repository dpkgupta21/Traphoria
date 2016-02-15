package com.app.traphoria.task.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.app.traphoria.R;
import com.app.traphoria.model.MemberDTO;


public class SelectMemberAdapter extends BaseAdapter {


    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<MemberDTO> memberList;


    public SelectMemberAdapter(Activity mActivity, List<MemberDTO> memberList) {
        this.mActivity = mActivity;
        this.memberList = memberList;

        try {
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {

        return memberList.size();

    }

    @Override
    public Object getItem(int position) {


        return memberList.get(position);

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

        holder.member_name.setText(memberList.get(position).getName());

        if(memberList.get(position).getSelected().equalsIgnoreCase("N"))
        {
            holder.select_img.setImageResource(R.drawable.unactive_circle);
        }
        else
        {
            holder.select_img.setImageResource(R.drawable.active_circle);
        }

        return convertView;
    }


    public class ViewHolder {
        TextView member_name;
        ImageView select_img;
    }
}
