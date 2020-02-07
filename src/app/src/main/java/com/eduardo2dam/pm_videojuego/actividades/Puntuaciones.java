package com.eduardo2dam.pm_videojuego.actividades;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.eduardo2dam.pm_videojuego.R;
import com.eduardo2dam.pm_videojuego.clases_juego.TempStorage;
import com.eduardo2dam.pm_videojuego.clases_juego.services.SQLiteService;

import java.util.ArrayList;
import java.util.List;

public class Puntuaciones extends AppCompatActivity {
  private String textoDialogo = "";
  private ListView lista;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_puntuaciones);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("¡Escribe tu nombre!");

    // Set up the input
    final EditText input = new EditText(this);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        textoDialogo = input.getText().toString();
        guardarPuntuacion();
        cargarDatos();
      }
    });
    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        cargarDatos();
      }
    });

    builder.show();
  }

  private void cargarDatos() {
    // Cargar datos en la lista
    lista = findViewById(R.id.listaPuntuaciones);
    List<Object[]> puntuaciones = SQLiteService.getInstance(getApplicationContext()).obtenerPuntuaciones(getApplicationContext());
    List<String> sPuntuaciones = new ArrayList<>();

    for (Object[] puntuacion : puntuaciones) {
      sPuntuaciones.add(String.join(" :: ", puntuacion[0].toString(), puntuacion[1].toString()));
    }

    ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sPuntuaciones);
    lista.setAdapter(adapter);
  }

  private void guardarPuntuacion() {
    int puntuacion = 0;
    puntuacion = TempStorage.puntuacion;
    SQLiteService.getInstance(getApplicationContext()).insertarPuntuacion(getApplicationContext(), textoDialogo, puntuacion);
  }
}
