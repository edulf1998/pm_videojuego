package com.eduardo2dam.pm_videojuego.actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    SharedPreferences sp = getSharedPreferences("ajustes_froggdroid", MODE_PRIVATE);
    int numVidas = (int) sp.getFloat("numVidas", 5);
    float bgmVolumen = sp.getInt("bgmVolumen", 100);
    float sfxVolumen = sp.getInt("sfxVolumen", 100);

    setContentView(new GameView(getApplicationContext(), numVidas, bgmVolumen, sfxVolumen));
  }

  @Override
  public void onBackPressed() {
    // super.onBackPressed();
    // Not calling **super**, disables back button in current screen.
  }
}
