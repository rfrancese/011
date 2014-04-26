package it.unisa.followteam;



import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class RegistrazioneFragment extends Fragment {

	 private MyDatabase db;
	
	 public RegistrazioneFragment() {
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    
	    	View rootView = inflater.inflate(R.layout.registrazione_fragment, container, false);
	    	
	    	if(db == null)
	    		db=new MyDatabase(rootView.getContext());

	    	db.open();
			
			Spinner lista = (Spinner) rootView.findViewById(R.id.spinner);
			Cursor c=db.fetchStadio();
		     
		    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
		    		rootView.getContext(), 
		    		R.layout.list_item, 
		    		c, 
		    		new String[]{MyDatabase.StadioMetaData.STADIO_NOME_SQUADRA}, 
		    		new int[] {R.id.textView1},
		    		1);
		    
		    lista.setAdapter(adapter);
		    db.close();
	        return rootView;
	    }
	    
}
