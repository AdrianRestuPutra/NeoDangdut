package com.pitados.neodangdut.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.SeekBar;

import com.devbrackets.android.exomedia.EMAudioPlayer;
import com.devbrackets.android.exomedia.EMVideoView;
import com.devbrackets.android.exomedia.event.EMMediaProgressEvent;
import com.devbrackets.android.exomedia.listener.EMPlaylistServiceCallback;
import com.devbrackets.android.exomedia.listener.EMProgressCallback;
import com.devbrackets.android.exomedia.manager.EMPlaylistManager;
import com.devbrackets.android.exomedia.service.EMPlaylistService;
import com.pitados.neodangdut.R;

/**
 * Created by adrianrestuputranto on 4/16/16.
 */
public class CustomMediaPlayer implements EMPlaylistServiceCallback, EMProgressCallback {

    public static CustomMediaPlayer instance;

    // Panel
    private View panelMusicPlayer, panelVideoPlayer;

    // Media Player
    private Context context;
    private EMAudioPlayer audioPlayer;
    private SeekBar audioProgress;
    // TODO audio player pause
    // TODO audio player shuffle
    // TODO audio player loop
    private EMVideoView videoPlayer;

    public static CustomMediaPlayer getInstance() {
        if(instance == null)
            instance = new CustomMediaPlayer();
        return instance;
    }

    public CustomMediaPlayer() {

    }

    public void setAudioPanel(Context context, final View panelMusicPlayer, SeekBar audioProgress) {
        this.panelMusicPlayer = panelMusicPlayer;
        this.context = context;

        audioPlayer = new EMAudioPlayer(context);
        this.audioProgress = audioProgress;
    }

    public void setVideoPanel(View panelVideoPlayer, final EMVideoView videoPlayer) {
        this.panelVideoPlayer = panelVideoPlayer;
        this.videoPlayer = videoPlayer;
    }

    public void playTrack(String url) {
        if (panelMusicPlayer.getVisibility() != View.VISIBLE) {
                    showMusicPlayer();

        }
        // TODO set panel music player default / loading
        panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        if(audioPlayer.isPlaying())
            audioPlayer.stopPlayback();
        audioPlayer.setDataSource(context, Uri.parse(url));
        audioPlayer.prepareAsync();
        audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                audioPlayer.start();
                panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));

                // TODO change audio data
                int duration = (int)audioPlayer.getDuration() / 1000;
                audioProgress.setMax(duration);
            }
        });

        final Handler mHandler = new Handler();
        Activity currentActivity =  (Activity) context;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(audioPlayer != null) {
                    int mCurrentPos = (int)audioPlayer.getCurrentPosition() / 1000;
                    audioProgress.setProgress(mCurrentPos);
                }

                mHandler.postDelayed(this, 1000);
            }
        });

        audioProgress.setThumb(ContextCompat.getDrawable(context, R.color.transparent));
        audioProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pos, boolean fromUser) {
                if(audioPlayer != null && fromUser) {
                    audioPlayer.seekTo(pos * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO set seek thumb
//                seekBar.setThumb(ContextCompat.getDrawable(context, R.color.colorPrimary));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO set seek thumb transparent
//                seekBar.setThumb(ContextCompat.getDrawable(context, R.color.colorPrimary));
            }
        });
    }

    public void stopTrack() {
        closeMusicPlayer();
        audioPlayer.stopPlayback();
    }

    public long getCurrentPos() {
        return audioPlayer.getCurrentPosition();
    }

    public void playVideo(String url) {
        showVideoPlayer();
        videoPlayer.setVideoURI(Uri.parse(url));
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO close buffering indicator
                videoPlayer.start();
            }
        });
    }

    // Animate panel
    private void showMusicPlayer() {
        TranslateAnimation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                panelMusicPlayer.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translate.setDuration(200);
        translate.setFillAfter(true);
        panelMusicPlayer.startAnimation(translate);
    }

    private void closeMusicPlayer() {
        TranslateAnimation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                panelMusicPlayer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translate.setDuration(200);
        translate.setFillAfter(true);
        panelMusicPlayer.startAnimation(translate);
    }

    private void showVideoPlayer() {
        panelVideoPlayer.setVisibility(View.VISIBLE);
    }

    public void closeVideoPlayer() {
        panelVideoPlayer.setVisibility(View.INVISIBLE);
        videoPlayer.stopPlayback();
    }

    @Override
    public boolean onPlaylistItemChanged(EMPlaylistManager.PlaylistItem currentItem, boolean hasNext, boolean hasPrevious) {
        return false;
    }

    @Override
    public boolean onMediaStateChanged(EMPlaylistService.MediaState mediaState) {
        return false;
    }

    @Override
    public boolean onProgressUpdated(EMMediaProgressEvent event) {
        return false;
    }

}
