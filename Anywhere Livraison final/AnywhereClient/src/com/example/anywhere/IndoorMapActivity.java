package com.example.anywhere;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class IndoorMapActivity extends Activity {

	private ImageView iv;
	private Bitmap bm;
	private Uri mImageUri;
	private Button showButton;
	private boolean b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indoor_map);
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File("/sdcard/Anywhere/departure.jpg");
			mImageUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException e) {
			System.out.println(e.getMessage());
		}
		iv = (ImageView) findViewById(R.id.map);
		showButton = (Button) findViewById(R.id.show_button);
		showButton.setOnClickListener(new ShowListener());
	}

	private class ShowListener implements OnClickListener {
		public void onClick(View v) {
			WebService.decodeFile("/sdcard/Anywhere/departure.jpg", 800, 600);
			b = WebService.getIndoorPlan("departure.jpg");
			if (b) {
				try {
					FileInputStream fis = new FileInputStream(
							"/sdcard/Anywhere/indoorMap.jpg");
					bm = BitmapFactory.decodeStream(fis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,
						bm.getWidth(), bm.getHeight(), matrix, true);
				iv.setImageBitmap(rotatedBitmap);
			} else {
				iv.setImageDrawable(getResources().getDrawable(
						R.drawable.data_not_found));
			}
			iv.setVisibility(View.VISIBLE);
			showButton.setVisibility(View.GONE);
		}
	}
}
