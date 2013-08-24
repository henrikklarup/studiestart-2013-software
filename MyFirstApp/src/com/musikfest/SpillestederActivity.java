package com.musikfest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.musikfest.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SpillestederActivity extends ListActivity {
	
	ArrayList<String> listItems = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	private Context mCtx;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spillesteder_layout);
		mCtx = this;
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		Cursor cursor =  dbHelper.rawQueryMe("select Name from Spillesteder;");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			listItems.add(cursor.getString(0));
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
	        	Intent newIntent = new Intent(mCtx, SpillestedInfoActivity.class);
	        	String selectedFromList = (itemList.getItemAtPosition(arg2).toString());
	        	newIntent.putExtra("spillestedId", selectedFromList);
	        	startActivity(newIntent);

	        }
		});

		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		setListAdapter(adapter);
	}
}
