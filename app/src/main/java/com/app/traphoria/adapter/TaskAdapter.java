package com.app.traphoria.adapter;

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
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.DetailsViewHolder> {


    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row_layout, parent, false);

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
        ImageView status_pin;
        TextView task, task_description, shared_with;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            status_pin = (ImageView) itemView.findViewById(R.id.status_pin);
            task = (TextView) itemView.findViewById(R.id.task);
            task_description = (TextView) itemView.findViewById(R.id.task_description);
            shared_with = (TextView) itemView.findViewById(R.id.shared_with);
        }
    }

}
