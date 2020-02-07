package com.eduardo2dam.pm_videojuego.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eduardo2dam.pm_videojuego.R;

public class Pierde extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pierde);
  }

  public void click(View v) {
    Intent i = new Intent(getApplicationContext(), Puntuaciones.class);
    startActivity(i);
  }

  @Override
  public void onBackPressed() {
    // super.onBackPressed();
    // Not calling **super**, disables back button in current screen.
  }
}
