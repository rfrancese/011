package it.unisa.followteam;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import it.unisa.followteam.support.RSSItem;

public class News extends Fragment {

	String feedUrl = "";
	ListView rssListView = null;
	ArrayList<RSSItem> RSSItems = new ArrayList<RSSItem>();
	ArrayAdapter<RSSItem> array_adapter = null;
	RSSParseHandler rssparsehandler = new RSSParseHandler();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.news, container, false);
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