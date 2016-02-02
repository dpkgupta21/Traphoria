package com.app.traphoria.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.traphoria.R;

/**
 * Created by Harish on 1/26/2016.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.DetailsViewHolder> {


    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


        if (position == 0 || position == 3) {
            holder.passport_details.setVisibility(View.VISIBLE);
            holder.visa_details.setVisibility(View.GONE);
            holder.type_tv.setText(R.string.P);
        } else {
            holder.passport_details.setVisibility(View.GONE);
            holder.visa_details.setVisibility(View.VISIBLE);
            holder.type_tv.setText(R.string.V);


        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView thumbnail;
        TextView type_tv;
        RelativeLayout passport_details, visa_details;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            passport_details = (RelativeLayout) itemView.findViewById(R.id.passport_details);
            visa_details = (RelativeLayout) itemView.findViewById(R.id.visa_details);
        }
    }

}
