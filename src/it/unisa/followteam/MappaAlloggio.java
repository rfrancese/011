package it.unisa.followteam;

import it.unisa.followteam.database.MyDatabase;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MappaAlloggio extends Fragment {

	private SupportMapFragment fragment;
	private GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mappaalloggio, container,
				false);
		getActivity().getActionBar().setTitle("Mappa Percorso");
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm
				.findFragmentById(R.id.mappa_alloggio);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.mappa_alloggio, fragment)
					.commit();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (map == null) {
			//prendo il riferimento della mappa
			map = fragment.getMap();
			//prendo gli argomenti passati dalla classe DettagliPartita
			Bundle args = getArguments();
			//prendo lat e long stadio
			double latStadio = Double.parseDouble(args
					.getString(MyDatabase.StadioMetaData.STADIO_LAT));
			double longStadio = Double.parseDouble(args
					.getString(MyDatabase.StadioMetaData.STADIO_LONG));
			//creo cordinata
			LatLng coordinateStadio = new LatLng(latStadio, longStadio);
			//inserisco il marker dello stadio
			Marker markerStadio = map.addMarker(new MarkerOptions()
					.position(coordinateStadio));
			//inserisco icon al marker
			int sqCasa = args.getInt(DettagliPartita.ICON_SQUADRA_CASA);
			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromResource(sqCasa);
			markerStadio.setIcon(icon);
			

		}
	}
}
