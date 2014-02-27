package com.example.tests;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment {
	private static final String TAG = "Setting";	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d(TAG, "SettingFragment::onCreate");
		super.onCreate(savedInstanceState);
		
		//Load the preferences from an XML resource
			
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		Log.d(TAG, "SettingFragment::onCreateView");
		return inflater.inflate(R.layout.settings, container, false);
	}
	
//	private void restorePrefs() {
//		// TODO Auto-generated method stub
//				
//		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
//		int setting_gps_p = settings.getInt(SETTING_GPS_POSITION, 0);
//		int setting_map_level_p = settings.getInt(SETTING_MAP_POSITION, 0);
//		Log.d(TAG, "restore prefs: setting_gps= " + setting_gps_p 
//				+ ", " + "setting_map_level= " + setting_map_level_p);
//		
//		//if value exist, set it to assit
//		if (setting_gps_p != 0 && setting_map_level_p != 0) {
//			field_setting_gps.setSelection(setting_gps_p);
//			field_setting_map_level.setSelection(setting_map_level_p);
//			button_setting_submit.requestFocus();
//		} else if (setting_gps_p != 0) {
//			field_setting_gps.setSelection(setting_gps_p);
//			field_setting_map_level.requestFocus();
//		} else if(setting_map_level_p != 0){
//			field_setting_map_level.setSelection(setting_map_level_p);
//			field_setting_gps.requestFocus();
//		} else{
//			field_setting_gps.requestFocus();
//		}
//		
//	}
//
//
//	private void setListensers() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "set listensers");
//		
//		button_setting_submit.setOnClickListener(setting_submit);
//	}
//	
//	private OnClickListener setting_submit = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Log.d(TAG, "On Click, new track...");
//			
//			try {
//				String gps = field_setting_gps.getSelectedItem().toString();
//				String map = field_setting_map_level.getSelectedItem().toString();
//				
//				if (gps.equals("") || map.equals("")) {
//					Toast.makeText(Setting.this, 
//							getString(R.string.setting_null), 
//							Toast.LENGTH_SHORT).show();
//				} else {
//					//save  setting
//					storePrefs();
//					Toast.makeText(Setting.this, 
//							getString(R.string.setting_ok), 
//							Toast.LENGTH_SHORT).show();
//					//jump to main Activity
//					Intent intent = new Intent();
//					intent.setClass(Setting.this, MainActivity.class);
//					startActivity(intent);
//				}
//			} catch (Exception err) {
//				// TODO: handle exception
//				Log.d(TAG, "error" + err.toString());
//				Toast.makeText(Setting.this, 
//						getString(R.string.setting_fail), 
//						Toast.LENGTH_SHORT).show();
//			}
//		}	
//	};		
//	private void storePrefs() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "storePrefs setting infos");
//		//get SharedPreferences Object
//		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
//		//save the setting 
//		settings.edit()
//		.putString(SETTING_GPS, field_setting_gps.getSelectedItem().toString())
//		.putString(SETTING_MAP, field_setting_map_level.getSelectedItem().toString())
//		.putInt(SETTING_GPS_POSITION, field_setting_gps.getSelectedItemPosition())
//		.putInt(SETTING_MAP_POSITION, field_setting_map_level.getSelectedItemPosition())
//		.commit();
//	}
//
//
//
//	private void findViews() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "find Views");
//		
//		button_setting_submit = (Button) findViewById(R.id.setting_submit);
//		field_setting_gps = (Spinner) findViewById(R.id.setting_gps);
//		field_setting_map_level = (Spinner) findViewById(R.id.setting_map_level);
//		
//		//set gps drop down view
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				this, R.array.gps, android.R.layout.simple_spinner_item);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		field_setting_gps.setAdapter(adapter);
//		
//		//set map drop down view
//		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
//				this, R.array.map, android.R.layout.simple_spinner_item);
//		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		field_setting_map_level.setAdapter(adapter2);
//		
//		
//	}
//	@Override
//	protected void onStop(){
//		super.onStop();
//		Log.d(TAG, "on stop");
//		//save individual settings.
//		storePrefs();
//	}
}
