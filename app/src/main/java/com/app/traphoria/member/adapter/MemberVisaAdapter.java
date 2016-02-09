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
import com.app.traphoria.model.VisaDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class MemberVisaAdapter extends RecyclerView.Adapter<MemberVisaAdapter.DetailsViewHolder> {


    private static Context context;
    private DisplayImageOptions options;
    private List<VisaDTO> visaList;


    public MemberVisaAdapter(List<VisaDTO> visaList, Context context) {

        this.visaList = visaList;
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
        holder.country_name_visa.setText(visaList.get(position).getCountry());
        holder.visa_entry_type.setText(visaList.get(position).getEntry_type());
        holder.visa_type.setText(visaList.get(position).getPassport_type());
        holder.visa_expires_date.setText(visaList.get(position).getExpire_date());
        ImageLoader.getInstance().displayImage(visaList.get(position).getCountry_image(), holder.thumbnail,
                options);

    }

    @Override
    public int getItemCount() {
        return visaList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView type_tv;
        TextView country_name_visa;
        TextView visa_type;
        TextView visa_entry_type;
        TextView visa_expires_date;

        public DetailsViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            country_name_visa = (TextView) itemView.findViewById(R.id.country_name_visa);

            visa_type = (TextView) itemView.findViewById(R.id.visa_type);
            visa_entry_type = (TextView) itemView.findViewById(R.id.visa_entry_type);
            visa_expires_date = (TextView) itemView.findViewById(R.id.visa_expires_date);


        }
    }

}

