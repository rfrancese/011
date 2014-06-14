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
import it.unisa.followteam.support.Alloggio;
import it.unisa.followteam.support.Connessione;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.GeomagneticField;
import android.net.Uri;
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
	private final String API_KEY = "AIzaSyBWfNyuurLqKhv9L54F8GDDo5JBps2dZfk";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mappaalloggio, container,
				false);
		getActivity().getActionBar().setTitle("Mappa Alloggio");
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
		//controllo connessione 
				//viene inserito sempre prima di ogni chiamata a SendDataToServer
				Connessione conn= new Connessione(getView().getContext());
				//se la connessione non è presente fa il return
				//e non effettua l'execute del SendDataToServer
				if(!conn.controllaConnessione()){
					AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getView().getContext(),R.style.MyDialogBox);
					myAlertDialog.setTitle("Attenzione");
					
					myAlertDialog.setMessage("Connessione assente! Riprova");
					myAlertDialog.setNeutralButton("Ok", null);
					myAlertDialog.show();
					return;
				}
		if (map == null) {
			
			
			// prendo il riferimento della mappa
			map = fragment.getMap();
			// prendo gli argomenti passati dalla classe DettagliPartita
			Bundle args = getArguments();
			// prendo lat e long stadio
			double latStadio = Double.parseDouble(args
					.getString(MyDatabase.StadioMetaData.STADIO_LAT));
			double longStadio = Double.parseDouble(args
					.getString(MyDatabase.StadioMetaData.STADIO_LONG));
			// creo coordinata
			LatLng coordinateStadio = new LatLng(latStadio, longStadio);
			// inserisco il marker dello stadio
			Marker markerStadio = map.addMarker(new MarkerOptions()
					.position(coordinateStadio));
			// inserisco icon al marker dello stadio
			int sqCasa = args.getInt(DettagliPartita.ICON_SQUADRA_CASA);
			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromResource(sqCasa);
			markerStadio.setIcon(icon);

			final String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
					+ "location="
					+ latStadio
					+ ","
					+ longStadio
					+ "&"
					+ "radius=1000&"
					+ "types=lodging&"
					+ "sensor=false&"
					+ "key=" + API_KEY;

			GooglePlaces places = new GooglePlaces();
			String res = "";
			
			try {
				res = places.execute(url).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//log per vedere cosa restituisce il json request
			Log.v("Mappa Alloggio", res);
			
			JSONArray alloggi = null;
			
			final ArrayList<Alloggio> listaAlloggi = new ArrayList<Alloggio>();
			try {
				JSONObject jsonAlloggi = new JSONObject(res);
				alloggi = jsonAlloggi.getJSONArray("results");
				for (int i = 0; i < alloggi.length(); i++) {

					JSONObject alloggio = alloggi.getJSONObject(i);

					JSONObject geometry = alloggio.getJSONObject("geometry");
					JSONObject location = geometry.getJSONObject("location");

					String lat = location.getString("lat");
					String longi = location.getString("lng");
					String nome = alloggio.getString("name");
					String indirizzo = alloggio.getString("vicinity");
					String rating = null;
					//controllo se quell'oggetto ha il tag rating
					if(alloggio.opt("rating") != null){
						rating = alloggio.getString("rating");
						listaAlloggi.add(new Alloggio(Double.parseDouble(lat),
								Double.parseDouble(longi), nome, indirizzo,rating));
					}else{
						listaAlloggi.add(new Alloggio(Double.parseDouble(lat),
								Double.parseDouble(longi), nome, indirizzo));
					}
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//setto l'icona al marker degli alloggi
			int iconAlloggi = getResources().getIdentifier("alloggi",
					"drawable", getActivity().getPackageName());
			BitmapDescriptor iconA = BitmapDescriptorFactory
					.fromResource(iconAlloggi);

			for (Alloggio a : listaAlloggi) {
				Marker m = map.addMarker(new MarkerOptions().position(
						new LatLng(a.getLatitudine(), a.getLongitudine()))
						.icon(iconA));
				m.setTitle(a.getNomeHotel());
				if(a.getRating()!=null)
					m.setSnippet(a.getIndrizzo()+"\n Rating:"+a.getRating());
				else
					m.setSnippet(a.getIndrizzo());
			}

			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					
					Alloggio all = null;
					for (Alloggio a : listaAlloggi)
						if (a.getNomeHotel().equals(marker.getTitle()))
							all = a;
					if (all == null)
						return;
					String url = "http://www.google.it/#q=";
					
					String nomeHotel = all.getNomeHotel();
					String indirizzo = all.getIndrizzo();
					
					//cambio gli spazi con +
					url+=nomeHotel.replace(" ", "+")+"+"+indirizzo.replace(" ","+");
					
					if(url.contains("&"))
						url = url.replace("&", "%26");
					if(url.contains(","))
						url=url.replace(",", "%2");
					
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

				}
			});

			map.setMyLocationEnabled(true);

			final CameraPosition STADIO = new CameraPosition.Builder()
					.target(coordinateStadio).zoom(14).bearing(0).tilt(25)
					.build();

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinateStadio,
					5));
			map.animateCamera(CameraUpdateFactory.newCameraPosition(STADIO));
		}
	}

	public class GooglePlaces extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			String url = params[0];
			String temp = "";
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
