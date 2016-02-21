package com.app.traphoria.locationservice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.NearBYLocationDTO;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HospitalFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private GoogleMap map;
    private List<NearBYLocationDTO> list;

    public HospitalFragment() {
        // Required empty public constructor
    }

    public static HospitalFragment newInstance() {
        HospitalFragment fragment = new HospitalFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hospital, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHospitalList(TraphoriaPreference.getLatitude(getActivity()) + "", TraphoriaPreference.getLongitude(getActivity()) + "", "hospital");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LatLng location = new LatLng(Double.parseDouble(list.get(i).getGeometry().getLocation().getLat()), Double.parseDouble(list.get(i).getGeometry().getLocation().getLng()));
                map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hospital_icon_copy)).title(list.get(i).getName() + "\n" + list.get(i).getVicinity()));

            }

        }

        LatLng current = new LatLng(TraphoriaPreference.getLatitude(getActivity()), TraphoriaPreference.getLongitude(getActivity()));
        map.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.place_violet)));
        map.moveCamera(CameraUpdateFactory.newLatLng(current));
    }


    private void getHospitalList(String lat, String lng, String type) {
        if (Utils.isOnline(getActivity())) {

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.locationUrl(lat, lng, type, "500"), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog("Hospital", "got some response = " + response.toString());

                                Type type = new TypeToken<ArrayList<NearBYLocationDTO>>() {
                                }.getType();

                                list = new Gson().fromJson(response.getJSONArray("results").toString(), type);

                                setList();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(getActivity(), null);

        } else {
            Utils.showNoNetworkDialog(getActivity());
        }

    }

    private void setList() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.hospital_map);
        mapFragment.getMapAsync(this);

    }


}
