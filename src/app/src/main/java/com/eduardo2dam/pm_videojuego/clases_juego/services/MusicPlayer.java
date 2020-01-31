package com.eduardo2dam.pm_videojuego.clases_juego.services;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.eduardo2dam.pm_videojuego.R;

public class MusicPlayer {
  private static MusicPlayer _instance;

  private SoundPool sfx;
  private int idCoin;

  private MediaPlayer bgm;

  private boolean play = false;

  private MusicPlayer(Context context) {
    AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
    sfx = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(attributes).build();
  }

  public static MusicPlayer getInstance(Context context) {
    if (_instance == null) {
      _instance = new MusicPlayer(context);
      _instance.cargar(context);
    }
    return _instance;
  }

  private void cargar(Context c) {
    idCoin = sfx.load(c, R.raw.sm_coin, 1);
  }

  public void playCoinSfx() {
    if (sfx != null) {
      sfx.play(idCoin, 1, 1, 0, 0, 1);
    }
  }

  public void startBgMusic(Context c) {
    bgm = MediaPlayer.create(c, R.raw.sm_coin);
    bgm.setLooping(true);
    bgm.start();
  }

  public void stopBgMusic() {
    if(bgm != null && bgm.isPlaying()) {
      bgm.stop();
    }
  }
}
