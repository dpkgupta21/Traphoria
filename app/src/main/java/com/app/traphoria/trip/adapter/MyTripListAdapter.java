package com.app.traphoria.trip.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.TripDTO;
import com.app.traphoria.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;

public class MyTripListAdapter extends RecyclerView.Adapter<MyTripListAdapter.DetailsViewHolder> {

    private List<TripDTO> tripList;
    private Context context;
    private DisplayImageOptions options;

    public MyTripListAdapter(List<TripDTO> tripList, Context context) {

        this.tripList = tripList;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.loading_fail)
                .build();
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trip_row_layout, parent, false);
        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        if (!tripList.get(position).getTrip_type().equalsIgnoreCase("1")) {
            holder.trip_type_icon.setImageResource(R.drawable.group_icon);
        } else {
            holder.trip_type_icon.setImageResource(R.drawable.single_group_icon);
        }

        holder.dest_name.setText(tripList.get(position).getCountry_name());
        holder.date.setText(tripList.get(position).getStart_date() + " - " + tripList.get(position).getEnd_date());
        holder.expiry.setText("Visa Expires on: " + tripList.get(position).getExpire_date());

        try {
            final ImageView imgThumbnail = holder.thumbnail;
            String imageUrl = tripList.get(position).getCountry_image();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl, imgThumbnail,
                        options, new ImageLoadingListener() {
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

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        ImageView trip_type_icon;
        TextView dest_name;
        TextView date;
        TextView expiry;

        public DetailsViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            trip_type_icon = (ImageView) itemView.findViewById(R.id.trip_type_icon);
            dest_name = (TextView) itemView.findViewById(R.id.dest_name);
            date = (TextView) itemView.findViewById(R.id.date);
            expiry = (TextView) itemView.findViewById(R.id.expiry);


        }
    }

}
