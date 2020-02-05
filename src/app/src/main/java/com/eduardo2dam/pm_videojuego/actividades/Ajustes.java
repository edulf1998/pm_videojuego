package com.eduardo2dam.pm_videojuego.actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.eduardo2dam.pm_videojuego.R;

public class Ajustes extends AppCompatActivity implements Button.OnClickListener {
  private Button btnGuardar;
  private SeekBar bgmVolumen;
  private SeekBar sfxVolumen;
  private RatingBar numVidas;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ajustes);

    btnGuardar = findViewById(R.id.btnGuardar);

    bgmVolumen = findViewById(R.id.bgmVolumen);
    sfxVolumen = findViewById(R.id.sfxVolumen);

    numVidas = findViewById(R.id.numVidas);

    btnGuardar.setOnClickListener(this);

    cargarPreferencias();
  }

  private void cargarPreferencias() {
    SharedPreferences sp = getSharedPreferences("ajustes_froggdroid", MODE_PRIVATE);

    int _bgmVolumen = sp.getInt("bgmVolumen", 100);
    int _sfxVolumen = sp.getInt("sfxVolumen", 100);
    float _numVidas = sp.getFloat("numVidas", 5);

    bgmVolumen.setProgress(_bgmVolumen);
    sfxVolumen.setProgress(_sfxVolumen);
    numVidas.setRating(_numVidas);
  }

  @Override
  public void onClick(View v) {
    if (v.equals(btnGuardar)) {

      SharedPreferences sp = getSharedPreferences("ajustes_froggdroid", MODE_PRIVATE);
      SharedPreferences.Editor e = sp.edit();

      int _bgmVolumen = bgmVolumen.getProgress();
      int _sfxVolumen = sfxVolumen.getProgress();
      e.putInt("bgmVolumen", _bgmVolumen);
      e.putInt("sfxVolumen", _sfxVolumen);

      float _numVidas = numVidas.getRating();
      e.putFloat("numVidas", _numVidas);

      e.apply();

      Toast.makeText(this, "¡Ajustes guardados!", Toast.LENGTH_LONG).show();
    }
  }
}
