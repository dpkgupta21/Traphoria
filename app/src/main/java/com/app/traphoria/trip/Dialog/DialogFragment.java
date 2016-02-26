package com.app.traphoria.trip.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;


import com.app.traphoria.R;
import com.app.traphoria.lacaldabase.CountryDataSource;
import com.app.traphoria.model.CountryDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.app.traphoria.model.TripDTO;
import com.app.traphoria.utility.MyOnClickListener;
import com.app.traphoria.utility.RecyclerTouchListener;


import java.util.ArrayList;
import java.util.List;


@SuppressLint("NewApi")
public class DialogFragment extends android.app.DialogFragment {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private DialogAdapter adapter;
    private List<TripCountryDTO> countryList;
    private FetchInterface fetchVehicleInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fragment);
        init(dialog);
        dialog.show();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return dialog;
    }

    private void init(final Dialog dialog) {
        countryList = getCountryList();
        searchEditText = (EditText) dialog.findViewById(R.id.searchedVehicle);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.dialogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DialogAdapter(getActivity(), countryList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new MyOnClickListener() {
            @Override
            public void onRecyclerClick(View view, int position) {
                fetchVehicleInterface.vehicleName(countryList.get(position).getName(),
                        countryList.get(position).getId());
                dialog.dismiss();
            }

            @Override
            public void onRecyclerLongClick(View view, int position) {

            }

            @Override
            public void onItemClick(View view, int position) {

            }

        }));
    }

    private List<TripCountryDTO> getCountryList() {
        List<TripCountryDTO> list = null;
        try {
            list = new CountryDataSource((Context) getActivity()).getCountry();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    public void setFetchVehicleInterface(Object activity) {
        fetchVehicleInterface = (FetchInterface) activity;
    }
}
