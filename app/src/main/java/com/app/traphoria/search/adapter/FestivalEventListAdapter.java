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
import com.app.traphoria.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

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

        try {
            final ImageView imgThumbnail = holder.thumbnail;
            String imageUrl = festivalList.get(position).getImage();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl,
                        imgThumbnail, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                                imgThumbnail.setImageResource(R.drawable.login_bg);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                imgThumbnail.setImageResource(R.drawable.loading_fail);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, 20, 0, 20);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                imgThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                imgThumbnail.setImageResource(R.drawable.loading_fail);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, 20, 0, 20);
                            }

                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String s, View view, int i, int i1) {

                            }
                        });
            } else {
                imgThumbnail.setImageResource(R.drawable.loading_fail);
                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgThumbnail.setPadding(0, 20, 0, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String formattedDateString = festivalList.get(position).getStart_date();
        if (formattedDateString != null && !formattedDateString.equalsIgnoreCase("")) {
            if (festivalList.get(position).getEnd_date() != null
                    && !festivalList.get(position).getEnd_date().equalsIgnoreCase("")) {
                formattedDateString += " to ";
                formattedDateString += festivalList.get(position).getEnd_date();
            }
        } else {
            if (festivalList.get(position).getEnd_date() != null && !festivalList.get(position).getEnd_date().equalsIgnoreCase("")) {
                formattedDateString += festivalList.get(position).getEnd_date();
            }
        }
        holder.date_tv.setText(formattedDateString);
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
