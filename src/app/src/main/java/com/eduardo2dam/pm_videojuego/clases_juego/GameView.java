package com.eduardo2dam.pm_videojuego.clases_juego;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.eduardo2dam.pm_videojuego.MainActivity;
import com.eduardo2dam.pm_videojuego.R;
import com.eduardo2dam.pm_videojuego.actividades.Gana;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.Container;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.Sprite;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.SpriteManager;
import com.eduardo2dam.pm_videojuego.clases_juego.services.MusicPlayer;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
  private Container cars;
  private SpriteManager carsSprite;
  private Container logs;
  private SpriteManager logsSprite;

  private SharedPreferences sp;
  private int numVidas;
  private int numVidasActual;

  int[] bmpCars = new int[]
      {
          R.drawable.coche_amarillo,
          R.drawable.coche_azul,
          R.drawable.coche_morado,
          R.drawable.coche_rojo,
          R.drawable.coche_verde
      };

  int[] bmpLogs = new int[]
      {
          R.drawable.large_log,
          R.drawable.short_log,
          R.drawable.medium_log,
          R.drawable.big_log
      };

  MainThread thread;
  public int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
  public int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

  private AssetManager aM;
  private Typeface fuentePixel;

  private int colorFondo = Color.rgb(89, 89, 89);
  private int colorTexto = Color.rgb(255, 255, 255);

  public GameView(Context context, SharedPreferences sp) {
    super(context);
    getHolder().addCallback(this);
    thread = new MainThread(getHolder(), this);
    setFocusable(true);

    cars = new Container();
    logs = new Container();
    carsSprite = new SpriteManager();
    logsSprite = new SpriteManager();

    this.sp = sp;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

    //Logica de inicializacion
    aM = getContext().getApplicationContext().getAssets();
    fuentePixel = getResources().getFont(R.font.pixeloperator);

    carsGenerator();
    logsGenerator();

    thread.setRunning(true);
    thread.start();
  }

  public void carsGenerator() {
    cars.createRotateBitmaps(bmpCars, 90, this);
    int increase = 3;
    for (int i = 0; i < cars.getBitmaps().size(); i++) {
      carsSprite.addSprite(new Sprite(cars.getBitmaps().get(i), 0 - cars.getBitmaps().get(i).getWidth(), screenHeight - ((screenHeight / 24 * increase) + (cars.getBitmaps().get(i).getHeight() / 2))));
      increase += 2;
    }
    decideVelocity(1, carsSprite);
  }

  public void logsGenerator() {
    logs.createBitmaps(bmpLogs, this);
    int increase = 15;
    for (int i = 0; i < logs.getBitmaps().size(); i++) {
      logsSprite.addSprite(new Sprite(logs.getBitmaps().get(i), screenWidth + logs.getBitmaps().get(i).getWidth(), screenHeight - ((screenHeight / 24 * increase) + (logs.getBitmaps().get(i).getHeight() / 2))));
      increase += 2;
    }
    decideVelocity(-1, logsSprite);
  }

  public void decideVelocity(int i, SpriteManager sprites) {
    for (Sprite s : sprites.getSprites()) {
      s.setVelocity(i);
      if (i >= 0) {
        i += 2;
      } else {
        i -= 2;
      }
    }
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
    for (Sprite coche : carsSprite.getSprites()) {
      if (coche.x >= screenWidth) {
        coche.x = -coche.getImage().getWidth();
        coche.setVelocity((int) (Math.random() * 10));
      }
      coche.update();
    }

    // Mover troncos
    for (Sprite log : logsSprite.getSprites()) {
      if (log.x <= -log.getImage().getWidth()) {
        log.x = screenWidth + log.getImage().getWidth();
        log.setVelocity((int) -(Math.random() * 10 + 1));
      }
      log.update();
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
      int numRayas = 12;
      int offset = 0;
      for (int i = 0; i < numRayas; i++) {
        canvas.drawRect(new Rect(0, offset, screenWidth, 10 + offset), pinturaTexto);

        offset += (screenHeight / numRayas);
      }

      // Dibujar coches
      for (Sprite s : carsSprite.getSprites()) {
        s.draw(canvas);
      }

      // Dibujar troncos
      for (Sprite s : logsSprite.getSprites()) {
        s.draw(canvas);
      }

      // Mostrar FPS del juego en la esquina superior derecha
      String fpsActuales = "FPS: " + thread.getAverageFPS();
      canvas.drawText(fpsActuales, screenWidth - pinturaTexto.measureText(fpsActuales) - (float) screenWidth / 20, (float) (screenHeight / 20), pinturaTexto);
    }
  }

  private void collision() {
    // Quitar una vida al jugador.
    this.numVidasActual--;
    if (this.numVidasActual <= 0) {
      // Jugador pierde...
      redirectTo(Gana.class);

    } else {
      // Devolver jugador a posicion inicial

      // Si el jugador ya está en la pantalla, asignar su X e Y.
      // Si no está en la pantalla, hacerle aparecer en X e Y.
    }
  }

  private void redirectTo(Class c) {
    Intent i = new Intent(getContext(), c);
    i.addFlags(FLAG_ACTIVITY_NEW_TASK); // Si no se especifica, crashea porque salta una excepción!

    getContext().startActivity(i);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    Toast.makeText(getContext(), "Click!", Toast.LENGTH_LONG).show();
    MusicPlayer.getInstance(getContext(), sp).playCoinSfx();

    return false;
  }
}
