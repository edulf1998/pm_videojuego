package com.eduardo2dam.pm_videojuego.clases_juego;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.eduardo2dam.pm_videojuego.R;
import com.eduardo2dam.pm_videojuego.actividades.Gana;
import com.eduardo2dam.pm_videojuego.actividades.Pierde;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.Animation;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.FrogPosition;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.Container;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.Sprite;
import com.eduardo2dam.pm_videojuego.clases_juego.game_objects.SpriteManager;
import com.eduardo2dam.pm_videojuego.clases_juego.services.MusicPlayer;

import java.text.DecimalFormat;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
  private Container cars;
  private SpriteManager carsSprite;
  private Container logs;
  private SpriteManager logsSprite;
  private Container controls;
  private SpriteManager controlsSprite;
  private Container frogForward;
  private Container frogLeft;
  private Container frogRight;
  private Container frogBackwards;
  private Container water;
  private Bitmap blood;
  private Sprite bloodSprite;
  private SpriteManager bloodSprites;
  private Bitmap hearth;
  private SpriteManager hearthSprites;

  private Animation animForwardFrog;
  private Animation animLeftFrog;
  private Animation animRightFrog;
  private Animation animBackwardFrog;
  private Animation animWater;

  private int numVidas;
  private int numVidasActual;
  private boolean collision;

  int[] bmpCars = {
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

  int[] bmpControls = new int[]
      {
          R.drawable.up_control,
          R.drawable.right_control,
          R.drawable.down_control,
          R.drawable.left_control
      };

  int[] bmpForwardFrog = new int[]
      {
          R.drawable.frog_sitting_forward,
          R.drawable.frog_jumping_forward
      };

  int[] bmpRightFrog = new int[]
      {
          R.drawable.frog_sitting_right,
          R.drawable.frog_jumping_right
      };

  int[] bmpLeftFrog = new int[]
      {
          R.drawable.frog_sitting_left,
          R.drawable.frog_jumping_left
      };

  int[] bmpBackwardFrog = new int[]
      {
          R.drawable.frog_sitting_backwards,
          R.drawable.frog_jumping_backwards
      };

  int[] bmpWater = new int[]{
      R.drawable.water,
      R.drawable.water2
  };

  MainThread thread;
  public int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
  public int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

  private AssetManager aM;
  private Typeface fuentePixel;

  private int colorFondo = Color.rgb(89, 89, 89);
  private int colorTexto = Color.rgb(255, 255, 255);

  private int puntuacion;
  private DecimalFormat df;

  private float bgmVolumen, sfxVolumen;

  public GameView(Context context, int numVidas, float bgmVolumen, float sfxVolumen) {
    super(context);
    getHolder().addCallback(this);
    thread = new MainThread(getHolder(), this);
    setFocusable(true);

    cars = new Container();
    logs = new Container();
    controls = new Container();
    carsSprite = new SpriteManager();
    logsSprite = new SpriteManager();
    controlsSprite = new SpriteManager();


    frogForward = new Container();
    frogLeft = new Container();
    frogRight = new Container();
    frogBackwards = new Container();
    water = new Container();
    blood = BitmapFactory.decodeResource(getResources(), R.drawable.puddle_green);
    bloodSprites = new SpriteManager();
    hearthSprites = new SpriteManager();

    createFrogs();
    createWater();

    stablishLives();

    animForwardFrog = new Animation(frogForward.getBitmaps(), 1f);
    animLeftFrog = new Animation(frogLeft.getBitmaps(), 1f);
    animRightFrog = new Animation(frogRight.getBitmaps(), 1f);
    animBackwardFrog = new Animation(frogBackwards.getBitmaps(), 1f);
    FrogPosition.x = screenWidth / 2 - frogForward.getBitmaps().get(0).getWidth() / 2;
    FrogPosition.y = screenHeight - (screenHeight / 12 - frogForward.getBitmaps().get(0).getHeight() / 2);
    FrogPosition.state = 0;

    animWater = new Animation(water.getBitmaps(), 2f);

    this.numVidas = numVidas;
    this.numVidasActual = numVidas;

    df = new DecimalFormat("00000");

    this.bgmVolumen = bgmVolumen;
    this.sfxVolumen = sfxVolumen;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    //Logica de inicializacion
    aM = getContext().getApplicationContext().getAssets();
    fuentePixel = getResources().getFont(R.font.pixeloperator);

    carsGenerator();
    logsGenerator();
    createControls();


    thread.setRunning(true);
    thread.start();
  }

  public void stablishLives() {
    hearth = BitmapFactory.decodeResource(getResources(), R.drawable.hearth);
    int offset = screenHeight / 12;
    for (int i = 0; i <= numVidas; i++) {
      hearthSprites.addSprite(new Sprite(hearth, offset, 0));
      offset += hearth.getWidth();
    }
  }

  public void createWater() {
    water.createBitmaps(bmpWater, getContext());
  }

  public void createFrogs() {
    frogForward.createBitmaps(bmpForwardFrog, getContext());
    frogLeft.createBitmaps(bmpLeftFrog, getContext());
    frogRight.createBitmaps(bmpRightFrog, getContext());
    frogBackwards.createBitmaps(bmpBackwardFrog, getContext());
  }

  public void carsGenerator() {
    cars.createRotateBitmaps(bmpCars, 90, getContext());
    int increase = 3;
    for (int i = 0; i < cars.getBitmaps().size(); i++) {
      carsSprite.addSprite(new Sprite(cars.getBitmaps().get(i), 0 - cars.getBitmaps().get(i).getWidth(), screenHeight - ((screenHeight / 24 * increase) + (cars.getBitmaps().get(i).getHeight() / 2))));
      increase += 2;
    }
    decideVelocity(1, carsSprite);
  }

  public void logsGenerator() {
    logs.createBitmaps(bmpLogs, getContext());
    int increase = 15;
    for (int i = 0; i < logs.getBitmaps().size(); i++) {
      logsSprite.addSprite(new Sprite(logs.getBitmaps().get(i), screenWidth + logs.getBitmaps().get(i).getWidth(), screenHeight - ((screenHeight / 24 * increase) + (logs.getBitmaps().get(i).getHeight() / 2))));
      increase += 2;
    }
    decideVelocity(-1, logsSprite);
  }

  public void createControls() {
    controls.createBitmaps(bmpControls, getContext());
    controlsSprite.addSprite(new Sprite(controls.getBitmaps().get(0), screenWidth - (screenWidth / 100 * 34), screenHeight - (screenHeight / 100 * 27)));
    controlsSprite.addSprite(new Sprite(controls.getBitmaps().get(1), screenWidth - (screenWidth / 100 * 16), screenHeight - (screenHeight / 100 * 20)));
    controlsSprite.addSprite(new Sprite(controls.getBitmaps().get(2), screenWidth - (screenWidth / 100 * 34), screenHeight - (screenHeight / 100 * 10)));
    controlsSprite.addSprite(new Sprite(controls.getBitmaps().get(3), screenWidth - (screenWidth / 100 * 47), screenHeight - (screenHeight / 100 * 20)));
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
      if (coche.getX() >= screenWidth) {
        coche.setX(-coche.getImage().getWidth());
        coche.setVelocity((int) (Math.random() * 10));
      }
      coche.update();
    }

    // Mover troncos
    for (Sprite log : logsSprite.getSprites()) {
      if (log.getX() <= -log.getImage().getWidth()) {
        log.setX(screenWidth + log.getImage().getWidth());
        log.setVelocity((int) -(Math.random() * 10 + 1));
      }
      log.update();
    }

    // Actualizar rana
    if (!collision) {
      if (FrogPosition.state == 0) {
        animForwardFrog.play();
      } else if (FrogPosition.state == 1) {
        animRightFrog.play();
      } else if (FrogPosition.state == 2) {
        animBackwardFrog.play();
      } else if (FrogPosition.state == 3) {
        animLeftFrog.play();
      }
    } else {
      collision();
    }


    // Animar agua
    animWater.play();

    // Comprobar si la rana está arriba
    if(FrogPosition.y <= screenHeight / 12) {
      redirectTo(Gana.class);
    }
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    if (canvas != null) {
      collision = false;
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
      int offset = (screenHeight / numRayas) * 6;
      for (int i = 5; i < numRayas; i++) {
        canvas.drawRect(new Rect(0, offset, screenWidth, 10 + offset), pinturaTexto);

        offset += (screenHeight / numRayas);
      }

      // Dibujar agua
      animWater.draw(canvas, 0, 0 - (screenHeight / 100 * 84));

      // Dibujar coches
      for (Sprite s : carsSprite.getSprites()) {
        s.draw(canvas);
        if (FrogPosition.x > s.getX() && FrogPosition.x < s.getX() + s.getImage().getWidth()
            && FrogPosition.y > s.getY() && FrogPosition.y < s.getY() + s.getImage().getHeight()) {
          collision = true;
          bloodSprite = new Sprite(blood, FrogPosition.x, FrogPosition.y);
          bloodSprites.addSprite(bloodSprite);
        }
      }

      // Dibujar troncos
      for (Sprite s : logsSprite.getSprites()) {
        s.draw(canvas);
        if (FrogPosition.y <= logsSprite.getSprites().get(0).getY() + logsSprite.getSprites().get(0).getImage().getHeight()) {
          if (FrogPosition.x < s.getX() && FrogPosition.x < s.getX() + s.getImage().getWidth()
              && FrogPosition.y > s.getY() && FrogPosition.y < s.getY() + s.getImage().getHeight()) {
            collision = true;
            bloodSprite = new Sprite(blood, FrogPosition.x, FrogPosition.y);
            bloodSprites.addSprite(bloodSprite);
          }
        }

      }

      for (Sprite s : bloodSprites.getSprites()) {
        s.draw(canvas);
      }

      for (Sprite s : controlsSprite.getSprites()) {
        s.draw(canvas);
      }

      // Dibujar rana
      if (FrogPosition.state == 0) {
        animForwardFrog.draw(canvas, FrogPosition.x, FrogPosition.y);
      } else if (FrogPosition.state == 1) {
        animRightFrog.draw(canvas, FrogPosition.x, FrogPosition.y);
      } else if (FrogPosition.state == 2) {
        animBackwardFrog.draw(canvas, FrogPosition.x, FrogPosition.y);
      } else if (FrogPosition.state == 3) {
        animLeftFrog.draw(canvas, FrogPosition.x, FrogPosition.y);
      }

      // Mostrar vidas
      for (Sprite s : hearthSprites.getSprites()) {
        s.draw(canvas);
      }

      // Mostrar FPS del juego en la esquina superior derecha
      String fpsActuales = "FPS: " + thread.getAverageFPS();
      canvas.drawText(fpsActuales, screenWidth - pinturaTexto.measureText(fpsActuales) - (float) screenWidth / 20, (float) (screenHeight / 20), pinturaTexto);

      // Mostrar puntuación en la esquina superior izquierda
      String sPuntuacion = "Puntos: " + df.format(this.puntuacion);
      canvas.drawText(sPuntuacion, 15, (float) (screenHeight / 20), pinturaTexto);
    }
  }

  private void collision() {
    // Quitar una vida al jugador.
    this.numVidasActual--;
    if (this.numVidasActual <= 0) {
      // Jugador pierde...
      redirectTo(Pierde.class);

    } else {
      // Devolver jugador a posicion inicial
      FrogPosition.x = screenWidth / 2 - frogForward.getBitmaps().get(0).getWidth() / 2;
      FrogPosition.y = screenHeight - (screenHeight / 12 - frogForward.getBitmaps().get(0).getHeight() / 2);
      FrogPosition.state = 0;
      hearthSprites.getSprites().remove(0);
    }
  }

  private void redirectTo(Class c) {
    Intent i = new Intent(getContext(), c);
    i.addFlags(FLAG_ACTIVITY_NEW_TASK); // Si no se especifica, crashea porque salta una excepción!

    getContext().startActivity(i);
    thread.setRunning(false);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    MusicPlayer.getInstance(getContext(), bgmVolumen, sfxVolumen).playCoinSfx();

    if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
      if (event.getX() >= controlsSprite.getSprites().get(0).getX() &&
          event.getX() <= controlsSprite.getSprites().get(0).getX() + controlsSprite.getSprites().get(0).getImage().getWidth()
          && event.getY() >= controlsSprite.getSprites().get(0).getY() &&
          event.getY() <= (controlsSprite.getSprites().get(0).getY() + controlsSprite.getSprites().get(0).getImage().getHeight())) {
        FrogPosition.y -= screenHeight / 12;
        FrogPosition.state = 0;

      } else if (event.getX() > controlsSprite.getSprites().get(1).getX() &&
          event.getX() < controlsSprite.getSprites().get(1).getX() + controlsSprite.getSprites().get(1).getImage().getWidth()
          && event.getY() > controlsSprite.getSprites().get(1).getY() &&
          event.getY() < (controlsSprite.getSprites().get(1).getY() + controlsSprite.getSprites().get(1).getImage().getHeight())) {
        if (!((FrogPosition.x + screenWidth / 8) >= screenWidth - controlsSprite.getSprites().get(1).getImage().getWidth())) {
          FrogPosition.x += screenWidth / 8;
        }
        FrogPosition.state = 1;

      } else if (event.getX() > controlsSprite.getSprites().get(2).getX() &&
          event.getX() < controlsSprite.getSprites().get(2).getX() + controlsSprite.getSprites().get(2).getImage().getWidth()
          && event.getY() > controlsSprite.getSprites().get(2).getY() &&
          event.getY() < (controlsSprite.getSprites().get(2).getY() + controlsSprite.getSprites().get(2).getImage().getHeight())) {
        if (!((FrogPosition.y + screenHeight / 12) >= screenHeight)) {
          FrogPosition.y += screenHeight / 12;
        }

        FrogPosition.state = 2;

      } else if (event.getX() > controlsSprite.getSprites().get(3).getX() &&
          event.getX() < controlsSprite.getSprites().get(3).getX() + controlsSprite.getSprites().get(3).getImage().getWidth()
          && event.getY() > controlsSprite.getSprites().get(3).getY() &&
          event.getY() < (controlsSprite.getSprites().get(3).getY() + controlsSprite.getSprites().get(3).getImage().getHeight())) {
        if (!((FrogPosition.x - screenWidth / 8) <= 0)) {
          FrogPosition.x -= screenWidth / 8;
        }
        FrogPosition.state = 3;

      }
    }

    return true;
  }

  private void cambiarPuntuacion(int puntos) {
    this.puntuacion += puntos;
    if (puntuacion < 0) {
      puntuacion = 0;
    }
  }
}