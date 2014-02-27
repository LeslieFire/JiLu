package com.example.tests;


import java.util.ResourceBundle;

import android.R.integer;
import android.app.Activity;
import android.content.ClipData.Item;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Identity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ShowTrackFragment extends Fragment{
	private static final String TAG = "ShowTrack";
	
	public static final String TRACK_POSITION = "track position";
	
	
	private float mZoomLevel;
	private float mGPSFreqency; //GPS 
	private String mDefValue = "";
	private String mStatus = "";
	
	private Button mGps;
	private Button mSat;
	private Button mTraffic;
	private Button mStreetview;
	private Button mStopTrack;
	private String mDefCaption = "";
	
	private LocationManager lm;
	private LocationListener locationListener;
	
	
	private long rowId;
	private int track_id;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "ShowTrack:onCreate.");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		Log.d(TAG, "ShowTrack:onCreateView.");
		return inflater.inflate(R.layout.show_track, container, false);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		/*The system calls this method as the first indication 
		that the user is leaving the fragment (though it does 
		not always mean the fragment is being destroyed). 
		This is usually where you should commit any 
		changes that should be persisted beyond the current 
		user session (because the user might not come back).*/
		
	}	
}
