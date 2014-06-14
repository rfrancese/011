package it.unisa.followteam;

import it.unisa.followteam.support.Connessione;
import android.app.AlertDialog;
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
			return rootView;
		}

		getActivity().getActionBar().setTitle("Classifica");


		WebView web = (WebView) rootView.findViewById(R.id.web);
		web.loadUrl("http://www.corrieredellosport.it/live/SerieA/classifica.shtml");

		return rootView;

	}
}
