package com.example.tests;

import java.util.ArrayList;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends ActionBarActivity {

	public final String TAG = "mainActivity";
	
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Main::onCreate");
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

        mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
        setContentView(mViewPager);
        
		try {
			final ActionBar bar = getSupportActionBar();
			Log.d(TAG, "Main::getSupportActionBar");
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
				Log.d(TAG, "Main::onCreate*savedInstanceState");
				bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
			}
		} catch (Exception e){
			System.out.println("ActionBar error:" + e.toString());
		}
	}
	
	public static class TabsAdapter extends FragmentPagerAdapter
		implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
		
		public final String TAG = "TabsAdapter";
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
	
		public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			Log.d(TAG, "Constructor");
		    mContext = activity;
		    mActionBar = activity.getSupportActionBar();
		    mViewPager = pager;
		    mViewPager.setAdapter(this);
		    mViewPager.setOnPageChangeListener(this);
		}
		
		public void addTab(Tab tab, Class<?> clss, Bundle args) {
			Log.d(TAG, "addTab");
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
			Log.d(TAG, "onPageSelected");
			mActionBar.setSelectedNavigationItem(position);
		}
		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Log.d(TAG, "getItem");
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}
		
		@Override
		public int getCount() {
		// TODO Auto-generated method stub
		return mTabs.size();
		}
		
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
					
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
			Log.d(TAG, "onTabSelected");
			Object tag = tab.getTag();
			for (int i=0; i<mTabs.size(); i++){
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}
		
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.d(TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");
		outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
	}

}
