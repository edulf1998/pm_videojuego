package com.eduardo2dam.pm_videojuego.clases_juego.game_objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Animation {
    private ArrayList<Bitmap> frames;
    private int frameIndex;

    private float frameTime;
    private long lastFrame;
    int x = 0;
    int y = 0;


    public Animation(ArrayList<Bitmap> frames, float animTime) {
        this.frames = frames;
        frameIndex = 0;

        frameTime = animTime / frames.size();

        lastFrame = System.currentTimeMillis();
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void play() {
        if (frameIndex >= frames.size()) {
            stop();
        } else {
            if (System.currentTimeMillis() - lastFrame > frameTime * 1000) {
                frameIndex++;
                lastFrame = System.currentTimeMillis();
            }
        }
    }

    public void playOnce() {
        if (frameIndex < frames.size()) {
            if (System.currentTimeMillis() - lastFrame > frameTime * 1000) {
                frameIndex++;
                lastFrame = System.currentTimeMillis();
            }
        }

    }

    public void stop() {
        frameIndex = 0;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(frames.get(frameIndex), x, y, null);
    }

}
