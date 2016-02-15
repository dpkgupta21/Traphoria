package com.app.traphoria.settings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.adapter.DialogAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.TripDetailsDTO;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonImageRequest;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsScreenFragment extends BaseFragment {


    private View view;
    private String TAG="SETTINGS";
    private DisplayImageOptions options;
    private UserDTO userDTO;
    private ToggleButton tgl_location, tgl_trip;
    private static Activity mActivity;
    private File file;

    public SettingsScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings_screen_fragment, container, false);
        init();
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setData();

    }

    private void init() {
        mActivity = NavigationDrawerActivity.mActivity;
        tgl_location = (ToggleButton) view.findViewById(R.id.tgl_location);
        tgl_trip = (ToggleButton) view.findViewById(R.id.tgl_trip);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();

        setClick(R.id.edt_dob,view);
        setClick(R.id.gender,view);

        userDTO = TraphoriaPreference.getObjectFromPref(getActivity(), PreferenceConstant.USER_INFO);

        showDialog();

    }

    private void setData() {

        ImageView imageView = (ImageView) view.findViewById(R.id.img_user_image);
        ImageLoader.getInstance().displayImage(userDTO.getImage(), imageView,
                options);

        setViewText(R.id.edt_user_name, userDTO.getName(), view);
        setViewText(R.id.edt_dob, userDTO.getDob(), view);
        setViewText(R.id.gender, userDTO.getGender(), view);
        if (userDTO.is_location_service()) {

            tgl_location.setChecked(true);
        } else {
            tgl_location.setChecked(false);
        }

        if (userDTO.is_trip_tracker()) {
            tgl_trip.setChecked(true);
        } else {
            tgl_trip.setChecked(false);
        }
    }

    public void showDialog() {
        Dialog mDialog = null;
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mActivity != null) {
            mDialog = new Dialog(mActivity, R.style.DialogSlideAnim);
            // hide to default title for Dialog
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // mDialog.getWindow().setGravity(Gravity.BOTTOM);

            // inflate the layout dialog_layout.xml and set it as
            // contentView
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sliding_dialog, null,
                    false);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            lp.gravity = Gravity.BOTTOM;
            mDialog.getWindow().setAttributes(lp);


            final ListView listView = (ListView) mDialog
                    .findViewById(R.id.listview);

            final DialogAdapter adapter = new DialogAdapter(
                    mActivity);


            listView.setAdapter(adapter);


            try {
                // Display the dialog
                mDialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gender:
                showSexDialog();
                break;
            case R.id.edt_dob:
                showCalendarDialog();
                break;
        }
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
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.edt_dob, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year,view);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }


    public void showSexDialog() {
        final CharSequence[] items = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Gender");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                // Do something with the selection
                setViewText(R.id.gender, items[item].toString(),view);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void updateProfile()
    {
        if(Utils.isOnline(getActivity()))
        {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.EDIT_PROFILE);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));

            params.put("name",getViewText(R.id.edt_user_name, view));
            params.put("dob",getViewText(R.id.edt_dob, view));
            params.put("gender",getViewText(R.id.gender,view).equals("Male") ? "M" : "F");
            params.put("location","");
            params.put("is_push_alert","");
            params.put("is_location_service",tgl_location.isChecked()?"true":"false");
            params.put("is_trip_tracker",tgl_trip.isChecked()?"true":"false");
            params.put("family_contact", getViewText(R.id.edt_number, view));

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonImageRequest postReq = new CustomJsonImageRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,file,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());

                            try {

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
                }
            });

            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.hideProgressDialog();

        }
        else
        {
            Utils.showNoNetworkDialog(getActivity());
        }
    }


}