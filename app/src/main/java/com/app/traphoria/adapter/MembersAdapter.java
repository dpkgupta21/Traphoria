package com.app.traphoria.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.PassportVisaDetailsDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.DetailsViewHolder> {


    private static Context context;
    private DisplayImageOptions options;
    private PassportVisaDetailsDTO passportVisaDetails;


    public MembersAdapter(PassportVisaDetailsDTO passportVisaDetails, Context context) {

        this.passportVisaDetails = passportVisaDetails;
        this.context = context;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        holder.type_tv.setText(R.string.V);


    }

    @Override
    public int getItemCount() {
        return passportVisaDetails.getPassport().size() + passportVisaDetails.getVisa().size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView type_tv;
        TextView country_name_psprt;
        TextView pasprt_type;
        TextView pasprt_no;
        TextView expires_date;

        public DetailsViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            country_name_psprt = (TextView) itemView.findViewById(R.id.country_name_psprt);

            pasprt_type = (TextView) itemView.findViewById(R.id.pasprt_type);
            pasprt_no = (TextView) itemView.findViewById(R.id.pasprt_no);
            expires_date = (TextView) itemView.findViewById(R.id.expires_date);


        }
    }

}
