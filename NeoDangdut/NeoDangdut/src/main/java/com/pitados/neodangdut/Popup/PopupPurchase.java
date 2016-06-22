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
import com.pitados.neodangdut.model.AlbumData;
import com.pitados.neodangdut.model.AlbumItem;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.UserLoginData;
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.FontLoader;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupPurchase extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail, buttonExit;
    private TextView windowTitle, artistName, songTitle, saldo, buttonPrice, buttonBuyText, sisaSaldo;
    private RelativeLayout buttonBuy;

    private MusicData musicData;
    private VideoData videoData;
    private AlbumData albumdata;
    private AlbumItem albumItemData;
    private String selectedID;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private UserLoginData userLoginData;

    public PopupPurchase(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_purchase);

        this.setCanceledOnTouchOutside(true);

        initialize();

        userLoginData = new UserLoginData(context);
    }

    private void initialize() {
        thumbnail = (ImageView) findViewById(R.id.popup_purchase_thumb);
        buttonExit = (ImageView) findViewById(R.id.popup_purchase_exit);
        windowTitle = (TextView) findViewById(R.id.popup_purchase_window_title);
        artistName = (TextView) findViewById(R.id.popup_purchase_artist_name);
        songTitle = (TextView) findViewById(R.id.popup_purchase_song_title);
        saldo = (TextView) findViewById(R.id.popup_purchase_saldo);
        buttonPrice = (TextView) findViewById(R.id.popup_purchase_button_price);
        buttonBuyText = (TextView) findViewById(R.id.popup_purchase_button_text);
        sisaSaldo = (TextView) findViewById(R.id.popup_purchase_sisa_saldo);

        buttonBuy = (RelativeLayout) findViewById(R.id.popup_purchase_button_buy);


        windowTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        songTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        saldo.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        buttonPrice.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        buttonBuyText.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        sisaSaldo.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));


        // Button
        buttonBuy = (RelativeLayout) findViewById(R.id.popup_purchase_button_buy);

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        buttonBuy.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
    }

    public void showPopupPurchase(MusicData data) {
        musicData = data;
        videoData = null;
        albumdata = null;
        albumItemData = null;
        selectedID = data.ID;

        imageLoader.displayImage(data.albumCover, thumbnail, opts);
        artistName.setText(data.singerName);
        songTitle.setText(data.songTitle);
        saldo.setText("Saldo anda: Rp "+userLoginData.getCredit());
        buttonPrice.setText(data.price);
        int saldoNum = Integer.valueOf(userLoginData.getCredit());
        int priceNum = Integer.valueOf(data.price);
        int sisaSaldoNum = saldoNum - priceNum;
        sisaSaldo.setText("Sisa saldo: Rp " + sisaSaldoNum);

        this.show();
    }

    public void showPopupPurchase(VideoData data) {
        videoData = data;
        musicData = null;
        albumdata = null;
        albumItemData = null;
        selectedID = data.ID;

        imageLoader.displayImage(data.cover, thumbnail, opts);
        artistName.setText(data.singerName);
        songTitle.setText(data.videoTitle);
        saldo.setText("Saldo anda: Rp "+userLoginData.getCredit());
        buttonPrice.setText(data.price);
        int saldoNum = Integer.valueOf(userLoginData.getCredit());
        int priceNum = Integer.valueOf(data.price);
        int sisaSaldoNum = saldoNum - priceNum;
        sisaSaldo.setText("Sisa saldo: Rp "+sisaSaldoNum);

        this.show();
    }

    public void showPopupPurchase(AlbumData data) {
        albumdata = data;
        videoData = null;
        musicData = null;
        albumItemData = null;
        selectedID = data.ID;

        imageLoader.displayImage(data.coverURL, thumbnail, opts);
        artistName.setText(data.singerName);
        songTitle.setText(data.albumName);
        saldo.setText("Saldo anda: Rp "+userLoginData.getCredit());
        buttonPrice.setText(data.price);
        int saldoNum = Integer.valueOf(userLoginData.getCredit());
        int priceNum = Integer.valueOf(data.price);
        int sisaSaldoNum = saldoNum - priceNum;
        sisaSaldo.setText("Sisa saldo: Rp "+sisaSaldoNum);

        this.show();
    }

    public void showPopupPurchase(AlbumItem data) {
        albumItemData = data;
        videoData = null;
        musicData = null;
        albumdata = null;
        selectedID = data.ID;

        imageLoader.displayImage(null, thumbnail, opts);
        artistName.setText(data.singerName);
        songTitle.setText(data.songName);
        saldo.setText("Saldo anda: Rp "+userLoginData.getCredit());
        buttonPrice.setText("Rp "+data.price);
        int saldoNum = Integer.valueOf(userLoginData.getCredit());
        int priceNum = Integer.valueOf(data.price);
        int sisaSaldoNum = saldoNum - priceNum;
        sisaSaldo.setText("Sisa saldo: Rp "+sisaSaldoNum);

        this.show();
    }

    @Override
    public void cancel() {
        closePopupPurchase();
        super.cancel();
    }

    public void closePopupPurchase() {
        musicData = null;
        videoData = null;
        albumdata = null;
        albumItemData = null;
        selectedID = null;

        artistName.setText("");
        songTitle.setText("");
        saldo.setText("");
        buttonPrice.setText("");
        sisaSaldo.setText("");

        this.dismiss();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonBuy) {
//            ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
//
//                @Override
//                public void onUserAccessTokenSaved() {
//                    if(albumdata == null)
//                        ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.SINGLE, selectedID);
//                    else
//                        ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.ALBUM, selectedID);
//
//                }
//
//                @Override
//                public void onError(String message) {
//
//                }
//            });
            if(albumdata == null)
                ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.SINGLE, selectedID);
            else
                ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.ALBUM, selectedID);
//            ApiManager.getInstance().getUserAccessToken();

            closePopupPurchase();
        }

        if(view == buttonExit) {
            closePopupPurchase();
        }
    }
}
