package com.app.traphoria.locationservice.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.EmbassyDTO;

import java.util.List;

public class EmbassyAdapter extends RecyclerView.Adapter<EmbassyAdapter.EmbassyViewHolder> {

    private Context context;
    private List<EmbassyDTO> embassyList;

    public EmbassyAdapter(Context context, List<EmbassyDTO> embassyList) {
        this.context = context;
        this.embassyList = embassyList;
    }


//    @Override
//    public EmbassyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.embassy_row_layout, null);
//        return new EmbassyViewHolder(view);
//    }

    @Override
    public EmbassyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.embassy_row_layout, parent, false);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        view.setLayoutParams(new RecyclerView.LayoutParams(width,
                RecyclerView.LayoutParams.MATCH_PARENT));
        return new EmbassyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmbassyViewHolder holder, int position) {
        EmbassyDTO embassyBean = embassyList.get(position);
        holder.txt_title.setText(embassyBean.getLocation_country() + " Embassy in " + embassyBean.getLocation_city());
        holder.txt_address.setText(embassyBean.getAddress());
        holder.txt_tel.setText("Tel: " + embassyBean.getPhone());
        if (embassyBean.getWeb() != null && !embassyBean.getWeb().equalsIgnoreCase(""))
            holder.txt_web.setText("Web: " + embassyBean.getWeb());
        else
            holder.txt_web.setVisibility(View.INVISIBLE);
        if (embassyBean.getDistance() != null && !embassyBean.getDistance().equalsIgnoreCase(""))
            holder.txt_distance.setText("Distance :" + embassyBean.getDistance());
        else
            holder.txt_web.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return embassyList.size();
    }


    class EmbassyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_address;
        TextView txt_tel;
        TextView txt_web;
        TextView txt_distance;

        public EmbassyViewHolder(View itemView) {
            super(itemView);


            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            txt_tel = (TextView) itemView.findViewById(R.id.txt_tel);
            txt_web = (TextView) itemView.findViewById(R.id.txt_web);
            txt_distance = (TextView) itemView.findViewById(R.id.txt_distance);


        }
    }
}
