package it.unisa.followteam;

import it.unisa.followteam.database.MyDatabase;
import it.unisa.followteam.support.Connessione;
import it.unisa.followteam.support.GPSTracker;

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
			//controllo connessione 
			//viene inserito sempre prima di ogni chiamata a SendDataToServer
			Connessione conn= new Connessione(rootView.getContext());
			//se la connessione non è presente fa il return
			//e non effettua l'execute del SendDataToServer
			if(!conn.controllaConnessione()){
				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(rootView.getContext());
				myAlertDialog.setTitle("Attenzione");
				myAlertDialog.setMessage("Connessione assente! Riprova");
				myAlertDialog.setNeutralButton("Ok", null);
				myAlertDialog.show();
				return;
			}

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
			LatLng myPosizione = new LatLng(myLat, myLong);
			map.addPolyline(new PolylineOptions()
					.add(myPosizione, coordinateStadio).width(5)
					.color(Color.RED));

			int sqCasa = args.getInt(DettagliPartita.ICON_SQUADRA_CASA);
			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromResource(sqCasa);
			
			map.setMyLocationEnabled(true);

			puntatore = map.addMarker(new MarkerOptions().position(
					coordinateStadio).title(
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
					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(url));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);
					return true;
				}
			});
			
			final CameraPosition MYPOSIZIONE = new CameraPosition.Builder()
			.target(myPosizione).zoom(5).bearing(0).tilt(25)
			.build();
			
			
			map.animateCamera(CameraUpdateFactory.newCameraPosition(MYPOSIZIONE));

			
		}
	}
}
