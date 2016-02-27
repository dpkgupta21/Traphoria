package com.app.traphoria.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.FestivalDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;


public class FestivalEventListAdapter extends RecyclerView.Adapter<FestivalEventListAdapter.DetailsViewHolder> {

    private Context context;
    private List<FestivalDTO> festivalList;
    private DisplayImageOptions options;

    public FestivalEventListAdapter(Context context, List<FestivalDTO> festivalList) {

        this.context = context;
        this.festivalList = festivalList;
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.login_bg)
                .showImageForEmptyUri(R.drawable.login_bg)
                .build();
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.festivals_events_row_layout, parent, false);
        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        ImageLoader.getInstance().displayImage(festivalList.get(position).getImage(),
                holder.thumbnail, options);
        holder.date_tv.setText(festivalList.get(position).getStart_date() +
                " to " + festivalList.get(position).getEnd_date());
        holder.event_tv.setText(festivalList.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return festivalList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView event_tv, date_tv;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            event_tv = (TextView) itemView.findViewById(R.id.event_tv);
            date_tv = (TextView) itemView.findViewById(R.id.date_tv);


        }
    }

}
