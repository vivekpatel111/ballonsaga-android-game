package com.rocketapps.balloonsaga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LevelHelper {

  // Database fields
  private SQLiteDatabase database;
  private SQLiteHelper dbHelper;
//  private String[] allColumns = { SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_COMMENT };

  public LevelHelper(Context context) {
    dbHelper = new SQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void addLevel(int levelNo)
  {  
	 
	 String countQuery = "SELECT * FROM "+SQLiteHelper.TABLE_COMMENTS+" WHERE "+ SQLiteHelper.COLUMN_ID+" = "+levelNo+";";
     Cursor cursor = database.rawQuery(countQuery, null);	 
    if(cursor.getCount() >0)
    {
    	
    }
    else{
	  ContentValues c = new ContentValues();
	  c.put(SQLiteHelper.COLUMN_ID,levelNo);
	  c.put(SQLiteHelper.COLUMN_COMMENT,0);
	  database.insert(SQLiteHelper.TABLE_COMMENTS, null,c);
    }
    }
  public int getLevelStatus(int levelNo)
  {
		Cursor cursor = database.query(SQLiteHelper.TABLE_COMMENTS, null,SQLiteHelper.COLUMN_ID+"=?", new String[] { String.valueOf(levelNo) }, null, null, null);
		cursor.moveToFirst();

		if(cursor.getString(1).equals("1"))
			 return 1;
	     else
	    	 return 0;
	 
  }
  public void updateLevel(int levelNo)
  {
	  ContentValues updateValues=new ContentValues();
      updateValues.put(SQLiteHelper.COLUMN_COMMENT,1);
      //update language set langName=newLangName where _id=rowIndex
      database.update(SQLiteHelper.TABLE_COMMENTS, updateValues, SQLiteHelper.COLUMN_ID+" = "+levelNo, null);
    	  
  }
   

}