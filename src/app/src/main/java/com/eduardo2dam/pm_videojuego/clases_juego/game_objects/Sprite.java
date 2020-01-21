package com.eduardo2dam.pm_videojuego.clases_juego.game_objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
  private Bitmap image;
  public int x;
  public int y;

  public Sprite() {
  }

  public Sprite(Bitmap bmp) {
    image = bmp;
    x = 0;
    y = 0;
  }

  public Sprite(Bitmap bmp, int x, int y) {
    image = bmp;
    this.x = x;
    this.y = y;
  }

  public void draw(Canvas canvas) {
    canvas.drawBitmap(image, x, y, null);
  }
}
