package com.app.traphoria.settings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.adapter.DialogAdapter;
import com.app.traphoria.camera.CameraChooseDialogFragment;
import com.app.traphoria.camera.CameraSelectInterface;
import com.app.traphoria.camera.GallerySelectInterface;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.lacaldabase.NotificationDataSource;
import com.app.traphoria.model.NotificationDurationDTO;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.settings.adapter.CountryCodeAdapter;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonImageRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsScreenFragment extends BaseFragment implements FetchInterface {

    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;
    private CameraChooseDialogFragment dFragment;
    private byte[] bitmapdata;
    private ImageView ivProfile;
    private View view;
    private String TAG = "SETTINGS";
    private DisplayImageOptions options;
    private UserDTO userDTO;
    private ToggleButton tgl_location, tgl_trip;
    private static Activity mActivity;
    private File file =null;
    private List<Map<String, String>> countryCodeList;
    private Dialog dialogCountryCode;
    private Dialog mDialog = null;
    private List<NotificationDurationDTO> menuItemList;
    private String countryId = null;

    public SettingsScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings_screen_fragment, container, false);

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
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
                .showImageOnLoading(R.drawable.avtar_icon)
                .showImageOnFail(R.drawable.avtar_icon)
                .showImageForEmptyUri(R.drawable.avtar_icon)
                .build();


        ivProfile = (ImageView) view.findViewById(R.id.img_user_image);
        ivProfile.setOnClickListener(addImageClick);
        userDTO = TraphoriaPreference.getObjectFromPref(getActivity(), PreferenceConstant.USER_INFO);
        countryCodeList = getCountryCode();

    }

    public View.OnClickListener getAddImageClick() {
        return addImageClick;
    }

    private void setData() {
        setClick(R.id.edt_dob, view);
        setClick(R.id.gender, view);
        setClick(R.id.btn_save, view);
        setClick(R.id.notification, view);
        setClick(R.id.sel, view);
        setClick(R.id.txt_select_country_dropdown, view);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_user_image);
        ImageLoader.getInstance().displayImage(userDTO.getImage(), imageView,
                options);
        setViewText(R.id.edt_user_name, userDTO.getName(), view);
        setViewText(R.id.edt_dob, userDTO.getDob(), view);
        setViewText(R.id.gender, userDTO.getGender().equalsIgnoreCase("M") ? "Male" : "Female", view);
        setViewText(R.id.sel, userDTO.getCountrycode(), view);
        setViewText(R.id.edt_number, userDTO.getFamily_contact(), view);
        if (userDTO.getCountry() != null && userDTO.getCountry().getName() != null) {
            setViewText(R.id.txt_select_country_dropdown, userDTO.getCountry().getName(), view);
            setViewTag(R.id.txt_select_country_dropdown, userDTO.getCountry().getId(), view);
        }

        if (userDTO.getNotification_duration() != null && !userDTO.getNotification_duration().equals("")) {
            NotificationDurationDTO notificationDurationDTO = new NotificationDataSource(getActivity()).getWhereData("id", userDTO.getNotification_duration());
            if (notificationDurationDTO != null)
                setViewText(R.id.notification, notificationDurationDTO.getName(), view);

        }
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
            final View view1 = inflater.inflate(R.layout.sliding_dialog, null,
                    false);
            mDialog.setContentView(view1);
            mDialog.setCancelable(true);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            lp.gravity = Gravity.BOTTOM;
            mDialog.getWindow().setAttributes(lp);

            try {
                final ListView listView = (ListView) mDialog
                        .findViewById(R.id.listview);


                menuItemList = new NotificationDataSource(getActivity()).getNotification();
                final DialogAdapter adapter = new DialogAdapter(
                        mActivity, menuItemList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(dialogNotificationListener);


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
            case R.id.btn_save:
                updateProfile();
                break;
            case R.id.notification:
                showDialog();
                break;

            case R.id.sel:
                openDialogForCountry();
                break;

            case R.id.txt_select_country_dropdown:
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setFetchVehicleInterface(this);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getActivity().getFragmentManager(), "");
                break;
        }
    }


    View.OnClickListener addImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertCamera();
        }
    };

    private void showAlertCamera() {
        try {
            if (dFragment == null) {
                dFragment = new CameraChooseDialogFragment();
            }
            dFragment.setCallBack(cameraSelectInterface, gallerySelectInterface);
            // Show DialogFragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dFragment.show(fm, "Dialog Fragment");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    CameraSelectInterface cameraSelectInterface = new CameraSelectInterface() {
        @Override
        public void startCamera() {
            clickPictureUsingCamera();
        }
    };

    GallerySelectInterface gallerySelectInterface = new GallerySelectInterface() {
        @Override
        public void startGallery() {
            selectImageFromGallery();
        }
    };


    /**
     * This method is used to click image using camera and set the clicked image
     * in round image view.
     */
    private void clickPictureUsingCamera() {
        try {
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectImageFromGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            byte[] hash = null;
            if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK
                    && null != data) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Uri selectedImage = data.getData();
                Utils.ShowLog("DATA", data.toString());
                Utils.ShowLog("uri ", selectedImage.toString());

                // setting image in image in profile pic.
//                image_loader
//                        .displayImage(selectedImage.toString(), img_profile);
//
//                Bitmap bitmap = ((BitmapDrawable) img_profile.getDrawable())
//                        .getBitmap();
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), selectedImage);
                    ivProfile.setImageBitmap(bitmap);

                    file = new File(getActivity().getCacheDir(), "profile.png");
                    file.createNewFile();

//Convert bitmap to byte array
                    Bitmap bitmap1 = bitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                // Converting image's bitmap to byte array.
                // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //   bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                //hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            } else if (requestCode == CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivProfile.setImageBitmap(photo);

                file = new File(getActivity().getCacheDir(), "profile.png");
                file.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap1 = photo;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                // Converting image's bitmap to byte array.
                //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //photo.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                //hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

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
                    public void onDateSet(DatePicker view1, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.edt_dob, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, view);

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
                setViewText(R.id.gender, items[item].toString(), view);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void updateProfile() {
        if (Utils.isOnline(getActivity())) {
            CustomProgressDialog.showProgDialog(getActivity(), null);
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.EDIT_PROFILE);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));
            params.put("name", getViewText(R.id.edt_user_name, view));
            params.put("dob", getViewText(R.id.edt_dob, view));
            params.put("gender", getViewText(R.id.gender, view).equals("Male") ? "M" : "F");
            params.put("location", "");
            params.put("country_id", getViewTag(R.id.txt_select_country_dropdown, view));
            params.put("is_location_service", tgl_location.isChecked() ? "true" : "false");
            params.put("is_trip_tracker", tgl_trip.isChecked() ? "true" : "false");
            params.put("family_contact", getViewText(R.id.edt_number, view));
            if (getViewText(R.id.notification, view).equals("")) {
                params.put("notification_duration", "");
            } else {
                params.put("notification_duration", new NotificationDataSource(getActivity()).getWhereData("name", getViewText(R.id.notification, view)).getId());
            }
            params.put("countrycode", getViewText(R.id.sel, view));
            CustomJsonImageRequest postReq = new CustomJsonImageRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params, file,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());

                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                    TraphoriaPreference.putObjectIntoPref(getActivity(),
                                            userDTO, PreferenceConstant.USER_INFO);
                                    Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                                    intent.putExtra("fragmentNumber", 7);
                                    startActivity(intent);
                                } else {
                                    Utils.customDialog(Utils.getWebServiceMessage(response), getActivity());
                                }
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

        } else {
            Utils.showNoNetworkDialog(getActivity());
        }
    }


    private List<Map<String, String>> getCountryCode() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(Utils.loadJSONFromAsset(getActivity()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Map<String, String> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                map.put("dial_code", jsonObject.getString("dial_code"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void openDialogForCountry() {
        dialogCountryCode = new Dialog(getActivity());
        dialogCountryCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCountryCode.setContentView(R.layout.layout_country_code);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ListView listView = (ListView) dialogCountryCode.findViewById(R.id.list);
        CountryCodeAdapter adapter = new CountryCodeAdapter(getActivity(), countryCodeList);
        listView.setAdapter(adapter);
        dialogCountryCode.show();
        listView.setOnItemClickListener(dialogItemClickListener);
    }


    AdapterView.OnItemClickListener dialogItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {
            setViewText(R.id.sel, countryCodeList.get(i).get("dial_code"), view);
            dialogCountryCode.dismiss();
        }
    };


    AdapterView.OnItemClickListener dialogNotificationListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {
            mDialog.dismiss();
            setViewText(R.id.notification, menuItemList.get(i).getName(), view);
        }
    };


    @Override
    public void vehicleName(String text, String countryId) {
        setViewText(R.id.txt_select_country_dropdown, text, view);
        setViewTag(R.id.txt_select_country_dropdown, countryId, view);

        this.countryId = countryId;
    }
}