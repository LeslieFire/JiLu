package com.leslie.itracks;

import java.security.Principal;
import java.security.PublicKey;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class TrackListFragment extends ListFragment {

	// Container Activity must implement this interface
	OnTrackSelectedListener mCallback;
		
	public final String TAG = "TrackList";
	public static String CONTINUE = "continue";
	public static String NEW = "new";
	public static String SHOW = "show";
	public static String STATUS = "status";
	
	private TrackDbAdapter mDbHelper;
	private Cursor mTrackCursor;
	
	private boolean isExist = false;
	
	//Define static factors needed in menu
	private static final int MENU_NEW = Menu.FIRST + 1;
	private static final int MENU_CON = MENU_NEW + 1;
	private static final int MENU_SETTING = MENU_CON + 1;
	private static final int MENU_HELPS = MENU_SETTING + 1;
	private static final int MENU_EXIT = MENU_HELPS + 1;
	
	public interface OnTrackSelectedListener {
		public void onTrackSelected(int position);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		try {
			mCallback = (OnTrackSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTrackSelectedListener");
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		mDbHelper = new TrackDbAdapter(getActivity().getApplicationContext());
		mDbHelper.open();
		
//		render_tracks();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_pager_list, container, false);
	}
	
	@Override
	public void onPause(){
		/*The system calls this method as the first indication 
		that the user is leaving the fragment (though it does 
		not always mean the fragment is being destroyed). 
		This is usually where you should commit any 
		changes that should be persisted beyond the current 
		user session (because the user might not come back).*/
		
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "onListItemClick.");
		mCallback.onTrackSelected(position);
		
	}
	
/*	private void conTrackService() {
		// TODO Auto-generated method stub
		Log.d(TAG, "conTrackService");
		Intent intent = new Intent("com.leslie.itracks.START_TRACK_SERVICE");
		Long track_id = getListView().getSelectedItemId();
		intent.putExtra(LocateDbAdapter.TRACKID, track_id);
		startService(intent);
	}*/

/*	private void render_tracks() {
		// TODO Auto-generated method stub
		renderListView();
	}
*/
/*	private void renderListView() {
		// TODO Auto-generated method stub
		Log.d(TAG, "render list view");
		try {
			mTrackCursor = mDbHelper.getAllTrack();
			if (mTrackCursor == null)
				return ;
			
			String[] from = new String[] { TrackDbAdapter.NAME,
					TrackDbAdapter.CREATED ,TrackDbAdapter.DESC};
			int[] to = new int[] { R.id.name, R.id.created ,R.id.desc};
			SimpleCursorAdapter tracks = new SimpleCursorAdapter(this,
					R.layout.track_row, mTrackCursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			setListAdapter(tracks);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		
		
	}*/
	
}
