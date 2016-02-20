package com.app.traphoria.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.WakeLocker;
import com.app.traphoria.camera.CameraChooseDialogFragment;
import com.app.traphoria.camera.CameraSelectInterface;
import com.app.traphoria.camera.GallerySelectInterface;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.gps.GPSTracker;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonImageRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.app.traphoria.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.app.traphoria.CommonUtilities.EXTRA_MESSAGE;
import static com.app.traphoria.CommonUtilities.SENDER_ID;


public class SignUpScreen extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUp";
    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;
    private ImageLoader image_loader;
    private CameraChooseDialogFragment dFragment;
    private ImageView ivProfile;
    private File f = null;
    private byte[] bitmapdata;
    private String mobNumber;
    private Activity mActivity;
    private DisplayImageOptions options;
    private Toolbar mToolbar;
    private AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        mActivity = this;
        mobNumber = getIntent().getStringExtra("MOBILE_NUMBER");
        init();

        String pushRegistrationId = TraphoriaPreference.getPushRegistrationId(mActivity);
        if (pushRegistrationId == null || pushRegistrationId.equalsIgnoreCase("")) {
            registrationPushNotification();
        }

//        options = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(true)
//                .cacheOnDisk(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .considerExifParams(true)
//                .displayer(new SimpleBitmapDisplayer())
//                .showImageOnLoading(R.drawable.avtar)
//                .showImageOnFail(R.drawable.avtar)
//                .showImageForEmptyUri(R.drawable.avtar)
//                .build();


    }

    private void setToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.sign_up);
    }

    private void init() {

        setToolBar();
        ((Button) findViewById(R.id.btn_signup)).setOnClickListener(signUpClick);
        ivProfile = (ImageView) findViewById(R.id.img_user_image);
        ivProfile.setOnClickListener(addImageClick);
        setClick(R.id.edt_gender);
        setClick(R.id.edt_dob);

        String pushRegistrationId = TraphoriaPreference.getPushRegistrationId(mActivity);
        if (pushRegistrationId == null || pushRegistrationId.equalsIgnoreCase("")) {
            //registrationPushNotification();
        }

        GPSTracker gpsTracker = new GPSTracker(this);
    }

    View.OnClickListener signUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            doSignUp();
        }
    };


    View.OnClickListener addImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertCamera();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_gender:
                showSexDialog();
                break;
            case R.id.edt_dob:
                showCalendarDialog();
                break;
        }
    }

    public void showSexDialog() {
        final CharSequence[] items = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Gender");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.edt_gender, items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAlertCamera() {
        try {
            if (dFragment == null) {
                dFragment = new CameraChooseDialogFragment();
            }
            dFragment.setCallBack(cameraSelectInterface, gallerySelectInterface);
            // Show DialogFragment
            FragmentManager fm = getSupportFragmentManager();
            dFragment.show(fm, "Dialog Fragment");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.edt_dob, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
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

                    f = new File(this.getCacheDir(), "profile.png");
                    f.createNewFile();

//Convert bitmap to byte array
                    Bitmap bitmap1 = bitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(f);
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

                f = new File(this.getCacheDir(), "profile.png");
                f.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap1 = photo;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
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


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void doSignUp() {
        Utils.hideKeyboard(mActivity);
        if (validateForm()) {
            if (Utils.isOnline(mActivity)) {
                String mobNumber = getIntent().getStringExtra("MOB_NUMBER");
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.DO_SIGNUP);
                params.put("email", getViewText(R.id.edt_user_email));
                params.put("password", getViewText(R.id.edt_user_pwd));
                params.put("confirm_password", getViewText(R.id.edt_user_cnfrm_pwd));
                params.put("gender", getViewText(R.id.edt_gender).equals("Male") ? "M" : "F");
                params.put("dob", getViewText(R.id.edt_dob));
                params.put("device", "android");
                params.put("device_id", TraphoriaPreference.getPushRegistrationId(mActivity));
                params.put("name", getViewText(R.id.edt_user_name));
                params.put("mobile", mobNumber != null ? mobNumber : "");
                params.put("mobie_countrycode", TraphoriaPreference.getCountryCode(mActivity));
                params.put("lat", String.valueOf(TraphoriaPreference.getLatitude(mActivity)));
                params.put("lng",  String.valueOf(TraphoriaPreference.getLongitude(mActivity)));

                CustomProgressDialog.showProgDialog(mActivity, null);
                CustomJsonImageRequest postReq = new CustomJsonImageRequest(Request.Method.POST,
                        WebserviceConstant.SERVICE_BASE_URL, params, f,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                CustomProgressDialog.hideProgressDialog();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").
                                                toString(), UserDTO.class);

                                        TraphoriaPreference.putObjectIntoPref(mActivity, userDTO,
                                                PreferenceHelp.USER_INFO);
                                        startActivity(new Intent(mActivity, NavigationDrawerActivity.class));

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressDialog.hideProgressDialog();
                        Utils.showExceptionDialog(mActivity);
                    }
                });

                Log.i("info", postReq.toString());
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(mActivity);
            }
        }
    }

    public boolean validateForm() {

        if (getViewText(R.id.edt_user_name).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter name");
            return false;
        } else if (getViewText(R.id.edt_dob).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter DOB");
            return false;
        } else if (Utils.isFromDateGreater(Utils.getCurrentDate(), getViewText(R.id.edt_dob))) {
            Utils.showDialog(mActivity, "Message", "Please enter valid DOB");
            return false;
        } else if (getViewText(R.id.edt_gender).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter gender");
            return false;
        } else if (getViewText(R.id.edt_user_email).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter email id");
            return false;
        } else if (!Utils.isValidEmail(getViewText(R.id.edt_user_email))) {
            Utils.showDialog(mActivity, "Message", "Please enter valid email id");
            return false;
        } else if (getViewText(R.id.edt_user_pwd).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter password");
            return false;
        } else if (getViewText(R.id.edt_user_cnfrm_pwd).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter confirm password");
            return false;
        } else if (!getViewText(R.id.edt_user_pwd).equals(getViewText(R.id.edt_user_cnfrm_pwd))) {
            Utils.showDialog(mActivity, "Message", "password not match");
            return false;
        }
        return true;
    }

    // For Push notification
    private void registrationPushNotification() {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(mActivity);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(mActivity);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar
                .getRegistrationId(mActivity);

        TraphoriaPreference.setPushRegistrationId(mActivity, regId);
        Log.i("info", "RegId :" + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("info", "RegId :" + regId);
            // Registration is not present, register now with GCM
            GCMRegistrar.register(mActivity, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar
                    .isRegisteredOnServer(mActivity)) {
                // Skips registration.
                Log.i("info", "Already registered with GCM");
            } else {
                Log.i("info", "Not registered with GCM");
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message depending upon your app
             * requirement For now i am just displaying it on the screen
             * */

            // Showing received message

            // Releasing wake lock
            WakeLocker.release();
        }
    };


    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(mActivity);
        } catch (Exception e) {
            Utils.ShowLog(TAG, "UnRegister Receiver Error " + e.getMessage());
        }
        super.onDestroy();
    }


}
