package com.app.traphoria.camera;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.app.traphoria.R;


public class CameraChooseDialogFragment extends DialogFragment {

    private CameraSelectInterface cameraSelectInterface;
    private GallerySelectInterface gallerySelectInterface;

    public static CameraChooseDialogFragment newInstance(String param1, String param2) {
        CameraChooseDialogFragment fragment = new CameraChooseDialogFragment();
        return fragment;
    }

    public CameraChooseDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_choose_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().setTitle("Profile photo");
        ((ImageView) view.findViewById(R.id.img_camera)).setOnClickListener(positiveOnClick);
        ((ImageView) view.findViewById(R.id.img_gallery)).setOnClickListener(negativeOnClick);
        return view;
    }

    public void setCallBack(CameraSelectInterface cameraSelectInterface, GallerySelectInterface gallerySelectInterface) {
        this.cameraSelectInterface = cameraSelectInterface;
        this.gallerySelectInterface = gallerySelectInterface;
    }

    View.OnClickListener positiveOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraSelectInterface.startCamera();
        }

    };


    View.OnClickListener negativeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gallerySelectInterface.startGallery();
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }
}
