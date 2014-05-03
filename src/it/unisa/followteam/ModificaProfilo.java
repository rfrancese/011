package it.unisa.followteam;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ModificaProfilo extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	final View rootView = inflater.inflate(R.layout.modificaprofilo, container, false);
    	
        return rootView;
    }
}
