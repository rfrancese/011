package it.unisa.followteam;

import java.util.concurrent.ExecutionException;

import it.unisa.followteam.database.MyDatabase;
import it.unisa.followteam.database.SendDataToServer;
import it.unisa.followteam.support.Connessione;
import android.support.v4.app.Fragment;
import android.database.Cursor;
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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.modificaprofilo,
				container, false);
		
		getActivity().getActionBar().setTitle("Modifica Profilo");

		
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
			//controllo connessione 
			//viene inserito sempre prima di ogni chiamata a SendDataToServer
			Connessione conn= new Connessione(getView().getContext());
			//se la connessione non è presente fa il return
			//e non effettua l'execute del SendDataToServer
			if(!conn.controllaConnessione()){
				Toast.makeText(getView().getContext(), "Controlla la tua connessione a internet e riprova", Toast.LENGTH_LONG).show();
				return;
				
			}
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

			String res = "";

			SendDataToServer sdts = new SendDataToServer(getView().getContext());

			try {
				res = sdts.execute(HomeActivity.ACCOUNT.getUsername(), pass,
						team, SendDataToServer.TYPE_UPDATE).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (res.length() == 0)
				Toast.makeText(getActivity(), "Profilo aggiornato!",
						Toast.LENGTH_LONG).show();

			// da gestire internet assente

		}

	}
}
