package com.leslie.itracks;


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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.leslie.itracks.R.layout;




public class ShowTrack extends FragmentActivity{
	private static final String TAG = "ShowTrack";
	
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private GoogleMap mMap;
	private float mZoomLevel;
	private float mGPSFreqency; //GPS 
	private String mDefValue = "";
	private String mStatus = "";
	
	
	private Polyline mMutablePolyline;
	private PolylineOptions options = new PolylineOptions();
	private Button mGps;
	private Button mSat;
	private Button mTraffic;
	private Button mStreetview;
	private Button mStopTrack;
	private String mDefCaption = "";
	private LatLng mCurLatLng; 			//current latitude...
	
	private LocationManager lm;
	private LocationListener locationListener;
	
	private TrackDbAdapter mtDbHelper;
	private LocateDbAdapter mlcDbHelper;
	
	private long rowId;
	private int track_id;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "ShowTrack:onCreate.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_track);
		
		findViews();
		setListeners();
		openDatabase();
		setUpMapIfNeeded();
		startTrackServiceIfNeeded();
	
	}
	
	private void startTrackServiceIfNeeded() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack::startTrackServiceIfNeeded.");
		Bundle extras = getIntent().getExtras();
		mStatus = extras.getString(MainActivity.STATUS);
		if (mStatus == MainActivity.NEW) {
			startTrackService();
		}
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack::setListeners.");
		mStopTrack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "mStopTrack clicked");
				stopTrackService();
			}
		});
	}

	private void findViews() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack:findViews.");
		mStopTrack = (Button) findViewById(R.id.stop_track);		
	}

	private void openDatabase() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack:openDatabase.");
		rowId = getIntent().getLongExtra(TrackDbAdapter.KEY_ROWID, 0);
		mtDbHelper = new TrackDbAdapter(this);
		mtDbHelper.open();
		Cursor mCursor = mtDbHelper.getTrack(rowId);
		track_id = mCursor.getColumnIndex(TrackDbAdapter.ID);
		mtDbHelper.close();
		
		mlcDbHelper = Track.getDbHelper();		
	}

	@Override
	public void onResume(){
		Log.d(TAG, "ShowTrack:onResume.");
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		Log.d(TAG, "ShowTrack:setUpMapIfNeeded.");
		// TODO Auto-generated method stub
		if (mMap == null){
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();				
			}	
		}
	}

	private void setUpMap() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack:setUpMap.");
		Bundle extras = getIntent().getExtras();
		mStatus = extras.getString(MainActivity.STATUS);
		
		
		if (mStatus.equals(MainActivity.SHOW) || mStatus.equals(MainActivity.CONTINUE)) {
			//TODO:draw the former track
			Log.d(TAG, mStatus);
			paintLocates();
		} 
		if (mStatus.equals(MainActivity.CONTINUE) || mStatus.equals(MainActivity.NEW)) {
			//location manager to get the information of GPS
			try {
				Log.d(TAG, mStatus);
				lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
				//set current location the center of this camera and 
				//at the same time, save the location to Table
				//return LatLng
				mCurLatLng = new LatLng(location.getLatitude(), location.getLongitude());		
				options.add(mCurLatLng);
				mMap.addMarker(new MarkerOptions().position(mCurLatLng).title("Marker"));
				mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurLatLng));
				
				//set customer zoom level
				setZoomLevel();	
				
		 		//set update listener  (long)mGPSFreqency*
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60*1000, 0, new MyLocationListener());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
			}
		}
		
		
	}
	
	private void setZoomLevel() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack:setZoomLevel.");
		SharedPreferences settings = getSharedPreferences(Setting.SETTING_INFOS, 0);
		mZoomLevel = Float.parseFloat(settings.getString(Setting.SETTING_MAP, mDefValue));	
		mGPSFreqency = Float.parseFloat(settings.getString(Setting.SETTING_GPS, mDefValue));
 		mMap.animateCamera(CameraUpdateFactory.zoomTo(mZoomLevel));
	}

//	//set GPS location the center of this camera
//	public LatLng setCenterAndSave(Location location) {
//		Log.d(TAG, "ShowTrack:setCenterAndSave.");
//		mMap.addMarker(new MarkerOptions().position(mCurLatLng).title("Marker"));
//		mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurLatLng));
//		//because the service save locations, so this is not needed anymore
//		//add to Table Locate		
////		mlcDbHelper.createLocate(track_id, 
////								location.getLongitude(), 
////								location.getLatitude(), 
////								location.getAltitude());
//		
//		return new LatLng(location.getLatitude(), location.getLongitude());
//	}
	
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d(TAG, "MyLocationListener::onLocationChanged..");
			if (location != null) {
				Toast.makeText(getBaseContext(), 
						"Location changed : Lat: " + location.getLatitude()
						+ " Lng: " + location.getLongitude(), 
						Toast.LENGTH_SHORT).show();

				mCurLatLng = new LatLng(location.getLatitude(), location.getLongitude());
				mMap.addMarker(new MarkerOptions().position(mCurLatLng).title("Marker"));
				mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurLatLng));
				
				options.add(mCurLatLng);
				mMutablePolyline = mMap.addPolyline(options);				
			}	
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}
	
//	/**called when the activity is first created*/
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		mBMapMan=new BMapManager(getApplication());
//    	mBMapMan.init("8Y5Wcz84UeF6Htc1gOBzHh14", null);  
//    	//注意：请在试用setContentView前初始化BMapManager对象，否则会报错
//    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.show_track);
////		findViews();
////		centerOnGPSPosition();
////		revArgs();
////		
////		paintLocates();
////		startTrackService();
//		
//	}
//
	private void startTrackService() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack:startTrackService.");
		Intent intent = new Intent("com.leslie.itracks.START_TRACK_SERVICE");
		intent.putExtra(LocateDbAdapter.TRACKID, track_id);
		startService(intent);
	}
	
	private void stopTrackService(){
		Log.d(TAG, "ShowTrack:stopTrackService.");
		stopService(new Intent("com.leslie.itracks.START_TRACK_SERVICE"));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
		// mDbHelper.close();
		//mlcDbHelper.close();
	}
	@Override
    public void onDestroy() {
		Log.d(TAG, "onDestroy.");
        super.onDestroy();
//      stopTrackService();
    }
//
	private void paintLocates() {
		// TODO Auto-generated method stub
		Log.d(TAG, "show tracks::paintLocates");
		try {
			mlcDbHelper = new LocateDbAdapter(this);
			mlcDbHelper.open();
			Cursor mLocatesCursor = mlcDbHelper.getTrackAllLocates(track_id);
			if (!mLocatesCursor.isFirst()) {
				mLocatesCursor.moveToFirst();
			}
			PolylineOptions options = new PolylineOptions();
			int latColIndex;
			int lonColIndex;
			LatLng latLng;
			for(; !mLocatesCursor.isLast(); mLocatesCursor.moveToNext()){
				latColIndex = mLocatesCursor.getColumnIndex(LocateDbAdapter.LAT);
				lonColIndex = mLocatesCursor.getColumnIndex(LocateDbAdapter.LON);
				latLng = new LatLng(mLocatesCursor.getDouble(latColIndex), 
						mLocatesCursor.getDouble(lonColIndex));
				options.add(latLng);
			}
				
			latColIndex = mLocatesCursor.getColumnIndex(LocateDbAdapter.LAT);
			lonColIndex = mLocatesCursor.getColumnIndex(LocateDbAdapter.LON);
			latLng = new LatLng(mLocatesCursor.getDouble(latColIndex), 
					mLocatesCursor.getDouble(lonColIndex));
			options.add(latLng);
			
			mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
			mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		} catch (Exception e) {
			// TODO: handle exception
		}	
		mMap.addPolyline(options);
		
		mlcDbHelper.close();
	}
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	private void revArgs() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "revArgs.");
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			String name = extras.getString(TrackDbAdapter.NAME);
//			//String desc = extras.getString(TrackDbAdapter.DESC);
//			rowId = extras.getLong(TrackDbAdapter.KEY_ROWID);
//			track_id = rowId.intValue();
//			Log.d(TAG, "rowId=" + rowId);
//			if (name != null) {
//				setTitle(name);
//			}
//		}
//	}
//

//		mGps.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				centerOnGPSPosition();
//			}
//		});
//		


//	

//	
	
	
}
