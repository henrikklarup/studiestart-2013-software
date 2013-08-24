package com.musikfest;

import java.util.ArrayList;

import com.musikfest.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BandActivity extends ListActivity {

	ArrayList<String> listItems = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	private Context mCtx;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.band_layout);
		mCtx = this;
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		Cursor cursor =  dbHelper.rawQueryMe("select Name,Country from Band;");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (!cursor.getString(0).contentEquals("Alle")) {
				listItems.add(cursor.getString(1)+"|"+cursor.getString(0));
			}
			cursor.moveToNext();
		}
		cursor.close();
		dbHelper.close();
		
		final ListView itemList = (ListView) findViewById(android.R.id.list);
		itemList.setOnItemClickListener(new OnItemClickListener()  {
			
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	            // TODO Auto-generated method stub
	        	Intent newIntent = new Intent(mCtx, BandInfoActivity.class);
	        	String selectedFromList = (itemList.getItemAtPosition(arg2).toString());
	        	String lines[] = selectedFromList.split("\\r?\\n");
	        	newIntent.putExtra("bandId", lines[0].split("\\|")[1]);
	        	startActivity(newIntent);
	        }
		});

		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		setListAdapter(adapter);
	}
}
