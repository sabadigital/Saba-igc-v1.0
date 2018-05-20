package com.saba.igc.org.models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class SabaDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_PATH = "/data/data/com.saba.igc.org/";
	private static final String DATABASE_NAME = "Saba.db";
	private static final int SCHEMA_VERSION = 1;
	private final Context mContext; 
	public SQLiteDatabase mSQLiteDB;

	public SabaDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		mContext = context; 
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public void createDatabase(){
		if(!databaseExists()){
			this.getReadableDatabase();
			copyDatabaseFromResourse();
		}
	}

	private void copyDatabaseFromResourse() {
		InputStream inStream = null;
		OutputStream outStream = null;
		String dbFilePath = DATABASE_PATH + DATABASE_NAME;
		
		try{
			inStream = mContext.getAssets().open(DATABASE_NAME);
			outStream = new FileOutputStream(dbFilePath);
			byte[] buffer = new byte[1024];
			int length = 0;
			
			while ((length = inStream.read(buffer)) > 0 ){
				outStream.write(buffer, 0, length);
			}
			
			outStream.flush();
			outStream.close();
			inStream.close();
		} catch(IOException e){
			
		}
	}

	private boolean databaseExists() {
		SQLiteDatabase database = null;
		
		try{
			String databasePath = DATABASE_PATH + DATABASE_NAME;
			database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			database.setLocale(Locale.getDefault());
			database.setLockingEnabled(true);
			database.setVersion(1);
		} catch (SQLiteException e) {
			Log.e("SabaDatabaseHelper: ", "database not found.");
		}
		
		if(database != null){
			database.close();
		}
		
		return database != null ? true : false;
	}
}