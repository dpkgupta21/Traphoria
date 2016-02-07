package com.app.traphoria.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;

/**
 * Created by Harish on 1/26/2016.
 */
public class TrackLocationsAdapter extends RecyclerView.Adapter<TrackLocationsAdapter.DetailsViewHolder> {


    private Activity mActivity;

    public TrackLocationsAdapter(Activity mActivity) {
        this.mActivity = mActivity;

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_locations_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        if (position % 2 == 0) {

            holder.location_tv.setTextColor(ContextCompat.getColor(mActivity, R.color.purple));
            holder.location_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.place_icon_mini, 0, 0, 0);
            holder.time_tv.setTextColor(ContextCompat.getColor(mActivity, R.color.purple));
            holder.time_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clock_icon, 0, 0, 0);
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView marker_icon, clock_icon;
        TextView location_tv, time_tv;


        public DetailsViewHolder(View itemView) {

            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            location_tv = (TextView) itemView.findViewById(R.id.location_tv);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
        }
    }

}
