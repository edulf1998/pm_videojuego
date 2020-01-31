package com.eduardo2dam.pm_videojuego.clases_juego;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.eduardo2dam.pm_videojuego.R;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.Sprite;
import com.eduardo2dam.pm_videojuego.clases_juego.services.MusicPlayer;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener {
  ArrayList<Bitmap> coches;
  ArrayList<Sprite> cochesSprite;

  int[] bmpCoches = new int[]
      {
          R.drawable.coche_amarillo,
          R.drawable.coche_azul,
          R.drawable.coche_morado,
          R.drawable.coche_rojo,
          R.drawable.coche_verde
      };

  MainThread thread;
  private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
  private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

  private AssetManager aM;
  private Typeface fuentePixel;

  private int colorFondo = Color.rgb(89, 89, 89);
  private int colorTexto = Color.rgb(255, 255, 255);

  public GameView(Context context) {
    super(context);
    getHolder().addCallback(this);
    thread = new MainThread(getHolder(), this);
    setFocusable(true);

    coches = new ArrayList<>();
    cochesSprite = new ArrayList<>();
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    //Logica de inicializacion
    aM = getContext().getApplicationContext().getAssets();
    fuentePixel = getResources().getFont(R.font.pixeloperator);

    // Añadir coches a la lista y crear los sprite adecuado
    Matrix m = new Matrix();
    m.postRotate(90);

    Bitmap bmp;
    for (int res : bmpCoches) {
      bmp = BitmapFactory.decodeResource(getResources(), res);
      coches.add(Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false));
      cochesSprite.add(new Sprite(bmp, 500, 500));
    }

    thread.setRunning(true);
    thread.start();

    setOnTouchListener(this);

    // Musica Fondo
    MusicPlayer.getInstance(getContext()).startBgMusic(getContext());
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
    // Mover coches
    for (Sprite s : cochesSprite) {
      s.x += 0.5;
    }
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    if (canvas != null) {
      canvas.drawColor(Color.WHITE);

      // Paints para distintos elementos
      Paint pinturaTexto = new Paint();
      pinturaTexto.setStyle(Paint.Style.FILL_AND_STROKE);
      pinturaTexto.setTextSize(64);
      pinturaTexto.setColor(colorTexto);
      pinturaTexto.setTypeface(fuentePixel);

      // Dibujar fondo
      Paint pinturaFondo = new Paint();
      pinturaFondo.setColor(colorFondo);
      pinturaFondo.setStyle(Paint.Style.FILL_AND_STROKE);

      canvas.drawRect(new Rect(0, 0, screenWidth, screenHeight), pinturaFondo);

      // Dibujar rayas carretera
      int numRayas = 10;
      int offset = (screenWidth / 5);
      for (int i = 0; i < numRayas; i++) {
        canvas.drawRect(new Rect(0, offset, screenWidth, 10 + offset), pinturaTexto);
        offset += (screenWidth / 5);
      }

      // Dibujar coches
      for (Sprite s : cochesSprite) {
        s.draw(canvas);
      }

      // Mostrar FPS del juego en la esquina superior derecha
      String fpsActuales = "FPS: " + thread.getAverageFPS();
      canvas.drawText(fpsActuales, screenWidth - pinturaTexto.measureText(fpsActuales) - (screenWidth / 20), (screenHeight / 20), pinturaTexto);
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    MusicPlayer.getInstance(getContext()).playCoinSfx();
    return false;
  }
}
