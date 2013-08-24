package com.musikfest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.musikfest.R;

import android.app.ListActivity;
import android.support.v4.util.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SpillestedInfoActivity extends ListActivity {
	
	ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	private Context mCtx;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spillestedinfo_layout);
		mCtx = this;
		
		
		Intent myIntent= getIntent(); // gets the previously created intent
		
		TextView textView = (TextView)findViewById(R.id.textbox1);
		
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		textView.setText(myIntent.getStringExtra("spillestedId"));
		
		Cursor locationCursor =  dbHelper.rawQueryMe("select Location from Spillesteder s where s.Name = '"+myIntent.getStringExtra("spillestedId")+"';");
		locationCursor.moveToFirst();
		final String Location = locationCursor.getString(0);
		locationCursor.close();
		
		
		
		Cursor cursor =  dbHelper.rawQueryMe("select * from concert c where c.SpillestedId = (select _id from spillesteder s where s.Name = '"+ myIntent.getStringExtra("spillestedId") +"') ORDER BY StartTime;");
		if (cursor != null)
		{
			cursor.moveToFirst();
			
			DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM-hh:mm:ss");
			
			while (!cursor.isAfterLast()) {
				String bandId = cursor.getString(1);
				Cursor bcursor =  dbHelper.rawQueryMe("select * from band b where b._id in ("+ bandId +");");
				bcursor.moveToFirst();
				bandId = "";
				while (!bcursor.isAfterLast()) {
					if (bandId == "")
					{
						bandId = bcursor.getString(2);
					}
					else
					{
						bandId += "|" + bcursor.getString(2);
					} 
					bcursor.moveToNext();
				}
				bcursor.close();
				
				String startTime = cursor.getString(3); // Start
				String endTime = cursor.getString(4); // End
				Date endTimeAsDate = null;
				try {
					endTimeAsDate = (Date) formatter.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				int dayMatch = Calendar.getInstance().getTime().compareTo(endTimeAsDate);
				if (dayMatch <= 0) {
					startTime = startTime.subSequence(8, 10) + "-" + startTime.subSequence(5, 7) + "-" + startTime.subSequence(0, 4) + "-" + startTime.subSequence(11, 16);
					endTime = endTime.subSequence(8, 10) + "-" + endTime.subSequence(5, 7) + "-" + endTime.subSequence(0, 4) + "-" + endTime.subSequence(11, 16);
					listItems.add(bandId + "\r\n Fra: \r\n" + startTime + "\r\n Til: \r\n" + endTime);
				}
				
				cursor.moveToNext();
			}
			cursor.close();
			dbHelper.close();
		}
		
		
		final ListView itemList = (ListView) findViewById(android.R.id.list);
		
		itemList.setOnItemClickListener(new OnItemClickListener()  {
			
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	            // TODO Auto-generated method stub
	        	String selectedFromList = (itemList.getItemAtPosition(arg2).toString());
	        	String lines[] = selectedFromList.split("\\r?\\n");
	        	Intent newIntent;
	        	if(lines[0].contains("|")) {
		        	newIntent = new Intent(mCtx, ChooseBandFragment.class);
		        	newIntent.putExtra("bandIds",lines[0]);
	        	}
	        	else {
	        		newIntent = new Intent(mCtx, BandInfoActivity.class);
	        		newIntent.putExtra("bandId",lines[0]);
	        	}
	        	
	        	startActivity(newIntent);
	        }
		});

		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		setListAdapter(adapter);
		
		Button myBackButton = (Button) findViewById(R.id.back_button);
		myBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button myRuteButton = (Button) findViewById(R.id.rute_button);
		myRuteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(Location+ "?z=20"));
				intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
				startActivity(intent);
			}
		});
	}
}
