package com.app.traphoria.trip.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

import com.app.traphoria.R;
import com.app.traphoria.customViews.MyTextView14;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.TripUserDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;

public class ViewTripGroupDetailsAdapter extends RecyclerView.Adapter<ViewTripGroupDetailsAdapter.DetailsViewHolder> {


    private List<TripUserDTO> tripUserList;
    private Context context;
    private DisplayImageOptions options;
    private View v;

    public ViewTripGroupDetailsAdapter(List<TripUserDTO> tripUserList, Context context) {
        this.tripUserList = tripUserList;
        this.context = context;
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
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_member_detail_row, parent, false);
        //addPassportVisaTextView(v);
        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);

        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        holder.member_name.setText(tripUserList.get(position).getName());
        holder.gender_age.setText(tripUserList.get(position).getGender() + " | " + tripUserList.get(position).getAge());
        try {
            ImageLoader.getInstance().displayImage(tripUserList.get(position).getImage(), holder.img_user_image,
                    options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                            ((ImageView) view).setImageResource(R.drawable.login_bg);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String s, View view, int i, int i1) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        addPassportVisaTextView(v, position);

    }

    private void addPassportVisaTextView(View mView, int position) {


        LinearLayout linear_detail = (LinearLayout) mView.findViewById(R.id.linear_detail);


        List<PassportDTO> listPassportDTO = tripUserList.get(position).getPassport();

        int tv_passport_no_id = 0;
        for (PassportDTO passportDTO : listPassportDTO) {

            tv_passport_no_id = tv_passport_no_id + 1;
            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.setPadding(convertDpToPixel((float) 20, context),
                    convertDpToPixel((float) 5, context),
                    convertDpToPixel((float) 20, context),
                    convertDpToPixel((float) 5, context));


            // Add First textview
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            MyTextView14 tv_passport_et = new MyTextView14(context);
            tv_passport_et.setText("Passport:");
            tv_passport_et.setTextColor(context.getResources().getColor(R.color.black));
            tv_passport_et.setTextSize((float) 14);
            tv_passport_et.setId(tv_passport_no_id);
            tv_passport_et.setLayoutParams(params1);
            relativeLayout.addView(tv_passport_et);


            // Add Second textview
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            MyTextView14 tv_pasport_no = new MyTextView14(context);
            tv_pasport_no.setText(passportDTO.getPassport_no());
            tv_pasport_no.setTextSize((float) 14);
            tv_pasport_no.setTextColor(context.getResources().getColor(R.color.black));
            params2.setMargins(convertDpToPixel((float) 20, context), 0, 0, 0);
            params2.addRule(RelativeLayout.RIGHT_OF, tv_passport_et.getId());
            tv_pasport_no.setLayoutParams(params2);
            relativeLayout.addView(tv_pasport_no);


            // Add Third textview
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            Drawable img = context.getResources().getDrawable(R.drawable.calender_btn);

            MyTextView14 tv_passport_time = new MyTextView14(context);
            tv_passport_time.setText(passportDTO.getExpire_date());
            tv_passport_time.setTextSize((float) 14);
            tv_passport_time.setTextColor(context.getResources().getColor(R.color.black));
            tv_passport_time.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            params3.setMargins(convertDpToPixel((float) 20, context), 0, 0, 0);
            params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tv_passport_time.setLayoutParams(params3);

            relativeLayout.addView(tv_passport_time);

            linear_detail.addView(relativeLayout);
        }


        List<VisaDTO> listVisaDTO = tripUserList.get(position).getVisa();

        int tv_visa_id = 0;
        for (VisaDTO visaDTO : listVisaDTO) {
            tv_visa_id = tv_visa_id + 1;

            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.setPadding(convertDpToPixel((float) 20, context),
                    convertDpToPixel((float) 5, context),
                    convertDpToPixel((float) 20, context),
                    convertDpToPixel((float) 5, context));


            // Add First textview
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            MyTextView14 tv_passport_et = new MyTextView14(context);
            tv_passport_et.setText("Travel Visa");
            tv_passport_et.setTextColor(context.getResources().getColor(R.color.black));
            tv_passport_et.setTextSize((float) 14);
            tv_passport_et.setId(tv_visa_id);
            tv_passport_et.setLayoutParams(params1);
            relativeLayout.addView(tv_passport_et);


            // Add Second textview
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            MyTextView14 tv_pasport_no = new MyTextView14(context);
            tv_pasport_no.setText(visaDTO.getEntry_type());
            tv_pasport_no.setTextSize((float) 14);
            tv_pasport_no.setTextColor(context.getResources().getColor(R.color.black));
            params2.setMargins(convertDpToPixel((float) 20, context), 0, 0, 0);
            params2.addRule(RelativeLayout.RIGHT_OF, tv_passport_et.getId());
            tv_pasport_no.setLayoutParams(params2);
            relativeLayout.addView(tv_pasport_no);


            // Add Third textview
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            Drawable img = context.getResources().getDrawable(R.drawable.calender_btn);

            MyTextView14 tv_passport_time = new MyTextView14(context);
            tv_passport_time.setText(visaDTO.getExpire_date());
            tv_passport_time.setTextSize((float) 14);
            tv_passport_time.setTextColor(context.getResources().getColor(R.color.black));
            tv_passport_time.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            params3.setMargins(convertDpToPixel((float) 20, context), 0, 0, 0);
            params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tv_passport_time.setLayoutParams(params3);

            relativeLayout.addView(tv_passport_time);

            linear_detail.addView(relativeLayout);
        }


    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }


    @Override
    public int getItemCount() {
        return tripUserList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_user_image;
        TextView member_name;
        TextView gender_age;
        TextView pasport_no;
        TextView passport_time;
        TextView visa;
        TextView visa_time;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            img_user_image = (CircleImageView) itemView.findViewById(R.id.img_user_image);
            member_name = (TextView) itemView.findViewById(R.id.member_name);
            gender_age = (TextView) itemView.findViewById(R.id.gender_age);


        }
    }

}
