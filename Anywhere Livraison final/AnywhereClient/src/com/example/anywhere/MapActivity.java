package com.example.anywhere;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Marker.OnMarkerClickListener;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MapActivity extends Activity {

	private MapView map = null;
	private Button btnok;
	private EditText txt;
	private String search = null;
	private TextView textView1;
	private Button hideShowButton;
	private Button confirmButton1;
	private Button confirmButton2;
	private Button calculateButton;
	private double lat = 0.0;
	private double lon = 0.0;
	private GeoPoint startPoint = new GeoPoint(43.61554, 7.07184);
	private GeoPoint endPoint = new GeoPoint(43.6914053, 7.2450862);
	private String option = "shortest";
	private String s1;
	private String s2;
	private Marker startMarker;
	private Marker endMarker;
	private String depCity;
	private double range = 0.1;

	// MyItemizedOverlay myItemizedOverlay = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// necessary in onCreate()
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Bundle bundle = this.getIntent().getExtras();
		s1 = bundle.getString("Departure");
		s2 = bundle.getString("Destination");
		depCity = bundle.getString("DepCity");

		map = (MapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		map.setTileSource(TileSourceFactory.MAPNIK);
		if (!s1.equals("-1") && !s1.equals("-1,-1") && !s2.equals("-1")
				&& !s2.equals("-1,-1")) {
			IMapController mapController = map.getController();
			mapController.setZoom(17);
			String a[] = s1.split(",");
			startPoint = new GeoPoint(Double.valueOf(a[0]),
					Double.valueOf(a[1]));
			String b[] = s2.split(",");
			endPoint = new GeoPoint(Double.valueOf(b[0]), Double.valueOf(b[1]));
			mapController.setCenter(startPoint);
		} else if (!s1.equals("-1") && !s1.equals("-1,-1")) {
			IMapController mapController = map.getController();
			mapController.setZoom(17);
			String a[] = s1.split(",");
			startPoint = new GeoPoint(Double.valueOf(a[0]),
					Double.valueOf(a[1]));
			mapController.setCenter(startPoint);
		} else if (!s2.equals("-1") && !s2.equals("-1,-1")) {
			IMapController mapController = map.getController();
			mapController.setZoom(17);
			String a[] = s2.split(",");
			endPoint = new GeoPoint(Double.valueOf(a[0]), Double.valueOf(a[1]));
			mapController.setCenter(endPoint);
		} else {
			IMapController mapController = map.getController();
			mapController.setZoom(1);
			mapController.setCenter(new GeoPoint(0, 0));

		}
		// add an overlay
		MapOverlay movl = new MapOverlay(this);
		map.getOverlays().add(movl);

		startMarker = new Marker(map);
		startMarker.setPosition(startPoint);
		// startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		map.getOverlays().add(startMarker);

		startMarker.setIcon(getResources().getDrawable(
				R.drawable.marker_departure));
		// startMarker.setTitle("Start point");

		endMarker = new Marker(map);
		if (endPoint.getLatitude() != 43.6914053
				|| endPoint.getLongitude() != 7.2450862) {
			endMarker.setPosition(endPoint);
			// endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
			map.getOverlays().add(endMarker);
			endMarker.setIcon(getResources().getDrawable(
					R.drawable.marker_destination));
			endMarker.setTitle("End point");
		}
		// calculateRoute(startPoint, endPoint);

		map.invalidate();

		btnok = (Button) this.findViewById(R.id.btnok);
		txt = (EditText) this.findViewById(R.id.txt);
		textView1 = (TextView) this.findViewById(R.id.textview1);
		confirmButton1 = (Button) this.findViewById(R.id.confirm_button1);
		confirmButton2 = (Button) this.findViewById(R.id.confirm_button2);
		calculateButton = (Button) this.findViewById(R.id.calculate_button);
		hideShowButton = (Button) this.findViewById(R.id.button4);

		// txt.setText(s1 + s2);
		btnok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				map.invalidate();
				search = txt.getText().toString();
				GeoPoint tmpGeoPoint = new GeoPoint(lat, lon);
				findAPlace(tmpGeoPoint);

			}
		});

		hideShowButton.setOnClickListener(new HideShowListener());
		confirmButton1.setOnClickListener(new ConfirmListener1());
		confirmButton2.setOnClickListener(new ConfirmListener2());
		calculateButton.setOnClickListener(new CalculateListener());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu(0, 1, 1, R.string.option);
		menu.add(0, 2, 2, R.string.dep_reset);
		menu.add(0, 3, 3, R.string.des_reset);
		subMenu1.setHeaderTitle(R.string.option);
		subMenu1.add(0, 4, 0, R.string.shortest);
		subMenu1.add(0, 5, 0, R.string.fastest);
		subMenu1.add(0, 6, 0, R.string.pedestrian);
		subMenu1.add(0, 7, 0, R.string.bicycle);
		subMenu1.add(0, 8, 0, R.string.multimodal);
		return super.onCreateOptionsMenu(menu);
	}

	private class ConfirmListener1 implements OnClickListener {
		public void onClick(View v) {
			MapActivity.this.startPoint = new GeoPoint(lat, lon);
		}
	}

	private class ConfirmListener2 implements OnClickListener {
		public void onClick(View v) {
			MapActivity.this.endPoint = new GeoPoint(lat, lon);
		}
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == 2) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString(
					"Destination",
					String.valueOf(endPoint.getLatitude()) + ","
							+ String.valueOf(endPoint.getLongitude()));
			intent.putExtras(bundle);
			intent.setClass(MapActivity.this, ResetDepActivity.class);
			MapActivity.this.startActivity(intent);
		} else if (item.getItemId() == 3) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString(
					"Departure",
					String.valueOf(startPoint.getLatitude()) + ","
							+ String.valueOf(startPoint.getLongitude()));
			bundle.putString("DepCity", depCity);
			intent.putExtras(bundle);
			intent.setClass(MapActivity.this, DestPhotoGetActivity.class);
			MapActivity.this.startActivity(intent);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 4:
			option = getResources().getString(R.string.shortest);
			break;
		case 5:
			option = getResources().getString(R.string.fastest);
			break;
		case 6:
			option = getResources().getString(R.string.pedestrian);
			break;
		case 7:
			option = getResources().getString(R.string.bicycle);
			break;
		case 8:
			option = getResources().getString(R.string.multimodal);
			break;
		}
		return true;
	}

	private class HideShowListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (hideShowButton.getText() == getResources().getString(
					R.string.hide)) {
				hideShowButton.setText(getResources().getString(R.string.show));
				btnok.setVisibility(View.GONE);
				txt.setVisibility(View.GONE);
				confirmButton1.setVisibility(View.GONE);
				confirmButton2.setVisibility(View.GONE);
				calculateButton.setVisibility(View.GONE);
			} else {
				hideShowButton.setText(getResources().getString(R.string.hide));
				btnok.setVisibility(View.VISIBLE);
				txt.setVisibility(View.VISIBLE);
				confirmButton1.setVisibility(View.VISIBLE);
				confirmButton2.setVisibility(View.VISIBLE);
				calculateButton.setVisibility(View.VISIBLE);
			}
		}
	}

	private class CalculateListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			calculateRoute(startPoint, endPoint);
		}
	}

	public class MapOverlay extends org.osmdroid.views.overlay.Overlay {

		public MapOverlay(Context ctx) {
			super(ctx);
		}

		@Override
		protected void draw(Canvas c, MapView osmv, boolean shadow) {
		}

		public boolean onTouchEvent(MotionEvent e, MapView mapView) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				MapView map = (MapView) mapView;
				Projection proj = map.getProjection();
				GeoPoint loc = (GeoPoint) proj.fromPixels((int) (e.getX()),
						(int) (e.getY()));
				lat = loc.getLatitudeE6() / 1E6;
				lon = loc.getLongitudeE6() / 1E6;
				textView1.setText(+lat + "  " + lon);
				// textView1.setText("sdf");
			}
			return false;
		}
	}

	public void calculateRoute(GeoPoint depart, GeoPoint dest) {

		ArrayList<GeoPoint> wayPoints = new ArrayList<GeoPoint>();
		wayPoints.add(depart);
		wayPoints.add(dest);

		RoadManager roadManager = new MapQuestRoadManager(
				"Fmjtd%7Cluur2g612h%2C7n%3Do5-9az2du");
		// fastest/shortest/pedestrian/bicycle/multimodal
		roadManager.addRequestOption("routeType=" + option);
		Road road = roadManager.getRoad(wayPoints);
		Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
		map.getOverlays().add(roadOverlay);

		Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_node);
		for (int i = 0; i < road.mNodes.size(); i++) {
			RoadNode node = road.mNodes.get(i);
			Marker nodeMarker = new Marker(map);
			nodeMarker.setPosition(node.mLocation);
			nodeMarker.setIcon(nodeIcon);
			nodeMarker.setTitle("Step " + i);
			map.getOverlays().add(nodeMarker);
			// Set the bubble snippet with the instructions
			nodeMarker.setSnippet(node.mInstructions);
			// Set the bubble sub-description with the length and duration of
			// the step
			nodeMarker.setSubDescription(Road.getLengthDurationText(
					node.mLength, node.mDuration));
			// put an icon showing the maneuver at this step
			Drawable icon = getResources().getDrawable(R.drawable.ic_continue);
			nodeMarker.setImage(icon);
		}

	}

	public void findAPlace(GeoPoint geoPoint) {
		NominatimPOIProvider poiProvider = new NominatimPOIProvider();
		ArrayList<POI> pois = poiProvider.getPOICloseTo(geoPoint, search, 100,
				range);
		FolderOverlay poiMarkers = new FolderOverlay(this);
		map.getOverlays().add(poiMarkers);
		Drawable poiIcon1 = getResources().getDrawable(
				android.R.drawable.star_big_on);
		for (POI poi : pois) {
			Marker poiMarker = new Marker(map);
			poiMarker.setTitle(poi.mType);
			poiMarker.setSnippet(poi.mDescription);
			poiMarker.setPosition(poi.mLocation);
			poiMarker.setIcon(poiIcon1);
			poiMarker.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0, MapView arg1) {
					DecimalFormat formater = new DecimalFormat();
					formater.setMaximumFractionDigits(4);
					formater.setGroupingSize(0);
					formater.setRoundingMode(RoundingMode.FLOOR);
					WebService.Download(String.valueOf(formater.format(lat)),
							String.valueOf(formater.format(lon)), depCity);
					arg0.setImage(getImageFromSdCard("Anywhere/AnywhereTemp"));
					arg0.showInfoWindow();
					return true;
				}
			});
			poiMarkers.add(poiMarker);
		}
	}

	@SuppressWarnings("deprecation")
	public Drawable getImageFromSdCard(String imageName) {
		Drawable d = null;
		try {
			String path = Environment.getExternalStorageDirectory().toString();
			Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + imageName
					+ ".jpg");
			d = new BitmapDrawable(bitmap);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return d;
	}
}