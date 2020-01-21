package com.eduardo2dam.pm_videojuego.clases_juego;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

  MainThread thread;
  private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
  private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

  private AssetManager aM;
  private Typeface fuentePixel;

  public GameView(Context context) {
    super(context);
    getHolder().addCallback(this);

    thread = new MainThread(getHolder(), this);
    setFocusable(true);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {


    //Logica de inicializacion
    aM = getContext().getApplicationContext().getAssets();
    fuentePixel = Typeface.createFromAsset(aM, "fonts/pixeloperator.ttf");

    thread.setRunning(true);
    thread.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    boolean retry = true;
    while (retry) {
      try {
        thread.setRunning(false);
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      retry = false;
    }
  }

  public void update() {
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    if (canvas != null) {
      canvas.drawColor(Color.WHITE);

      // Paints para distintos elementos
      Paint pinturaTexto = new Paint();
      pinturaTexto.setStyle(Paint.Style.FILL_AND_STROKE);
      pinturaTexto.setTextSize(16);
      pinturaTexto.setColor(Color.rgb(37, 41, 38));
      // pinturaTexto.setTypeface(fuentePixel);

      // Dibujar fondo




      // Mostrar FPS del juego en la esquina superior derecha
      String fpsActuales = "FPS: " + thread.getAverageFPS();
      canvas.drawText(fpsActuales, screenWidth - pinturaTexto.measureText(fpsActuales) - 24, 24, pinturaTexto);
    }
  }
}