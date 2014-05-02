package it.unisa.followteam.database;


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

public class MyDatabase {  

        SQLiteDatabase mDb;
        DbHelper mDbHelper;	
        Context mContext;
        private static final String DB_NAME="followTeam";//nome del db
        private static final int DB_VERSION=1; //numero di versione del nostro db
        
        //classe che ci aiuta nella creazione del db
        private class DbHelper extends SQLiteOpenHelper { 

            public DbHelper(Context context, String name, CursorFactory factory,int version) {
                    super(context, name, factory, version);
            }

            @Override
            public void onCreate(SQLiteDatabase _db) { //solo quando il db viene creato, creiamo la tabella
                    _db.execSQL(STADIO_TABLE_CREATE);
                    load(_db, new InputStreamReader(this.getClass().getResourceAsStream("stadi.csv")));
                   
            }

            @Override
            public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            	_db.execSQL("DROP TABLE " + StadioMetaData.STADIO_TABLE);
           	  	_db.execSQL(STADIO_TABLE_CREATE);
            }
            

        }
        //fine DbHelper
        
        public MyDatabase(Context ctx){
                mContext=ctx;
                mDbHelper=new DbHelper(ctx, DB_NAME, null, DB_VERSION);   //quando istanziamo questa classe, istanziamo anche l'helper     
        }
        
        public void open(){  //il database su cui agiamo è leggibile/scrivibile
                mDb=mDbHelper.getWritableDatabase();
                
        }
        
        private void load(SQLiteDatabase db, InputStreamReader in) {
            BufferedReader reader = new BufferedReader(in);
            try {
                    String line = null;
                   
                    while ( (line = reader.readLine()) != null ) {
                            db.insert(StadioMetaData.STADIO_TABLE, null, getContentValues(line));
                    } 
                   
                    reader.close();
            } catch (IOException e) {
                    e.printStackTrace();
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
        
        private static ContentValues getContentValues(String value) {
            ContentValues result = new ContentValues();
            StringTokenizer stringTokenizer = new StringTokenizer(value, ";");
            result.put(StadioMetaData.STADIO_NOME_SQUADRA, stringTokenizer.nextToken());
            result.put(StadioMetaData.STADIO_NOME_STADIO, stringTokenizer.nextToken());
            result.put(StadioMetaData.STADIO_INDIRIZZO, stringTokenizer.nextToken());
            result.put(StadioMetaData.STADIO_LAT, stringTokenizer.nextToken());
            result.put(StadioMetaData.STADIO_LONG, stringTokenizer.nextToken());
            return result;
        }
        
        public void close(){ //chiudiamo il database su cui agiamo
                mDb.close();
        }
        
        
        
        public Cursor fetchStadio(){ //metodo per fare la query di tutti i dati
                return mDb.query(StadioMetaData.STADIO_TABLE, null,null,null,null,null,null);               
        }

        public static class StadioMetaData {  // i metadati della tabella, accessibili ovunque
               public static final String STADIO_TABLE = "stadio";
               public static final String STADIO_ID = "_id";
               public static final String STADIO_NOME_SQUADRA = "nomeSquadra";
               public static final String STADIO_NOME_STADIO = "nomeStadio";
               public static final String STADIO_INDIRIZZO = "indirizzo";
               public static final String STADIO_LONG = "longitudine";
               public static final String STADIO_LAT = "latitudine";
                
        }

        private static final String STADIO_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  //codice sql di creazione della tabella
                        + StadioMetaData.STADIO_TABLE + " (" 
                        + StadioMetaData.STADIO_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + StadioMetaData.STADIO_NOME_SQUADRA+ " VARCHAR(20) UNIQUE,"
                        + StadioMetaData.STADIO_NOME_STADIO + " TEXT,"
                        + StadioMetaData.STADIO_INDIRIZZO + " TEXT,"
                        + StadioMetaData.STADIO_LAT + " TEXT,"
                        + StadioMetaData.STADIO_LONG + " TEXT"
                        + ");";

        
                

}