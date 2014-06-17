package it.unisa.followteam;

import java.util.concurrent.ExecutionException;

import it.unisa.followteam.R.id;
import it.unisa.followteam.database.MyDatabase;
import it.unisa.followteam.database.SendDataToServer;
import it.unisa.followteam.support.Connessione;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.database.Cursor;
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
	private final static String ERRORE_PASS_CONFERMA = "Password diverse";
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

				SendDataToServer sdts = new SendDataToServer(getView().getContext());
				String res = "";

				if (pass.equals(passConf)) {
					try {
						res = sdts.execute(user, pass, team,
								SendDataToServer.TYPE_REG).get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(v.getContext(), res, Toast.LENGTH_LONG)
							.show();

				} else {
					Toast.makeText(v.getContext(), ERRORE_PASS_CONFERMA,
							Toast.LENGTH_LONG).show();
				}

			}
		});

		return rootView;
	}

}
