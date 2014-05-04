package it.unisa.followteam;

import java.util.ArrayList;

import it.unisa.followteam.database.MyDatabase;
import android.app.Fragment;
import android.app.DownloadManager.Query;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Calendario  extends Fragment {
	
	private MyDatabase db;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	final View rootView = inflater.inflate(R.layout.calendario, container, false);
    	
    	if(db == null)
    		db=new MyDatabase(rootView.getContext());

    	db.open();
		ArrayList<String> giornate=new ArrayList<String>();
		Spinner lista = (Spinner) rootView.findViewById(R.id.spinnerGiornate);
		Cursor c=db.fetchGiornata();
		c.moveToFirst();
		
		while(c.isAfterLast()==false){
			giornate.add("Giornata "+c.getString(0));
			c.moveToNext();
		}
		
		c.close();
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(rootView.getContext(),R.layout.list_item,giornate);
	    lista.setAdapter(adapter);
	    
	    db.close();
	   
	    return rootView;
    }

}
