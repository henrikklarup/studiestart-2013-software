package com.musikfest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String DB_PATH = "data/data/com.musikfest/databases";
	private static String DB_NAME = "MusikfestDB";
	
	private SQLiteDatabase myDataBase;
	
	private final Context myContext;
	
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}
	
	public void createDataBase() throws IOException{
		boolean dbExist = checkDataBase();
		
		if(dbExist) {
			//Nothing needs to happen
		}
		else {
			this.getReadableDatabase();
			
			try {
				copyDataBase();
			} catch (IOException e) {
				// TODO: handle exception
				throw new Error("Error copying database");
			}
		}
	}
	
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			
			
			DataBaseHelper dbHelper = new DataBaseHelper(myContext);
			dbHelper.openDataBase();
			Cursor cursor =  dbHelper.rawQueryMe("select VERSION from DB_INFO;");
			cursor.moveToFirst();
			if (cursor.getInt(0) < 1)
			{
				cursor.close();
				dbHelper.close();
				File f = new File(DB_PATH + "/" + DB_NAME);
				f.delete();
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			}
			
		} catch (SQLiteException e) {
			// TODO: handle exception
		}
		
		if (checkDB != null) {
			checkDB.close();
		}
		
		return checkDB != null ? true : false;
		
	}
	
	 private void copyDataBase() throws IOException{
		 
	    	//Open your local db as the input stream
	    	InputStream myInput = myContext.getAssets().open(DB_NAME);
	 
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH + DB_NAME;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	 
	    }
	 
	    public void openDataBase() throws SQLException{
	 
	    	//Open the database
	        String myPath = DB_PATH + DB_NAME;
	    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    }
	    
	    @Override
		public synchronized void close() {
	 
	    	    if(myDataBase != null)
	    		    myDataBase.close();
	 
	    	    super.close();
	 
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
	 
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
		}
		
		  // Add your public helper methods to access and get content from the database.
	       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	       // to you to create adapters for your views.
		
		
		public Cursor getAllBandNames() {
			return myDataBase.query("band", new String[] {"Name"}, null, null, null, null, null);
		}
		
		public Cursor rawQueryMe(String sql) {
			return myDataBase.rawQuery(sql, null);
		}
		
		
	
	
}