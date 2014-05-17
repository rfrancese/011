package it.unisa.followteam;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CalendariOff extends Calendario {

	public CalendariOff() {
		super();
	}

	public void aggiungiSpecifiche() {
		super.listaPartite.setOnItemClickListener(new MyListnerListView());
		TextView label = (TextView) super.rootView.findViewById(R.id.benvenuto);
		TextView user = (TextView) super.rootView.findViewById(R.id.username);
		ImageView imageTeam = (ImageView) super.rootView
				.findViewById(R.id.imageSquadra);

		imageTeam.setImageResource(android.R.color.transparent);
		user.setText("");
		label.setText("Registrati!\n"
				+ "Potrai tracciare il percorso dalla tua posizione\n "
				+ "alla posizione dello stadio!");

	}

	public class MyListnerListView implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(
					getActivity(),
					"Per i dettagli della partita devi effettuare il login in!",
					Toast.LENGTH_LONG).show();

		}
	}
}
