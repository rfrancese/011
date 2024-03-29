package it.unisa.followteam;

import it.unisa.followteam.support.Account;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView listaOpzioni;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence titoloLista;
	private CharSequence titolo;
	private String[] opzioni;
	public static Account ACCOUNT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//salvo le info dell'utente che ha effettuato il login
		String pkg = getPackageName();
		ACCOUNT = (Account) getIntent()
				.getSerializableExtra(pkg + ".myAccount");

		titolo = titoloLista = getTitle();
		opzioni = getResources().getStringArray(R.array.optionsMenuLogin);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listaOpzioni = (ListView) findViewById(R.id.left_drawer);

		// inserisce un'ombra dopo l'apertura del menu laterale
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// inserisco le opzioni nella listview e setto le azioni
		listaOpzioni.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, opzioni));
		listaOpzioni.setOnItemClickListener(new DrawerItemClickListener());

		// seleziono la prima opzione della lista
		// come quella di default all'apertura dell'applicazione
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// setto il bottone per l'apertura del menu laterale
		getActionBar().setHomeButtonEnabled(true);

		// interazione menu laterale
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) { // chiusura menu
				getActionBar().setTitle(titolo);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) { // apertura menu
				getActionBar().setTitle(titoloLista);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// seleziona il calendario come prima entry
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
				.add(R.id.content_frame, new Calendario()).commit();
			setTitle(opzioni[0]);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// mDrawerToggle gestisce l'apertura e la chiusura del menu
		// quando viene selezionata un'opzione
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selezionaItem(position);
		}
	}

	private void selezionaItem(int posizione) {

		String scelta = getResources().getStringArray(R.array.optionsMenuLogin)[posizione];
		Fragment fragment = new Calendario();

		switch (scelta) {
		case "Calendario":
			fragment = new Calendario();
			break;
		case "Profilo":
			fragment = new Profilo();
			break;
		case "Classifica":
			fragment = new Classifica();
			break;
		case "News" : 
			fragment=new News(); 
			break;
		case "Logout":
			Intent main = new Intent(this, MainActivity.class);
			startActivity(main);
			finish();
			break;
		default:
			Toast.makeText(this, scelta, Toast.LENGTH_LONG).show();
			break;
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

		// aggiorna il titolo e l'item selezionato e chiude il menu
		listaOpzioni.setItemChecked(posizione, true);
		setTitle(opzioni[posizione]);
		mDrawerLayout.closeDrawer(listaOpzioni);
	}

	@Override
	public void setTitle(CharSequence title) {
		titolo = title;
		getActionBar().setTitle(titolo);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
		
	}
	
	@Override
	public void onBackPressed() {
		if(getSupportFragmentManager().getBackStackEntryCount() != 0)
			super.onBackPressed();
	}
}
