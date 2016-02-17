//package com.app.traphoria.passportvisa.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.app.traphoria.R;
//import com.app.traphoria.model.VisaDTO;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
//
//import java.util.List;
//
//
//public class VisaAdapter extends RecyclerView.Adapter<VisaAdapter.DetailsViewHolder> {
//
//    private Context context;
//    private List<VisaDTO> visaList;
//    private DisplayImageOptions options;
//    private static MyClickListener myClickListener;
//
//    public VisaAdapter(Context context, List<VisaDTO> visaList) {
//        this.context = context;
//        this.visaList = visaList;
//        options = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(true)
//                .cacheOnDisk(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .considerExifParams(true)
//                .displayer(new SimpleBitmapDisplayer())
//                .showImageOnLoading(R.drawable.slide_img)
//                .showImageOnFail(R.drawable.slide_img)
//                .showImageForEmptyUri(R.drawable.slide_img)
//                .build();
//    }
//
//    @Override
//    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.visa_row_layout, parent, false);
//
//        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
//        return detailsViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(DetailsViewHolder holder, int position) {
//        ImageLoader.getInstance().displayImage(visaList.get(position).getCountry_image(), holder.thumbnail,
//                options);
//
//        holder.txt_country_name_visa.setText(visaList.get(position).getCountry());
//        holder.txt_visa_entry_type.setText(visaList.get(position).getEntry_type());
//        holder.txt_visa_type.setText(visaList.get(position).getPassport_type());
//        holder.txt_visa_expires_date.setText("Expires On: " + visaList.get(position).getExpire_date());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return visaList.size();
//    }
//
//    public static class DetailsViewHolder extends RecyclerView.ViewHolder implements View
//            .OnClickListener  {
//
//        ImageView thumbnail, edit_btn;
//        TextView txt_country_name_visa, txt_visa_type, txt_visa_entry_type, txt_visa_expires_date, explore_visa;
//
//        public DetailsViewHolder(View itemView) {
//
//            super(itemView);
//            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
//            edit_btn = (ImageView) itemView.findViewById(R.id.edit_btn);
//
//            explore_visa = (TextView) itemView.findViewById(R.id.explore_visa);
//            txt_visa_expires_date = (TextView) itemView.findViewById(R.id.txt_visa_expires_date);
//            txt_visa_entry_type = (TextView) itemView.findViewById(R.id.txt_visa_entry_type);
//            txt_visa_type = (TextView) itemView.findViewById(R.id.txt_visa_type);
//            txt_country_name_visa = (TextView) itemView.findViewById(R.id.txt_country_name_visa);
//
//            thumbnail.setOnClickListener(this);
//            edit_btn.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            myClickListener.onItemClick(getAdapterPosition(), v);
//        }
//    }
//
//
//    public void setOnItemClickListener(MyClickListener myClickListener) {
//        this.myClickListener = myClickListener;
//    }
//
//    public interface MyClickListener {
//        public void onItemClick(int position, View v);
//    }
//}
