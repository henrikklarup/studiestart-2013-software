package com.musikfest;

import java.util.ArrayList;

import com.musikfest.R;

import android.app.Activity;
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

public class BandInfoActivity extends Activity {

	ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	Context mCtx;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandinfo_layout);
		
		mCtx = this;
		
		Intent myIntent= getIntent(); // gets the previously created intent
		
		TextView textView = (TextView)findViewById(R.id.textbox1);
		TextView textView2 = (TextView)findViewById(R.id.textbox2);
		
		DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
		dbHelper.openDataBase();
		final String bandName = myIntent.getStringExtra("bandId");
		textView.setText(bandName);
		
		Cursor cursor =  dbHelper.rawQueryMe("select * from band b where b.Name = '"+ bandName  +"';");
		if (cursor != null)
		{
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				String Country = cursor.getString(0);
				String Desc = cursor.getString(3);
				
				textView2.setText("Nationalitet: " + Country + "\r\n\r\n" + Desc);
				cursor.moveToNext();
			}
			cursor.close();
			dbHelper.close();
		}
		
		Button myBackButton = (Button) findViewById(R.id.back_button);
		myBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		Button myGetButton = (Button) findViewById(R.id.get_playlist);
		myGetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	        	Intent newIntent = new Intent(mCtx, BandConcertsActivity.class);
	        	newIntent.putExtra("bandId", bandName);
	        	startActivity(newIntent);
				
			}
		});
	}
}
