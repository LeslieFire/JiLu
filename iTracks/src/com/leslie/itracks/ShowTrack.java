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
	
	private TrackDbAdapter mDbHelper;
	private LocateDbAdapter mlcDbHelper;
	
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private GoogleMap mMap;
	private float mZoomLevel = 14;
	private Polyline mMutablePolyline;
	private PolylineOptions options = new PolylineOptions();
	private Button mGps;
	private Button mSat;
	private Button mTraffic;
	private Button mStreetview;
	private String mDefCaption = "";
	private LatLng mCurLatLng; 			//current latitude...
	private Location mCurLocation;		//current location
	
	private LocationManager lm;
	private LocationListener locationListener;
	
	private int track_id;
	private Long rowId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "ShowTrack:onCreate.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_track);
		
		setUpMapIfNeeded();
	
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
//				Log.d(TAG, "ShowTrack:setUpMapIfNeeded, mMap != null");
				setUpMap();				
			}	
		}
	}

	private void setUpMap() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ShowTrack:setUpMap.");
		
		//location manager to get the information of GPS
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mCurLocation = new Location(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		mCurLatLng = new LatLng(mCurLocation.getLatitude(), mCurLocation.getLongitude());

		options.add(mCurLatLng);
		mMap.addMarker(new MarkerOptions().position(mCurLatLng).title("Marker"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurLatLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(mZoomLevel));
		
		locationListener = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10*60*1000, 0, locationListener);
	}
	
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

				options.add(mCurLatLng);
				mMutablePolyline = mMap.addPolyline(options);
				
				mMap.addMarker(new MarkerOptions().position(mCurLatLng).title("Marker"));
				mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurLatLng));
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
//	private void startTrackService() {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent("com.leslie.itracks.START_TRACK_SERVICE");
//		intent.putExtra(LocateDbAdapter.TRACKID, track_id);
//		startService(intent);
//	}
//	
//	private void stopTrackService(){
//		stopService(new Intent("com.leslie.itracks.START_TRACK_SERVICE"));
//	}
//
//	private void paintLocates() {
//		// TODO Auto-generated method stub
//		mlcDbHelper = new LocateDbAdapter(this);
//		mlcDbHelper.open();
//		Cursor mLocatesCursor = mlcDbHelper.getTrackAllLocates(track_id);
//		startManagingCursor(mLocatesCursor);
//		Resources resources = getResources();
//		Overlay overlays = new Overlay(resources
//				.getDrawable(R.drawable.icon), mLocatesCursor);
//		mMapView.getOverlays().add(overlays);
//		mlcDbHelper.close();
//	}
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
//	private void findViews() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "find views");
//		
//		//obtain view assit
//		mMapView = (MapView) findViewById(R.id.mv);
//		mMapView.setBuiltInZoomControls(true);
//		
//		
//		//set all kinds of buttons
//		mZin = (Button) findViewById(R.id.zin);
//		mZout = (Button) findViewById(R.id.zout);
//		mPanE = (Button) findViewById(R.id.pane);
//		mPanN = (Button) findViewById(R.id.pann);
//		mPanW = (Button) findViewById(R.id.panw);
//		mPanS = (Button) findViewById(R.id.pans);
//		mGps = (Button) findViewById(R.id.gps);
//		mSat = (Button) findViewById(R.id.sat);
//		mTraffic = (Button) findViewById(R.id.traffic);
//		mStreetview = (Button) findViewById(R.id.streetview);
//		
//		//location manager to get the information of GPS 
//		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		
//		locationListener = new MyLocationListener();
//		
//		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//		
//		
//		/*all kinds of click listener*/
//		mZin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				zoomIn();
//			}
//		});
//		
//		
//		mZout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				zoomOut();
//			}
//		});
//		
//		mPanE.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				panEast();
//			}
//		});
//		
//		mPanW.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				panWest();
//			}
//		});
//		
//		mPanN.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				panNorth();
//			}
//		});
//		
//		mPanS.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				panSouth();
//			}
//		});
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
//		mSat.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				toggleSatellite();
//			}
//		});
//		
//		mTraffic.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				toggleTraffic();
//			}
//		});
//		
//		mStreetview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				toggleStreetView();
//			}
//		});
//	}
//
//	public void panEast(){
//		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6(), 
//				mMapView.getMapCenter().getLongitudeE6()
//				 + mMapView.getLongitudeSpan() / 4);
//		mc.setCenter(pt);		
//	}
//	
//	public void panWest(){
//		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6(), 
//				mMapView.getMapCenter().getLongitudeE6()
//				 - mMapView.getLongitudeSpan() / 4);
//		mc.setCenter(pt);	
//		
//	}
//	
//	public void panSouth(){
//		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6()
//				- mMapView.getLatitudeSpan() / 4, 
//				mMapView.getMapCenter().getLongitudeE6());
//		mc.setCenter(pt);
//	}
//	
//	public void panNorth(){
//		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6()
//				+ mMapView.getLatitudeSpan() / 4, 
//				mMapView.getMapCenter().getLongitudeE6());
//		mc.setCenter(pt);
//		
//	}
//	
//	public void zoomIn(){
//		mc.zoomIn();
//		
//	}
//	
//	public void zoomOut(){
//		mc.zoomOut();
//		
//	}
//
//
//	private void toggleSatellite() {
//		// TODO Auto-generated method stub
//		mMapView.setSatellite(true);
//		mMapView.setTraffic(false);
//	}
//
//	private void toggleTraffic() {
//		// TODO Auto-generated method stub
//		mMapView.setSatellite(false);
//		mMapView.setTraffic(true);
//	}
//	
//	private void toggleStreetView() {
//		// TODO Auto-generated method stub
//		mMapView.setSatellite(false);
//		mMapView.setTraffic(true);
//	}
//
//	private void centerOnGPSPosition() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "center on GPS Position");
//		String provider = "gps";
//		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		
//		Location loc = lm.getLastKnownLocation(provider);
//		loc = lm.getLastKnownLocation(provider);
//		
//		mDefPoint = new GeoPoint((int)(loc.getLatitude() * 1000000),
//				(int)(loc.getLongitude() * 1000000));
//		mDefCaption = "I'm here.";
//		mc.animateTo(mDefPoint);
//		mc.setCenter(mDefPoint);
//		
//		//show the overlay
//		MyOverlay mo = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka), mMapView);
//		mo.onTap(mDefPoint, mMapView);
//		mMapView.getOverlays().add(mo);
//		
//	}
//	//this is used  draw an overlay on the map
//	protected class MyOverlay extends ItemizedOverlay{
//		
//
//		public MyOverlay(Drawable defaultMarker, MapView mapView) {
//			super(defaultMarker, mapView);
//			// TODO Auto-generated constructor stub
//		}
//	}
//	
//	protected class MyLocationListener implements LocationListener{
//
//		@Override
//		public void onLocationChanged(Location location) {
//			// TODO Auto-generated method stub
//			Log.d(TAG, "MyLocationListener::onLocationChanged..");
//			if (location != null) {
//				Toast.makeText(getBaseContext(), 
//						"Location changed : Lat: " + location.getLatitude()
//						+ " Lng: " + location.getLongitude(), 
//						Toast.LENGTH_SHORT).show();
//				
//				mDefPoint = new GeoPoint((int)(location.getLatitude() * 1000000), 
//										(int)(location.getLongitude() * 1000000));
//				mc.animateTo(mDefPoint);
//				mc.setCenter(mDefPoint);
//				
//				//show the map
//				MyOverlay mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka), mMapView);
//				mOverlay.onTap(mDefPoint, mMapView);
//				mMapView.getOverlays().add(mOverlay);
//				
//			}
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		
//	}
//	@Override
//	protected void onStop() {
//		super.onStop();
//		Log.d(TAG, "onStop");
//		// mDbHelper.close();
//		//mlcDbHelper.close();
//	}
//	
//	@Override
//    public void onDestroy() {
//		Log.d(TAG, "onDestroy.");
//        super.onDestroy();
//        stopTrackService();
//        }
//	
	
	
}
