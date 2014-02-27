package com.example.tests;

import android.os.Bundle;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class TrackListFragment extends ListFragment {
		
	public final String TAG = "TrackList";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "SettingFragment::onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		Log.d(TAG, "SettingFragment::onCreateView");
		return inflater.inflate(R.layout.fragment_pager_list, container, false);
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
