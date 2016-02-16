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
import com.app.traphoria.model.PassportDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class PassportAdapter extends RecyclerView.Adapter<PassportAdapter.DetailsViewHolder> {

    private Context context;
    private List<PassportDTO> passportList;
    private DisplayImageOptions options;
    private static MyClickListener myClickListener;

    public PassportAdapter(Context context, List<PassportDTO> passportList) {
        this.context = context;
        this.passportList = passportList;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.passport_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


        holder.txt_country_name.setText(passportList.get(position).getCountry());
        holder.txt_passport_no.setText("Passport No: " + passportList.get(position).getPassport_no());
        holder.txt_passport_type.setText(passportList.get(position).getPassport_type());
        holder.txt_expires_date.setText("Expires On: "+passportList.get(position).getExpire_date());

        ImageLoader.getInstance().displayImage(passportList.get(position).getCountry_image(), holder.thumbnail,
                options);
    }

    @Override
    public int getItemCount() {
        return passportList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        ImageView thumbnail, edit_btn;
        TextView txt_country_name, txt_passport_type, txt_passport_no, txt_expires_date, explore;

        public DetailsViewHolder(View itemView) {

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

}
