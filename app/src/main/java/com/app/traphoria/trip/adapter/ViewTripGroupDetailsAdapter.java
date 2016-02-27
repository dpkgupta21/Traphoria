package com.app.traphoria.trip.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

import com.app.traphoria.R;
import com.app.traphoria.model.TripUserDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class ViewTripGroupDetailsAdapter extends RecyclerView.Adapter<ViewTripGroupDetailsAdapter.DetailsViewHolder> {


    private List<TripUserDTO> tripUserList;
    private Context context;
    private DisplayImageOptions options;

    public ViewTripGroupDetailsAdapter(List<TripUserDTO> tripUserList, Context context) {
        this.tripUserList = tripUserList;
        this.context = context;
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

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_member_detail_row, parent, false);
        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        holder.member_name.setText(tripUserList.get(position).getName());
        holder.gender_age.setText(tripUserList.get(position).getGender() + " | " + tripUserList.get(position).getAge());
        ImageLoader.getInstance().displayImage(tripUserList.get(position).getImage(), holder.img_user_image,
                options);

    }

    @Override
    public int getItemCount() {
        return tripUserList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_user_image;
        TextView member_name, gender_age, pasport_no, passport_time, visa, visa_time;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            img_user_image = (CircleImageView) itemView.findViewById(R.id.img_user_image);
            member_name = (TextView) itemView.findViewById(R.id.member_name);
            gender_age = (TextView) itemView.findViewById(R.id.gender_age);
            pasport_no = (TextView) itemView.findViewById(R.id.pasport_no);
            passport_time = (TextView) itemView.findViewById(R.id.passport_time);
            visa = (TextView) itemView.findViewById(R.id.visa);
            visa_time = (TextView) itemView.findViewById(R.id.visa_time);


        }
    }

}
