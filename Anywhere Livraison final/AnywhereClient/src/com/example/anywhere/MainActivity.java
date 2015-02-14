package com.example.anywhere;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button nextButton;
	private Button quitButton;
	private RadioButton rb1;
	private RadioButton rb2;
	private Button button1;
	private Button button2;
	private EditText lat;
	private EditText lon;
	private String inOut;

	public void onCreate(Bundle paramBundle) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_main);
		nextButton = (Button) findViewById(R.id.next);
		quitButton = (Button) findViewById(R.id.quit_button);
		rb1 = (RadioButton) findViewById(R.id.radiobutton1);
		rb2 = (RadioButton) findViewById(R.id.radiobutton2);
		nextButton.setOnClickListener(new NextListener());
		quitButton.setOnClickListener(new QuitListener());
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new UploadListener());
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new UploadListener2());
		lat = (EditText) findViewById(R.id.et1);
		lon = (EditText) findViewById(R.id.et2);
		File file = new File("sdcard/Anywhere");
		if (!file.exists())
			file.mkdirs();
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			inOut = bundle.getString("InOut");
		}
	}

	private class NextListener implements OnClickListener {
		public void onClick(View v) {
			if (MainActivity.this.rb1.isChecked()) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("Departure", "-1");
				bundle.putString("InOut", inOut);
				intent.putExtras(bundle);
				intent.setClass(MainActivity.this, DestActivity.class);
				MainActivity.this.startActivity(intent);
			}
			if (MainActivity.this.rb2.isChecked()) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("InOut", inOut);
				intent.putExtras(bundle);
				intent.setClass(MainActivity.this, DepartPhotoActivity.class);
				MainActivity.this.startActivity(intent);
			}
		}
	}

	private class QuitListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.this.startActivity(intent);
			System.exit(0);
		}
	}

	private class UploadListener implements OnClickListener {
		public void onClick(View v) {
			String s1 = lat.getText().toString();
			String s2 = lon.getText().toString();
			if (s1.equals("") && s2.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.need_lat_lon),
						Toast.LENGTH_SHORT).show();
			} else if (s1.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.need_lat),
						Toast.LENGTH_SHORT).show();
			} else if (s2.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.need_lat),
						Toast.LENGTH_SHORT).show();
			} else {
				// String[] sArray1 = s1.split("\\.");
				// String[] sArray2 = s2.split("\\.");
				// int i1 = Integer.parseInt(sArray1[0]);
				// int i2 = Integer.parseInt(sArray1[1]);
				// int i3 = Integer.parseInt(sArray2[0]);
				// int i4 = Integer.parseInt(sArray2[1]);
				// if (((i1 < -180) || (i1 > 180) || (i2 < 10000) || (i2 >
				// 99999))
				// && ((i3 < -180) || (i3 > 180) || (i4 < 10000) || (i4 >
				// 99999))) {
				// Toast.makeText(getApplicationContext(),
				// getResources().getString(R.string.invalid_lat_lon),
				// Toast.LENGTH_SHORT).show();
				// } else if ((i1 < -180) || (i1 > 180) || (i2 < 10000)
				// || (i2 > 99999)) {
				// Toast.makeText(getApplicationContext(),
				// getResources().getString(R.string.invalid_lat),
				// Toast.LENGTH_SHORT).show();
				// } else if ((i3 < -180) || (i3 > 180) || (i4 < 10000)
				// || (i4 > 99999)) {
				// Toast.makeText(getApplicationContext(),
				// getResources().getString(R.string.invalid_lon),
				// Toast.LENGTH_SHORT).show();
				// } else {
				Bundle bundle = new Bundle();
				Intent intent = new Intent();
				bundle.putString("Latitude", s1);
				bundle.putString("Longitude", s2);
				bundle.putString("InOut", inOut);
				intent.putExtras(bundle);
				intent.setClass(MainActivity.this, UploadActivity.class);
				MainActivity.this.startActivity(intent);
				// }
			}
		}
	}

	private class UploadListener2 implements OnClickListener {
		public void onClick(View v) {
			String s1 = lat.getText().toString();
			String s2 = lon.getText().toString();
			if (s1.equals("") && s2.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.need_lat_lon),
						Toast.LENGTH_SHORT).show();
			} else if (s1.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.need_lat),
						Toast.LENGTH_SHORT).show();
			} else if (s2.equals("")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.need_lat),
						Toast.LENGTH_SHORT).show();
			} else {
				// String[] sArray1 = s1.split("\\.");
				// String[] sArray2 = s2.split("\\.");
				// int i1 = Integer.parseInt(sArray1[0]);
				// int i2 = Integer.parseInt(sArray1[1]);
				// int i3 = Integer.parseInt(sArray2[0]);
				// int i4 = Integer.parseInt(sArray2[1]);
				// if (((i1 < -180) || (i1 > 180) || (i2 < 10000) || (i2 >
				// 99999))
				// && ((i3 < -180) || (i3 > 180) || (i4 < 10000) || (i4 >
				// 99999))) {
				// Toast.makeText(getApplicationContext(),
				// getResources().getString(R.string.invalid_lat_lon),
				// Toast.LENGTH_SHORT).show();
				// } else if ((i1 < -180) || (i1 > 180) || (i2 < 10000)
				// || (i2 > 99999)) {
				// Toast.makeText(getApplicationContext(),
				// getResources().getString(R.string.invalid_lat),
				// Toast.LENGTH_SHORT).show();
				// } else if ((i3 < -180) || (i3 > 180) || (i4 < 10000)
				// || (i4 > 99999)) {
				// Toast.makeText(getApplicationContext(),
				// getResources().getString(R.string.invalid_lon),
				// Toast.LENGTH_SHORT).show();
				// } else {
				Bundle bundle = new Bundle();
				Intent intent = new Intent();
				bundle.putString("Latitude", s1);
				bundle.putString("Longitude", s2);
				bundle.putString("InOut", inOut);
				intent.putExtras(bundle);
				intent.setClass(MainActivity.this, Upload2Activity.class);
				MainActivity.this.startActivity(intent);
				// }
			}
		}
	}
}