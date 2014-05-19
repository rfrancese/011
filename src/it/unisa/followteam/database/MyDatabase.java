package it.unisa.followteam.database;

import it.unisa.eccezioni.TableNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.ParagraphStyle;

public class MyDatabase {

	SQLiteDatabase mDb;
	DbHelper mDbHelper;
	Context mContext;
	private static final String DB_NAME = "followTeam";// nome del db
	private static final int DB_VERSION = 4; // numero di versione del nostro db

	// classe che ci aiuta nella creazione del db
	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) { // solo quando il db viene
													// creato, creiamo la
													// tabella
			_db.execSQL(STADIO_TABLE_CREATE);
			_db.execSQL(GIORNATA_TABLE_CREATE);
			_db.execSQL(PARTITA_TABLE_CREATE);
			load(_db, new InputStreamReader(this.getClass()
					.getResourceAsStream("stadi.csv")),
					StadioMetaData.STADIO_TITLE_TABLE);
			load(_db, new InputStreamReader(this.getClass()
					.getResourceAsStream("giornate.csv")),
					GiornataMetaData.GIORNATA_TITLE_TABLE);
			load(_db, new InputStreamReader(this.getClass()
					.getResourceAsStream("partite.csv")),
					PartiteMetaData.PARTITA_TITLE_TABLE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			_db.execSQL("DROP TABLE " + StadioMetaData.STADIO_TITLE_TABLE);
			_db.execSQL(STADIO_TABLE_CREATE);
			_db.execSQL("DROP TABLE " + GiornataMetaData.GIORNATA_TITLE_TABLE);
			_db.execSQL(GIORNATA_TABLE_CREATE);
			_db.execSQL("DROP TABLE " + PartiteMetaData.PARTITA_TITLE_TABLE);
			_db.execSQL(PARTITA_TABLE_CREATE);
			onCreate(_db);
		}

	}

	// fine DbHelper

	public MyDatabase(Context ctx) {
		mContext = ctx;
		mDbHelper = new DbHelper(ctx, DB_NAME, null, DB_VERSION); // quando
																	// istanziamo
																	// questa
																	// classe,
																	// istanziamo
																	// anche
																	// l'helper
	}

	public void open() { // il database su cui agiamo è leggibile/scrivibile
		mDb = mDbHelper.getWritableDatabase();

	}

	private void load(SQLiteDatabase db, InputStreamReader in,
			String titolo_tabella) {
		BufferedReader reader = new BufferedReader(in);
		try {
			String line = null;

			while ((line = reader.readLine()) != null) {

				switch (titolo_tabella) {
				case "stadio":
					db.insert(titolo_tabella, null,
							getContentValuesStadio(line));
					break;

				case "partita":
					db.insert(titolo_tabella, null,
							getContentValuesPartita(line));
					break;
				case "giornata":
					db.insert(titolo_tabella, null,
							getContentValuesGiornata(line));
					break;
				default:
					throw new TableNotFoundException();
				}

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TableNotFoundException t) {
			t.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static ContentValues getContentValuesStadio(String value) {
		ContentValues result = new ContentValues();
		StringTokenizer stringTokenizer = new StringTokenizer(value, ";");
		result.put(StadioMetaData.STADIO_NOME_SQUADRA,
				stringTokenizer.nextToken());
		result.put(StadioMetaData.STADIO_NOME_STADIO,
				stringTokenizer.nextToken());
		result.put(StadioMetaData.STADIO_INDIRIZZO, stringTokenizer.nextToken());
		result.put(StadioMetaData.STADIO_LAT, stringTokenizer.nextToken());
		result.put(StadioMetaData.STADIO_LONG, stringTokenizer.nextToken());
		return result;
	}

	private static ContentValues getContentValuesGiornata(String value) {
		ContentValues result = new ContentValues();
		StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
		result.put(GiornataMetaData.GIORNATA_ID, stringTokenizer.nextToken());
		result.put(GiornataMetaData.GIORNATA_DATA, stringTokenizer.nextToken());
		return result;
	}

	private static ContentValues getContentValuesPartita(String value) {
		ContentValues result = new ContentValues();
		StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
		result.put(PartiteMetaData.PARTITA_ID, stringTokenizer.nextToken());
		result.put(PartiteMetaData.PARTITA_SQ_CASA, stringTokenizer.nextToken());
		result.put(PartiteMetaData.PARTITA_SQ_OSPITE,
				stringTokenizer.nextToken());
		result.put(PartiteMetaData.PARTITA_IDGIORNATA,
				stringTokenizer.nextToken());
		return result;
	}

	public void close() { // chiudiamo il database su cui agiamo
		mDb.close();
	}

	public Cursor fetchStadio() { // metodo per fare la query di tutti i dati
		return mDb.query(StadioMetaData.STADIO_TITLE_TABLE, null, null, null,
				null, null, null);
	}

	public Cursor fetchGiornata() { // metodo per fare la query di tutti i dati
		return mDb.query(GiornataMetaData.GIORNATA_TITLE_TABLE, null, null,
				null, null, null, null);
	}

	public Cursor fetchPartita() { // metodo per fare la query di tutti i dati
		return mDb.query(PartiteMetaData.PARTITA_TITLE_TABLE, null, null, null,
				null, null, null);
	}

	public Cursor query(String sql, String[] selectionArgs) {
		return mDb.rawQuery(sql, selectionArgs);
	}

	public static class StadioMetaData { // i metadati della tabella,
											// accessibili ovunque
		public static final String STADIO_TITLE_TABLE = "stadio";
		public static final String STADIO_ID = "_id";
		public static final String STADIO_NOME_SQUADRA = "nomeSquadra";
		public static final String STADIO_NOME_STADIO = "nomeStadio";
		public static final String STADIO_INDIRIZZO = "indirizzo";
		public static final String STADIO_LONG = "longitudine";
		public static final String STADIO_LAT = "latitudine";

	}

	public static class PartiteMetaData {

		public static final String PARTITA_TITLE_TABLE = "partita";
		public static final String PARTITA_ID = "_id";
		public static final String PARTITA_SQ_CASA = "squadraCasa";
		public static final String PARTITA_SQ_OSPITE = "squadraOspite";
		public static final String PARTITA_IDGIORNATA = "idGiornata";

	}

	public static class GiornataMetaData {

		public static final String GIORNATA_TITLE_TABLE = "giornata";
		public static final String GIORNATA_ID = "_id";
		public static final String GIORNATA_DATA = "dataGiornata";
	}

	private static final String STADIO_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " // codice
																					// sql
																					// di
																					// creazione
																					// della
																					// tabella
			+ StadioMetaData.STADIO_TITLE_TABLE
			+ " ("
			+ StadioMetaData.STADIO_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ StadioMetaData.STADIO_NOME_SQUADRA
			+ " VARCHAR(20) UNIQUE,"
			+ StadioMetaData.STADIO_NOME_STADIO
			+ " TEXT,"
			+ StadioMetaData.STADIO_INDIRIZZO
			+ " TEXT,"
			+ StadioMetaData.STADIO_LAT
			+ " TEXT,"
			+ StadioMetaData.STADIO_LONG
			+ " TEXT" + ");";

	private static final String GIORNATA_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ GiornataMetaData.GIORNATA_TITLE_TABLE
			+ " ("
			+ GiornataMetaData.GIORNATA_ID
			+ " INTEGER PRIMARY KEY,"
			+ GiornataMetaData.GIORNATA_DATA + " TEXT" + ");";

	private static final String PARTITA_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ PartiteMetaData.PARTITA_TITLE_TABLE
			+ " ("
			+ PartiteMetaData.PARTITA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ PartiteMetaData.PARTITA_SQ_CASA
			+ " TEXT,"
			+ PartiteMetaData.PARTITA_SQ_OSPITE
			+ " TEXT,"
			+ PartiteMetaData.PARTITA_IDGIORNATA
			+ " INTEGER, FOREIGN KEY ("
			+ PartiteMetaData.PARTITA_IDGIORNATA
			+ ") REFERENCES "
			+ GiornataMetaData.GIORNATA_TITLE_TABLE
			+ "("
			+ GiornataMetaData.GIORNATA_ID + ")" + ");";

}