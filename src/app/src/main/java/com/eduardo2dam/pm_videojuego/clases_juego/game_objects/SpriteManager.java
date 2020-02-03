package com.eduardo2dam.pm_videojuego.clases_juego.game_objects;

import java.util.ArrayList;

public class SpriteManager {
  private ArrayList<Sprite> sprites;

  public SpriteManager() {
    this.sprites = new ArrayList<>();
  }

  public void addSprite(Sprite sprite) {
    sprites.add(sprite);
  }

  public ArrayList<Sprite> getSprites() {
    return sprites;
  }
}
