package it.unisa.followteam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class Classifica extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.webview, container,
				false);
		
		getActivity().getActionBar().setTitle("Classifica");


		WebView web = (WebView) rootView.findViewById(R.id.web);
		web.loadUrl("http://www.corrieredellosport.it/live/SerieA/classifica.shtml");

		return rootView;

	}
}
