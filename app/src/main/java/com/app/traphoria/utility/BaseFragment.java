package com.app.traphoria.utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;




public class BaseFragment extends Fragment implements OnClickListener, OnCheckedChangeListener{

	public static final TouchEffect TOUCH = new TouchEffect();
	public static final TouchEffectImageView TOUCH_IMAGEVIEW = new TouchEffectImageView();
	FragmentManager fm;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/**
	 * Method use to set Touch and Click listener on View.
	 * @param id Resource id of View
	 * @param view your layout view.
	 * @return
	 */
	public View setTouchNClick(int id, View view) {

		View v = view.findViewById(id);
		v.setOnClickListener(this);
		if(v instanceof ImageView){
			v.setOnTouchListener(TOUCH_IMAGEVIEW);
		}else
			v.setOnTouchListener(TOUCH);
		return v;
	}
	
	/**
	 Method use to set Click listener on View.
	 * @param id Resource id of View
	 * @param view your layout view.
	 * @return
	 */
	public View setClick(int id, View view) {

		View v = view.findViewById(id);
		v.setOnClickListener(this);
		return v;
	}
	
	/**
	 * Method use to enable/disable view.
	 * @param id resource id.
	 * @param view
	 * @param flag flag true if you want to make view enable else false
	 */
	public void setViewEnable(int id, View view, boolean flag){
		View v = view.findViewById(id);
		v.setEnabled(flag);
	}
	
	/**
	 * Method use to set ViewVisiblity
	 * @param id id resource id of View.
	 * @param view
	 * @param flag flag value can be VISIBLE, GONE, INVISIBLE.
	 */
	public void setViewVisibility(int id, View view, int flag){
		View v = view.findViewById(id);
		v.setVisibility(flag);
	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent i  = null;
		switch (arg0.getId()) {
		
		}
	}
	
	/**
	 * Method use to set text on TextView, EditText, Button.
	 * @param id resource of TextView, EditText, Button.
	 * @param text string you want to set on TextView, EditText, Button
	 * @param view
	 */
	public void setViewText(int id, String text, View view){
		View v = ((View)view.findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			tv.setText(text);
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			et.setText(text);
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			btn.setText(text);
		}
		
	}
	
	/**
	 * Method use to get Text from TextView, EditText, Button.
	 * @param id resource id TextView, EditText, Button
	 * @param view
	 * @return string text from view
	 */
	public String getViewText(int id, View view){
		String text="";
		View v = ((View)view.findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			text = tv.getText().toString().trim();
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			text = et.getText().toString().trim();
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			text = btn.getText().toString().trim();
		}
		return text;
	}
	

	/**
	 * Method use to put focus on EditText.
	 * @param id resourceid of EditText.
	 * @param view
	 */
	public void setEditTextFocus(int id, View view){
		EditText et = (EditText) view.findViewById(id);
		et.requestFocus();
	}
	
	/**
	 * To check whether ToggleButton is checked or not
	 * @param id resource id ToggleButton
	 * @param view
	 * @return true if ToggleButton is checked else false
	 */
	public boolean isToggleButtonChecked(int id, View view){
		ToggleButton cb = (ToggleButton) view.findViewById(id);
		return cb.isChecked();
	}
	
	/**
	 * Method use to add Checkbox listener on CheckBox
	 * @param id resource id of checkbox
	 * @param view
	 */
	public void setCheckBoxCheckListener(int id, View view) {

		CheckBox cb = (CheckBox) view.findViewById(id);
		cb.setOnCheckedChangeListener(this);
	}
	
	/**
	 * Method use to check checkbox
	 * @param id resourceid of CheckBox
	 * @param checked true for checked and else for unchecked.
	 * @param view
	 */
	public void setCheckBoxCheck(int id, boolean checked, View view){
		CheckBox cb = (CheckBox) view.findViewById(id);
		cb.setChecked(checked);
	}
	
	/**
	 * Method use to add ToggleButtonListner
	 * @param id resource id of Togglebutton
	 * @param view
	 */
	public void setToggleButtonListner(int id, View view){
		ToggleButton cb = (ToggleButton) view.findViewById(id);
		cb.setOnCheckedChangeListener(this);
	}
	
	
	/**
	 * To check whether checkbox is checked or not
	 * @param id resouce id of checkbox
	 * @param view
	 * @return true if checkbox is checked else false
	 */
	public boolean isCheckBoxChecked(int id, View view){
		CheckBox cb = (CheckBox) view.findViewById(id);
		return cb.isChecked();
	}
	
	

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}
	public void setToggleButtonCheked(int id, View view, boolean isChecked){
		ToggleButton cb = (ToggleButton) view.findViewById(id);
		//cb.setSelected(isChecked);
		cb.setChecked(isChecked);
	}
	
	
	


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
}
