package com.eduardo2dam.pm_videojuego.actividades;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.eduardo2dam.pm_videojuego.R;
import com.eduardo2dam.pm_videojuego.clases_juego.GameView;

public class Juego extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Pantalla Completa y Esconder Action Bar
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    getSupportActionBar().hide();
    setContentView(new GameView(getApplicationContext(), getSharedPreferences("ajustes_froggdroid", MODE_PRIVATE)));
  }

  @Override
  public void onBackPressed() {
    // super.onBackPressed();
    // Not calling **super**, disables back button in current screen.
  }
}
