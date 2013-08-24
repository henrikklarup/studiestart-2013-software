package com.musikfest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BandConcertsActivity extends ListActivity {

	ArrayList<String> listItems = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandconcerts_layout);
		
		Intent myIntent= getIntent(); // gets the previously created intent
	
		TextView textView = (TextView)findViewById(R.id.textbox1);
		final String bandName = myIntent.getStringExtra("bandId");
		textView.setText(bandName);
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		Cursor getBandId = dbHelper.rawQueryMe("select _id from band b where b.Name = '"+ myIntent.getStringExtra("bandId") +"'");
		getBandId.moveToFirst();
		String band_Id = getBandId.getString(0);
		getBandId.close();
		Cursor getMultiEventIds = dbHelper.rawQueryMe("select * from Concert c;");
		getMultiEventIds.moveToFirst();
		String searchIds = "";
		while(!getMultiEventIds.isAfterLast()){
			String[] splitIds = getMultiEventIds.getString(1).split(",");
			for (int i = 0; i < splitIds.length; i++) {
				if(splitIds[i].contentEquals(band_Id)){
					searchIds = (searchIds == "") ? getMultiEventIds.getString(2) : searchIds + "," + getMultiEventIds.getString(2);
				}
			}
			getMultiEventIds.moveToNext();
		}
		
		getMultiEventIds.close();
		
		//Log.w("myApp", searchIds);
		Cursor findBandsCursor = dbHelper.rawQueryMe("select * from Concert c where c._id in ("+ searchIds +")  ORDER BY StartTime;");
		
		findBandsCursor.moveToFirst();
		if (findBandsCursor != null)
		{			
			DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM-hh:mm:ss");
			
			while (!findBandsCursor.isAfterLast()) {
				
				String spillested = findBandsCursor.getString(0);
				//String bandId = findBandsCursor.getString(1);
				String startTime = findBandsCursor.getString(3);
				String endTime = findBandsCursor.getString(4);
				
				Date endTimeAsDate = null;
				try {
					endTimeAsDate = (Date) formatter.parse(endTime);
				} catch (ParseException e) {
					// TODO: handle exception
				}
				
				int dayMatch = Calendar.getInstance().getTime().compareTo(endTimeAsDate);
				if (dayMatch <= 0) {
					//Cursor findBand = dbHelper.rawQueryMe("select Name from Band where _id = '"+ band_Id +"';");
					Cursor findSpillested = dbHelper.rawQueryMe("select Name from Spillesteder where _id = '"+ spillested +"';");
					//findBand.moveToFirst();
					findSpillested.moveToFirst();
					//band_Id = findBand.getString(0);
					spillested = findSpillested.getString(0);
					//findBand.close();
					findSpillested.close();
					startTime = startTime.subSequence(8, 10) + "-" + startTime.subSequence(5, 7) + "-" + startTime.subSequence(0, 4) + "-" + startTime.subSequence(11, 16);
					endTime = endTime.subSequence(8, 10) + "-" + endTime.subSequence(5, 7) + "-" + endTime.subSequence(0, 4) + "-" + endTime.subSequence(11, 16);
					listItems.add(spillested + "\r\n Fra: " + startTime + "\r\n Til: " + endTime);
				}
				
				findBandsCursor.moveToNext();
			}
		}
		
		findBandsCursor.close();
		dbHelper.close();
		
		Button myBackButton = (Button) findViewById(R.id.back_button);
		myBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});

		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		setListAdapter(adapter);
	
	}
	
}
