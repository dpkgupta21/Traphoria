package com.app.traphoria.menucount;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.gps.GPSTracker;
import com.app.traphoria.lacaldabase.CountryDataSource;
import com.app.traphoria.model.MenuDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by deepak.gupta on 07-09-2015.
 */
public class MenuCountHandler implements Runnable {

    public static final int MENU_COUNT_HANDLER = 1001;
    private static final String TAG = "MenuCountHandler";
    private Handler handler;
    private Activity mActivity;

    public MenuCountHandler(Handler handler, Activity mActivity) {
        this.handler = handler;
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        String countryName = getLocation();
        getMenuCount(countryName);
    }

    public String getLocation() {
        String countryName = null;
        LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GPSTracker gpsTracker = new GPSTracker(mActivity);
            countryName = getMyLocationAddress(TraphoriaPreference.getLatitude(mActivity),
                    TraphoriaPreference.getLongitude(mActivity));

        }

        return countryName;

    }

//    private String getCountryIdFromList(String countryName) {
//        String countryId = null;
//        List<TripCountryDTO> listCountryDTO = null;
//        try {
//            listCountryDTO = new CountryDataSource(mActivity).getCountry();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for (TripCountryDTO countryDTO : listCountryDTO) {
//            if (countryDTO.getName().equalsIgnoreCase(countryName)) {
//                countryId = countryDTO.getId();
//            }
//        }
//        return countryId;
//    }

    public String getMyLocationAddress(double latitude, double longitude) {

        String countryName = "";
        Geocoder geocoder = new Geocoder(mActivity, Locale.ENGLISH);

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {

                Address fetchedAddress = addresses.get(0);
                countryName = fetchedAddress.getCountryName();


            } else
                countryName = "No location found..!";

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return countryName;
    }


    private void getMenuCount(String countryName) {

        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_EMERGENCY_NUMBER_ALERT_MENU_COUNT);
            params.put("country_name", countryName);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            //CustomProgressDialog.showProgDialog(mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    //CustomProgressDialog.hideProgressDialog();
                                    Utils.ShowLog(TAG, "got Menu count response = " + response.toString());
                                    MenuDTO menuDTO;
                                    menuDTO = new Gson().fromJson(response.
                                            getJSONObject("EmergencyNumber").toString(), MenuDTO.class);
                                    //setUpMenu();
                                    handleMenuCountResponse(menuDTO);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // CustomProgressDialog.hideProgressDialog();
                    // Utils.showExceptionDialog(mActivity);
                    //setUpMenu();
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } else {

        }


    }

    private void handleMenuCountResponse(MenuDTO menuDTO) {
        Utils.ShowLog(TAG, "handleMenuCountResponse");
        Message msg = handler.obtainMessage(MENU_COUNT_HANDLER, menuDTO);
        handler.sendMessage(msg);

    }

//
}
