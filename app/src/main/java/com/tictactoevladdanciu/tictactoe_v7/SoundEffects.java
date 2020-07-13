package com.tictactoevladdanciu.tictactoe_v7;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

public class SoundEffects {
    private  Context context;
    private SoundPool soundPool;
    private int sword_scrape, swipe_sound_button, victory_shout, draw, victory_music;

    public SoundEffects(Context context){
        this.context = context;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }

        sword_scrape = soundPool.load(this.context, R.raw.sword_scrape, 1);
        swipe_sound_button = soundPool.load(this.context, R.raw.swipe_sound_button, 1);
        victory_shout = soundPool.load(this.context, R.raw.victory_shout, 1);
        draw = soundPool.load(this.context, R.raw.draw, 1);
        victory_music = soundPool.load(this.context, R.raw.victory_music, 1);
    }

    public void playSwordScrape(){
        soundPool.play(sword_scrape,1,1,0,0,1);
        //onDestroy();
    }

    public void playSwipeSoundButton(){
        soundPool.play(swipe_sound_button,1,1,0,0,1);
    }

    public void playVictoryShout(){
        soundPool.play(victory_shout,1,1,0,0,1);
    }

    public void playDraw(){
        soundPool.play(draw,1,1,0,0,1);
    }

    public void playVictoryMusic(){
        soundPool.play(victory_music,1,1,0,0,1);
    }


    protected void onDestroy(){
        soundPool.release();
        soundPool = null;
    }
}
