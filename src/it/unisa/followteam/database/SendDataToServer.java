package it.unisa.followteam.database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.*;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

public class SendDataToServer extends AsyncTask<String, Void, String> {

	public static final String TYPE_REG = "Registrazione";
	public static final String TYPE_LOG = "Login";
	public static final String TYPE_UPDATE = "Update";
	public static final String TYPE_DELETE = "Delete";
	private Context context;
	ProgressDialog dialog;

	public SendDataToServer(Context cxt) {
		context = cxt;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(true);
		dialog.setTitle("Caricamento in corso..");
		dialog.show();

	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i("SendDataToServer", "" + result);
		if (result != null)
			dialog.dismiss();
	}

	@Override
	protected String doInBackground(String... params) {

		String temp = "";

		String user = params[0];
		String pass = params[1];
		String team = params[2];
		String caso = params[3];

		JSONObject j = new JSONObject();

		try {
			j.put("user", user);
			j.put("password", pass);
			j.put("team", team);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = "";
		switch (caso) {
		case TYPE_LOG:
			url = "http://followteam.altervista.org/login.php";
			break;
		case TYPE_REG:
			url = "http://followteam.altervista.org/new_account.php";
			break;
		case TYPE_UPDATE:
			url = "http://followteam.altervista.org/update.php";
			break;
		case TYPE_DELETE:
			url = "http://followteam.altervista.org/delete.php";
			break;
		default:
			throw new IllegalArgumentException("Caso " + caso + " non corretto");
		}
		
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
