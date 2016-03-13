package com.app.traphoria.locationservice;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.locationservice.adapter.EmbassyAdapter;
import com.app.traphoria.model.EmbassyDTO;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.JSONParser;
import com.app.traphoria.utility.MyOnClickListener;
import com.app.traphoria.utility.RecyclerTouchListener;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmbassyFragment extends Fragment {

    private View view;
    private GoogleMap map;
    private MapView mapview;
    private List<EmbassyDTO> list;
    private List<Polyline> PolyLinesList;

    public EmbassyFragment() {
        // Required empty public constructor
    }


    public static EmbassyFragment newInstance() {
        EmbassyFragment fragment = new EmbassyFragment();
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
        view = inflater.inflate(R.layout.fragment_embassy, container, false);

        mapview = (MapView) view.findViewById(R.id.map);
        mapview.onCreate(savedInstanceState);
        map = mapview.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        MapsInitializer.initialize(this.getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getEmbassyList();
    }


    public void onMapReady() {

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LatLng location = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_embassy_icon)).title(list.get(i).getAddress()));

            }

            PolyLinesList = new ArrayList<>();
            animateCamera(0);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.embassy_recycler_view);
            final LinearLayoutManager manager = new LinearLayoutManager(this.getActivity(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            EmbassyAdapter adapter = new EmbassyAdapter(
                    this.getActivity(), list);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int currentPos = manager.findFirstVisibleItemPosition();
                        animateCamera(currentPos);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new MyOnClickListener() {
                @Override
                public void onRecyclerClick(View view, int position) {
                    animateCamera(position);
                }

                @Override
                public void onRecyclerLongClick(View view, int position) {

                }

                @Override
                public void onItemClick(View view, int position) {

                }
            }));
        }


        LatLng current = new LatLng(TraphoriaPreference.getLatitude(getActivity()), TraphoriaPreference.getLongitude(getActivity()));
        map.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.place_violet)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }


    private void getEmbassyList() {

        if (Utils.isOnline(getActivity())) {

            Map<String, String> param = new HashMap<>();
            param.put("action", WebserviceConstant.GET_EMBASSY);
            param.put("lat", TraphoriaPreference.getLatitude(getActivity()) + "");
            param.put("lng", TraphoriaPreference.getLongitude(getActivity()) + "");
            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog("BANK ATM", "got some response = " + response.toString());

                                Type type = new TypeToken<ArrayList<EmbassyDTO>>() {
                                }.getType();

                                list = new Gson().fromJson(response.getJSONArray("embassy").toString(), type);

                                setList();
                            } catch (Exception e) {
                                setList();
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
        onMapReady();


    }


    private void animateCamera(int position) {
        EmbassyDTO embassyDTO = list.get(position);
        drawPath(TraphoriaPreference.getLatitude(getActivity()), TraphoriaPreference.getLongitude(getActivity()), Double.valueOf(embassyDTO.getLat()), Double.valueOf(embassyDTO.getLng()));
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(Double.valueOf(embassyDTO.getLat()),
                        Double.valueOf(embassyDTO.getLng())), 13));
    }


    public void drawPath(double startLat, double startLng, double endLat, double endLng) {
        StringBuilder urlString = new StringBuilder();
        try {
            urlString
                    .append("http://maps.googleapis.com/maps/api/directions/json");
            urlString.append("?origin=");
            urlString.append(startLat);
            urlString.append(",");
            urlString.append(startLng);
            urlString.append("&destination=");// to
            urlString.append(endLat);
            urlString.append(",");
            urlString.append(endLng);

            urlString.append("&sensor=false&mode=driving&alternatives=true");
            String str = urlString.toString();
            Log.i("info1", str);
            connectAsyncTask cat = new connectAsyncTask(str);
            cat.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                drawPath1(result);
            }

        }
    }

    public void drawPath1(String result) {
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);


            if (PolyLinesList != null && PolyLinesList.size() > 0) {
                for (Polyline line : PolyLinesList) {
                    line.remove();
                }

                PolyLinesList.clear();
            }

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                PolyLinesList.add(map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(10).color(Color.YELLOW).geodesic(true)));
                Log.i("info", "src :" + src.latitude + ", " + src.longitude);
                Log.i("info", "dest:" + dest.latitude + ", " + dest.longitude);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


}
