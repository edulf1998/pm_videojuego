package com.eduardo2dam.pm_videojuego.clases_juego.game_objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
  private Bitmap image;
  public float x;
  public float y;
  public double velocity;

  public Sprite(Bitmap bmp, float x, float y) {
    image = bmp;
    this.x = x;
    this.y = y;
  }

  public void draw(Canvas canvas) {
    canvas.drawBitmap(image, x, y, null);
  }

  public void update() {
    this.x += velocity;
  }

  public Bitmap getImage() {
    return image;
  }

  public double getVelocity() {
    return velocity;
  }

  public void setVelocity(double nuevaVelocidad) {
    this.velocity = nuevaVelocidad;
  }

  public void setVelocity(int i) {
    if (i >= 0) {
      if (i <= 2) {
        this.velocity = 16;
      } else if (i <= 4) {
        this.velocity = 19;
      } else if (i <= 6) {
        this.velocity = 11;
      } else if (i <= 8) {
        this.velocity = 9;
      } else {
        this.velocity = 12;
      }
    } else {
      if (i >= -3) {
        this.velocity = -7;
      } else if (i >= -5) {
        this.velocity = -9;
      } else if (i >= -7) {
        this.velocity = -6;
      } else if (i >= -9) {
        this.velocity = -4;
      } else {
        this.velocity = -5;
      }
    }
  }
}