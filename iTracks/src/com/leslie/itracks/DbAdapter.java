package com.leslie.itracks;

import java.util.jar.Attributes.Name;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	private static final String TAG = "DbAdapper";
	
	//set data file name
	private static final String DATABASE_NAME = "iTracks.db";
	private static final int DATABASE_VERSION = 1;
	public class DatabaseHelper extends SQLiteOpenHelper{
		public DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//SQL language
			String tracks_sql = "CREATE TABLE " + TrackDbAdapter.TABLE_NAME + " ("
					+ TrackDbAdapter.ID + " INTEGER primary key autoincrement, "
					+ TrackDbAdapter.NAME + " text not null, "
					+ TrackDbAdapter.DESC + " text, "
					+ TrackDbAdapter.DIST + " LONG, "
					+ TrackDbAdapter.TRACKEDTIME + " LONG, "
					+ TrackDbAdapter.LOCATE_COUNT + " INTEGER, "
					+ TrackDbAdapter.CREATED + " text, "
					+ TrackDbAdapter.AVGSPEED + " LONG, "
					+ TrackDbAdapter.MAXSPEED + " LONG, "
					+ TrackDbAdapter.UPDATED + " text"
					+ ");";
			Log.d(TAG, tracks_sql);
			
			db.execSQL(tracks_sql);
			
			
			String locats_sql = "CREATE TABLE " + LocateDbAdapter.TABLE_NAME + " ("
					+ LocateDbAdapter.ID + " INTEGER primary key autoincrement, "
					+ LocateDbAdapter.TRACKID + "INEGER not null, "
					+ LocateDbAdapter.LON + " DOUBLE , "
					+ LocateDbAdapter.LAT + " DOUBLE , "
					+ LocateDbAdapter.ALT + " DOUBLE , "
					+ LocateDbAdapter.CREATED + " text "
					+ "); ";
			Log.d(TAG, locats_sql);
			db.execSQL(locats_sql);
			
					
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXIST " + LocateDbAdapter.TABLE_NAME + ";");
			db.execSQL("DROP TABLE IF EXIST " + TrackDbAdapter.TABLE_NAME + ";");
			onCreate(db);
		}
		
		
	}
}
