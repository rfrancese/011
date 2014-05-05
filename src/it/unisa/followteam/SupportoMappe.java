package it.unisa.followteam;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class SupportoMappe extends FragmentActivity {

    public static FragmentManager fragmentManager;
    
    protected void onCreate(Bundle savedInstanceState){
    	
    	super.onCreate(savedInstanceState);
    	fragmentManager=getSupportFragmentManager();
    }

}
