package com.leslie.itracks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewTrack extends Fragment{
	private static final String TAG = "NewTrack";
	private Button new_submit;
	private EditText field_new_name;
	private EditText field_new_desc;
	
	private TrackDbAdapter mDbHelper;
	
	@Override
	public void  onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_track);
		setTitle(R.string.menu_new);
		
		findViews();
		setListensers();
		
		mDbHelper = new TrackDbAdapter(this);
		mDbHelper.open();
	}

	private void setListensers() {
		// TODO Auto-generated method stub
		Log.d(TAG, "setListender");
		
		new_submit.setOnClickListener(new_track);
	}
	
	private OnClickListener new_track = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "OnClick new_track...");
				try {
					String name = (field_new_name.getText().toString());
					String desc = (field_new_desc.getText().toString());
					
					if (name.equals("")){
						Toast.makeText(NewTrack.this, 
									   getString(R.string.new_name_null), 
									   Toast.LENGTH_SHORT)
									   .show();
						
					}else {
						//���ô洢�ӿڱ��浽��ݿⲢ����Service
						Long row_id = mDbHelper.createTrack(name, desc);
						
						Log.d(TAG, "row_id=" + row_id);
						
						Intent intent = new Intent();
						intent.setClass(NewTrack.this, ShowTrack.class);
						intent.putExtra(MainActivity.STATUS, MainActivity.NEW);
						intent.putExtra(TrackDbAdapter.KEY_ROWID, row_id);
						intent.putExtra(TrackDbAdapter.NAME, name);
						intent.putExtra(TrackDbAdapter.DESC, desc);
						
						startActivity(intent);
					}
					
				} catch (Exception err) {
					// TODO: handle exception
					Log.e(TAG, "error: " + err.toString());
					Toast.makeText(NewTrack.this, 
								  getString(R.string.new_fail), 
								  Toast.LENGTH_SHORT)
								  .show();
				}
			}
	};

	private void findViews() {
		// TODO Auto-generated method stub
		Log.d(TAG, "find Views");
		
		field_new_name = (EditText) findViewById(R.id.new_name);
		field_new_desc = (EditText) findViewById(R.id.new_desc);
		new_submit = (Button) findViewById(R.id.new_submit);
		
	}

}
