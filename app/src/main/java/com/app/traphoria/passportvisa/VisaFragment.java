package com.app.traphoria.passportvisa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.app.traphoria.R;
import com.app.traphoria.lacaldabase.PassportTypeDataSource;
import com.app.traphoria.lacaldabase.VisaTypeDataSource;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.model.VisaTypeDTO;
import com.app.traphoria.passportvisa.adapter.VisaTypeAdapter;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseFragment;

import java.util.Calendar;
import java.util.List;


public class VisaFragment extends BaseFragment implements FetchInterface {

    private View view;

    public VisaFragment() {

    }

    public static VisaFragment newInstance() {
        VisaFragment fragment = new VisaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.fragment_visa, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setClick(R.id.visa_type, view);
        setClick(R.id.visa_country, view);
        setClick(R.id.visa_expire_on_tv, view);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.visa_type:

                openVisaTypeDialog();
                break;

            case R.id.visa_country:
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setFetchVehicleInterface(this);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getActivity().getFragmentManager(), "");
                break;
            case R.id.visa_expire_on_tv:
                showCalendarDialog();
                break;
        }
    }


    private void openVisaTypeDialog() {

        try {
            List<VisaTypeDTO> visaTypeList = new VisaTypeDataSource(getActivity()).getVisaType();
            visaTypeDialog(visaTypeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void visaTypeDialog(final List<VisaTypeDTO> passportTypeList) {

        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.relation_dialog);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        VisaTypeAdapter countryListAdapter = new VisaTypeAdapter(getActivity(), passportTypeList);
        listView.setAdapter(countryListAdapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                dialog.dismiss();
//                if (dialog != null) {
//                    dialog = null;
//                }
                setViewText(R.id.passprt_type, passportTypeList.get(position).getName(), view);
            }
        });
    }


    @Override
    public void vehicleName(String text) {
        setViewText(R.id.visa_country, text, view);
    }

    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view1, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        setViewText(R.id.txt_expiry, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year, view);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }





}
