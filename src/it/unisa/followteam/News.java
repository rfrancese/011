package it.unisa.followteam;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import it.unisa.followteam.support.Connessione;
import it.unisa.followteam.support.RSSItem;

public class News extends Fragment {

	String feedUrl = "";
	ListView rssListView = null;
	ArrayList<RSSItem> RSSItems = new ArrayList<RSSItem>();
	ArrayAdapter<RSSItem> array_adapter = null;
	RSSParseHandler rssparsehandler = new RSSParseHandler();
	Context context;
	ProgressDialog PD;
	ConnectivityManager cm;
	boolean connessioneDisponibile;
	Dialog dialog;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.news, container, false);
		
		//controllo connessione 
		//viene inserito sempre prima di ogni chiamata a SendDataToServer
		Connessione conn= new Connessione(rootView.getContext());
		//se la connessione non è presente fa il return
		//e non effettua l'execute del SendDataToServer
		if(!conn.controllaConnessione()){
			Toast.makeText(rootView.getContext(), "Controlla la tua connessione a internet e riprova", Toast.LENGTH_LONG).show();
			return rootView;
			
		}
		
		getActivity().getActionBar().setTitle("News");

		feedUrl = "http://www.gazzetta.it/rss/calcio.xml";
		
		
			rssListView = (ListView) rootView.findViewById(R.id.rssListView);
	
			array_adapter = new ArrayAdapter<RSSItem>(rootView.getContext(), R.layout.list_item,
					RSSItems);
			rssListView.setAdapter(array_adapter);
			refreshRSSList();
		
		return rootView;

	}

	public void onSelected(View theSelected) {
		Toast.makeText(getView().getContext(), "hai cliccato", Toast.LENGTH_LONG).show();
	}

	private void refreshRSSList() {

		ArrayList<RSSItem> newItems = null;
		try {
			newItems = rssparsehandler.execute(feedUrl).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RSSItems.clear();
		RSSItems.addAll(newItems);

	}
	
}