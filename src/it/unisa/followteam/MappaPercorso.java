package it.unisa.followteam;

import it.unisa.followteam.database.MyDatabase;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MappaPercorso extends Fragment {

	private SupportMapFragment fragment;
	private GoogleMap map;
	private GPSTracker gps;
	private double myLat;
	private double myLong;
	private Marker puntatore;
	private View rootView;
	String uriIniziale = "";
	private String url = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.mappapercorso, container, false);
		getActivity().getActionBar().setTitle("Mappa Percorso");

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
		if (map == null) {

			Bundle args = getArguments();
			map = fragment.getMap();

			final double latitudine = Double.parseDouble(args
					.getString(MyDatabase.StadioMetaData.STADIO_LAT));
			final double longitudine = Double.parseDouble(args
					.getString(MyDatabase.StadioMetaData.STADIO_LONG));
			LatLng coordinateStadio = new LatLng(latitudine, longitudine);
			gps = new GPSTracker(getActivity().getApplicationContext());

			myLat = gps.getLatitude();
			myLong = gps.getLongitude();
			map.addPolyline(new PolylineOptions().geodesic(true)
					.add(new LatLng(myLat, myLong), coordinateStadio).width(5)
					.color(Color.RED));

			int sqCasa = args.getInt(DettagliPartita.ICON_SQUADRA_CASA);
			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromResource(sqCasa);

			map.setMyLocationEnabled(true);

			puntatore = map.addMarker(new MarkerOptions()
					.position(coordinateStadio));
			puntatore.setIcon(icon);

			map.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					url = "http://maps.google.com/maps?f=d&daddr=" + latitudine
							+ "," + longitudine + "&mode=driving";
					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(url));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);
					return true;
				}
			});
		}
	}
}
