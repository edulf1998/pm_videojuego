package com.eduardo2dam.pm_videojuego.clases_juego.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SQLiteService {
  private static SQLiteService _instance;
  private SQLiteOpenHelper dbHelper;

  private SQLiteService(Context c) {
    dbHelper = new DbHelper(c);
  }

  public SQLiteService getInstance(Context c) {
    if (_instance == null) {
      _instance = new SQLiteService(c);
    }
    return _instance;
  }

  public List<Object[]> obtenerPuntuaciones(Context c) {
    List<Object[]> puntuaciones = new ArrayList<>();

    SQLiteDatabase db = dbHelper.getReadableDatabase();
    Cursor dbC = db.query("Puntuaciones", new String[]{"nombre", "puntuacion"}, null, null, null, null, null, null);

    // Leer todas las puntuaciones del cursor
    if (dbC != null && dbC.moveToFirst()) {
      do {
        puntuaciones.add(
            new Object[]{dbC.getString(dbC.getColumnIndex("nombre")), dbC.getInt(dbC.getColumnIndex("puntuacion"))}
        );
      } while (dbC.moveToNext());
    }

    if (dbC != null && !dbC.isClosed()) {
      dbC.close();
    }

    return puntuaciones;
  }

  public void insertarPuntuacion(Context c, String nombre, int puntuacion) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    String q = "INSERT INTO Puntuaciones VALUES ('{0}', '{1}')";
    q = q.replace("{0}", nombre);
    q = q.replace("'{1}'", "" + puntuacion);

    try {
      db.execSQL(q);
    } catch (Exception ex) {
      Toast.makeText(c, "¡Error guardando puntuacion!", Toast.LENGTH_LONG).show();
      Log.i("GAME_ERROR", ex.toString());
    }
  }

  private class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FROGG_DROID_DB";
    private static final int DB_VERSION = 1;
    private static final String CREATE_SQL = "CREATE TABLE Puntuaciones(nombre TEXT, puntuacion INTEGER)";

    public DbHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      Log.i("GAME_INFO", "DB: onCreate()");
      sqLiteDatabase.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      Log.i("GAME_INFO", "DB: onUpgrade()");
      sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Puntuaciones");
      onCreate(sqLiteDatabase);
    }
  }
}
