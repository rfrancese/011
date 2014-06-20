package it.unisa.followteam;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

	private String feedUrl = "";
	private ListView rssListView = null;
	private ArrayList<RSSItem> RSSItems = new ArrayList<RSSItem>();
	private ArrayAdapter<RSSItem> array_adapter = null;
	private RSSParseHandler rssparsehandler = new RSSParseHandler();
	private View rootView;
	boolean connessioneDisponibile;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.news, container, false);

		// controllo connessione
		// viene inserito sempre prima di ogni chiamata a SendDataToServer
		Connessione conn = new Connessione(rootView.getContext());
		// se la connessione non è presente fa il return
		// e non effettua l'execute del SendDataToServer

		if (!conn.controllaConnessione()) {
			View viewConnessione = inflater.inflate(
					R.layout.controllo_connessione, container, false);
			return viewConnessione;
		}

		getActivity().getActionBar().setTitle("News");

		feedUrl = "http://www.gazzetta.it/rss/calcio.xml";

		rssListView = (ListView) rootView.findViewById(R.id.rssListView);

		array_adapter = new ArrayAdapter<RSSItem>(rootView.getContext(),
				R.layout.list_item, RSSItems);
		rssListView.setAdapter(array_adapter);
		refreshLista();

		return rootView;

	}

	private void refreshLista() {
		rssparsehandler.execute(feedUrl);
	}

	private class RSSParseHandler extends
			AsyncTask<String, Void, ArrayList<RSSItem>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(rootView.getContext());
			dialog.setCancelable(true);
			dialog.setTitle("Caricamento in corso..");
			dialog.show();

		}

		@Override
		protected void onPostExecute(ArrayList<RSSItem> items) {
			dialog.dismiss();
			RSSItems.clear();
			RSSItems.addAll(items);
			array_adapter = new ArrayAdapter<RSSItem>(rootView.getContext(),
					R.layout.list_item, RSSItems);
			rssListView.setAdapter(array_adapter);
		}

		@Override
		protected ArrayList<RSSItem> doInBackground(String... feedUrl) {
			ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();

			try {

				URL url = new URL(feedUrl[0]);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream is = conn.getInputStream();

					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();

					Document document = db.parse(is);
					Element element = document.getDocumentElement();

					NodeList nodeList = element.getElementsByTagName("item");

					if (nodeList.getLength() > 0) {
						for (int i = 0; i < nodeList.getLength(); i++) {

							Element entry = (Element) nodeList.item(i);
							Element _titleE = (Element) entry
									.getElementsByTagName("title").item(0);
							Element _descriptionE = (Element) entry
									.getElementsByTagName("description")
									.item(0);
							Element _pubDateE = (Element) entry
									.getElementsByTagName("pubDate").item(0);
							Element _linkE = (Element) entry
									.getElementsByTagName("link").item(0);
							String _title = _titleE.getFirstChild()
									.getNodeValue();
							String _description = _descriptionE.getFirstChild()
									.getNodeValue();
							Date _pubDate = new Date(_pubDateE.getFirstChild()
									.getNodeValue());
							String _link = _linkE.getFirstChild()
									.getNodeValue();

							RSSItem rssItem = new RSSItem(_title, _description,
									_pubDate, _link);

							rssItems.add(rssItem);

						}
					}

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

			return rssItems;
		}
	}

}