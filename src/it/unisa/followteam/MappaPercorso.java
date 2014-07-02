package it.unisa.followteam;

import java.util.ArrayList;
import java.util.List;

import it.unisa.followteam.database.MyDatabase;
import it.unisa.followteam.support.GPSTracker;
import it.unisa.followteam.support.HttpConnection;
import it.unisa.followteam.support.PathJSONParser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;

import org.json.JSONObject;

public class MappaPercorso extends Fragment {

	private SupportMapFragment fragment;
	private GoogleMap map;
	private GPSTracker gps;
	private double myLat;
	private double myLong;
	private Marker puntatore;
	private View rootView;
	private String url = "";
	private LatLng coordinateStadio;
	private double latitudine;
	private double longitudine;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.mappapercorso, container, false);

		getActivity().setTitle("Mappa Percorso");

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm
				.findFragmentById(R.id.mappa_percorso);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.mappa_percorso, fragment)
					.commit();
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		Bundle args = getArguments();
		map = fragment.getMap();

		 latitudine = Double.parseDouble(args
				.getString(MyDatabase.StadioMetaData.STADIO_LAT));
		longitudine = Double.parseDouble(args
				.getString(MyDatabase.StadioMetaData.STADIO_LONG));
		
		coordinateStadio = new LatLng(latitudine, longitudine);

		gps = new GPSTracker(getActivity().getApplicationContext());

		// se non c'è connessione e il gps è disabilitato
		if (!gps.canGetLocation()) {
			showAlertOpzioniGPS();
		}else if (gps.getLocation() != null) { // se il segnale del gps è
												// stabile

			myLat = gps.getLatitude();
			myLong = gps.getLongitude();
			LatLng miaPosizione = new LatLng(myLat, myLong);
			map.setMyLocationEnabled(true);
			final CameraPosition MYPOSIZIONE = new CameraPosition.Builder()
					.target(miaPosizione).zoom(5).bearing(0).tilt(25).build();

			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(MYPOSIZIONE));
			
			String urlPolyline = getMapsApiDirectionsUrl();
			ReadTask downloadTask = new ReadTask();
			downloadTask.execute(urlPolyline);

		} else { // gps instabile
			Toast.makeText(
					rootView.getContext(),
					"Il segnale gps non è stabile\nConsigliamo di modificare le impostazioni",
					Toast.LENGTH_LONG).show();
			zoomSuStadio();

		}

		int sqCasa = args.getInt(DettagliPartita.ICON_SQUADRA_CASA);
		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(sqCasa);

		puntatore = map.addMarker(new MarkerOptions()
				.position(coordinateStadio).title(
						"Clicca sul logo per tracciare il percorso"));
		puntatore.setIcon(icon);
		puntatore.showInfoWindow();

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			// traccia il percorso dalla propria posizione allo stadio
			// tramite maps
			@Override
			public boolean onMarkerClick(Marker marker) {
				url = "http://maps.google.com/maps?f=d&daddr=" + latitudine
						+ "," + longitudine + "&mode=driving";
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(url));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);
				return true;
			}
		});

	}

	private String getMapsApiDirectionsUrl() {
		String waypoints = "waypoints=optimize:true|" + myLat
				+ "," + myLong + "|" + "|"
				+ latitudine + "," + longitudine;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

	public void zoomSuStadio() {
		final CameraPosition MYSTADIO = new CameraPosition.Builder()
				.target(coordinateStadio).zoom(5).bearing(0).tilt(25).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(MYSTADIO));
	}

	public void showAlertOpzioniGPS() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				rootView.getContext());

		alertDialog.setTitle("Opzioni GPS");

		alertDialog.setMessage("Il Gps è disabilitato. Vuoi attivarlo?");

		alertDialog.setPositiveButton("Si",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						rootView.getContext().startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						zoomSuStadio();
					}
				});

		alertDialog.show();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private class ReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}

		private class ParserTask extends
				AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

			@Override
			protected List<List<HashMap<String, String>>> doInBackground(
					String... jsonData) {

				JSONObject jObject;
				List<List<HashMap<String, String>>> routes = null;

				try {
					jObject = new JSONObject(jsonData[0]);
					PathJSONParser parser = new PathJSONParser();
					routes = parser.parse(jObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return routes;
			}

			@Override
			protected void onPostExecute(
					List<List<HashMap<String, String>>> routes) {
				ArrayList<LatLng> points = null;
				PolylineOptions polyLineOptions = null;

				// traversing through routes
				for (int i = 0; i < routes.size(); i++) {
					points = new ArrayList<LatLng>();
					polyLineOptions = new PolylineOptions();
					List<HashMap<String, String>> path = routes.get(i);

					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);

						points.add(position);
					}

					polyLineOptions.addAll(points);
					polyLineOptions.width(2);
					polyLineOptions.color(Color.BLUE);
				}

				map.addPolyline(polyLineOptions);
			}
		}
	}
}
