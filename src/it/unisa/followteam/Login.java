package it.unisa.followteam;

import java.util.concurrent.ExecutionException;

import it.unisa.followteam.database.SendDataToServer;
import it.unisa.gestioneAutenticazione.Account;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Fragment {

	private EditText editUser, editPass;

	public Login() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater
				.inflate(R.layout.login, container, false);
		// bottone login apertura intent

		Button buttonLogin = (Button) rootView.findViewById(R.id.buttonLogin);
		editUser = (EditText) rootView.findViewById(R.id.username);
		editPass = (EditText) rootView.findViewById(R.id.password);

		buttonLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SendDataToServer sdts = new SendDataToServer();
				String res = "";

				String user = editUser.getText().toString();
				String pass = editPass.getText().toString();

				try {
					res = sdts.execute(user, pass, null,
							SendDataToServer.TYPE_LOG).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (res.equals("errore")) {
					Toast.makeText(v.getContext(), "Dati non trovati",
							Toast.LENGTH_LONG).show();
				} else {
					String pkg = getActivity().getPackageName();
					Intent home = new Intent(rootView.getContext(),
							HomeActivity.class);
					Account account = new Account(user, pass, res);
					home.putExtra(pkg + ".myAccount", account);
					startActivity(home);
					getActivity().finish();
				}

			}
		});
		return rootView;
	}

}