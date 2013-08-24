package com.musikfest;

import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		
		TextView textView = (TextView)findViewById(R.id.textbox1);
		TextView textView2 = (TextView)findViewById(R.id.textbox2);
		TextView textView3 = (TextView)findViewById(R.id.textbox3);
		TextView textView4 = (TextView)findViewById(R.id.textbox4);
		
		textView.setText("Information");
		
		String InfoText = "Arrangør: \n"+
							"Musikkorps Sæby \n" +
							"Skolegade 6 \n" +
							"9300  Sæby \n" +
							"\n" + 
							"Kontaktoplysninger: \n" +
							"Formand Inge Nørgaard Rasmussen \n" +
							"E-mail:";
		textView3.setText("engbæk@email.dk");
		textView4.setText("Tlf: \n +4598469295");
		textView2.setText(InfoText);
		textView3.setAutoLinkMask(0);
		
	    // Detect US postal ZIP codes and link to a lookup service
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
		          "[^\\s].*\\@.*\\..*"
				
		      );
	    String scheme = "";
	    Linkify.addLinks(textView3, EMAIL_ADDRESS_PATTERN, scheme);
	    
		Linkify.addLinks(textView4, Linkify.PHONE_NUMBERS);
	}

}
