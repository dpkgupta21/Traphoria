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
import com.app.traphoria.view.DestinationDetailScreen;

/**
 * Created by Harish on 1/26/2016.
 */
public class SearchDetinationAdapter extends RecyclerView.Adapter<SearchDetinationAdapter.DetailsViewHolder> {


    static Activity mActivity;

    public SearchDetinationAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_destination_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView thumbnail;
        TextView dest_name, type, expire;


        public DetailsViewHolder(View itemView) {

            super(itemView);


            cardView = (CardView) itemView.findViewById(R.id.card_view);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type = (TextView) itemView.findViewById(R.id.type);
            dest_name = (TextView) itemView.findViewById(R.id.dest_name);
            expire = (TextView) itemView.findViewById(R.id.expire);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mActivity.startActivity(new Intent(mActivity, DestinationDetailScreen.class));
                }
            });


        }
    }

}
