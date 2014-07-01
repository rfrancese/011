package it.unisa.followteam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import it.unisa.followteam.database.HTTPPoster;
import it.unisa.followteam.database.MyDatabase;
import it.unisa.followteam.support.Connessione;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ModificaProfilo extends Fragment {

	private MyDatabase db;
	private EditText editPass, editPassConf;
	private Spinner lista;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.modificaprofilo,
				container, false);

		getActivity().setTitle("Modifica Profilo");

		if (db == null)
			db = new MyDatabase(rootView.getContext());

		db.open();

		lista = (Spinner) rootView.findViewById(R.id.spinnerModificaProfilo);
		Cursor c = db.fetchStadio();

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				rootView.getContext(), R.layout.list_item_spinner_squadra, c,
				new String[] { MyDatabase.StadioMetaData.STADIO_NOME_SQUADRA },
				new int[] { R.id.textView1 }, 1);

		lista.setAdapter(adapter);
		int i;
		String squadra = HomeActivity.ACCOUNT.getSquadra();

		for (i = 0; i < adapter.getCount(); i++) {
			Cursor cu = (Cursor) adapter.getItem(i);
			String sq = cu.getString(1);
			if (sq.equals(squadra)) {
				lista.setSelection(i);
				break;
			}
		}
		db.close();

		editPass = (EditText) rootView.findViewById(R.id.modPassProfilo);
		editPassConf = (EditText) rootView
				.findViewById(R.id.confermaPassProfilo);
		editPass.setText(HomeActivity.ACCOUNT.getPassword());
		editPassConf.setText(HomeActivity.ACCOUNT.getPassword());

		Button modProfilo = (Button) rootView
				.findViewById(R.id.buttonConfermaModifica);
		modProfilo.setOnClickListener(new MyButtonListenerModifica());

		return rootView;
	}

	public class MyButtonListenerModifica implements OnClickListener {

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
			
			confermaModifiche();
		}

		private void confermaModifiche() {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getView().getContext());
			// setto il titolo della dialog
			alertDialog.setTitle("Modifica account");
			//setto messaggio dialog
			alertDialog.setMessage("Sei sicuro di voler modificare l'account ?");
			// button si
			alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int which) {
					String pass = editPass.getText().toString();
					String confPass = editPassConf.getText().toString();

					if (!(pass.equals(confPass))) {
						Toast.makeText(getActivity(), "Le password non coincidono",
								Toast.LENGTH_LONG).show();
						return;
					}

					Cursor c = (Cursor) lista.getItemAtPosition(lista
							.getSelectedItemPosition());
					String team = c.getString(1);

					SendDataToServer sdts = new SendDataToServer();

					sdts.execute(HomeActivity.ACCOUNT.getUsername(), pass, team);

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
	}

	private class SendDataToServer extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getView().getContext());
			dialog.setCancelable(true);
			dialog.setTitle("Caricamento in corso..");
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();

			if (result.length() == 0)
				Toast.makeText(getActivity(), "Profilo aggiornato! Rieffettua il login per visualizzare le modifiche",
						Toast.LENGTH_LONG).show();

		}

		@Override
		protected String doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			String team = params[2];

			JSONObject j = new JSONObject();

			try {
				j.put("user", user);
				j.put("password", pass);
				j.put("team", team);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			String url = "http://followteam.altervista.org/update.php";
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
