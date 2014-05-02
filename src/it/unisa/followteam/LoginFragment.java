package it.unisa.followteam;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends Fragment {
	
	Button buttonLogin;
	
    public LoginFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	final View rootView = inflater.inflate(R.layout.login_fragment, container, false);
    	 //bottone login apertura intent
        
        buttonLogin=(Button) rootView.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
 	
 		@Override
		public void onClick(View v) {
 			
 			Intent login= new Intent(rootView.getContext(), HomeLogin.class);
 			startActivity(login);				
 			
 			}
 		});
        return rootView;
    }
    
}