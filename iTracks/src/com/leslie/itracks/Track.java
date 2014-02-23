package com.leslie.itracks;


import android.R.integer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Track extends Service{

	private static final String TAG = "Track";
	
	private LocationManager lManager;
	private LocationListener locationListener;
	static LocateDbAdapter mlcDbHelper = null;
	private int track_id;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onBind.");
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.d(TAG, "onStart.");
		super.onStartCommand(intent, flags, startId);
		
		mlcDbHelper = new LocateDbAdapter(this);
		mlcDbHelper.open();
		
		Bundle extras = intent.getExtras();
		if (extras != null) {
			track_id = extras.getInt(LocateDbAdapter.TRACKID);
		}
		Log.d(TAG, "track id = " + track_id);
		
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		SharedPreferences settings = getSharedPreferences(Setting.SETTING_INFOS, 0);	

		lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
										Long.parseLong(settings.getString(Setting.SETTING_GPS, ""))*60*1000, 
										0, 
										locationListener);
		
		return startId;
 	}
	
	public static LocateDbAdapter getDbHelper(){
		return mlcDbHelper;
	}
	public void onDestory(){
		Log.d(TAG, "OnDestory.");
		super.onDestroy();
		mlcDbHelper.close();
	}
	
	protected class MyLocationListener implements LocationListener{
		@Override
		public void onLocationChanged(Location loc){
			Log.d(TAG, "MyLocationListener::onLocationChanged");
			
			if (loc != null) {
				mlcDbHelper.createLocate(track_id, loc.getLongitude(), 
						loc.getLatitude(), loc.getAltitude());
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), 
					"Provider Disabled.", 
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), 
					"Provider Enable. Provider:" + provider, 
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void startTrackService(){
		Intent intent = new Intent("com.leslie.itracks.START_TRACK_SERVICE");
		intent.putExtra(LocateDbAdapter.TRACKID, track_id);
	}
	private void stopTrackService(){
		stopService(new Intent("com.leslie.itracks.START_TRACK_SERVICE"));
	}
}
