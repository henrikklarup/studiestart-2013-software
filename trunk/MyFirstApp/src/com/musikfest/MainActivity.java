package com.musikfest;

import java.io.IOException;

import com.musikfest.R;

import android.os.Bundle;
import android.content.Intent;
import android.database.SQLException;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
        myDbHelper = new DataBaseHelper(this);
 
        try {
 
        	myDbHelper.createDataBase();
 
	 	} catch (IOException ioe) {
	 
	 		throw new Error("Unable to create database");
	 
	 	}
	 
	 	try {
	 
	 		myDbHelper.openDataBase();
	 
	 	}catch(SQLException sqle){
	 
	 		throw sqle;
	 
	 	}
		
		TabHost tabHost = getTabHost();
		
		//Program
		TabSpec programSpec = tabHost.newTabSpec("Program");
		programSpec.setIndicator("Program", getResources().getDrawable(R.drawable.icon_program_tab));
		Intent programIntent = new Intent(this, ProgramActivity.class);
		programSpec.setContent(programIntent);
		
		//Spillesteder
		TabSpec spillestederSpec = tabHost.newTabSpec("Spillesteder");
		spillestederSpec.setIndicator("Spillesteder", getResources().getDrawable(R.drawable.icon_spillesteder_tab));
		Intent spillestederIntent = new Intent(this, SpillestederActivity.class);
		spillestederSpec.setContent(spillestederIntent);
		
		//Bands
		TabSpec bandSpec = tabHost.newTabSpec("Orkestre");
		bandSpec.setIndicator("Orkestre", getResources().getDrawable(R.drawable.icon_band_tab));
		Intent bandIntent = new Intent(this, BandActivity.class);
		bandSpec.setContent(bandIntent);
		
		//Info
		TabSpec infoSpec = tabHost.newTabSpec("Information");
		infoSpec.setIndicator("Information", getResources().getDrawable(R.drawable.ic_concert));
		Intent infoIntent = new Intent(this, InfoActivity.class);
		infoSpec.setContent(infoIntent);
		
		tabHost.addTab(programSpec);
		tabHost.addTab(spillestederSpec);
		tabHost.addTab(bandSpec);
		tabHost.addTab(infoSpec);
	}
}
