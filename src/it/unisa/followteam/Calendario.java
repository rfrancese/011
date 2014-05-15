package it.unisa.followteam;


import it.unisa.followteam.database.MyDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Calendario  extends Fragment {

	private MyDatabase db;
	private Spinner spinner;
	protected ListView listaPartite;
	private String giornataScelta;
	protected View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	 rootView = inflater.inflate(R.layout.calendario, container, false);
    	
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

	    spinner.setOnItemSelectedListener(new MyListenerSpinner());
	    listaPartite.setOnItemClickListener(new MyListnerListView());
	    
	   
	    aggiungiSpecifiche();
	    

	    return rootView;
    }

	public void aggiungiSpecifiche(){
		TextView userlabel = (TextView) getView().findViewById(R.id.username);
		ImageView imageTeam = (ImageView) getView().findViewById(R.id.imageSquadra);
		userlabel.setText(HomeActivity.ACCOUNT.getUsername());
	    String nomeSquadra = HomeActivity.ACCOUNT.getSquadra();
	    int image = getResources().getIdentifier(nomeSquadra.toLowerCase(),"drawable", getActivity().getPackageName());
    	imageTeam.setImageResource(image);
	}
	public class MyListenerSpinner implements OnItemSelectedListener{
		
		@Override
 	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
 		 	Cursor cursor = (Cursor) arg0.getItemAtPosition(arg2);
 		 	giornataScelta = cursor.getString(0);


 		 	db.open();
 		 	String sql="SELECT "+ MyDatabase.PartiteMetaData.PARTITA_ID
 		 			+", "+MyDatabase.PartiteMetaData.PARTITA_SQ_CASA
 		 			+ ", "+ MyDatabase.PartiteMetaData.PARTITA_SQ_OSPITE 
 		 			+ " FROM "+ MyDatabase.PartiteMetaData.PARTITA_TITLE_TABLE
 		 			+ " WHERE "+MyDatabase.PartiteMetaData.PARTITA_IDGIORNATA
 		 			+ "='"+giornataScelta+"'";
 		 	Cursor c = db.query(sql, null);

 		 	SimpleCursorAdapter adapter = new SimpleCursorAdapter(
 			    		getView().getContext(), 
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
	}

	
	public class MyListnerListView implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Cursor c=(Cursor)parent.getItemAtPosition(position);
			String sqCasa=c.getString(1);
			String sqOspite=c.getString(2);

			Bundle args = new Bundle();
			args.putString(MyDatabase.PartiteMetaData.PARTITA_SQ_CASA, sqCasa);
			args.putString(MyDatabase.PartiteMetaData.PARTITA_SQ_OSPITE, sqOspite);
			args.putString(MyDatabase.PartiteMetaData.PARTITA_IDGIORNATA,giornataScelta);
			Fragment fragment =new DettagliPartita();
 			FragmentManager fragmentManager = getFragmentManager();
 			fragment.setArguments(args);
 	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


		}
	}
}