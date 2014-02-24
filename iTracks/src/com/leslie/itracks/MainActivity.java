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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ListActivity {

	public final String TAG = "mainActivity";
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(R.string.title);
		
		mDbHelper = new TrackDbAdapter(this);
		mDbHelper.open();
		
		render_tracks();
	}

	//init menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");
		
		super.onCreateOptionsMenu(menu);
		
		// add several menu
		menu.add(0, MENU_NEW, 0, R.string.menu_new).setIcon(
				R.drawable.new_track).setAlphabeticShortcut('N');
		menu.add(0, MENU_CON, 0, R.string.menu_con).setIcon(
				R.drawable.con_track).setAlphabeticShortcut('C');
		menu.add(0, MENU_SETTING, 0, R.string.menu_setting).setIcon(
				R.drawable.setting).setAlphabeticShortcut('S');
		menu.add(0, MENU_HELPS, 0, R.string.menu_helps).setIcon(
				R.drawable.helps).setAlphabeticShortcut('H');
		menu.add(0, MENU_EXIT, 0, R.string.menu_exit).setIcon(
				R.drawable.exit).setAlphabeticShortcut('E');
				
		return true;
	}
	
	//当一个菜单被选中时调用
	public boolean onOptionsItemSelected(MenuItem item){
		Log.d(TAG, "onOptionsItemSelected");
		Intent intent = new Intent();
		
		switch (item.getItemId()) {
		case MENU_NEW:
			intent.setClass(MainActivity.this, NewTrack.class);
			startActivity(intent);
			return true;
		case MENU_CON:
			//TODO:继续跟踪选择的记录
			conTrackService();
			return true;
		case MENU_SETTING:
			intent.setClass(MainActivity.this, Setting.class);
			startActivity(intent);
			return true;
		case MENU_HELPS:
			intent.setClass(MainActivity.this, Helps.class);
			startActivity(intent);
			return true;
		case MENU_EXIT:
			finish();
			
		default:
			break;
		}
		return true;
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "onListItemClick.");
		
		super.onListItemClick(l, v, position, id);
		Cursor cursor = mTrackCursor;
		cursor.moveToPosition(position);
		
		Intent intent = new Intent(this, ShowTrack.class);
		intent.putExtra(STATUS, SHOW);
		intent.putExtra(TrackDbAdapter.KEY_ROWID, id);
		intent.putExtra(TrackDbAdapter.NAME, cursor
				.getString(cursor.getColumnIndexOrThrow(TrackDbAdapter.NAME)));
		intent.putExtra(TrackDbAdapter.DESC, cursor
				.getString(cursor.getColumnIndexOrThrow(TrackDbAdapter.DESC)));
		
		startActivity(intent);
		
	}
	
	private void conTrackService() {
		// TODO Auto-generated method stub
		Log.d(TAG, "conTrackService");
		Intent intent = new Intent("com.leslie.itracks.START_TRACK_SERVICE");
		Long track_id = getListView().getSelectedItemId();
		intent.putExtra(LocateDbAdapter.TRACKID, track_id);
		startService(intent);
	}

	private void render_tracks() {
		// TODO Auto-generated method stub
		renderListView();
	}

	private void renderListView() {
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
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		
		if (keyCode == KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExist = false;
        }
    };
	private void exit() {
		// TODO Auto-generated method stub
		if (!isExist) {
			isExist = true;
			Toast.makeText(getApplicationContext(), 
					"请再按一次退出", 
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}
