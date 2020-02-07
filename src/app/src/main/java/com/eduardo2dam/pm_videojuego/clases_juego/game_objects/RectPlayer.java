//package com.eduardo2dam.pm_videojuego.clases_juego.game_objects;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.provider.SyncStateContract;
//
//import com.eduardo2dam.pm_videojuego.R;
//
//public class RectPlayer {
//
//  private Rect rectangle;
//
//  private Animation idle;
//  private Animation jumpForwardAnim;
//  private FrogPosition animManager;
//
//  public Rect getRectangle() {
//    return rectangle;
//  }
//
//  public RectPlayer(Context context, Rect rectangle) {
//    this.rectangle = rectangle;
//
//
//    BitmapFactory bf = new BitmapFactory();
//    Bitmap idleImg = bf.decodeResource(context.getResources(), R.drawable.backward_sit);
//    Bitmap jumpForward = bf.decodeResource(context.getResources(), R.drawable.forward_jump);
//    Bitmap sitForward = bf.decodeResource(context.getResources(), R.drawable.left_sit);
//
//    idle = new Animation(new Bitmap[]{idleImg}, 2);
//    jumpForwardAnim = new Animation(new Bitmap[]{jumpForward, sitForward}, 0.5f);
//
//    animManager = new FrogPosition(new Animation[]{idle, jumpForwardAnim});
//
//  }
//
//  public void draw(Canvas canvas) {
//    //Paint paint = new Paint();
//    //paint.setColor(color);
//    //canvas.drawRect(rectangle, paint);
//    animManager.draw(canvas, rectangle);
//  }
//
//  public void update() {
//    animManager.update();
//  }
//
//  public void update(Point point) {
//    float oldLeft = rectangle.left;
//
//    rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
//
//    int state = 0;
//    if (rectangle.left - oldLeft > 5)
//      state = 1;
//    else if (rectangle.left - oldLeft < -5)
//      state = 2;
//
//    animManager.playAnim(state);
//    animManager.update();
//  }
//}