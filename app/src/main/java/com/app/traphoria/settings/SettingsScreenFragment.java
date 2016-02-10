package com.app.traphoria.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.app.traphoria.R;
import com.app.traphoria.adapter.DialogAdapter;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsScreenFragment extends BaseFragment {


    private View view;
    private DisplayImageOptions options;
    private UserDTO userDTO;
    private ToggleButton tgl_location, tgl_trip;
    private static Activity mActivity;

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


}