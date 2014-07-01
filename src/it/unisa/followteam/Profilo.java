package it.unisa.followteam;

import it.unisa.followteam.database.HTTPPoster;
import it.unisa.followteam.support.Connessione;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Profilo extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.profilo, container,
				false);
		
		getActivity().setTitle("Profilo");


		TextView userProfilo = (TextView) rootView
				.findViewById(R.id.usernameProfilo);
		TextView nomeSquadraProfilo = (TextView) rootView
				.findViewById(R.id.squadraProfilo);
		ImageView squadraProfiloIcon = (ImageView) rootView
				.findViewById(R.id.imageSquadraProfilo);

		userProfilo.setText(HomeActivity.ACCOUNT.getUsername());
		String nomeSquadra = HomeActivity.ACCOUNT.getSquadra();
		nomeSquadraProfilo.setText(nomeSquadra);
		int image = getResources().getIdentifier(nomeSquadra.toLowerCase(),
				"drawable", getActivity().getPackageName());
		squadraProfiloIcon.setImageResource(image);
		
		
		Button buttonModifica = (Button) rootView
				.findViewById(R.id.buttonModificaProfilo);
		buttonModifica.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Fragment fragment = new ModificaProfilo();
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

			}
		});

		Button buttonElimina = (Button) rootView
				.findViewById(R.id.buttonEliminaProfilo);
		buttonElimina.setOnClickListener(new View.OnClickListener() {
			// elimina l'istanza dal database
			@Override
			public void onClick(View v) {
				
				Connessione conn = new Connessione(getView().getContext());
				// se la connessione non è presente fa il return
				// e non effettua l'execute del SendDataToServer
				if (!conn.controllaConnessione()) {
					Toast.makeText(getView().getContext(),
							"Controlla la tua connessione a internet e riprova",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				confermaEliminaAccount();
				
			}

			private void confermaEliminaAccount() {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(rootView.getContext());
				// setto il titolo della dialog
				alertDialog.setTitle("Elimina account");
				//setto messaggio dialog
				alertDialog.setMessage("Sei sicuro di voler eliminare l'account ?");
				// button si
				alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						String user=HomeActivity.ACCOUNT.getUsername();
						
						SendDataToServer sdts=new SendDataToServer();
						sdts.execute(user);
					}
				});
				// button no
				alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						}
				});
				alertDialog.show();	
			}
		});
	    return rootView;
	    
	}

	private class SendDataToServer extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getView().getContext());
			dialog.setCancelable(true);
			dialog.setTitle("Eliminazione account in corso...");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();

			if (result.length() == 0){
				Toast.makeText(getActivity(), "Account eliminato!",
						Toast.LENGTH_LONG).show();
				Intent main = new Intent(getView().getContext(),
						MainActivity.class);
				startActivity(main);
				getActivity().finish();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			
			String user = params[0];
			
			JSONObject j = new JSONObject();

			try {
				j.put("user", user);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			String url = "http://followteam.altervista.org/delete.php";
			String temp = "";
			try {
				Map<String, String> kvPairs = new HashMap<String, String>();
				kvPairs.put("pippo", j.toString());
				HttpResponse re = HTTPPoster.doPost(url, kvPairs);
				temp = EntityUtils.toString(re.getEntity());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return temp;
		}

	}

}
