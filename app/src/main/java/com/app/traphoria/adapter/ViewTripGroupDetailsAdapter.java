package com.app.traphoria.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import com.app.traphoria.R;

/**
 * Created by Harish on 1/26/2016.
 */
public class ViewTripGroupDetailsAdapter extends RecyclerView.Adapter<ViewTripGroupDetailsAdapter.DetailsViewHolder> {


    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_member_detail_row, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        CircleImageView img_user_image;
        TextView member_name, gender_age, pasport_no, passport_time, visa, visa_time;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            img_user_image = (CircleImageView) itemView.findViewById(R.id.img_user_image);
            member_name = (TextView) itemView.findViewById(R.id.member_name);
            gender_age = (TextView) itemView.findViewById(R.id.gender_age);
            pasport_no = (TextView) itemView.findViewById(R.id.pasport_no);
            passport_time = (TextView) itemView.findViewById(R.id.passport_time);
            visa = (TextView) itemView.findViewById(R.id.visa);
            visa_time = (TextView) itemView.findViewById(R.id.visa_time);


        }
    }

}
