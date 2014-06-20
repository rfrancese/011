package it.unisa.followteam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import it.unisa.followteam.R.id;
import it.unisa.followteam.database.HTTPPoster;
import it.unisa.followteam.database.MyDatabase;
import it.unisa.followteam.support.Connessione;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Registrazione extends Fragment {

	private MyDatabase db;
	private final static String ERRORE_PASS_CONFERMA = "Le password non coincidono";
	private final static String ERRORE_USER = "Il nome utente o la password non possono essere vuoti";

	private Spinner lista;
	private EditText editUser, editPass, editPassConf;

	public Registrazione() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.registrazione, container,
				false);
		getActivity().getActionBar().setTitle("Registrati");

		if (db == null)
			db = new MyDatabase(rootView.getContext());

		db.open();

		lista = (Spinner) rootView.findViewById(R.id.spinner);
		Cursor c = db.fetchStadio();

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				rootView.getContext(), R.layout.list_item_spinner_squadra, c,
				new String[] { MyDatabase.StadioMetaData.STADIO_NOME_SQUADRA },
				new int[] { R.id.textView1 }, 1);

		lista.setAdapter(adapter);
		db.close();

		editUser = (EditText) rootView.findViewById(R.id.usernameReg);
		editPass = (EditText) rootView.findViewById(R.id.passwordReg);
		editPassConf = (EditText) rootView.findViewById(R.id.passwordConferma);

		Button registrati = (Button) rootView
				.findViewById(id.buttonRegistrazione);
		registrati.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//controllo connessione 
				//viene inserito sempre prima di ogni chiamata a SendDataToServer
				Connessione conn= new Connessione(getView().getContext());
				//se la connessione non è presente fa il return
				//e non effettua l'execute del SendDataToServer
				if(!conn.controllaConnessione()){
					Toast.makeText(getView().getContext(), "Controlla la tua connessione a internet e riprova", Toast.LENGTH_LONG).show();
				}

				String user = editUser.getText().toString();
				String pass = editPass.getText().toString();
				String passConf = editPassConf.getText().toString();
				Cursor c = (Cursor) lista.getItemAtPosition(lista
						.getSelectedItemPosition());
				String team = c.getString(1); // punta alla colonna nome_squadra

				SendDataToServer sdts = new SendDataToServer();
				
				//se le password coincidono eseguo l'asyncTask
				if((!user.equals("")) && !(pass.equals(""))){
					if(pass.equals(passConf)){
							sdts.execute(user,pass,team);
					}else{
							Toast.makeText(getView().getContext(), ERRORE_PASS_CONFERMA,
								Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getView().getContext(), ERRORE_USER,
							Toast.LENGTH_LONG).show();
				}
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
			dialog.setTitle("Caricamento in corso..");
			dialog.show();
		}
		@Override
		protected void onPostExecute(String result) {
			
			dialog.dismiss();
				Toast.makeText(getView().getContext(), result, Toast.LENGTH_LONG)
				.show();
		}
		protected String doInBackground(String... params) {
			
			//prelevo le variabili da params
			String user = params[0];
			String pass = params[1];
			String team=params[2];
			
			//inizializzo il jsonObject
			JSONObject j = new JSONObject();
			try {			
					j.put("user", user);
					j.put("password", pass);
					j.put("team", team);
				}
			catch (JSONException e) {
				e.printStackTrace();
			}
			String url = "http://followteam.altervista.org/new_account.php";
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
