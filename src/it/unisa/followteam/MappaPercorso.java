package it.unisa.followteam;


import it.unisa.followteam.database.MyDatabase;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 




import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class MappaPercorso extends Fragment {
	
        private SupportMapFragment fragment;
        private GoogleMap map;

  
        
       
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView =inflater.inflate(R.layout.mappapercorso, container, false);
                getActivity().getActionBar().setTitle("Mappa Percorso");
                
                return rootView;
        }
       
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                
                
             
                FragmentManager fm = getChildFragmentManager();
                fragment = (SupportMapFragment) fm.findFragmentById(R.id.mappa_percorso);
                if (fragment == null) {
                        fragment = SupportMapFragment.newInstance();
                        fm.beginTransaction().replace(R.id.mappa_percorso, fragment).commit();
                }
        }
       
        @Override
        public void onResume() {
                super.onResume();
                if (map == null) {
                	
                    Bundle args=getArguments();
                    
                    Double latitudine = Double.parseDouble(args.getString(MyDatabase.StadioMetaData.STADIO_LAT));
                    Double longitudine =Double.parseDouble(args.getString(MyDatabase.StadioMetaData.STADIO_LONG));

                    int sqCasa=args.getInt(DettagliPartita.ICON_SQUADRA_CASA);
                    BitmapDescriptor icon=BitmapDescriptorFactory.fromResource(sqCasa);
                    
                    LatLng coordinate =new LatLng(latitudine, longitudine);
                    
                    map = fragment.getMap();
                    map.setMyLocationEnabled(true);
                    map.addMarker(new MarkerOptions().position(coordinate)).setIcon(icon);
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
 
                }
        }
}
