package com.app.traphoria.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import traphoria.com.app.traphoria.R;

/**
 * Created by Harish on 1/26/2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.DetailsViewHolder> {


    static Activity mActivity;

    public MessagesAdapter(Activity mActivity) {

        this.mActivity = mActivity;

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView more_icon;
        TextView name, message_detail, date, time;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            more_icon = (ImageView) itemView.findViewById(R.id.more_icon);
            name = (TextView) itemView.findViewById(R.id.name);
            message_detail = (TextView) itemView.findViewById(R.id.message_detail);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);


        }
    }

}
