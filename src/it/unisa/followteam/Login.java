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
import it.unisa.followteam.support.Account;
import it.unisa.followteam.support.Connessione;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Fragment {

	private EditText editUser, editPass;
	private String user,pass;

	public Login() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater
				.inflate(R.layout.login, container, false);

		getActivity().setTitle("Login");

		Button buttonLogin = (Button) rootView.findViewById(R.id.buttonLogin);
		editUser = (EditText) rootView.findViewById(R.id.username);
		editPass = (EditText) rootView.findViewById(R.id.password);

		buttonLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SendDataToServer sdts = new SendDataToServer();

				user = editUser.getText().toString();
				pass = editPass.getText().toString();

				// controllo connessione
				// viene inserito sempre prima di ogni chiamata a
				// SendDataToServer
				Connessione conn = new Connessione(rootView.getContext());
				// se la connessione non è presente fa il return
				// e non effettua l'execute del SendDataToServer
				if (!conn.controllaConnessione()) {
					Toast.makeText(
							getView().getContext(),
							"Controlla la tua connessione a internet e riprova",
							Toast.LENGTH_LONG).show();
					return;
				}

				sdts.execute(user, pass);
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

			if (result.equals("errore")) {
				Toast.makeText(getView().getContext(), "Dati non trovati",
						Toast.LENGTH_LONG).show();
			} else {
				String pkg = getActivity().getPackageName();
				Intent home = new Intent(getView().getContext(),
						HomeActivity.class);
				Account account = new Account(user, pass, result);
				home.putExtra(pkg + ".myAccount", account);
				startActivity(home);
				getActivity().finish();
			}

		}

		@Override
		protected String doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];

			JSONObject j = new JSONObject();

			try {
				j.put("user", user);
				j.put("password", pass);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			String url = "http://followteam.altervista.org/login.php";
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