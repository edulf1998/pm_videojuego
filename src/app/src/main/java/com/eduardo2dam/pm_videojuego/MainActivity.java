package com.eduardo2dam.pm_videojuego;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.eduardo2dam.pm_videojuego.actividades.Ajustes;
import com.eduardo2dam.pm_videojuego.actividades.Juego;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  /**
   * Este método se ejecuta al pulsar uno de los botones en pantalla.
   * Permite iniciar el juego o los ajustes lanzando un Intent con la actividad necesaria, extraída del ID del propio botón clickado.
   *
   * @param v el botón pulsado
   */
  protected void navegarMenu(View v) {
    Intent cambioActividad = null;
    switch (((Button) v).getId()) {
      case R.id.botonJugar:
        cambioActividad = new Intent(this, Juego.class);
        break;
      case R.id.botonAjustes:
        cambioActividad = new Intent(this, Ajustes.class);
        break;
    }

    startActivity(cambioActividad);
  }
}
