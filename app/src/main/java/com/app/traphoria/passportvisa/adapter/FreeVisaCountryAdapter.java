package com.app.traphoria.passportvisa.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.FreeVisaCountryDTO;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.webservice.WebserviceConstant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;

public class FreeVisaCountryAdapter extends RecyclerView.Adapter<FreeVisaCountryAdapter.DetailsViewHolder> {

    private Context context;
    private DisplayImageOptions options;
    private List<FreeVisaCountryDTO> list;

    public FreeVisaCountryAdapter(Context context, List<FreeVisaCountryDTO> list) {
        this.context = context;
        this.list = list;
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
                inflate(R.layout.free_visa_destination_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView txt_country_name;

        public DetailsViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            txt_country_name = (TextView) itemView.findViewById(R.id.dest_name);


        }


    }


    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        // Using imageLoader
        try {
            final ImageView imgThumbnail = holder.thumbnail;
            String imageUrl = list.get(position).getImage();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl, imgThumbnail,
                        options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                                imgThumbnail.setImageResource(R.drawable.login_bg);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                imgThumbnail.setImageResource(R.drawable.loading_fail);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                imgThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imgThumbnail.setPadding(0, 0, 0, 0);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                imgThumbnail.setImageResource(R.drawable.loading_fail);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String s, View view, int i, int i1) {

                            }
                        });
            } else {
                imgThumbnail.setImageResource(R.drawable.loading_fail);
                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgThumbnail.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                        0, WebserviceConstant.IMAGE_MARGIN);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txt_country_name.setText(Utils.underlineText(list.get(position).getName()));
    }
}