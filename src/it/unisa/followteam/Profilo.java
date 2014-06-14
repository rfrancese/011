package it.unisa.followteam;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Profilo extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.profilo, container,
				false);
		
		getActivity().getActionBar().setTitle("Profilo");


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
			public void onClick(View v) {

				Toast toast = Toast.makeText(getActivity(),
						"L'account verrà eliminato", Toast.LENGTH_LONG);
				toast.show();
			}
		});

		return rootView;
	}
}
