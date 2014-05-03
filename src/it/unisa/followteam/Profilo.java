package it.unisa.followteam;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Profilo extends Fragment {

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	    	
	    	final View rootView = inflater.inflate(R.layout.profilo, container, false);
	    	
	    	
	        Button buttonModifica = (Button) rootView.findViewById(R.id.buttonModificaProfilo);
	        buttonModifica.setOnClickListener(new View.OnClickListener() {
	 	
	 		@Override
			public void onClick(View v) {
	 			Fragment fragment =new ModificaProfilo();
	 			FragmentManager fragmentManager = getFragmentManager();
	 	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

	 			}
	 		});
	        Button buttonElimina=(Button) rootView.findViewById(R.id.buttonEliminaProfilo);
	        buttonElimina.setOnClickListener(new View.OnClickListener() {
				
	        	//elimina l'istanza dal databas
				public void onClick(View v) {
				
					Toast toast =Toast.makeText( getActivity(), "L'account verrà eliminato", Toast.LENGTH_LONG);
					toast.show();
				}
			});
	        
	        return rootView;
	    }
}
