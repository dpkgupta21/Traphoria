package com.app.traphoria.trip.Dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.TripCountryDTO;

import java.util.ArrayList;
import java.util.List;

public class DialogAdapter
        extends RecyclerView.Adapter<DialogAdapter.VehicleHolder> {

    private Context context;
    private List<TripCountryDTO> vehicleList;
    private List<TripCountryDTO> filteredList;

    public DialogAdapter(Context context, List<TripCountryDTO> vehicleList) {
        this.context = context;
        this.vehicleList = vehicleList;
        filteredList = new ArrayList<>();
        filteredList.addAll(vehicleList);
    }

    @Override
    public VehicleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_list_item, null);
        VehicleHolder holder = new VehicleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VehicleHolder holder, int position) {
        holder.vehicleName.setText(vehicleList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    class VehicleHolder extends RecyclerView.ViewHolder {

        TextView vehicleName;

        VehicleHolder(View view) {
            super(view);
            vehicleName = (TextView) view.findViewById(R.id.vehicleName);
        }
    }

    public void getFilterList(String text) {
        vehicleList.clear();
        if (text.length() == 0) {
            vehicleList.addAll(filteredList);
        } else {
            for (int i = 0; i < filteredList.size(); i++) {
                if (filteredList.get(i).getName().toUpperCase().contains(text.toUpperCase())) {
                    vehicleList.add(filteredList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
