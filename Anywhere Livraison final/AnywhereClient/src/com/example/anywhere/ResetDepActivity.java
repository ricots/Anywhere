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

public class ResetDepActivity extends Activity {

	private Button confirmButton;
	private String departure;
	private String desString;
	private String city;
	private Uri mImageUri;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_dep);
		confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new ConfirmListener());
		city = getResources().getString(R.string.Nice);
		rb1 = (RadioButton) findViewById(R.id.radiobutton1);
		rb2 = (RadioButton) findViewById(R.id.radiobutton2);
		rb3 = (RadioButton) findViewById(R.id.radiobutton3);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			desString = bundle.getString("Destination");
		}
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File("/sdcard/Anywhere/departure.jpg");
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

	class ConfirmListener implements OnClickListener {
		public void onClick(View v) {
			if (rb1.isChecked())
				city = getResources().getString(R.string.Nice);
			else if (rb2.isChecked())
				city = getResources().getString(R.string.Sophia);
			else if (rb3.isChecked())
				city = getResources().getString(R.string.PNS);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			WebService ws = new WebService();
			ws.decodeFile("/sdcard/Anywhere/departure.jpg", 800, 600);
			String s = ws.getCoordinate("departure.jpg", city);
			departure = s;
			bundle.putString("Departure", departure);
			bundle.putString("Destination", desString);
			intent.putExtras(bundle);

			intent.setClass(ResetDepActivity.this, MapActivity.class);
			ResetDepActivity.this.startActivity(intent);
		}
	}

}
