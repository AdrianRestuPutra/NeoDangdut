package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.util.CustomMediaPlayer;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupArtistSong extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail;
    private TextView price, title, artistName, albumName, listenCount, likeCount;
    private RelativeLayout buttonPlay, buttonLike, buttonAlbum, buttonShareFB, buttonShareTwitter;

    private MusicData musicData;
    private String albumID;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupAlbumView popupAlbum;

    public PopupArtistSong(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_artist_song);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    public void setPopupAlbum(PopupAlbumView popupAlbum) {
        this.popupAlbum = popupAlbum;
    }

    private void initialize() {
        thumbnail = (ImageView) findViewById(R.id.popup_artist_song_thumbnail);
        price = (TextView) findViewById(R.id.popup_artist_song_price);
        title = (TextView) findViewById(R.id.popup_artist_song_title);
        artistName = (TextView) findViewById(R.id.popup_artist_song_artist_name);
        albumName = (TextView) findViewById(R.id.popup_artist_song_album);
        listenCount = (TextView) findViewById(R.id.popup_artist_listen_count);
        likeCount = (TextView) findViewById(R.id.popup_artist_like_count);

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_artist_song_button_play);
        buttonAlbum = (RelativeLayout) findViewById(R.id.popup_artist_song_button_album);

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        buttonPlay.setOnClickListener(this);
        buttonAlbum.setOnClickListener(this);
    }

    public void showPopupArtistSong(MusicData data) {
        musicData = data;

        albumID = data.albumID;

        imageLoader.displayImage(data.albumCover, thumbnail, opts);
        price.setText(data.price);
        title.setText(data.songTitle);
        artistName.setText(data.singerName);
        albumName.setText(data.albumName);
        listenCount.setText("0");
        likeCount.setText("0");
        this.show();
    }

    public void showPopupArtistSong(LibraryData data) {
        albumID = data.albumID;

        imageLoader.displayImage(data.albumCover, thumbnail, opts);
        price.setText(data.price);
        title.setText(data.songTitle);
        artistName.setText(data.singerName);
        albumName.setText(data.albumName);
        listenCount.setText("0");
        likeCount.setText("0");
        this.show();
    }

    public void closePopupArtistSong() {
        this.dismiss();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {
            CustomMediaPlayer.getInstance().playTrack(musicData, true);
            closePopupArtistSong();
        }
//        if(view == buttonLike) {
//            ApiManager.getInstance().likeItem(ApiManager.LikeType.MUSIC, musicData.ID);
//        }
        if(view == buttonAlbum) {
            popupAlbum.showPopupLoading(albumID);
            closePopupArtistSong();
        }
//        if(view == buttonShareFB) {
//
//        }
//        if(view == buttonShareTwitter) {
//
//        }
    }
}
