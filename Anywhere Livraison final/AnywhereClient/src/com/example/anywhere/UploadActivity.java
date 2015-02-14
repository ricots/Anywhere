package com.example.anywhere;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UploadActivity extends Activity {
	private File sdCardPath;
	private String[] fileName = null;
	private String str;
	private ImageView img;
	private List<HashMap<String, Object>> mData;
	private ListView listView;
	private String city;
	private String lat;
	private String lon;
	private String inOut;
	private String floor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getApplicationContext(), R.string.no_sd_card_info,
					Toast.LENGTH_SHORT).show();
		}
		Bundle bundle = this.getIntent().getExtras();
		lat = bundle.getString("Longitude");
		lon = bundle.getString("Latitude");
		inOut = bundle.getString("InOut");
		if (inOut.equals("in"))
			floor = bundle.getString("Floor");
		sdCardPath = Environment.getExternalStorageDirectory();
		str = sdCardPath.getAbsolutePath() + "/DCIM/Camera";
		city = getResources().getString(R.string.Nice);
		sdCardPath = new File(str);
		fileName = sdCardPath.list();
		mData = getData();
		MyAdapter adapter = new MyAdapter(this);

		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!fileName[arg2].endsWith(".jpg")
						&& !fileName[arg2].endsWith(".png"))
					Toast.makeText(getApplicationContext(), R.string.error1,
							Toast.LENGTH_SHORT).show();
				else {
					/*
					 * String uri = Environment.getExternalStorageDirectory() +
					 * "/DCIM/Camera/" + fileName[arg2]; File imgFile = new
					 * File("sdcard/DCIM/Camera/" + fileName[arg2]);
					 */
					Bitmap bm;
					bm = BitmapFactory.decodeFile("sdcard/DCIM/Camera/"
							+ fileName[arg2]);
					File imgFile = new File("sdcard/Anywhere/" + "upload.jpg");
					try {
						if (imgFile.exists()) {
							imgFile.delete();
						}
						imgFile.createNewFile();
						FileOutputStream out = new FileOutputStream(imgFile);
						bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
					} catch (IOException e) {
						e.printStackTrace();
					}
					WebService ws = new WebService();
					WebService.decodeFile2("/sdcard/Anywhere/upload.jpg", 800,
							600);
					if (inOut.equals("out")) {
						ws.Upload(lat, lon, city);
						Intent intent = new Intent();
						intent.putExtra("inOut", "out");
						intent.setClass(UploadActivity.this, MainActivity.class);
						UploadActivity.this.startActivity(intent);
					} else if (inOut.equals("in")) {
						ws.Upload(lat, lon, "PolytechNiceSophiaIndoor", "O1");
						Intent intent = new Intent();
						intent.putExtra("inOut", "in");
						intent.setClass(UploadActivity.this,
								IndoorActivity.class);
						UploadActivity.this.startActivity(intent);
					}
				}
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu(0, 1, 1, R.string.city);
		subMenu1.setHeaderTitle(R.string.city);
		subMenu1.add(0, 1, 0, R.string.Nice);
		subMenu1.add(0, 2, 0, R.string.Sophia);
		subMenu1.add(0, 3, 0, R.string.PNS);
		return super.onCreateOptionsMenu(menu);
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.photo_list, null);
			TextView fileName = (TextView) convertView
					.findViewById(R.id.file_name);
			fileName.setText(mData.get(position).get("file_name").toString());
			img = (ImageView) convertView.findViewById(R.id.img);
			img.setBackgroundDrawable((Drawable) mData.get(position).get("img"));
			return convertView;
		}
	}

	private List<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;
		int i = 0;
		while (i < fileName.length) {
			map = new HashMap<String, Object>();
			map.put("file_name", fileName[i]);
			File imgFile = new File("sdcard/DCIM/Camera/" + fileName[i]);
			int inSample = 128;
			Options opts = new BitmapFactory.Options();
			opts.inSampleSize = inSample;
			Bitmap myBitmap = BitmapFactory.decodeFile(
					imgFile.getAbsolutePath(), opts);
			Drawable d = new BitmapDrawable(getResources(), myBitmap);
			map.put("img", d);
			list.add(map);
			i++;
		}

		return list;
	}

	public void showInfo(int position) {
		getData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.list));
		System.gc();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
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
}