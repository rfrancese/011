package it.unisa.followteam;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DettagliPartita extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
		final View rootView = inflater.inflate(R.layout.dettaglipartita, container, false);
		
		return rootView;
	}
}
