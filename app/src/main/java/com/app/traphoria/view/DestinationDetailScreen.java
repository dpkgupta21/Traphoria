package com.app.traphoria.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;

public class DestinationDetailScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_btn;
    private TextView place_name, top_dest_tv, culture_dest_tv, festival_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_detail_screen);
        init();
        assignClicks();
    }

    private void init() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        place_name = (TextView) findViewById(R.id.place_name);
        top_dest_tv = (TextView) findViewById(R.id.top_dest_tv);
        culture_dest_tv = (TextView) findViewById(R.id.culture_dest_tv);
        festival_tv = (TextView) findViewById(R.id.festival_tv);

    }

    private void assignClicks() {
        top_dest_tv.setOnClickListener(this);
        culture_dest_tv.setOnClickListener(this);
        festival_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
