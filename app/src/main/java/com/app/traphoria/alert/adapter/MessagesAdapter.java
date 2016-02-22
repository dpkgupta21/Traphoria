package com.app.traphoria.alert.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.MessageDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.DetailsViewHolder> {


    private Context context;
    private List<MessageDTO> messageList;
    private DisplayImageOptions options;

    public MessagesAdapter(Context context, List<MessageDTO> messageList) {

        this.context = context;
        this.messageList = messageList;
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


        ImageLoader.getInstance().displayImage(messageList.get(position).getImage(), holder.status_pin,
                options);
        holder.name.setText(messageList.get(position).getName());
        holder.message_detail.setText(messageList.get(position).getMessage());
        holder.date.setText(messageList.get(position).getTimestamp());


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView status_pin;
        TextView name, message_detail, date;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            status_pin = (ImageView) itemView.findViewById(R.id.status_pin);
            name = (TextView) itemView.findViewById(R.id.name);
            message_detail = (TextView) itemView.findViewById(R.id.message_detail);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }

}
