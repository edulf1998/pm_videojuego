package com.eduardo2dam.pm_videojuego.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eduardo2dam.pm_videojuego.R;

public class Gana extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gana);
  }

  public void click(View v) {
    Intent i = new Intent(getApplicationContext(), Puntuaciones.class);
    startActivity(i);
  }
}
