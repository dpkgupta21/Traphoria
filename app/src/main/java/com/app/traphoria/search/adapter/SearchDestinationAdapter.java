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
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
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
        holder.expire.setText(searchList.get(position).getExpire_on());
        holder.type.setText(searchList.get(position).getPassportvisatext());
        ImageLoader.getInstance().displayImage(searchList.get(position).getCountry_image(), holder.thumbnail,
                options);

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView dest_name, type, expire;


        public DetailsViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type = (TextView) itemView.findViewById(R.id.type);
            dest_name = (TextView) itemView.findViewById(R.id.dest_name);
            expire = (TextView) itemView.findViewById(R.id.expire);
        }
    }

}