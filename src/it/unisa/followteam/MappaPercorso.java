package it.unisa.followteam;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
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
                View rootView =inflater.inflate(R.layout.mappa, container, false);
                return rootView;
        }
       
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
               
                FragmentManager fm = getChildFragmentManager();
                fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
                if (fragment == null) {
                        fragment = SupportMapFragment.newInstance();
                        fm.beginTransaction().replace(R.id.map, fragment).commit();
                }
        }
       
        @Override
        public void onResume() {
                super.onResume();
                if (map == null) {
                        map = fragment.getMap();
                        map.setMyLocationEnabled(true);
                        map.addMarker(new MarkerOptions().position(new LatLng(45.46, 9.18)).title("Milano"));
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
 
                }
        }
}
