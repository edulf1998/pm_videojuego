package com.eduardo2dam.pm_videojuego.clases_juego;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
  private SurfaceHolder surfaceHolder;
  private GameView gameView;
  private boolean running;
  public static Canvas canvas;

  private final int TARGET_FPS = 60;
  private double averageFPS;

  public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
    super();
    this.surfaceHolder = surfaceHolder;
    this.gameView = gameView;
  }

  public double getAverageFPS() {
    return averageFPS;
  }

  @Override
  public void run() {
    long startTime;
    long timeMillis;
    long waitTime;
    long totalTime = 0;
    int frameCount = 0;
    long targetTime = 1000 / TARGET_FPS;

    while (running) {
      startTime = System.nanoTime();
      canvas = null;

      try {
        canvas = this.surfaceHolder.lockCanvas();
        synchronized (surfaceHolder) {
          this.gameView.update();
          this.gameView.draw(canvas);
        }
      } catch (Exception ignored) {
      } finally {
        if (canvas != null) {
          try {
            surfaceHolder.unlockCanvasAndPost(canvas);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

      timeMillis = (System.nanoTime() - startTime) / 1000000;
      waitTime = targetTime - timeMillis;

      try {
        sleep(waitTime);
      } catch (Exception ignored) {
      }

      totalTime += System.nanoTime() - startTime;
      frameCount++;
      if (frameCount == TARGET_FPS) {
        averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
        frameCount = 0;
        totalTime = 0;

        Log.i("GAME_INFO", "FPS: " + averageFPS);
      }
    }
  }

  public void setRunning(boolean isRunning) {
    running = isRunning;
  }
}
