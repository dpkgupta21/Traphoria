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
import com.app.traphoria.model.PassportVisaDTO;
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

public class PassportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PassportVisaDTO> passportList;
    private DisplayImageOptions options;
    private static MyClickListener myClickListener;

    private final int passport = 0, visa = 1;

    public PassportAdapter(Context context, List<PassportVisaDTO> passportList) {
        this.context = context;
        this.passportList = passportList;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case passport:
                View v = inflater.inflate(R.layout.passport_row_layout, parent, false);
                viewHolder = new PassPortViewHolder(v);
                break;
            case visa:

                View v1 = inflater.inflate(R.layout.visa_row_layout, parent, false);
                viewHolder = new VisaViewHolder(v1);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case passport:
                PassPortViewHolder passPortViewHolder = (PassPortViewHolder) holder;
                configurePassportViewHolder(passPortViewHolder, position);
                break;
            case visa:

                VisaViewHolder visaViewHolder = (VisaViewHolder) holder;
                configureVisaViewHolder(visaViewHolder, position);
                break;

        }

    }

    @Override
    public int getItemCount() {
        return passportList.size();
    }

    public static class PassPortViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        ImageView thumbnail, edit_btn;
        TextView txt_country_name, txt_passport_type, txt_passport_no, txt_expires_date, explore;

        public PassPortViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            edit_btn = (ImageView) itemView.findViewById(R.id.edit_btn);
            explore = (TextView) itemView.findViewById(R.id.explore);
            txt_expires_date = (TextView) itemView.findViewById(R.id.txt_expires_date);
            txt_passport_no = (TextView) itemView.findViewById(R.id.txt_passport_no);
            txt_passport_type = (TextView) itemView.findViewById(R.id.txt_passport_type);
            txt_country_name = (TextView) itemView.findViewById(R.id.txt_country_name);

            thumbnail.setOnClickListener(this);
            edit_btn.setOnClickListener(this);
            explore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }


    public static class VisaViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        ImageView thumbnail, btn_edit;
        TextView txt_country_name_visa, txt_visa_type, txt_visa_entry_type, txt_visa_expires_date, explore_visa;

        public VisaViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            btn_edit = (ImageView) itemView.findViewById(R.id.btn_edit);

            explore_visa = (TextView) itemView.findViewById(R.id.explore_visa);
            txt_visa_expires_date = (TextView) itemView.findViewById(R.id.txt_visa_expires_date);
            txt_visa_entry_type = (TextView) itemView.findViewById(R.id.txt_visa_entry_type);
            txt_visa_type = (TextView) itemView.findViewById(R.id.txt_visa_type);
            txt_country_name_visa = (TextView) itemView.findViewById(R.id.txt_country_name_visa);

            thumbnail.setOnClickListener(this);
            btn_edit.setOnClickListener(this);
            explore_visa.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


    @Override
    public int getItemViewType(int position) {

        if (passportList.get(position).getType().equalsIgnoreCase("P")) {
            return passport;
        } else if (passportList.get(position).getType().equalsIgnoreCase("V")) {
            return visa;
        }

        return -1;
    }


    private void configurePassportViewHolder(PassPortViewHolder holder, int position) {

        holder.txt_country_name.setText(passportList.get(position).getCountry());
        holder.txt_passport_no.setText("Passport No: " + passportList.get(position).getPassportNumber());
        holder.txt_passport_type.setText(passportList.get(position).getPassport_visa_type());
        holder.txt_expires_date.setText("Expires On: " + passportList.get(position).getExpiryDate());

        try {
            final ImageView imgThumbnail = holder.thumbnail;
            String imageUrl = passportList.get(position).getCountry_image();
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
    }


    private void configureVisaViewHolder(VisaViewHolder holder, int position) {

        try {
            final ImageView iv = holder.thumbnail;
            String imageUrl = passportList.get(position).getCountry_image();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl, iv,
                        options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                                iv.setImageResource(R.drawable.login_bg);
                                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                iv.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                iv.setImageResource(R.drawable.loading_fail);
                                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                iv.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                iv.setPadding(0, 0, 0, 0);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                iv.setImageResource(R.drawable.loading_fail);
                                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                iv.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);
                            }

                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String s, View view, int i, int i1) {

                            }
                        });
            } else {
                iv.setImageResource(R.drawable.loading_fail);
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                        0, WebserviceConstant.IMAGE_MARGIN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txt_country_name_visa.setText(passportList.get(position).getCountry());
        holder.txt_visa_entry_type.setText(passportList.get(position).getEntry_type());
        holder.txt_visa_type.setText(passportList.get(position).getPassport_visa_type());
        holder.txt_visa_expires_date.setText("Expires On: " + passportList.get(position).getExpiryDate());


    }


}
