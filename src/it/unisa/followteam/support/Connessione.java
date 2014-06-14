package it.unisa.followteam.support;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

public class Connessione {
	
	protected Context context;
	
	public Connessione(Context context){
		this.context= context;
	}
	
	public boolean controllaConnessione(){
		boolean connessioneDisponibile = false;

		// inizializzo il ConnectivityManager
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
				Context.CONNECTIVITY_SERVICE);


		if (cm != null && cm.getActiveNetworkInfo() != null) {
			// controllo disponibilità di rete
			connessioneDisponibile = cm.getActiveNetworkInfo()
					.isConnectedOrConnecting();
		}
		
		return connessioneDisponibile;
		
	}

}
