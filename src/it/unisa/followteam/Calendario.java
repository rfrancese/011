package it.unisa.followteam;


import it.unisa.followteam.database.MyDatabase;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Calendario  extends Fragment {
	
	private MyDatabase db;
	private Spinner spinner;
	private ListView listaPartite;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	final View rootView = inflater.inflate(R.layout.calendario, container, false);
    	
    	if(db == null)
    		db=new MyDatabase(rootView.getContext());

    	db.open();
	 	listaPartite = (ListView) rootView.findViewById(R.id.listaPartite);
    	spinner = (Spinner) rootView.findViewById(R.id.spinnerGiornate);

		Cursor c=db.fetchGiornata();
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(
	    		rootView.getContext(), 
	    		R.layout.list_item_spinner_calendario, 
	    		c, 
	    		new String[]{MyDatabase.GiornataMetaData.GIORNATA_ID}, 
	    		new int[] {R.id.idGiornata},
	    		1);
	    spinner.setAdapter(adapter);
	    
	    db.close();

	    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    	 @Override
	    	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	    		 	Cursor cursor = (Cursor) arg0.getItemAtPosition(arg2);
	    		 	String giornataScelta = cursor.getString(0);
	    		 	
	    		 	db.open();
	    		 	String sql="SELECT "+ MyDatabase.PartiteMetaData.PARTITA_ID
	    		 			+", "+MyDatabase.PartiteMetaData.PARTITA_SQ_CASA
	    		 			+ ", "+ MyDatabase.PartiteMetaData.PARTITA_SQ_OSPITE 
	    		 			+ " FROM "+ MyDatabase.PartiteMetaData.PARTITA_TITLE_TABLE
	    		 			+ " WHERE "+MyDatabase.PartiteMetaData.PARTITA_IDGIORNATA
	    		 			+ "='"+giornataScelta+"'";
	    		 	Cursor c = db.query(sql, null);
	    		 	
	    		 	SimpleCursorAdapter adapter = new SimpleCursorAdapter(
	    			    		rootView.getContext(), 
	    			    		R.layout.list_item_view_partite, 
	    			    		c, 
	    			    		new String[]{MyDatabase.PartiteMetaData.PARTITA_SQ_CASA, MyDatabase.PartiteMetaData.PARTITA_SQ_OSPITE}, 
	    			    		new int[] {R.id.sqCasa, R.id.sqOspite},
	    			    		1);
	    			    
	    			
	    		 	listaPartite.setAdapter(adapter);
	    			db.close();
	    	 
	    	 	}

	    	    @Override
	    	    public void onNothingSelected(AdapterView<?> arg0) {
	    	        // TODO Auto-generated method stub
	    	    }   
	    });
	    
	    listaPartite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c=(Cursor)parent.getItemAtPosition(position);
				String sqCasa=c.getString(1);
				String sqOspite=c.getString(2);
				Toast.makeText(view.getContext(), sqCasa+" "+sqOspite, Toast.LENGTH_LONG).show();
				
			}
	    	
		});
	    
	    
	   
	    return rootView;
    }
	

}
