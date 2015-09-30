package com.choisaucelabs.magicconchshell;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class AudioController implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener
{
    private final String TAG = "magic_conch";

    private MainActivity main;
    private AudioManager manager;
    private MediaPlayer player;

    private boolean usedOnce;

    public AudioController(MainActivity mainActivity)
    {
        main = mainActivity;

        manager = (AudioManager) main.getSystemService(Context.AUDIO_SERVICE);

        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        usedOnce = false;
    }

    public int prepareAudio(Uri fileSource)
    {
        // request audio focus
        int result = manager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED != result)
            return -1;

        // got focus, now prepare player
        player.reset();

        try
        {
            player.setDataSource(main.getApplicationContext(), fileSource);
            player.prepare();
            usedOnce = true;
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage());
            return -1;
        }

        return player.getDuration();
    }

    public void playAudio()
    {
        // play sound
        player.start();
    }

    public void destroy()
    {
        // releasing an unused MediaPlayer causes infinite loop bug
        if(player != null && usedOnce)
        {
            player.stop();
            player.release();
        }
        player = null;
    }

    @Override
    public void onAudioFocusChange(int changeCode)
    {
        if(player == null)
            return;

        if(changeCode == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            player.pause();
        else if(changeCode == AudioManager.AUDIOFOCUS_GAIN)
            player.start();
        else if(changeCode == AudioManager.AUDIOFOCUS_LOSS)
        {
            onCompletion(player);
            manager.abandonAudioFocus(this);
        }
    }

    @Override
    public void onCompletion(MediaPlayer sourcePlayer)
    {
        sourcePlayer.stop();
    }
}
