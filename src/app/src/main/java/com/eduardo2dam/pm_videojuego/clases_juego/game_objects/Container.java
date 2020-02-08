package com.eduardo2dam.pm_videojuego.clases_juego.game_objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Container {
  private ArrayList<Bitmap> bitmaps;

  public Container() {
    this.bitmaps = new ArrayList<>();
  }

  public void createRotateBitmaps(int[] draws, int rotation, Context context) {
    Matrix m = new Matrix();
    m.postRotate(rotation);

    Bitmap bitmap;
    for (int res : draws) {
      bitmap = BitmapFactory.decodeResource(context.getResources(), res);
      bitmaps.add(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false));
    }
  }

  public void createBitmaps(int[] draws, Context context) {
    Bitmap bitmap;
    for (int res : draws) {
      bitmap = BitmapFactory.decodeResource(context.getResources(), res);
      bitmaps.add(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight()));
    }
  }

  public ArrayList<Bitmap> getBitmaps() {
    return bitmaps;
  }

  public void draw() {

  }

  public void update() {

  }

  public void setBitmaps(ArrayList<Bitmap> bitmaps) {
    this.bitmaps = bitmaps;
  }
}