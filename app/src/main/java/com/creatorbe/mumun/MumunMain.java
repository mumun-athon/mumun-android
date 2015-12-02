package com.creatorbe.mumun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MumunMain extends Activity {

	Button searchBtn = null;
	Intent locatorService = null;
	AlertDialog alertDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		searchBtn = (Button) findViewById(R.id.btnStartProgress);

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!startService()) {
					CreateAlert("Error!", "Piye mun..");
				} else {
					//Toast.makeText(MumunMain.this, " ",
					//		Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public boolean stopService() {
		if (this.locatorService != null) {
			this.locatorService = null;
		}
		return true;
	}

	public boolean startService() {
		try {
			FetchCordinates fetchCordinates = new FetchCordinates();
			fetchCordinates.execute();
			return true;
		} catch (Exception error) {
			return false;
		}

	}

	public AlertDialog CreateAlert(String title, String message) {
		AlertDialog alert = new AlertDialog.Builder(this).create();

		alert.setTitle(title);

		alert.setMessage(message);

		return alert;

	}

	public class FetchCordinates extends AsyncTask<String, Integer, String> {
		ProgressDialog progDailog = null;

		public double lati = 0.0;
		public double longi = 0.0;

		public LocationManager mLocationManager;
		public VeggsterLocationListener mVeggsterLocationListener;

		@Override
		protected void onPreExecute() {
			mVeggsterLocationListener = new VeggsterLocationListener();
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0,
					mVeggsterLocationListener);

			progDailog = new ProgressDialog(MumunMain.this);
			progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					FetchCordinates.this.cancel(true);
				}
			});
			progDailog.setMessage("Cari Cegatan...");
			progDailog.setIndeterminate(true);
			progDailog.setCancelable(true);
			progDailog.show();

		}

		@Override
		protected void onCancelled(){
			System.out.println("Ga jadi");
			progDailog.dismiss();
			mLocationManager.removeUpdates(mVeggsterLocationListener);
		}

		@Override
		protected void onPostExecute(String result) {
			progDailog.dismiss();

			Toast.makeText(MumunMain.this,
					"return= lat:" + lati + " long:" + longi,
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			while (this.lati == 0.0) {

			}
			return null;
		}

		public class VeggsterLocationListener implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {

				int lat = (int) location.getLatitude();
				int log = (int) location.getLongitude();
				int acc = (int) (location.getAccuracy());

				String info = location.getProvider();
				try {
					lati = location.getLatitude();
					longi = location.getLongitude();

				} catch (Exception e) {
					// progDailog.dismiss();
					// Toast.makeText(getApplicationContext(),"Lokasi ga bisa diakses boss"
					// , Toast.LENGTH_LONG).show();
				}

			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.i("OnProviderDisabled", "OnProviderDisabled");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.i("onProviderEnabled", "onProviderEnabled");
			}

			@Override
			public void onStatusChanged(String provider, int status,
										Bundle extras) {
				Log.i("onStatusChanged", "onStatusChanged");

			}

		}

	}

}