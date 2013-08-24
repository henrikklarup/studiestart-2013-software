package com.musikfest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.musikfest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class ProgramActivity extends ListActivity {

	ArrayList<String> listItems = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	private Context mCtx;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_layout);
	
		mCtx = this;
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		Cursor findBandsCursor = dbHelper.rawQueryMe("select * from Concert ORDER BY StartTime;");
		
		if (findBandsCursor != null)
		{
			findBandsCursor.moveToFirst();
			
			DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM-hh:mm:ss");
			
			while (!findBandsCursor.isAfterLast()) {
				
				String spillested = findBandsCursor.getString(0);
				String bandId = findBandsCursor.getString(1);
				String startTime = findBandsCursor.getString(3);
				String endTime = findBandsCursor.getString(4);
				
				Date endTimeAsDate = null;
				try {
					endTimeAsDate = (Date) formatter.parse(endTime);
				} catch (ParseException e) {
					// TODO: handle exception
				}
				
				int dayMatch = Calendar.getInstance().getTime().compareTo(endTimeAsDate);
				Toast.makeText(mCtx, dayMatch, 200).show();
				if (dayMatch <= 0 && Calendar.getInstance().getTime().before(endTimeAsDate)) {
					
					Cursor findBand = dbHelper.rawQueryMe("select Name from Band where _id in ("+ bandId +");");
					Cursor findSpillested = dbHelper.rawQueryMe("select Name from Spillesteder where _id = '"+ spillested +"';");
					findBand.moveToFirst();
					bandId = "";
					while (!findBand.isAfterLast()) {
						if (bandId == "")
						{
							bandId = findBand.getString(0);
						}
						else
						{
							bandId += "|" + findBand.getString(0);
						} 
						findBand.moveToNext();
					}
					findSpillested.moveToFirst();
					spillested = findSpillested.getString(0);
					findBand.close();
					findSpillested.close();
					
					startTime = startTime.subSequence(8, 10) + "-" + startTime.subSequence(5, 7) + "-" + startTime.subSequence(0, 4) + "-" + startTime.subSequence(11, 16);
					endTime = endTime.subSequence(8, 10) + "-" + endTime.subSequence(5, 7) + "-" + endTime.subSequence(0, 4) + "-" + endTime.subSequence(11, 16);
					
					listItems.add(spillested+ "\r\n" + bandId + "\r\n Fra: " + startTime + "\r\n Til: " + endTime);
				}
				
				findBandsCursor.moveToNext();
			}
		}
		
		findBandsCursor.close();
		dbHelper.close();

		
		
		
		final ListView itemList = (ListView) findViewById(android.R.id.list);
		itemList.setOnItemClickListener(new OnItemClickListener()  {
			
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	            // TODO Auto-generated method stub
	        	
	        	String selectedFromList = (itemList.getItemAtPosition(arg2).toString());
	        	String lines[] = selectedFromList.split("\\r?\\n");
	        	Intent newIntent;
	        	if(lines[1].contains("|")) {
		        	newIntent = new Intent(mCtx, ChooseBandFragment.class);
		        	newIntent.putExtra("bandIds",lines[1]);
	        	}
	        	else {
	        		newIntent = new Intent(mCtx, BandInfoActivity.class);
	        		newIntent.putExtra("bandId",lines[1]);
	        	}
	        	
	        	startActivity(newIntent);
	        }
		});

		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		setListAdapter(adapter);
	
	}
}
