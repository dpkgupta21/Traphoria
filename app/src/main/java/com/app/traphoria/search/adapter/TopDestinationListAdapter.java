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
import com.app.traphoria.model.DestinationDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class TopDestinationListAdapter extends RecyclerView.Adapter<TopDestinationListAdapter.DetailsViewHolder> {
    private Context context;
    private List<DestinationDTO> destinationList;
    private DisplayImageOptions options;

    public TopDestinationListAdapter(Context context, List<DestinationDTO> destinationList) {
        this.context = context;
        this.destinationList = destinationList;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_destination_row_layout, parent, false);
        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        ImageLoader.getInstance().displayImage(destinationList.get(position).getImage(), holder.thumbnail,
                options);
        holder.dest_name.setText(destinationList.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
       return   destinationList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView dest_name;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            dest_name = (TextView) itemView.findViewById(R.id.dest_name);
        }
    }

}
