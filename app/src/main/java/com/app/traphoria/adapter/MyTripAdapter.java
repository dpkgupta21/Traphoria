package com.app.traphoria.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import traphoria.com.app.traphoria.R;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.view.ViewTripGroupDetailsScreen;

/**
 * Created by Harish on 1/26/2016.
 */
public class MyTripAdapter extends RecyclerView.Adapter<MyTripAdapter.DetailsViewHolder> {


    static Activity mActivity;

    public MyTripAdapter(Activity mActivity) {

        this.mActivity = mActivity;

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trip_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        if (position == 1 || position == 3) {
            holder.trip_type_icon.setImageResource(R.drawable.group_icon);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView thumbnail, trip_type_icon;
        TextView dest_name, date, expiry;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            trip_type_icon = (ImageView) itemView.findViewById(R.id.trip_type_icon);
            dest_name = (TextView) itemView.findViewById(R.id.dest_name);
            date = (TextView) itemView.findViewById(R.id.date);
            expiry = (TextView) itemView.findViewById(R.id.expiry);


            trip_type_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mActivity.startActivity(new Intent(NavigationDrawerActivity.context, ViewTripGroupDetailsScreen.class));
                }
            });


        }
    }

}
