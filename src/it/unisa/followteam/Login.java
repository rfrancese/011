package it.unisa.followteam;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Login extends Fragment {
	
	Button buttonLogin;
	
    public Login() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	final View rootView = inflater.inflate(R.layout.login, container, false);
    	 //bottone login apertura intent
        
        buttonLogin=(Button) rootView.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
 	
 		@Override
		public void onClick(View v) {
 			
 			Intent login= new Intent(rootView.getContext(), HomeActivity.class);
 			startActivity(login);
 			getActivity().finish();
 			
 			}
 		});
        return rootView;
    }
    
}