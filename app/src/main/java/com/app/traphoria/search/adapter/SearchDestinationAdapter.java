package com.app.traphoria.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.SerachDTO;
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


public class SearchDestinationAdapter extends RecyclerView.Adapter<SearchDestinationAdapter.DetailsViewHolder> {


    private Context context;
    private List<SerachDTO> searchList;
    private DisplayImageOptions options;


    public SearchDestinationAdapter(Context context, List<SerachDTO> searchList) {
        this.context = context;
        this.searchList = searchList;
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.loading_fail)
                .build();
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.search_destination_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        // Underlined text
        holder.dest_name.setText(Utils.underlineText(searchList.get(position).getName()));


        if (searchList.get(position).getExpire_on() != null &&
                !searchList.get(position).getExpire_on().equals("")) {
            holder.txt_expire_on_lbl.setVisibility(View.VISIBLE);
        } else {
            holder.txt_expire_on_lbl.setVisibility(View.INVISIBLE);
        }
        holder.txt_expire.setText(searchList.get(position).getExpire_on());
        holder.txt_type.setText(searchList.get(position).getPassportvisatext());

         try {
             final ImageView imgThumbnail=holder.thumbnail;
             String imageUrl=searchList.get(position).getCountry_image();
             if(!imageUrl.equalsIgnoreCase("")) {
                 ImageLoader.getInstance().displayImage(searchList.get(position).
                                 getCountry_image(), imgThumbnail,
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
                                 imgThumbnail.setPadding(0, 0,
                                         0, 0);
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
             }else{
                 imgThumbnail.setImageResource(R.drawable.loading_fail);
                 imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                 imgThumbnail.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                         0, WebserviceConstant.IMAGE_MARGIN);

             }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView dest_name, txt_type, txt_expire, txt_expire_on_lbl;


        public DetailsViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            dest_name = (TextView) itemView.findViewById(R.id.dest_name);
            txt_expire = (TextView) itemView.findViewById(R.id.txt_expire);
            txt_expire_on_lbl = (TextView) itemView.findViewById(R.id.txt_expire_on_lbl);
        }
    }


    public void setSeachList(List<SerachDTO> seachList) {
        this.searchList = seachList;
    }

}
