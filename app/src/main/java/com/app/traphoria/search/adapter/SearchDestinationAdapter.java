package com.app.traphoria.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.SerachDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_destination_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


        holder.dest_name.setText(searchList.get(position).getName());
        if (searchList.get(position).getExpire_on()!=null && !searchList.get(position).getExpire_on().equals("")) {
            holder.txt_expire_on_lbl.setVisibility(View.VISIBLE);
        } else {
            holder.txt_expire_on_lbl.setVisibility(View.INVISIBLE);
        }
        holder.txt_expire.setText(searchList.get(position).getExpire_on());
        holder.txt_type.setText(searchList.get(position).getPassportvisatext());
        ImageLoader.getInstance().displayImage(searchList.get(position).getCountry_image(), holder.thumbnail,
                options);

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
