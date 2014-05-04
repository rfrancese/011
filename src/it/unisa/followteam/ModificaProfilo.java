package it.unisa.followteam;

import it.unisa.followteam.database.MyDatabase;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

public class ModificaProfilo extends Fragment {

	private MyDatabase db;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	final View rootView = inflater.inflate(R.layout.modificaprofilo, container, false);
    	if(db == null)
    		db=new MyDatabase(rootView.getContext());

    	db.open();
		
    	Spinner lista = (Spinner) rootView.findViewById(R.id.spinnerModificaProfilo);
		Cursor c=db.fetchStadio();
	     
	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
	    		rootView.getContext(), 
	    		R.layout.list_item_spinner_squadra, 
	    		c, 
	    		new String[]{MyDatabase.StadioMetaData.STADIO_NOME_SQUADRA}, 
	    		new int[] {R.id.textView1},
	    		1);
	    
	    lista.setAdapter(adapter);
	    db.close();
        return rootView;
    }
}
