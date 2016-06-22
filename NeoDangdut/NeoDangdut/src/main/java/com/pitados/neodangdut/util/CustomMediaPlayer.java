package com.pitados.neodangdut.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.AlbumItem;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.NewsData;
import com.pitados.neodangdut.model.UserLoginData;
import com.pitados.neodangdut.model.VideoData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adrianrestuputranto on 4/16/16.
 */
public class CustomMediaPlayer implements EMPlaylistServiceCallback, EMProgressCallback {
    public enum MusicType {
        SHOP,
        COMMUNITY,
        LIBRARY,
        ALBUM_ITEM
    }
    private MusicType trackType;
    private MusicData selectedMusicData;
    private CommunityContentData selectedCommunityData;
    private LibraryData selectedLibraryData;
    private AlbumItem selectedAlbumItemData;

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
    private ImageView audioPlayerThumbnail, audioPauseIcon, audioShuffleIcon, audioRepeatIcon, audioInfoIcon;
    private TextView audioPlayerSongTitle, audioPlayerArtistName, audioPlayerDuration;
    // Video
    private EMVideoView videoPlayer;
    private ImageView videoThumbnail;
    private TextView videoDate, videoDescription;
    // News
    private ImageView newsThumbnail;
    private TextView newsDetailTitle, newsDetailDate, newsDescription;
    private RelativeLayout loadingBar;
    private ScrollView newsScrollView;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    public boolean isVideoPlayerShowing, isAudioPlayerShowing, isNewsDetailShowing;

    private UserLoginData userLoginData;
    private NotificationManager notifManager;
    private NotificationCompat.Builder mBuilder;

    public static CustomMediaPlayer getInstance() {
        if(instance == null)
            instance = new CustomMediaPlayer();
        return instance;
    }

    public CustomMediaPlayer() {
        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        isVideoPlayerShowing = isAudioPlayerShowing = false;
    }

    public void setAudioPanel(Context context, final View panelMusicPlayer, SeekBar audioProgress, TextView duration,
                              ImageView thumbnailContainer, TextView songTitleContainer, TextView artistNameContainer,
                              ImageView audioPauseIcon) {
        this.panelMusicPlayer = panelMusicPlayer;
        this.context = context;

        notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);

        audioPlayerDuration = duration;
        audioPlayerThumbnail = thumbnailContainer;
        audioPlayerSongTitle = songTitleContainer;
        audioPlayerArtistName = artistNameContainer;
        this.audioPauseIcon = audioPauseIcon;

        audioPlayer = new EMAudioPlayer(context);
        this.audioProgress = audioProgress;
    }

    public void setVideoPanel(View panelVideoPlayer, final EMVideoView videoPlayer, TextView videoDate, TextView videoDescription, ImageView videoThumbnail) {
        this.panelVideoPlayer = panelVideoPlayer;
        this.videoPlayer = videoPlayer;
        this.videoDate = videoDate;
        this.videoDescription = videoDescription;
        this.videoThumbnail = videoThumbnail;
    }

    public void setNewsPanel(View panelNewsDetail, ImageView newsThumbnail, TextView newsDetailTitle, TextView newsDetailDate, TextView newsDescription, RelativeLayout loadingBar, ScrollView newsScrollView) {
        this.panelNewsDetail = panelNewsDetail;
        this.newsThumbnail = newsThumbnail;
        this.newsDetailTitle = newsDetailTitle;
        this.newsDetailDate = newsDetailDate;
        this.newsDescription = newsDescription;
        this.loadingBar = loadingBar;
        this.newsScrollView = newsScrollView;
    }

    public void playTrack(final MusicData data, boolean isPreview) {
        if(isVideoPlayerShowing) {
            isVideoPlayerShowing = false;
            closeVideoPlayer();
        }

        if (panelMusicPlayer.getVisibility() != View.VISIBLE) {
                    showMusicPlayer();

        }

        trackType = MusicType.SHOP;
        selectedMusicData = data;
        // TODO set notif
        mBuilder.setSmallIcon(R.drawable.nd_local_512);
        mBuilder.setContentTitle("Now Playing");
        mBuilder.setContentText(data.singerName + " - " + data.songTitle);
        notifManager.notify(100, mBuilder.build());

        panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
        audioPauseIcon.setImageResource(R.drawable.icon_play);

        if(audioPlayer.isPlaying())
            audioPlayer.stopPlayback();
        if(ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, data.albumName, data.songTitle)) {
            Log.d("MEDIA", "Local");
            String path = "/sdcard/"+Consts.APP_BASE_DIR+"/Music/"+data.albumName+"/"+data.songTitle+".mp3";

            audioPlayer.setDataSource(context, Uri.parse(new File(path).toString()));
        } else {
            Log.d("MEDIA", "URL");
            audioPlayer.setDataSource(context, Uri.parse(data.previewURL));
        }

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

                int duration = (int) audioPlayer.getDuration() / 1000;
                audioProgress.setMax(duration);
            }
        });

        audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                notifManager.cancelAll();
            }
        });

        audioPlayer.prepareAsync();

        final Handler mHandler = new Handler();
        Activity currentActivity =  (Activity) context;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(audioPlayer != null) {
                    int mCurrentPos = (int)audioPlayer.getCurrentPosition() / 1000;
                    audioProgress.setProgress(mCurrentPos);

                    int duration = ((int)audioPlayer.getDuration() / 1000) - mCurrentPos;
                    int minutes = duration / 60;
                    int seconds = duration % 60;
                    audioPlayerDuration.setText(minutes + ":" + ((seconds < 10) ? "0" + seconds : seconds));
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

    public void playTrack(final CommunityContentData data) {
        if(isVideoPlayerShowing) {
            isVideoPlayerShowing = false;
            closeVideoPlayer();
        }

        if (panelMusicPlayer.getVisibility() != View.VISIBLE) {
            showMusicPlayer();

        }

        trackType = MusicType.COMMUNITY;
        selectedCommunityData = data;
        data.totalViews += 1;
        ApiManager.getInstance().addListen(data.ID);

        mBuilder.setSmallIcon(R.drawable.nd_local_512);
        mBuilder.setContentTitle("Now Playing");
        mBuilder.setContentText(data.userName+" - "+data.songName);
        notifManager.notify(100, mBuilder.build());

        panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
        audioPauseIcon.setImageResource(R.drawable.icon_play);

        if(audioPlayer.isPlaying())
            audioPlayer.stopPlayback();

        audioPlayer.setDataSource(context, Uri.parse(data.fileURL));

        audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                audioPlayer.start();
                panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
                audioPauseIcon.setImageResource(R.drawable.icon_pause);

                // TODO change audio data
                imageLoader.displayImage(data.photoURL, audioPlayerThumbnail, opts);
                audioPlayerSongTitle.setText(data.songName);
                audioPlayerArtistName.setText(data.userName);
                audioPlayerDuration.setText("");

                int duration = (int) audioPlayer.getDuration() / 1000;
                audioProgress.setMax(duration);
            }
        });

        audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                notifManager.cancelAll();
            }
        });

        audioPlayer.prepareAsync();

        final Handler mHandler = new Handler();
        Activity currentActivity =  (Activity) context;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(audioPlayer != null) {
                    int mCurrentPos = (int)audioPlayer.getCurrentPosition() / 1000;
                    audioProgress.setProgress(mCurrentPos);

                    int duration = ((int)audioPlayer.getDuration() / 1000) - mCurrentPos;
                    int minutes = duration / 60;
                    int seconds = duration % 60;
                    audioPlayerDuration.setText(minutes + ":" + ((seconds < 10) ? "0" + seconds : seconds));
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

    public void playTrack(final AlbumItem data) {
        if(isVideoPlayerShowing) {
            isVideoPlayerShowing = false;
            closeVideoPlayer();
        }

        if (panelMusicPlayer.getVisibility() != View.VISIBLE) {
            showMusicPlayer();

        }

        trackType = MusicType.ALBUM_ITEM;
        selectedAlbumItemData = data;

        mBuilder.setSmallIcon(R.drawable.nd_local_512);
        mBuilder.setContentTitle("Now Playing");
        mBuilder.setContentText(data.singerName+" - "+data.songName);
        notifManager.notify(100, mBuilder.build());

        panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
        audioPauseIcon.setImageResource(R.drawable.icon_play);

        if(audioPlayer.isPlaying())
            audioPlayer.stopPlayback();

        if(ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, data.albumName, data.songName)) {
            Log.d("MEDIA", "Local");
            String path = "/sdcard/"+Consts.APP_BASE_DIR+"/Music/"+data.albumName+"/"+data.songName+".mp3";

            audioPlayer.setDataSource(context, Uri.parse(new File(path).toString()));
        } else {
            Log.d("MEDIA", "URL");
            audioPlayer.setDataSource(context, Uri.parse(data.previewURL));
        }

        audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                audioPlayer.start();
                panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
                audioPauseIcon.setImageResource(R.drawable.icon_pause);

                // TODO change audio data
                imageLoader.displayImage("", audioPlayerThumbnail, opts);
                audioPlayerSongTitle.setText(data.songName);
                audioPlayerArtistName.setText(data.singerName);
                audioPlayerDuration.setText("");

                int duration = (int) audioPlayer.getDuration() / 1000;
                audioProgress.setMax(duration);
            }
        });

        audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                notifManager.cancelAll();
            }
        });

        audioPlayer.prepareAsync();

        final Handler mHandler = new Handler();
        Activity currentActivity =  (Activity) context;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(audioPlayer != null) {
                    int mCurrentPos = (int)audioPlayer.getCurrentPosition() / 1000;
                    audioProgress.setProgress(mCurrentPos);

                    int duration = ((int)audioPlayer.getDuration() / 1000) - mCurrentPos;
                    int minutes = duration / 60;
                    int seconds = duration % 60;
                    audioPlayerDuration.setText(minutes + ":" + ((seconds < 10) ? "0" + seconds : seconds));
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

    public void playItem(final LibraryData data, final List<LibraryData> listLibrary) {
        if(data.category.equalsIgnoreCase("music")) {
            if(isVideoPlayerShowing) {
                isVideoPlayerShowing = false;
                closeVideoPlayer();
            }

            // Music
            if (panelMusicPlayer.getVisibility() != View.VISIBLE) {
                showMusicPlayer();

            }

            trackType = MusicType.LIBRARY;
            selectedLibraryData = data;

            mBuilder.setSmallIcon(R.drawable.nd_local_512);
            mBuilder.setContentTitle("Now Playing");
            mBuilder.setContentText(data.singerName+" - "+data.songTitle);
            notifManager.notify(100, mBuilder.build());

            panelMusicPlayer.setBackgroundColor(context.getResources().getColor(R.color.music_player_bg));
            audioPauseIcon.setImageResource(R.drawable.icon_play);

            if(audioPlayer.isPlaying())
                audioPlayer.stopPlayback();

            // TODO from local
            if(ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, data.albumName, data.songTitle)) {
                Log.d("MEDIA", "Local");
                String path = "/sdcard/"+Consts.APP_BASE_DIR+"/Music/"+data.albumName+"/"+data.songTitle+".mp3";

                audioPlayer.setDataSource(context, Uri.parse(new File(path).toString()));
            } else {
                Log.d("MEDIA", "URL");
                audioPlayer.setDataSource(context, Uri.parse(data.fileURL));
            }

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
                    audioPlayerDuration.setText("");

                    int duration = (int) audioPlayer.getDuration() / 1000;
                    audioProgress.setMax(duration);
                }
            });

            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    notifManager.cancelAll();
                }
            });

            audioPlayer.prepareAsync();

            final int nextIndex = (listLibrary.indexOf(data) + 1) % listLibrary.size();
            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playItem(listLibrary.get(nextIndex), listLibrary);
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

                        int duration = ((int)audioPlayer.getDuration() / 1000) - mCurrentPos;
                        int minutes = duration / 60;
                        int seconds = duration % 60;
                        audioPlayerDuration.setText(minutes + ":" + ((seconds < 10) ? "0"+seconds : seconds));
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
        } else {
            if(isAudioPlayerShowing) {
                isAudioPlayerShowing = false;
                stopTrack();
            }

            // Video
            showVideoPlayer();
            if(ConnManager.getInstance().fileExist(ConnManager.DataType.VIDEO, "", data.songTitle)) {
                Log.d("MEDIA", "Local");
                String path = "/sdcard/"+Consts.APP_BASE_DIR+"/Video/"+data.songTitle+".mp4";

                videoPlayer.setVideoPath(path);
            } else {
                Log.d("MEDIA", "URL");
                videoPlayer.setVideoURI(Uri.parse(data.fileURL));
            }
            videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // TODO close buffering indicator
                    videoPlayer.start();
                }
            });

            final int nextIndex = (listLibrary.indexOf(data) + 1) % listLibrary.size();
            videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playItem(listLibrary.get(nextIndex), listLibrary);
                }
            });

            imageLoader.displayImage(data.coverURL, videoThumbnail, opts);
            videoDate.setText(data.songTitle);
            videoDescription.setText("");
        }


    }

    public void pauseTrack() {
        audioPauseIcon.setImageResource(R.drawable.icon_play);
        audioPlayer.pause();
    }

    public void resumeTrack() {
        audioPauseIcon.setImageResource(R.drawable.icon_pause);
        audioPlayer.start();
    }

    public boolean isTrackPaused() {
        return !audioPlayer.isPlaying();
    }

    public void stopTrack() {
        closeMusicPlayer();
        audioPlayer.stopPlayback();
    }

    public MusicType trackType() {
        return trackType;
    }

    public MusicData getSelectedMusicData() {
        return selectedMusicData;
    }

    public CommunityContentData getSelectedCommunityData() {
        return selectedCommunityData;
    }

    public LibraryData getSelectedLibraryData() {
        return selectedLibraryData;
    }

    public AlbumItem getSelectedAlbumItemData() { return  selectedAlbumItemData; }

    public long getCurrentPos() {
        return audioPlayer.getCurrentPosition();
    }

    public void playVideo(VideoData videoData) {
        if(isAudioPlayerShowing) {
            isAudioPlayerShowing = false;
            stopTrack();
        }

        showVideoPlayer();
        if(ConnManager.getInstance().fileExist(ConnManager.DataType.VIDEO, "", videoData.videoTitle)) {
            Log.d("MEDIA", "Local");
            String path = "/sdcard/"+Consts.APP_BASE_DIR+"/Video/"+videoData.videoTitle+".mp4";

            videoPlayer.setVideoPath(path);
        } else {
            Log.d("MEDIA", "URL");
            videoPlayer.setVideoURI(Uri.parse(videoData.previewURL));
        }
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO close buffering indicator
                videoPlayer.start();
            }
        });

        imageLoader.displayImage(videoData.cover, videoThumbnail, opts);
        videoDate.setText(videoData.videoTitle);
        if(videoData.description.length() > 0)
            videoDescription.setText(videoData.description);
    }

    public void playVideo(CommunityContentData videoData) {
        if(isAudioPlayerShowing) {
            isAudioPlayerShowing = false;
            stopTrack();
        }

        videoData.totalViews += 1;
        ApiManager.getInstance().addListen(videoData.ID);
        showVideoPlayer();
        videoPlayer.setVideoURI(Uri.parse(videoData.fileURL));
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO close buffering indicator
                videoPlayer.start();
            }
        });

        imageLoader.displayImage(videoData.coverURL, videoThumbnail, opts);
        videoDate.setText(videoData.songName);
        if(videoData.description.length() > 0)
            videoDescription.setText(videoData.description);
    }

    public void pauseVideoPlayer() {
        videoPlayer.pause();
    }

    public String getVideoPlayerURL() {
        return videoPlayer.getVideoUri().toString();
    }

    public long getVideoPlayerCurrentPosition() {
        return videoPlayer.getCurrentPosition();
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

    public void closeMusicPlayer() {
        notifManager.cancelAll();

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

    public void showNewsDetail(final NewsData newsData) {
        panelNewsDetail.setVisibility(View.VISIBLE);
        newsScrollView.fullScroll(ScrollView.FOCUS_UP);
//        ApiManager.getInstance().getToken();
//        ApiManager.getInstance().setOnTokenReceived(new ApiManager.OnTokenReceived() {
//            @Override
//            public void onTokenSaved() {
//                getNewsDetail(ApiManager.getInstance().TOKEN, newsData.ID);
//            }
//
//            @Override
//            public void onUserAccessTokenSaved() {
//
//            }
//
//            @Override
//            public void onError(String message) {
//
//            }
//        });
        getNewsDetail(ApiManager.getInstance().USER_ACCESS_TOKEN, newsData.ID);

        isNewsDetailShowing = true;
    }

    public void closeNewsDetail() {
        panelNewsDetail.setVisibility(View.GONE);

        isNewsDetailShowing = false;
    }

    public void getNewsDetail(String token, String id) {
        Map<String, String> params = new HashMap<>();

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_NEWS+"/"+id, token, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                // TODO close loading
                if (result != null) {
                    Log.d("News", result);
                    try {
                        JSONObject rawData = new JSONObject(result);
                        JSONObject article = rawData.getJSONObject("article");

                        String ID = article.getString("id");
                        String title = article.getString("title");
                        String thumbnailURL = article.getString("image");
                        String content = article.getString("content");
                        String tags = article.getString("tags");
                        String slug = article.getString("slug");
                        String totalLikes = article.getString("total_likes");
                        String created = article.getString("created");
                        // TODO related
                        String isLikeable = article.getString("is_likeable");

                        imageLoader.displayImage(thumbnailURL, newsThumbnail, opts);
                        newsDetailTitle.setText(title);
                        newsDetailDate.setText(created);

                        String parsedContent = content.replaceAll("(\r\n)", "<br />");
                        newsDescription.setText(Html.fromHtml(parsedContent));

                        loadingBar.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void beforeExecute() {
                loadingBar.setVisibility(View.VISIBLE);
            }
        });

        request.execute();
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
