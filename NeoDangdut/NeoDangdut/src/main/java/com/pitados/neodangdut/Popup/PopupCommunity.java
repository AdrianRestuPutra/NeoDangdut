package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.FontLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupCommunity extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail;
    private TextView title, artistName, listenCountText, likeCountText;
    private RelativeLayout buttonPlay, buttonLike, buttonShareFB, buttonShareTwitter, buttonComment;
    private View lastDivider;

    private CommunityContentData commData;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private boolean isMusic;

    private ShareDialog shareDialog;
    private PopupComment popupComment;

    public PopupCommunity(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_community);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    public void setShareDialog(ShareDialog shareDialog) {
        this.shareDialog = shareDialog;
    }

    private void initialize() {
        thumbnail = (ImageView) findViewById(R.id.popup_community_song_thumbnail);
        title = (TextView) findViewById(R.id.popup_community_song_title);
        artistName = (TextView) findViewById(R.id.popup_community_song_artist_name);
        listenCountText = (TextView) findViewById(R.id.popup_community_listen_count_text);
        likeCountText = (TextView) findViewById(R.id.popup_community_like_count_text);

        title.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        listenCountText.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        likeCountText.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_community_song_button_play);
        buttonLike = (RelativeLayout) findViewById(R.id.popup_community_song_button_like);
        buttonShareFB = (RelativeLayout) findViewById(R.id.popup_community_song_button_share_fb);
        buttonShareTwitter = (RelativeLayout) findViewById(R.id.popup_community_song_button_share_twitter);
        buttonComment = (RelativeLayout) findViewById(R.id.popup_community_song_button_comment);

        lastDivider = findViewById(R.id.last_divider);

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        popupComment = new PopupComment(context, R.style.custom_dialog);

        buttonPlay.setOnClickListener(this);
        buttonLike.setOnClickListener(this);
        buttonShareFB.setOnClickListener(this);
        buttonShareTwitter.setOnClickListener(this);
        buttonComment.setOnClickListener(this);
    }

    public void showPopupCommunity(CommunityContentData data) {
        commData = data;

        imageLoader.displayImage(data.photoURL, thumbnail, opts);
        title.setText(data.songName);
        artistName.setText(data.userName);
        listenCountText.setText(String.valueOf(data.totalViews));
        likeCountText.setText(String.valueOf(data.totalLikes));

        Log.d("Category", data.category);

        if(data.category.equalsIgnoreCase("music")) {
            Log.d("Category Music", "benar");
            lastDivider.setVisibility(View.VISIBLE);
            buttonComment.setVisibility(View.VISIBLE);
        }

        this.show();
    }

    public void closePopupCommunity() {
        this.dismiss();
    }


    // TWITTER
    public String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {
            if(commData.category.equalsIgnoreCase("video"))
                CustomMediaPlayer.getInstance().playVideo(commData);
            else
                CustomMediaPlayer.getInstance().playTrack(commData);
        }
        if(view == buttonLike) {
            if(commData.category.equalsIgnoreCase("video")) {
                ApiManager.getInstance().likeItem(ApiManager.LikeType.VIDEO, commData.ID, commData, null);
            } else {
                ApiManager.getInstance().likeItem(ApiManager.LikeType.VIDEO, commData.ID, commData, null);
            }

            int count = 0;
            if(commData.isLikeable)
                count = commData.totalLikes + 1;
            else
                count = commData.totalLikes - 1;

            likeCountText.setText(String.valueOf(count));
        }
        if(view == buttonComment) {
            popupComment.showPopupComment(commData.ID);
        }
        if(view == buttonShareFB) {
            String url = "neodangdut.com";

            if(commData.category.equalsIgnoreCase("music")) {
                url = url + "/music/detail/" + commData.ID;
            } else {
                url = url + "/video/detail/" + commData.ID;
            }
            Log.d("Share", url);

            ShareLinkContent shareLink = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(url))
                    .build();

            shareDialog.show(shareLink);
        }
        if(view == buttonShareTwitter) {
            String url = "neodangdut.com";

            if(commData.category.equalsIgnoreCase("music")) {
                url = url + "/music/detail/" + commData.ID;
            } else {
                url = url + "/video/detail/" + commData.ID;
            }
            Log.d("Share", url);

            String toShare = "Mau tahu banyak ttg NeoDangdut? Klik disini. http://neodangdut.com/music #neo #dangdut #neodangdut";

            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s",
                    urlEncode(toShare));

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
            context.startActivity(intent);
        }
    }
}
