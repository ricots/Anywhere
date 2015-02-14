package com.example.anywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class IndoorActivity extends Activity {
	private Spinner floor;
	private EditText lat;
	private EditText lon;
	private Button nextButton;
	private Button button1;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indoor);
		floor = (Spinner) findViewById(R.id.spinner1);
		lat = (EditText) findViewById(R.id.et1);
		lon = (EditText) findViewById(R.id.et2);
		nextButton = (Button) findViewById(R.id.next);
		nextButton.setOnClickListener(new NextListener());
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new UploadListener());
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new UploadListener2());
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.floor_array,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		floor.setAdapter(adapter);
	}

	private class NextListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(IndoorActivity.this, IndoorMapActivity.class);
			IndoorActivity.this.startActivity(intent);
		}
	}

	private class UploadListener implements OnClickListener {
		public void onClick(View v) {
			String s1 = lat.getText().toString();
			String s2 = lon.getText().toString();
			String s3 = floor.getSelectedItem().toString();
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
				bundle.putString("InOut", "in");
				bundle.putString("Floor", s3);
				intent.putExtras(bundle);
				intent.setClass(IndoorActivity.this, UploadActivity.class);
				IndoorActivity.this.startActivity(intent);
				// }
			}
		}
	}

	private class UploadListener2 implements OnClickListener {
		public void onClick(View v) {
			String s1 = lat.getText().toString();
			String s2 = lon.getText().toString();
			String s3 = floor.getSelectedItem().toString();
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
				bundle.putString("InOut", "in");
				bundle.putString("Floor", s3);
				intent.putExtras(bundle);
				intent.setClass(IndoorActivity.this, Upload2Activity.class);
				IndoorActivity.this.startActivity(intent);
				// }
			}
		}
	}
}
