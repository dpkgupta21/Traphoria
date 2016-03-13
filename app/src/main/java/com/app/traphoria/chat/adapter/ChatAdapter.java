package com.app.traphoria.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.app.traphoria.R;
import com.app.traphoria.model.MessageDTO;
import com.app.traphoria.model.MessagesDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


public class ChatAdapter extends BaseAdapter {


    private Context context;
    private List<MessagesDTO> chatList;
    private DisplayImageOptions options;
    private String userId;

    public ChatAdapter(Context context, List<MessagesDTO> chatList) {
        this.context = context;
        this.chatList = chatList;
        userId = PreferenceHelp.getUserId(context);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avtar_icon)
                .showImageOnFail(R.drawable.avtar_icon)
                .showImageForEmptyUri(R.drawable.avtar_icon)
                .build();
    }

    public void setChatList(List<MessagesDTO> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getCount() {

        return chatList.size();
    }

    @Override
    public Object getItem(int position) {


        return chatList.get(position);

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
    public View getView(int position, View mView, ViewGroup parent) {
        View convertView = mView;
        ViewHolder holder = null;

        MessagesDTO chat = chatList.get(position);
        if (convertView == null) {

            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (chat.getUser_id().equals(userId)) {
                convertView = (View) li.inflate(R.layout.chat_right_row_layout, parent, false);

            } else {
                convertView = (View) li.inflate(R.layout.chat_left_row_layout, parent, false);
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


        holder.msg_tv.setText(chat.getMessage());
        holder.date.setText(Utils.secondsToDate(chat.getTimestamp()));
        try {
            ImageLoader.getInstance().displayImage(chat.getUserImg(), holder.img_user_image,
                    options);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    public class ViewHolder {
        TextView msg_tv, date, time;
        ImageView img_user_image;
    }
}
