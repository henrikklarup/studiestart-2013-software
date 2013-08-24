package com.musikfest;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseBandFragment extends ListActivity {
	
	ArrayList<String> listItems = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	private Context mCtx;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandchoose_layout);
		mCtx = this;
		
		Intent myIntent = getIntent();
    	String bands[] = myIntent.getStringExtra("bandIds").split("\\|");
		
		for (int i = 0; i < bands.length; i++) {
			listItems.add(bands[i]);
		}
		
		final ListView itemList = (ListView) findViewById(android.R.id.list);
		itemList.setOnItemClickListener(new OnItemClickListener()  {
			
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	            // TODO Auto-generated method stub
	        	Intent newIntent = new Intent(mCtx, BandInfoActivity.class);
	        	String selectedFromList = (itemList.getItemAtPosition(arg2).toString());
	        	newIntent.putExtra("bandId", selectedFromList);
	        	startActivity(newIntent);
	        }
		});
		
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
