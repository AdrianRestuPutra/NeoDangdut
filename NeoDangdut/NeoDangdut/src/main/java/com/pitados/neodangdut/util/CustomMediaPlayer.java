package com.pitados.neodangdut.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMAudioPlayer;
import com.devbrackets.android.exomedia.EMVideoView;
import com.devbrackets.android.exomedia.event.EMMediaProgressEvent;
import com.devbrackets.android.exomedia.listener.EMPlaylistServiceCallback;
import com.devbrackets.android.exomedia.listener.EMProgressCallback;
import com.devbrackets.android.exomedia.manager.EMPlaylistManager;
import com.devbrackets.android.exomedia.service.EMPlaylistService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.NewsData;
import com.pitados.neodangdut.model.VideoData;

/**
 * Created by adrianrestuputranto on 4/16/16.
 */
public class CustomMediaPlayer implements EMPlaylistServiceCallback, EMProgressCallback {

    public static CustomMediaPlayer instance;

    // Panel
    private View panelMusicPlayer, panelVideoPlayer, panelNewsDetail;

    // Media Player
    private Context context;
    private EMAudioPlayer audioPlayer;
    private SeekBar audioProgress;
    // TODO audio player pause
    // TODO audio player shuffle
    // TODO audio player loop

    // Data
    // Audio
    private ImageView audioPlayerThumbnail, audioPauseIcon;
    private TextView audioPlayerSongTitle, audioPlayerArtistName, audioPlayerDuration;
    // Video
    private EMVideoView videoPlayer;
    private TextView videoDate, videoDescription;
    // News
    private ImageView newsThumbnail;
    private TextView newsDetailTitle, newsDetailDate, newsDescription;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    public boolean isVideoPlayerShowing, isAudioPlayerShowing, isNewsDetailShowing;

    public static CustomMediaPlayer getInstance() {
        if(instance == null)
            instance = new CustomMediaPlayer();
        return instance;
    }

    public CustomMediaPlayer() {
        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        isVideoPlayerShowing = isAudioPlayerShowing = false;
    }

    public void setAudioPanel(Context context, final View panelMusicPlayer, SeekBar audioProgress, TextView duration,
                              ImageView thumbnailContainer, TextView songTitleContainer, TextView artistNameContainer, ImageView audioPauseIcon) {
        this.panelMusicPlayer = panelMusicPlayer;
        this.context = context;

        audioPlayerDuration = duration;
        audioPlayerThumbnail = thumbnailContainer;
        audioPlayerSongTitle = songTitleContainer;
        audioPlayerArtistName = artistNameContainer;
        this.audioPauseIcon = audioPauseIcon;

        audioPlayer = new EMAudioPlayer(context);
        this.audioProgress = audioProgress;
    }

    public void setVideoPanel(View panelVideoPlayer, final EMVideoView videoPlayer, TextView videoDate, TextView videoDescription) {
        this.panelVideoPlayer = panelVideoPlayer;
        this.videoPlayer = videoPlayer;
        this.videoDate = videoDate;
        this.videoDescription = videoDescription;
    }

    public void setNewsPanel(View panelNewsDetail, ImageView newsThumbnail, TextView newsDetailTitle, TextView newsDetailDate, TextView newsDescription) {
        this.panelNewsDetail = panelNewsDetail;
        this.newsThumbnail = newsThumbnail;
        this.newsDetailTitle = newsDetailTitle;
        this.newsDetailDate = newsDetailDate;
        this.newsDescription = newsDescription;
    }

    public void playTrack(final MusicData data, boolean isPreview) {
        if (panelMusicPlayer.getVisibility() != View.VISIBLE) {
                    showMusicPlayer();

        }
        // TODO set panel music player default / loading
        panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
        audioPauseIcon.setImageResource(R.drawable.icon_play);

        if(audioPlayer.isPlaying())
            audioPlayer.stopPlayback();
        if(isPreview) {
            audioPlayer.setDataSource(context, Uri.parse(data.previewURL));
        } else {
            // TODO
        }
        audioPlayer.prepareAsync();
        audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                audioPlayer.start();
                panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
                audioPauseIcon.setImageResource(R.drawable.icon_pause);

                // TODO change audio data
                imageLoader.displayImage(data.albumCover, audioPlayerThumbnail, opts);
                audioPlayerSongTitle.setText(data.songTitle);
                audioPlayerArtistName.setText(data.singerName);
                audioPlayerDuration.setText(data.duration);

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

    public void playVideo(VideoData videoData) {
        showVideoPlayer();
        videoPlayer.setVideoURI(Uri.parse(videoData.previewURL)); // TODO change url
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO close buffering indicator
                videoPlayer.start();
            }
        });

        videoDate.setText(videoData.videoTitle);
        if(videoData.description.length() > 0)
            videoDescription.setText(videoData.description);
    }

    public void playVideo(CommunityContentData videoData) {
        showVideoPlayer();
        videoPlayer.setVideoURI(Uri.parse(videoData.fileURL));
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO close buffering indicator
                videoPlayer.start();
            }
        });

        videoDate.setText(videoData.songName);
        videoDescription.setText(videoData.description);
    }

    // Animate panel
    private void showMusicPlayer() {
        TranslateAnimation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                panelMusicPlayer.setVisibility(View.VISIBLE);
                isAudioPlayerShowing = true;
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
                isAudioPlayerShowing = false;
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
        isVideoPlayerShowing = true;
    }

    public void closeVideoPlayer() {
        panelVideoPlayer.setVisibility(View.INVISIBLE);
        videoPlayer.stopPlayback();
        isVideoPlayerShowing = false;
    }

    public void showNewsDetail(NewsData newsData) {
        panelNewsDetail.setVisibility(View.VISIBLE);

        imageLoader.displayImage(newsData.thumbnailURL, newsThumbnail, opts);
        newsDetailTitle.setText(newsData.title);
        newsDetailDate.setText(newsData.created);
        newsDescription.setText(newsData.content);

        isNewsDetailShowing = true;
    }

    public void closeNewsDetail() {
        panelNewsDetail.setVisibility(View.GONE);

        isNewsDetailShowing = false;
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
