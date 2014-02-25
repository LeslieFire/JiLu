package com.leslie.itracks;

import java.security.Principal;
import java.security.PublicKey;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R;
import android.R.bool;
import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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


public class MainActivity extends Activity{

	public final String TAG = "mainActivity";
	public static String CONTINUE = "continue";
	public static String NEW = "new";
	public static String SHOW = "show";
	public static String STATUS = "status";
	
	private TrackDbAdapter mDbHelper;
	private Cursor mTrackCursor;
	
	private boolean isExist = false;
	
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);
		
		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.past),
				TrackListFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.preference),
				SettingFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.tracking),
				ShowTrackFragment.class, null);
		
		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}
	
	public static class TabsAdapter extends FragmentPagerAdapter
    			implements ActionBar.TabListener, ViewPager.OnPageChangeListener, TrackList.OnTrackSelectedListener {

		private final Context mContext;
	    private final ActionBar mActionBar;
	    private final ViewPager mViewPager;
	    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	    static final class TabInfo {
	    	private final Class<?> clss;
	        	private final Bundle args;

	            TabInfo(Class<?> _class, Bundle _args) {
	                clss = _class;
	                args = _args;
	            }
	        }
 
		public TabsAdapter(Activity activity, ViewPager pager) {
	            super(activity.getFragmentManager());
	            mContext = activity;
	            mActionBar = activity.getActionBar();
	            mViewPager = pager;
	            mViewPager.setAdapter(this);
	            mViewPager.setOnPageChangeListener(this);
	        }

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mActionBar.setSelectedNavigationItem(position);
		}
		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabs.size();
		}


		@Override
		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
						
		}


		@Override
		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Object tag = tab.getTag();
			for (int i=0; i<mTabs.size(); i++){
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}


		@Override
		public void onTabUnselected(Tab arg0,
				android.app.FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
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
	
	public void onTrackSelected(int position) {
		ShowTrackFragment showTrack = (ShowTrackFragment)
				getSupportFragmentManager().findFragmentById(R.id.map_fragment);
		
		if (showTrack != null) {
			//if in two-pane layout
			//call a method in the showTrackFragment to update its content
		} else {
			//otherwise, in one-pane layout and must swap frags..
			
			//create fragment and give it an argument for the selected track
			ShowTrackFragment newFragment = new ShowTrackFragment();
			Bundle args = new Bundle();
			args.putInt(ShowTrackFragment.TRACK_POSITION, position);
			newFragment.setArguments(args);
			
			android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
			
			transaction.replace(R.id.pager, newFragment);
			transaction.addToBackStack(null);
			
			transaction.commit();
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
					"���ٰ�һ���˳�", 
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
}
