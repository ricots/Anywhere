package com.example.anywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class DestActivity extends Activity {
	private Button nextButton;
	private Button quitButton;
	private RadioButton rb3;
	private RadioButton rb4;
	private String depString;
	private String city;
	private String inOut;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_dest);
		nextButton = (Button) findViewById(R.id.next);
		quitButton = (Button) findViewById(R.id.quit_button);
		rb3 = (RadioButton) findViewById(R.id.radiobutton1);
		rb4 = (RadioButton) findViewById(R.id.radiobutton2);
		nextButton.setOnClickListener(new ListListener());
		quitButton.setOnClickListener(new QuitListener());
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			city = bundle.getString("DepCity");
			depString = bundle.getString("Departure");
			inOut = bundle.getString("InOut");
		}

	}

	class ListListener implements OnClickListener {
		public void onClick(View v) {
			if (DestActivity.this.rb3.isChecked()) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				if (depString != null) {
					bundle.putString("DepCity", city);
					bundle.putString("Departure", depString);
					bundle.putString("Destination", "-1");
					intent.putExtras(bundle);
				}
				if (inOut.equals("out")) {
					intent.setClass(DestActivity.this, MapActivity.class);
					DestActivity.this.startActivity(intent);
				} else {
					return;
				}
			}
			if (DestActivity.this.rb4.isChecked()) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				if (depString != null) {
					bundle.putString("DepCity", city);
					bundle.putString("Departure", depString);
					intent.putExtras(bundle);
				}
				if (inOut.equals("out")) {
					intent.setClass(DestActivity.this,
							DestPhotoGetActivity.class);
					DestActivity.this.startActivity(intent);
				} else {
					return;
				}
			}
		}
	}

	class QuitListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			DestActivity.this.startActivity(intent);
			System.exit(0);
		}
	}
}