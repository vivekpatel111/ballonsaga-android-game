package com.rocketapps.balloonsaga;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
  	  public static final String TABLE_COMMENTS = "levels";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_COMMENT = "status";

	  private static final String DATABASE_NAME = "commments.db";
	  private static final int DATABASE_VERSION = 1;
      
	  private static final String DATABASE_CREATE = "create table "
		      + TABLE_COMMENTS + "(" + COLUMN_ID
		      + " integer primary key, " + COLUMN_COMMENT
		      + " integers);";
	  
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);
		
		ContentValues c = new ContentValues();
		 c.put(SQLiteHelper.COLUMN_ID,1);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,2);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,3);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,4);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,5);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,6);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,7);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,8);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,9);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
		 c.put(SQLiteHelper.COLUMN_ID,10);
		 c.put(SQLiteHelper.COLUMN_COMMENT,0);
		 database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 Log.w(SQLiteHelper.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
			    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
			    onCreate(db);
	}

}
