package com.example.android.templates;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.miwok.Word;

import java.util.ArrayList;

public abstract class MiwokActivityTemplate extends AppCompatActivity{

    public MediaPlayer translationPlayer = null;
    public ArrayList<Word> words = new ArrayList<>();
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = (int focusChange) -> {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                //resume your sound
                translationPlayer.start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                //Stop playing the sound
                releaseMediaPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //Pause playing the sound
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //Pause playing the sound
                translationPlayer.pause();
                translationPlayer.seekTo(0);
                break;
            default:
                //
        }
    };


    public void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (translationPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            translationPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            translationPlayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public void setWordOnClick(ListView listView, Context context) {
        listView.setOnItemClickListener((AdapterView<?> adapterView, View v, int position, long l) -> {
            Word word = words.get(position);
            releaseMediaPlayer();
            if (requestAudioFocus(context)) {
                translationPlayer = MediaPlayer.create(context, word.getSoundId());
                translationPlayer.start();
                translationPlayer.setOnCompletionListener( (MediaPlayer mp) -> releaseMediaPlayer());
            }
        });
    }

    private boolean requestAudioFocus(final Context context) {
        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        // Request audio focus for playback
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request transient focus.
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        return (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }
}
