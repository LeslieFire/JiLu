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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends BaseActivity{

	public final String TAG = "mainActivity";
	public static String CONTINUE = "continue";
	public static String NEW = "new";
	public static String SHOW = "show";
	public static String STATUS = "status";
	
	private TrackDbAdapter mDbHelper;
	private Cursor mTrackCursor;
	
	private boolean isExist = false;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main,menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Handle presses on the action bar items
		
		switch (item.getItemId()){
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void openSettings() {
		// TODO Auto-generated method stub
		
	}

	private void openSearch() {
		// TODO Auto-generated method stub
		
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
					"���ٰ�һ���˳�", 
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
}
