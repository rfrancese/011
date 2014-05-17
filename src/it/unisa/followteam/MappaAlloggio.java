package it.unisa.followteam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.unisa.followteam.database.HTTPPoster;
import it.unisa.followteam.database.MyDatabase;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Camera;
import android.hardware.GeomagneticField;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MappaAlloggio extends Fragment {

	private SupportMapFragment fragment;
	private GoogleMap map;
	private final String API_KEY="AIzaSyBWfNyuurLqKhv9L54F8GDDo5JBps2dZfk";

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
			
			final String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                    + "location="+latStadio+","+longStadio+"&"
                    + "radius=1000&"
                    + "types=lodging&"
                    + "sensor=false&"
                    + "key="+API_KEY;
   
			
			GooglePlaces places = new GooglePlaces();
			String res="";
			try {
				 res = places.execute(url).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray alloggi=null;
			ArrayList<Alloggio> listaAlloggi=new ArrayList<Alloggio>();
			try {
				 JSONObject jsonAlloggi = new JSONObject(res);
				  alloggi = jsonAlloggi.getJSONArray("results");
				  for(int i=0; i < alloggi.length(); i++){
					  
					  JSONObject alloggio = alloggi.getJSONObject(i);
					 
					  JSONObject geometry = alloggio.getJSONObject("geometry");
					  JSONObject location = geometry.getJSONObject("location");
					  
					  String lat = location.getString("lat");
					  String longi = location.getString("lng");
					  
					  String nome = alloggio.getString("name");
					  String indirizzo = alloggio.getString("vicinity");
					  listaAlloggi.add(new Alloggio(Double.parseDouble(lat),Double.parseDouble(longi),nome,indirizzo));
				  }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int iconAlloggi = getResources().getIdentifier("alloggi",
					"drawable", getActivity().getPackageName());
			BitmapDescriptor iconA = BitmapDescriptorFactory
					.fromResource(iconAlloggi);
			
			
			for (Alloggio a : listaAlloggi)
				map.addMarker(new MarkerOptions().position(
						new LatLng(a.getLatitudine(), a.getLongitudine()))
						.icon(iconA));
		}
	}
	
	public class GooglePlaces extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			String url = params[0];
			String temp="";
			HttpResponse re;
			try {
				re = HTTPPoster.doPost(url, null);
				 temp = EntityUtils.toString(re.getEntity());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return temp;
		}

	}
	
}
