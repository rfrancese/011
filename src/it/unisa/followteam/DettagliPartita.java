package it.unisa.followteam;



import it.unisa.followteam.database.MyDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DettagliPartita extends Fragment {
	
	private MyDatabase db;
	private Button buttonPercorso;
	private Button buttonAlloggio;
	private String indirizzoStadio;
	private String latitudine;
	private String longitudine;
	private int iconSqCasa;
	public static final String ICON_SQUADRA_CASA="icona_sq_casa";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
		final View rootView = inflater.inflate(R.layout.dettaglipartita, container, false);
		getActivity().getActionBar().setTitle("Dettagli Partita");

		Bundle args = getArguments();
		
		String sqCasa = args.getString(MyDatabase.PartiteMetaData.PARTITA_SQ_CASA);
		String sqOspite = args.getString(MyDatabase.PartiteMetaData.PARTITA_SQ_OSPITE);
		String idGiornata = args.getString(MyDatabase.PartiteMetaData.PARTITA_IDGIORNATA);
		
		TextView viewSqCasa = (TextView) rootView.findViewById(R.id.squadraCasa);
		TextView viewSqOspite = (TextView) rootView.findViewById(R.id.squadraOspite);
		
		viewSqCasa.setText(sqCasa);
		viewSqOspite.setText(sqOspite);
		
		ImageView imageSqCasa = (ImageView) rootView.findViewById(R.id.imageSquadraCasa);
		ImageView imageSqOspite = (ImageView) rootView.findViewById(R.id.imageSquadraOspite);
		
		if(sqCasa.contains(" ")){
			String[] sq= sqCasa.split(" ");
			sqCasa = sq[0] + sq[1];
		}
		
		if(sqOspite.contains(" ")){
			String[] sq= sqOspite.split(" ");
			sqOspite = sq[0] + sq[1];
		}
		
		iconSqCasa = getResources().getIdentifier(sqCasa.toLowerCase(), "drawable",getActivity().getPackageName());
		int iconSqOspite = getResources().getIdentifier(sqOspite.toLowerCase(), "drawable",getActivity().getPackageName());

		imageSqCasa.setImageResource(iconSqCasa);
		imageSqOspite.setImageResource(iconSqOspite);
		

		TextView descrizione = (TextView) rootView.findViewById(R.id.viewDescrizione);
		
		if(db == null)
    		db=new MyDatabase(rootView.getContext());

    	db.open();
    	String sql= "SELECT "+MyDatabase.GiornataMetaData.GIORNATA_DATA
    				+" FROM "+MyDatabase.GiornataMetaData.GIORNATA_TITLE_TABLE
    				+" WHERE "+MyDatabase.GiornataMetaData.GIORNATA_ID
    				+"='"+idGiornata+"'";
    	//controllare
    	Cursor c = db.query(sql, null);
    	c.moveToFirst();
    	String dataPartita =c.getString(0);
    	c=null;
    	sql="SELECT "+ MyDatabase.StadioMetaData.STADIO_NOME_STADIO
    		+", "+ MyDatabase.StadioMetaData.STADIO_INDIRIZZO
    		+", "+ MyDatabase.StadioMetaData.STADIO_LAT
    		+", "+ MyDatabase.StadioMetaData.STADIO_LONG
    		+" FROM "+ MyDatabase.StadioMetaData.STADIO_TITLE_TABLE
    		+" WHERE "+MyDatabase.StadioMetaData.STADIO_NOME_SQUADRA
    		+"='"+sqCasa+"'";
    	
    	c = db.query(sql, null);
    	c.moveToFirst();
    	String nomeStadio = c.getString(0);
    	indirizzoStadio = c.getString(1);
    	latitudine=c.getString(2);
    	longitudine=c.getString(3);
    	c.close();
    	descrizione.setText(dataPartita+"\n"+ nomeStadio+ "\n"+ indirizzoStadio);
    	
    	//Gestione button Percorso
    	buttonPercorso =(Button)rootView.findViewById(R.id.buttonPercorso);
    	buttonPercorso.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Bundle args = new Bundle();
				
				args.putString(MyDatabase.StadioMetaData.STADIO_INDIRIZZO,indirizzoStadio);
				args.putString(MyDatabase.StadioMetaData.STADIO_LAT, latitudine);
				args.putString(MyDatabase.StadioMetaData.STADIO_LONG, longitudine);
				args.putInt(DettagliPartita.ICON_SQUADRA_CASA, iconSqCasa);
				
	 			FragmentManager fragmentManager = getFragmentManager();
	 			Fragment fragment =new MappaPercorso();
	 			fragment.setArguments(args);
	 	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		});
    	
    	// Gestione Button Alloggio
    	buttonAlloggio =(Button)rootView.findViewById(R.id.buttonAlloggio);
    	buttonAlloggio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bundle args = new Bundle();
				
				Fragment fragment =new MappaAlloggio();
	 			FragmentManager fragmentManager = getFragmentManager();
	 	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		});
		return rootView;
	}

}
