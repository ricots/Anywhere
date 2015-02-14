package com.example.anywhere;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Upload2Activity extends Activity {

	private Button confirmButton;
	private String lat;
	private String lon;
	private String city;
	private Uri mImageUri;
	private RadioGroup rg1;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	private String inOut;
	private String floor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		lat = bundle.getString("Latitude");
		lon = bundle.getString("Longitude");
		inOut = bundle.getString("InOut");
		setContentView(R.layout.activity_upload2);
		confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new ConfirmListener());
		city = getResources().getString(R.string.Nice);
		rb1 = (RadioButton) findViewById(R.id.radiobutton1);
		rb2 = (RadioButton) findViewById(R.id.radiobutton2);
		rb3 = (RadioButton) findViewById(R.id.radiobutton3);
		rg1 = (RadioGroup) findViewById(R.id.radiogroup1);
		if (inOut.equals("in")) {
			floor = bundle.getString("Floor");
			rb1.setText(getResources().getString(R.string.t1));
			rb2.setText(getResources().getString(R.string.t2));
			rb3.setText(getResources().getString(R.string.t3));
			rb1.setFocusable(true);
		}
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File("/sdcard/Anywhere/upload.jpg");
			mImageUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu(0, 1, 1, R.string.city);
		subMenu1.setHeaderTitle(R.string.city);
		subMenu1.add(0, 1, 0, R.string.Nice);
		subMenu1.add(0, 2, 0, R.string.Sophia);
		subMenu1.add(0, 3, 0, R.string.PNS);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			city = getResources().getString(R.string.Nice);
			break;
		case 2:
			city = getResources().getString(R.string.Sophia);
			break;
		case 3:
			city = getResources().getString(R.string.PNS);
			break;
		}
		return true;
	}

	protected class aListener implements OnClickListener {
		public void onClick(View v) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (requestCode != 0) {
				return;
			}
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private class ConfirmListener implements OnClickListener {
		public void onClick(View v) {
			if (rb1.isChecked())
				city = getResources().getString(R.string.Nice);
			else if (rb2.isChecked())
				city = getResources().getString(R.string.Sophia);
			else if (rb3.isChecked())
				city = getResources().getString(R.string.PNS);
			WebService ws = new WebService();
			ws.decodeFile2("/sdcard/Anywhere/upload.jpg", 800, 600);
			if (inOut.equals("out")) {
				ws.Upload(lat, lon, city);
				Intent intent = new Intent();
				intent.putExtra("inOut", "out");
				intent.setClass(Upload2Activity.this, MainActivity.class);
				Upload2Activity.this.startActivity(intent);
			} else if (inOut.equals("in")) {
				ws.Upload(lat, lon, "PolytechNiceSophiaIndoor", "O1");
				Intent intent = new Intent();
				intent.putExtra("inOut", "in");
				intent.setClass(Upload2Activity.this, IndoorActivity.class);
				Upload2Activity.this.startActivity(intent);
			}
		}
	}

}
