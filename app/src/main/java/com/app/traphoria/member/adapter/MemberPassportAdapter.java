package com.app.traphoria.member.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.PassportVisaDTO;
import com.app.traphoria.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;


public class MemberPassportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context context;
    private DisplayImageOptions options;
    private List<PassportVisaDTO> passportList;
    private final int passport = 0, visa = 1;


    public MemberPassportAdapter(List<PassportVisaDTO> passportList, Context context) {

        this.passportList = passportList;
        this.context = context;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case passport:
                View v = inflater.inflate(R.layout.member_passport_row_layout, parent, false);
                viewHolder = new PassPortViewHolder(v);
                break;
            case visa:

                View v1 = inflater.inflate(R.layout.member_visa_row_layout, parent, false);
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

        }

    }

    @Override
    public int getItemCount() {
        return passportList.size();
    }

    public static class PassPortViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView type_tv;
        TextView country_name_psprt;
        TextView pasprt_type;
        TextView pasprt_no;
        TextView expires_date;

        public PassPortViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            country_name_psprt = (TextView) itemView.findViewById(R.id.country_name_psprt);

            pasprt_type = (TextView) itemView.findViewById(R.id.pasprt_type);
            pasprt_no = (TextView) itemView.findViewById(R.id.pasprt_no);
            expires_date = (TextView) itemView.findViewById(R.id.expires_date);


        }
    }


    public static class VisaViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView type_tv;
        TextView country_name_visa;
        TextView visa_type;
        TextView visa_entry_type;
        TextView visa_expires_date;

        public VisaViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            country_name_visa = (TextView) itemView.findViewById(R.id.txt_country_name_visa);

            visa_type = (TextView) itemView.findViewById(R.id.txt_visa_type);
            visa_entry_type = (TextView) itemView.findViewById(R.id.txt_visa_entry);
            visa_expires_date = (TextView) itemView.findViewById(R.id.txt_visa_expires_date);


        }
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

        holder.type_tv.setText(R.string.P);
        holder.country_name_psprt.setText(passportList.get(position).getCountry());
        holder.expires_date.setText("Expires On: " + passportList.get(position).getExpiryDate());
        holder.pasprt_no.setText("Passport No: " + passportList.get(position).getPassportNumber());
        holder.pasprt_type.setText(passportList.get(position).getPassport_visa_type());

        try {
            ImageLoader.getInstance().displayImage(passportList.get(position).getCountry_image(), holder.thumbnail,
                    options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                            ((ImageView) view).setImageResource(R.drawable.login_bg);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String s, View view, int i, int i1) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void configureVisaViewHolder(VisaViewHolder holder, int position) {

        holder.type_tv.setText(R.string.V);
        holder.country_name_visa.setText(passportList.get(position).getCountry());
        holder.visa_entry_type.setText(passportList.get(position).getEntry_type());
        holder.visa_type.setText(passportList.get(position).getPassport_visa_type());
        holder.visa_expires_date.setText("Expires On: " + passportList.get(position).getExpiryDate());

        try {
            ImageLoader.getInstance().displayImage(passportList.get(position).getCountry_image(), holder.thumbnail,
                    options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                            ((ImageView) view).setImageResource(R.drawable.login_bg);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String s, View view, int i, int i1) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

