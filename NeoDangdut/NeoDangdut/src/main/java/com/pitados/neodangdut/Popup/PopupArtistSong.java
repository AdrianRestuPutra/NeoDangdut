package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.AlbumItem;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupArtistSong extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail;
    private TextView price, title, artistName, albumName, listenCount, likeCount;
    private RelativeLayout buttonPlay, buttonLike, buttonAlbum, buttonShareFB, buttonShareTwitter, buttonBuy;

    private MusicData musicData;
    private String albumID;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupAlbumView popupAlbum;

    private boolean purchased, inLibrary;

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

        title.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        albumName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        price.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_artist_song_button_play);
        buttonAlbum = (RelativeLayout) findViewById(R.id.popup_artist_song_button_album);
        buttonBuy = (RelativeLayout) findViewById(R.id.popup_artist_song_price_buy_button);

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
        buttonBuy.setOnClickListener(this);
    }

    private boolean isInLibrary(String ID) {
        for(LibraryData data : DataPool.getInstance().listLibraryMusic) {
            Log.d("FEATURED MUSIC", data.ID + " | " + ID);
            if(ID.equalsIgnoreCase(data.ID))
                return true;
        }

        return false;
    }

    private LibraryData getLibraryItem(String songID) {
        Log.d("LIBRARY SIZE", DataPool.getInstance().listLibraryMusic.size()+"");
        for(int i = 0; i < DataPool.getInstance().listLibraryMusic.size(); i++) {
            LibraryData data = DataPool.getInstance().listLibraryMusic.get(i);
            Log.d("COMPARE", data.ID + " | "+songID);
            if(data.ID.equalsIgnoreCase(songID))
                return data;
        }

        return null;
    }

    public void showPopupArtistSong(MusicData data) {
        musicData = data;

        albumID = data.albumID;

        imageLoader.displayImage(data.albumCover, thumbnail, opts);
//        price.setText(data.price);
        title.setText(data.songTitle);
        artistName.setText(data.singerName);
        albumName.setText(data.albumName);
        listenCount.setText("0");
        likeCount.setText("0");

        purchased = false;
        inLibrary = false;

        if(data.purchased) {
            Log.d("SONG", "DOWNLOAD");
            buttonBuy.setBackgroundResource(R.drawable.btn_inlibrary_blank);
            price.setText("Download");
            price.setTextColor(context.getResources().getColor(R.color.white_font));
            purchased = true;
        } else if(data.inLibrary) {
            Log.d("SONG", "IN LIBRARY");
            buttonBuy.setBackgroundResource(R.drawable.btn_inlibrary_def);
            price.setText("");
            inLibrary = true;
        } else {
            buttonBuy.setBackgroundResource(R.drawable.btn_price_artist_song);
            price.setText("Rp " + data.price);
        }

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

    public void showPopupArtistSong(AlbumItem data, String coverURL) {
        albumID = data.ID;

        imageLoader.displayImage(coverURL, thumbnail, opts);
        price.setText(data.price);
        title.setText(data.songName);
        artistName.setText(data.singerName);
        albumName.setText("");
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
            if(albumID != null)
                popupAlbum.showPopupAlbum(albumID);
            closePopupArtistSong();
        }
//        if(view == buttonShareFB) {
//
//        }
//        if(view == buttonShareTwitter) {
//
//        }

        if(view == buttonBuy) {
            if(inLibrary) {
                DataPool.getInstance().mainActivity.goToLibrary(false);
                closePopupArtistSong();
            }
        }
    }
}
