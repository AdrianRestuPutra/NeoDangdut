package com.pitados.neodangdut.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.devbrackets.android.exomedia.EMVideoView;
import com.facebook.share.widget.ShareDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.Popup.PopupArtistSong;
import com.pitados.neodangdut.Popup.PopupCommunity;
import com.pitados.neodangdut.Popup.PopupExit;
import com.pitados.neodangdut.Popup.PopupLoading;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomPagerAdapter;
import com.pitados.neodangdut.fragments.FragmentHome;
import com.pitados.neodangdut.fragments.FragmentHomeMusic;
import com.pitados.neodangdut.fragments.FragmentHomeNews;
import com.pitados.neodangdut.fragments.FragmentHomeVideo;
import com.pitados.neodangdut.fragments.FragmentLibraryMusic;
import com.pitados.neodangdut.fragments.FragmentLibraryVideo;
import com.pitados.neodangdut.fragments.FragmentSearchCommunity;
import com.pitados.neodangdut.fragments.FragmentSearchShop;
import com.pitados.neodangdut.fragments.FragmentShopMusicAllSongs;
import com.pitados.neodangdut.fragments.FragmentShopMusicNewSongs;
import com.pitados.neodangdut.fragments.FragmentShopMusicTopAlbums;
import com.pitados.neodangdut.fragments.FragmentShopMusicTopSongs;
import com.pitados.neodangdut.fragments.FragmentShopVideoAllVideos;
import com.pitados.neodangdut.fragments.FragmentShopVideoNewVideos;
import com.pitados.neodangdut.fragments.FragmentShopVideoTopVideos;
import com.pitados.neodangdut.fragments.FragmentUploadedMusic;
import com.pitados.neodangdut.fragments.FragmentUploadedVideo;
import com.pitados.neodangdut.model.FragmentModel;
import com.pitados.neodangdut.model.SettingPref;
import com.pitados.neodangdut.model.UserLoginData;
import com.pitados.neodangdut.model.UserProfileData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FilePath;
import com.pitados.neodangdut.util.FontLoader;
import com.pitados.neodangdut.util.StateManager;
import com.pitados.neodangdut.util.UncaughtException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, BillingProcessor.IBillingHandler {

    private int REQUEST_STORAGE_PERMISSION_CODE = 100;
    private int UPLOAD_FILE_CODE = 250;
    private int UPLOAD_PROFPIC = 256;

    private enum PanelState {
        PANEL_NEW_POST,
        PANEL_LIBRARY,
        PANEL_SHOP_MUSIC,
        PANEL_SHOP_VIDEO,
        PANEL_DOWNLOAD,
        PANEL_SETTING,
        PANEL_PROFILE,
        PANEL_TOPUP,
        PANEL_SEARCH
    }
    private PanelState panelState;

    private boolean isUploadFormShowing;

    private DrawerLayout drawer;
    private FloatingActionsMenu fabMenu;
    private com.getbase.floatingactionbutton.FloatingActionButton fabMusic, fabVideo;
    // Panel
    private RelativeLayout panelNewPost, panelLibrary, panelShopMusic, panelShopVideo, panelDownloads, panelSettings, panelUpload, panelProfile, panelTopup, panelSearch;
//    private RelativeLayout panelLoading;
    // Media Panel
    private LinearLayout panelMusicPlayer;
    private RelativeLayout musicPlayerPauseButton, musicPlayerShuffleButton, musicPlayerLoopbutton;
    private ImageView musicPlayerPauseIcon, musicPlayerOptButton;
    private ImageView musicPlayerThumbnail;
    private TextView musicPlayerTitle, musicPlayerArtistName, musicPlayerDuration;
    private SeekBar musicPlayerProgress;
    // VIDEO
    private RelativeLayout panelVideoPlayer;
    private EMVideoView videoView;
    private ImageView videoPlayerThumbnail;
    private ImageView videoPlayerFullscreenButton;
    // TODO widget video
    private TextView videoDate, videoDescription;
    private EditText videoInputComment;
    private RelativeLayout videoSubmitCommentButton;
    private ListView videoComment;
    // NEWS
    private RelativeLayout panelNewsDetail, loadingBar;
    private ScrollView newsScrollView;
    private ImageView newsDetailThumbnail;
    private TextView newsDetailTitle, newsDetailDate, newsDetailDescription, newsDetailLikeCount;
    private RelativeLayout newsDetailLikeButton, newsDetailShareFB, newsDetailShareTweeter;
    private EditText newsDetailInputComment;
    private RelativeLayout newsDetailSubmitCommentButton;
    private ListView newsDetailComment;

    // SETTINGS
    private RelativeLayout settingToggleNotif, settingToggleDownload;
    private ImageView iconToggleNotif, iconToggleDownload;
    private RelativeLayout settingSupportButton, settingPrivacyButton, settingAboutButton;
    // UPLOAD
    private TextView uploadFileName, uploadButtonText;
    private SeekBar uploadProgress;
    private RelativeLayout uploadButtonPause, uploadButtonCancel, uploadButton;
    private EditText uploadInputTitle, uploadInputDescription;
    private File tempFile;
    private boolean isUploadMusic;

    // TOPUP
    private RelativeLayout topupButton3k, topupButton5k, topupButton10k, topupButton20k, topupButton50k;

    // Profile
    private ImageView profilePicture;
    private EditText profileName, profileCountry, profileCity, profileDescription;
    private ImageView profileEdit, editName, editCountry, editCity;
    private TextView profileLikeCount, profileMusicCount, profileVideoCount;

    private boolean notifIsOn, downloadWIFIOnlyOn;
    private boolean canEditProfile;

    // SEARCH
    private EditText searchInput;
    private RelativeLayout searchPanelButton;
    private String searchKey;

    // Fragments
    private ViewPager viewPagerNewPost, viewPagerLibrary, viewPagerShopMusic, viewPagerShopVideo, viewPagerSearch;
    private TabLayout slidingTabNewPost, slidingTabLibrary, slidingTabShopMusic, slidingTabShopVideo, slidingTabSearch;
    private CustomPagerAdapter pagerAdapterHome, pagerAdapterLibrary, pagerAdapterShopMusic, pagerAdapterShopVideo, pagerAdapterSearch;
    // Upload
    private ViewPager viewPagerUploaded;
    private TabLayout slidingTabUploaded;
    private CustomPagerAdapter pagerAdapterUploaded;

    // In App Purchase
    private String storeKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApFO+8ygQlDoMB1bp7EtAYHjVpvAc2T+x054MAQCimjwT4yZD0QVW7GpYXVgELhEZtLDWpiREPYgIe6UX8ZxHwaU50sYy7Q8GtIx2zRR2ZMSW0TGhGZCYsdTuVpTUDBPnZ21lxXRCkDYyEAbZ2NhoOma6QoVcjdMkbGh8K0NHksBFjg6ngFxtTFJ8N7RNFKgGtcUKPJdQSbI1rXjHLAoae4GhSiffi/vkZp60yFEjCqsbN6lF1hdGvgOiC+kd3wLIApR+UWAYE46jMS4giP+5kt6vwV3wXR1aWXmpQIehq8p3sS99/2Qv7R1kYKRHT0uPHZTKSXnDhfs8aWTSt4QntwIDAQAB";
    private BillingProcessor billingProc;

    // Side Menu
    private RelativeLayout sideMenuProfileButton;
    private ImageView sideMenuUserPic;
    private TextView sideMenuFullName;
    private LinearLayout sideMenuTopup, sideMenuHome, sideMenuLibrary, sideMenuShopMusic, sideMenuShopVideo, sideMenuDownloads, sideMenuSetting, sideMenuSignOut;
    private TextView sideMenuWallet;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;
    private UserLoginData userLoginData;

    private PopupLoading popupLoading;
    private PopupArtistSong popupArtistSong;
    private PopupCommunity popupCommunitySong;
    private PopupAlbumView popupAlbum;
    private ShareDialog shareDialog;
    private PopupExit popupExit;
    private PopupExit popupRetry;
    // EXIT

    // MENU
    private TextView actionBarTitle;
    private MenuItem searchButton;

    private String sku, originalJSON, purchaseSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Using custom exception handler
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtException(MainActivity.this));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check if storage permission allowed
        isStoragePermissionGranted();

        // Init in app purchase using OpenIAB
        initIAB();

        DataPool.getInstance().mainActivity = MainActivity.this;
        userLoginData = new UserLoginData(getBaseContext());

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        // init panel
        panelNewPost = (RelativeLayout) findViewById(R.id.panel_new_post);
        panelLibrary = (RelativeLayout) findViewById(R.id.panel_library);
        panelShopMusic = (RelativeLayout) findViewById(R.id.panel_shop_music);
        panelShopVideo = (RelativeLayout) findViewById(R.id.panel_shop_video);
        panelDownloads = (RelativeLayout) findViewById(R.id.panel_downloads);
        panelSettings = (RelativeLayout) findViewById(R.id.panel_settings);
        panelProfile = (RelativeLayout) findViewById(R.id.panel_profile);
        panelTopup = (RelativeLayout) findViewById(R.id.panel_topup);
        panelSearch = (RelativeLayout) findViewById(R.id.panel_search);


        popupLoading = new PopupLoading(MainActivity.this, R.style.custom_dialog);
        shareDialog = new ShareDialog(this);
        popupExit = new PopupExit(MainActivity.this, R.style.custom_dialog) {
            @Override
            public void onPositiveButton() {
                DataPool.getInstance().listHomeBanner.clear();
                DataPool.getInstance().listHomeTopVideos.clear();
                DataPool.getInstance().listHomeTopMusic.clear();

                MainActivity.this.finish();
            }
        };

        popupRetry = new PopupExit(MainActivity.this, R.style.custom_dialog) {
            @Override
            public void onPositiveButton() {
                ApiManager.getInstance().getUserAccessToken();
            }
        };

        initMediaPlayer();
        initSideMenu();
        // Home
        initPanelNewPost();
        // Library
        initPanelLibrary();
        // Shop
        initPanelShopMusic();
        initPanelShopVideo();
        initPanelSetting();
        initUploadForm();
        initPanelProfile();
        initPanelUpload();
        initPanelTopup();
        initPanelSearch();


        // init state
        panelState = PanelState.PANEL_NEW_POST;
        ConnManager.getInstance().init(getBaseContext());
        isUploadFormShowing = false;
        ApiManager.getInstance().setContext(MainActivity.this);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        // Default storage path
        Consts.APP_BASE_DIR = Environment.getExternalStorageDirectory()+"/NeoDangdut";

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMusic = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_upload_music);
        fabMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUploadMusic = true;
                showFileChooser(true);
            }
        });

        fabVideo = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_upload_video);
        fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUploadMusic = true;
                showFileChooser(false);
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Action Bar
        getSupportActionBar().setTitle("Neo Dangdut");

        setHomeListener();

        if(isNetworkAvailable()){
            ApiManager.getInstance().getUserAccessToken();
        } else {
            goToLibrary(false);
            popupRetry.showPopupExit("No Internet Connection", "No", "Retry");
        }
    }

    private void setHomeListener() {
        ApiManager.getInstance().setOnUserDataListener(new ApiManager.OnUserDataReceived() {
            @Override
            public void onUserDataLoaded() {
                setSideMenuData();
                showPanelProfile();
            }
        });

        ApiManager.getInstance().setOnUserDataUpdatedListener(new ApiManager.OnUserDataUpdated() {
            @Override
            public void onUserDataUpdated() {
                ApiManager.getInstance().getUserData();
            }

            @Override
            public void onError() {

            }
        });

        ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
            @Override
            public void onUserAccessTokenSaved() {
                if (panelState == PanelState.PANEL_SEARCH) {
                    // Search
                    ApiManager.getInstance().getSearchShopMusic(searchKey);
                    ApiManager.getInstance().getSearchShopVideos(searchKey);
                    ApiManager.getInstance().getSearchShopMusicAlbums(searchKey);
                    ApiManager.getInstance().getSearchCommunityMusic(searchKey);
                    ApiManager.getInstance().getSearchCommunityVideo(searchKey);
                } else if (panelState == PanelState.PANEL_TOPUP) {
                    Log.d("PURCHASE", "confirm");
                    ApiManager.getInstance().setOnConfirmIAP(new ApiManager.OnConfirmIAP() {
                        @Override
                        public void onConfirm() {

                            billingProc.consumePurchase(sku);
                            ApiManager.getInstance().getUserData();
                        }
                    });

                    ApiManager.getInstance().confirmIAP(originalJSON, purchaseSignature);
                } else {
                    ApiManager.getInstance().getUserData();

                    DataPool.getInstance().listLibraryMusic.clear();
                    DataPool.getInstance().listLibraryVideo.clear();
                    ApiManager.getInstance().getLibraryMusic();
                    ApiManager.getInstance().getLibraryVideo();

                    ApiManager.getInstance().getHomeBanner();
                    ApiManager.getInstance().getHomeTopMusic();
                    ApiManager.getInstance().getHomeTopVideos();
                    ApiManager.getInstance().getHomeLatestNews();
                    // SHOP MUSIC
                    ApiManager.getInstance().getShopMusicTopSongs();
                    ApiManager.getInstance().getFeaturedShopMusic();
                    ApiManager.getInstance().getShopMusicTopAlbums();
                    ApiManager.getInstance().getShopMusicNewSongs();
                    ApiManager.getInstance().getShopMusicAllSongs();
                    // SHOP VIDEO
                    ApiManager.getInstance().getShopVideoTopVideos();
                    ApiManager.getInstance().getFeaturedShopVideo();
                    ApiManager.getInstance().getShopVideoNewVideos();
                    ApiManager.getInstance().getShopVideoAllVideos();

                    // Community
                    ApiManager.getInstance().getCommunityMusic();
                    ApiManager.getInstance().getCommunityVideo();
                    ApiManager.getInstance().getAllNews();

                    ApiManager.getInstance().getUploadedMusic();
                    ApiManager.getInstance().getUploadedVideo();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void initIAB() {
        billingProc = new BillingProcessor(this, storeKey, this);
        ApiManager.getInstance().setBillingProc(MainActivity.this, billingProc);

        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if(isAvailable)
            Log.d("AVAILABLE", "Store OK");
        else
            Log.d("AVAILABLE", "No STore");
    }

    // Method init
    private void initMediaPlayer() {
        // Media
        panelMusicPlayer = (LinearLayout) findViewById(R.id.music_player_panel);
        panelMusicPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        StateManager.isTopView = true;
                        return true;

                    case MotionEvent.ACTION_UP :
                        StateManager.isTopView = false;
                        return true;
                }
                return false;
            }
        });

        musicPlayerTitle = (TextView) findViewById(R.id.music_player_title);
        musicPlayerArtistName = (TextView) findViewById(R.id.music_player_artist);

        musicPlayerTitle.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_REGULAR));
        musicPlayerArtistName.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));

        musicPlayerPauseIcon = (ImageView) findViewById(R.id.music_player_pause_icon);
        musicPlayerPauseButton = (RelativeLayout) findViewById(R.id.music_player_pause);
        musicPlayerOptButton = (ImageView) findViewById(R.id.music_player_opt_button);

        popupAlbum = new PopupAlbumView(this, R.style.custom_dialog);
        popupArtistSong = new PopupArtistSong(MainActivity.this, R.style.custom_dialog);
        popupArtistSong.setPopupAlbum(popupAlbum);
        popupCommunitySong = new PopupCommunity(MainActivity.this, R.style.custom_dialog);

        musicPlayerPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CustomMediaPlayer.getInstance().isTrackPaused())
                    CustomMediaPlayer.getInstance().resumeTrack();
                else
                    CustomMediaPlayer.getInstance().pauseTrack();
            }
        });

        musicPlayerOptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMusicDetail();
            }
        });


        musicPlayerProgress = (SeekBar) findViewById(R.id.music_player_progress);
        musicPlayerThumbnail = (ImageView) findViewById(R.id.music_player_thumbnail);
        musicPlayerShuffleButton = (RelativeLayout) findViewById(R.id.music_player_random);
        musicPlayerLoopbutton = (RelativeLayout) findViewById(R.id.music_player_loop);
        musicPlayerDuration = (TextView) findViewById(R.id.music_player_duration);

        musicPlayerDuration.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));

        panelVideoPlayer = (RelativeLayout) findViewById(R.id.video_player_panel);
        panelVideoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        StateManager.isTopView = true;
                        return true;

                    case MotionEvent.ACTION_UP :
                        StateManager.isTopView = false;
                        return true;
                }
                return false;
            }
        });

        videoPlayerFullscreenButton = (ImageView) findViewById(R.id.video_player_fullscreen_button);
        videoView = (EMVideoView) findViewById(R.id.video_player_view);
        videoPlayerThumbnail = (ImageView) findViewById(R.id.video_player_thumbnail);
        videoDate = (TextView) findViewById(R.id.video_player_detail_date);
        videoDescription = (TextView) findViewById(R.id.video_player_detail_description);

        videoDate.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));
        videoDescription.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_REGULAR));

        videoInputComment = (EditText) findViewById(R.id.video_player_comment_input);
        videoSubmitCommentButton = (RelativeLayout) findViewById(R.id.video_player_comment_button_submit);
        videoComment = (ListView) findViewById(R.id.video_player_comment_listview);

        videoPlayerFullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomMediaPlayer.getInstance().pauseVideoPlayer();
                String url = CustomMediaPlayer.getInstance().getVideoPlayerURL();
                int currentPos = (int) CustomMediaPlayer.getInstance().getCurrentPos();

                Intent intent = new Intent(MainActivity.this, FullscreenVideoPlayer.class);
                intent.putExtra("VideoURI", url);
                intent.putExtra("CurrentPosition", currentPos);

                startActivity(intent);
            }
        });

        newsScrollView = (ScrollView) findViewById(R.id.news_scroll_view);
        panelNewsDetail = (RelativeLayout) findViewById(R.id.news_detail);
        newsDetailThumbnail = (ImageView) findViewById(R.id.news_detail_thumbnail);
        newsDetailTitle = (TextView) findViewById(R.id.news_detail_title);
        newsDetailDate = (TextView) findViewById(R.id.news_detail_date);
        newsDetailDescription = (TextView) findViewById(R.id.news_detail_description);
        loadingBar = (RelativeLayout) findViewById(R.id.news_detail_loading);
        newsDetailLikeCount = (TextView) findViewById(R.id.news_detail_like_count_text);

        newsDetailLikeButton = (RelativeLayout) findViewById(R.id.news_detail_like_button);
        newsDetailShareFB = (RelativeLayout) findViewById(R.id.news_detail_facebook_button);
        newsDetailShareTweeter = (RelativeLayout) findViewById(R.id.news_detail_twitter_button);

        newsDetailTitle.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_REGULAR));
        newsDetailDate.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));
        newsDetailDescription.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_REGULAR));
        newsDetailLikeCount.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));

        newsDetailInputComment = (EditText) findViewById(R.id.news_detail_comment_input);
        newsDetailSubmitCommentButton = (RelativeLayout) findViewById(R.id.news_detail_comment_button_submit);
        newsDetailComment = (ListView) findViewById(R.id.news_detail_comment_listview);

        CustomMediaPlayer.getInstance().setAudioPanel(this, panelMusicPlayer, musicPlayerProgress, musicPlayerDuration,
                musicPlayerThumbnail, musicPlayerTitle, musicPlayerArtistName, musicPlayerPauseIcon);

        CustomMediaPlayer.getInstance().setVideoPanel(panelVideoPlayer, videoView, videoDate, videoDescription, videoPlayerThumbnail, videoComment, videoInputComment, videoSubmitCommentButton);

        CustomMediaPlayer.getInstance().setNewsPanel(panelNewsDetail, newsDetailThumbnail, newsDetailTitle, newsDetailDate, newsDetailDescription, loadingBar, newsScrollView, newsDetailLikeCount, newsDetailLikeButton, newsDetailShareFB, newsDetailShareTweeter, shareDialog, newsDetailComment, newsDetailInputComment, newsDetailSubmitCommentButton);

    }

    private void initSideMenu() {
        sideMenuHome = (LinearLayout) findViewById(R.id.side_menu_home);
        sideMenuLibrary = (LinearLayout) findViewById(R.id.side_menu_library);
        sideMenuShopMusic = (LinearLayout) findViewById(R.id.side_menu_shop_music);
        sideMenuShopVideo = (LinearLayout) findViewById(R.id.side_menu_shop_video);
        sideMenuDownloads = (LinearLayout) findViewById(R.id.side_menu_download);
        sideMenuSetting = (LinearLayout) findViewById(R.id.side_menu_setting);
        sideMenuSignOut = (LinearLayout) findViewById(R.id.side_menu_signout);
        sideMenuProfileButton = (RelativeLayout) findViewById(R.id.side_menu_profile_button);

        // Wallet
        sideMenuTopup = (LinearLayout) findViewById(R.id.side_menu_topup);
        sideMenuWallet = (TextView) findViewById(R.id.side_menu_user_wallet);

        // DATA
        sideMenuFullName = (TextView) findViewById(R.id.nav_header_user_name);
        sideMenuUserPic = (ImageView) findViewById(R.id.nav_header_user_prof_pic);

        sideMenuProfileButton.setOnClickListener(this);
        sideMenuTopup.setOnClickListener(this);
        sideMenuHome.setOnClickListener(this);
        sideMenuLibrary.setOnClickListener(this);
        sideMenuShopMusic.setOnClickListener(this);
        sideMenuShopVideo.setOnClickListener(this);
        sideMenuDownloads.setOnClickListener(this);
        sideMenuSetting.setOnClickListener(this);
        sideMenuSignOut.setOnClickListener(this);
    }

    private void setSideMenuData() {
        imageLoader.displayImage(userLoginData.getPhotoURL(), sideMenuUserPic, opts);
        if(userLoginData.getFullname().length() > 0)
            sideMenuFullName.setText(userLoginData.getFullname());
        else sideMenuFullName.setText("N/A");
        sideMenuWallet.setText("Rp " + userLoginData.getCredit());
    }

    private void initPanelNewPost() {
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(0, "Home", FragmentHome.newInstance(0, "Home")));
        list.add(new FragmentModel(1, "Community Music", FragmentHomeMusic.newInstance(0, "Community Music")));
        list.add(new FragmentModel(2, "Community Videos", FragmentHomeVideo.newInstance(0, "Community Videos")));
        list.add(new FragmentModel(3, "News", FragmentHomeNews.newInstance(3, "News")));

        viewPagerNewPost = (ViewPager) findViewById(R.id.pager_main_content);
        pagerAdapterHome = new CustomPagerAdapter(getSupportFragmentManager(), list);
        viewPagerNewPost.setAdapter(pagerAdapterHome);

        slidingTabNewPost = (TabLayout)findViewById(R.id.sliding_tab_new_post);
        slidingTabNewPost.setupWithViewPager(viewPagerNewPost);
    }

    private void initPanelLibrary() {
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(0, "Music", FragmentLibraryMusic.newInstance(0, "Music")));
        list.add(new FragmentModel(1, "Video", FragmentLibraryVideo.newInstance(1, "Video")));

        viewPagerLibrary = (ViewPager) findViewById(R.id.pager_library);
        pagerAdapterLibrary = new CustomPagerAdapter(getSupportFragmentManager(), list);
        viewPagerLibrary.setAdapter(pagerAdapterLibrary);

        slidingTabLibrary = (TabLayout) findViewById(R.id.sliding_tab_library);
        slidingTabLibrary.setupWithViewPager(viewPagerLibrary);

    }

    private void initPanelShopMusic() {
        // pool fragment
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(0, "Top Songs", FragmentShopMusicTopSongs.newInstance(0, "Top Songs")));
        list.add(new FragmentModel(1, "Top Albums", FragmentShopMusicTopAlbums.newInstance(1, "Top Albums")));
        list.add(new FragmentModel(2, "New Songs", FragmentShopMusicNewSongs.newInstance(2, "New Songs")));
        list.add(new FragmentModel(3, "All Songs", FragmentShopMusicAllSongs.newInstance(3, "All Songs")));

        viewPagerShopMusic = (ViewPager) findViewById(R.id.pager_shop_music);
        pagerAdapterShopMusic = new CustomPagerAdapter(getSupportFragmentManager(), list);
        viewPagerShopMusic.setAdapter(pagerAdapterShopMusic);

        slidingTabShopMusic = (TabLayout) findViewById(R.id.sliding_tab_shop_music);
        slidingTabShopMusic.setupWithViewPager(viewPagerShopMusic);
    }

    private void initPanelShopVideo() {
        // pool fragment
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(0, "Top Videos", FragmentShopVideoTopVideos.newInstance(0, "Top Videos")));
        list.add(new FragmentModel(1, "New Videos", FragmentShopVideoNewVideos.newInstance(1, "New Videos")));
        list.add(new FragmentModel(2, "All Videos", FragmentShopVideoAllVideos.newInstance(2, "All Videos")));

        viewPagerShopVideo = (ViewPager) findViewById(R.id.pager_shop_video);
        pagerAdapterShopVideo = new CustomPagerAdapter(getSupportFragmentManager(), list);
        viewPagerShopVideo.setAdapter(pagerAdapterShopVideo);

        slidingTabShopVideo = (TabLayout) findViewById(R.id.sliding_tab_shop_video);
        slidingTabShopVideo.setupWithViewPager(viewPagerShopVideo);
    }

    private void initPanelSetting() {
        final SettingPref settingPref = new SettingPref(getBaseContext());

        notifIsOn = settingPref.getNotifState();
        downloadWIFIOnlyOn = settingPref.getDowloadWifiOnlyState();

        settingToggleNotif = (RelativeLayout) findViewById(R.id.setting_toggle_notif);
        settingToggleDownload = (RelativeLayout) findViewById(R.id.setting_toggle_download);

        iconToggleNotif = (ImageView) findViewById(R.id.icon_toggle_notif);
        iconToggleDownload = (ImageView) findViewById(R.id.icon_toggle_download);

        settingSupportButton = (RelativeLayout) findViewById(R.id.setting_help_support_button);
        settingPrivacyButton = (RelativeLayout) findViewById(R.id.setting_privacy_policy_button);
        settingAboutButton = (RelativeLayout) findViewById(R.id.setting_about_button);

        if(notifIsOn) {
            iconToggleNotif.setImageResource(R.drawable.btn_toggle_on);
        } else {
            iconToggleNotif.setImageResource(R.drawable.btn_toggle_off);
        }

        if(downloadWIFIOnlyOn) {
            iconToggleDownload.setImageResource(R.drawable.btn_toggle_on);
        } else {
            iconToggleDownload.setImageResource(R.drawable.btn_toggle_off);
        }

        settingToggleNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifIsOn = !notifIsOn;
                settingPref.setNotifState(notifIsOn);

                if(notifIsOn) {
                    iconToggleNotif.setImageResource(R.drawable.btn_toggle_on);

                } else {
                    iconToggleNotif.setImageResource(R.drawable.btn_toggle_off);

                }
            }
        });

        settingToggleDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadWIFIOnlyOn = !downloadWIFIOnlyOn;
                settingPref.setDownloadWifiOnly(downloadWIFIOnlyOn);

                if(downloadWIFIOnlyOn) {
                    iconToggleDownload.setImageResource(R.drawable.btn_toggle_on);

                } else {
                    iconToggleDownload.setImageResource(R.drawable.btn_toggle_off);

                }
            }
        });

        settingSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInBrowser(Consts.URL_HELP_SUPPORT);
            }
        });

        settingPrivacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInBrowser(Consts.URL_PRIVACY_POLICY);
            }
        });

        settingAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInBrowser(Consts.URL_ABOUT);
            }
        });
    }

    private void openInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void initUploadForm() {
        panelUpload = (RelativeLayout) findViewById(R.id.upload_form);

        uploadFileName = (TextView) findViewById(R.id.upload_file_name);
        uploadProgress = (SeekBar) findViewById(R.id.upload_seek_bar);
        uploadButtonPause = (RelativeLayout) findViewById(R.id.upload_button_pause);
        uploadButtonCancel = (RelativeLayout) findViewById(R.id.upload_button_cancel);
        uploadButton = (RelativeLayout) findViewById(R.id.upload_button);
        uploadButtonText = (TextView) findViewById(R.id.upload_button_text);
        uploadInputTitle = (EditText) findViewById(R.id.upload_input_title);
        uploadInputDescription = (EditText) findViewById(R.id.upload_description);

        uploadButtonPause.setOnClickListener(this);
        uploadButtonCancel.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
    }

    private void initPanelProfile() {

        profilePicture = (ImageView) findViewById(R.id.profile_preview_prof_pic);
        profileName = (EditText) findViewById(R.id.profile_preview_name);
        profileCountry = (EditText) findViewById(R.id.profile_preview_country);
        profileCity = (EditText) findViewById(R.id.profile_preview_city);
        profileDescription = (EditText) findViewById(R.id.profile_preview_description);

        profileLikeCount = (TextView) findViewById(R.id.profile_preview_like_count_text);
        profileMusicCount = (TextView) findViewById(R.id.profile_preview_music_count_text);
        profileVideoCount = (TextView) findViewById(R.id.profile_preview_video_count_text);

        profileName.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_REGULAR));
        profileCity.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));
        profileCountry.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));
        profileDescription.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));

        profileLikeCount.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));
        profileMusicCount.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));
        profileVideoCount.setTypeface(FontLoader.getTypeFace(this, FontLoader.FontType.HEADLINE_LIGHT));

        editName = (ImageView) findViewById(R.id.profile_preview_name_edit);
        editCountry = (ImageView) findViewById(R.id.profile_preview_country_edit);
        editCity = (ImageView) findViewById(R.id.profile_preview_city_edit);
        profileEdit = (ImageView) findViewById(R.id.profile_preview_edit);

        profileEdit.setOnClickListener(this);
        profilePicture.setOnClickListener(this);

        // pool fragment TEST
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(0, "Music", FragmentUploadedMusic.newInstance(0, "Music")));
        list.add(new FragmentModel(1, "Video", FragmentUploadedVideo.newInstance(1, "Video")));

        viewPagerUploaded = (ViewPager) findViewById(R.id.pager_uploaded);
        pagerAdapterUploaded = new CustomPagerAdapter(getSupportFragmentManager(), list);
        viewPagerUploaded.setAdapter(pagerAdapterUploaded);

        slidingTabUploaded = (TabLayout) findViewById(R.id.sliding_tab_uploaded);
        slidingTabUploaded.setupWithViewPager(viewPagerUploaded);

    }

    public void showPanelProfile() {
        UserProfileData data = DataPool.getInstance().userProfileData;

        imageLoader.displayImage(data.photoURL, profilePicture, opts);
        if(data.fullName != null && data.fullName.length() > 0)
            profileName.setText(data.fullName);
        else
            profileName.setText("N/A");
        if(data.locationCity != null && data.locationCity.length() > 0)
            profileCity.setText(data.locationCity);
        else
            profileCity.setText("N/A");

        if(data.locationCountry != null && data.locationCountry.length() > 0)
            profileCountry.setText(data.locationCountry);
        else
            profileCountry.setText("N/A");
        profileLikeCount.setText(String.valueOf(data.totalLikes));
        profileMusicCount.setText(String.valueOf(data.totalMusic));
        profileVideoCount.setText(String.valueOf(data.totalVideo));

        if(data.profile != null && data.profile.length() > 0)
            profileDescription.setText(data.profile);
        else
            profileDescription.setText("N/A");
    }

    private void initPanelUpload() {
        uploadInputTitle = (EditText) findViewById(R.id.upload_input_title);
        uploadInputDescription = (EditText) findViewById(R.id.upload_description);
    }

    private void initPanelTopup() {
        topupButton3k = (RelativeLayout) findViewById(R.id.topup_btn_3k);
        topupButton5k = (RelativeLayout) findViewById(R.id.topup_btn_5k);
        topupButton10k = (RelativeLayout) findViewById(R.id.topup_btn_10k);
        topupButton20k = (RelativeLayout) findViewById(R.id.topup_btn_20k);
        topupButton50k = (RelativeLayout) findViewById(R.id.topup_btn_50k);

        topupButton3k.setOnClickListener(this);
        topupButton5k.setOnClickListener(this);
        topupButton10k.setOnClickListener(this);
        topupButton20k.setOnClickListener(this);
        topupButton50k.setOnClickListener(this);
    }

    private void initPanelSearch() {
        // pool fragment
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(0, "SHOP", FragmentSearchShop.newInstance(0, "Shop")));
        list.add(new FragmentModel(1, "COMMUNITY", FragmentSearchCommunity.newInstance(1, "Community")));

        viewPagerSearch = (ViewPager) findViewById(R.id.pager_search);
        pagerAdapterSearch = new CustomPagerAdapter(getSupportFragmentManager(), list);
        viewPagerSearch.setAdapter(pagerAdapterSearch);

        slidingTabSearch = (TabLayout) findViewById(R.id.sliding_tab_search);
        slidingTabSearch.setupWithViewPager(viewPagerSearch);

        searchInput = (EditText) findViewById(R.id.search_input);
        searchPanelButton = (RelativeLayout) findViewById(R.id.search_panel_button);

        searchPanelButton.setOnClickListener(this);
    }

    private void showMusicDetail() {
        if(CustomMediaPlayer.getInstance().trackType() == CustomMediaPlayer.MusicType.SHOP) {
            // SHOP
            popupArtistSong.showPopupArtistSong(CustomMediaPlayer.getInstance().getSelectedMusicData());
        } else if(CustomMediaPlayer.getInstance().trackType() == CustomMediaPlayer.MusicType.COMMUNITY) {
            // COMMUNITY
            popupCommunitySong.showPopupCommunity(CustomMediaPlayer.getInstance().getSelectedCommunityData());
        } else if(CustomMediaPlayer.getInstance().trackType() == CustomMediaPlayer.MusicType.ALBUM_ITEM) {
            popupArtistSong.showPopupArtistSong(CustomMediaPlayer.getInstance().getSelectedAlbumItemData(), null);
        } else {
            // LIBRARY
            popupArtistSong.showPopupArtistSong(CustomMediaPlayer.getInstance().getSelectedLibraryData());
        }
    }

    private void showFileChooser(boolean isMusic) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if(isMusic)
            intent.setType("audio/*");
        else
            intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Select a file to upload"), UPLOAD_FILE_CODE);

    }

    public String getUploadPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Audio.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!billingProc.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
        Log.d("ACTIVITY RESULT", requestCode + " | result : " + resultCode);
        if(requestCode == UPLOAD_FILE_CODE) {
            if(resultCode == RESULT_OK) {
                Uri uri = data.getData();

                String filePath = FilePath.getPath(this, uri);
                tempFile = new File(filePath);

                showUploadForm(filePath);
            }
        }

        if(requestCode == UPLOAD_PROFPIC) {
            if(resultCode == RESULT_OK) {
                Uri photoURI = data.getData();

                String filePath = FilePath.getPath(this, photoURI);
                File profPic = new File(filePath);
                ApiManager.getInstance().changeProfilePicture(profPic);
            }
        }

    }

    private void showUploadForm(String fileName) {
        panelUpload.setVisibility(View.VISIBLE);
        isUploadFormShowing = true;

        uploadFileName.setText(fileName);
        uploadProgress.setProgress(0);
        uploadButtonText.setText("SUBMIT");
        uploadButtonText.setTextColor(getResources().getColor(R.color.white_font));
    }

    private void closeUploadForm() {
        panelUpload.setVisibility(View.INVISIBLE);
        isUploadFormShowing = false;
    }

    private RelativeLayout getCurrentPanel(PanelState state) {
        switch (state) {
            case PANEL_NEW_POST:
                Log.d("CURRENT PANEL", "HOME");
                return panelNewPost;

            case PANEL_LIBRARY:
                Log.d("CURRENT PANEL", "LIBRARY");
                return panelLibrary;

            case PANEL_SHOP_MUSIC:
                Log.d("CURRENT PANEL", "SHOP MUSIC");
                return panelShopMusic;

            case PANEL_SHOP_VIDEO:
                Log.d("CURRENT PANEL", "SHOP VIDEO");
                return panelShopVideo;

            case PANEL_DOWNLOAD:
                Log.d("CURRENT PANEL", "DOWNLOAD");
                return panelDownloads;

            case PANEL_SETTING:
                Log.d("CURRENT PANEL", "SETTING");
                return panelSettings;

            case PANEL_PROFILE:
                Log.d("CURRENT PANEL", "PROFILE");
                return panelProfile;

            case PANEL_TOPUP:
                Log.d("CURRENT PANEL", "TOPUP");
                return panelTopup;

            case PANEL_SEARCH:
                Log.d("CURRENT PANEL", "SEARCH");
                return panelSearch;
        }
        return null;
    }

    private void changePanel(PanelState toState) {

        getCurrentPanel(panelState).setVisibility(View.INVISIBLE);

        switch (toState) {
            case PANEL_NEW_POST:
                panelNewPost.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_NEW_POST;
                fabMenu.setVisibility(View.VISIBLE);

                getSupportActionBar().setTitle("NEO DANGDUT");
                searchButton.setVisible(true);
                break;

            case PANEL_LIBRARY:
                panelLibrary.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_LIBRARY;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("LIBRARY");
                searchButton.setVisible(true);
                break;

            case PANEL_SHOP_MUSIC:
                panelShopMusic.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_SHOP_MUSIC;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("SHOP");
                searchButton.setVisible(true);
                break;

            case PANEL_SHOP_VIDEO:
                panelShopVideo.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_SHOP_VIDEO;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("SHOP");
                searchButton.setVisible(true);
                break;

            case PANEL_DOWNLOAD:
                panelDownloads.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_DOWNLOAD;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("DOWNLOADS");
                searchButton.setVisible(true);
                break;

            case PANEL_SETTING:
                panelSettings.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_SETTING;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("SETTINGS");
                searchButton.setVisible(true);
                break;

            case PANEL_PROFILE:
                panelProfile.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_PROFILE;
                fabMenu.setVisibility(View.INVISIBLE);

                canEditProfile = false;
                profileName.setEnabled(false);
                profileCountry.setEnabled(false);
                profileCity.setEnabled(false);

                editName.setVisibility(View.INVISIBLE);
                editCountry.setVisibility(View.INVISIBLE);
                editCity.setVisibility(View.INVISIBLE);

                showPanelProfile();

                getSupportActionBar().setTitle("MY PROFILE");
                searchButton.setVisible(true);
                break;

            case PANEL_TOPUP:
                panelTopup.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_TOPUP;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("TOPUP");
                searchButton.setVisible(true);
                break;

            case PANEL_SEARCH:
                panelSearch.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_SEARCH;
                fabMenu.setVisibility(View.INVISIBLE);

                getSupportActionBar().setTitle("SEARCH");
                searchButton.setVisible(true);
        }
    }

    private boolean isStoragePermissionGranted() {
        if(Build.VERSION.SDK_INT >= 23) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "GRANTED");
                return true;
            } else {
                Log.d("Permission", "NOT GRANTED");
                if(!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // TODO change message
                    showMessageRequestPermission();
                }

                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_STORAGE_PERMISSION_CODE);
                return false;
            }
        } else {
            Log.d("Permission", "GRANTED");
            return true;
        }
    }

    public void goToLibrary(boolean isVideo) {
        if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
            CustomMediaPlayer.getInstance().closeNewsDetail();
        if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
            CustomMediaPlayer.getInstance().closeVideoPlayer();
        changePanel(PanelState.PANEL_LIBRARY);

        FragmentLibraryMusic fragLibMusic = (FragmentLibraryMusic) pagerAdapterLibrary.getItem(0);
        fragLibMusic.reloadData();

        FragmentLibraryVideo fragLibVideo = (FragmentLibraryVideo) pagerAdapterLibrary.getItem(1);
        fragLibVideo.reloadData();

        if(isVideo)
            viewPagerLibrary.setCurrentItem(1);
        else
            viewPagerLibrary.setCurrentItem(0);
    }

    private void showMessageRequestPermission() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("You need to allow access to storage to download musics and videos")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_STORAGE_PERMISSION_CODE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_STORAGE_PERMISSION_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "GRANTED");
            } else {
                Log.d("Permission", "NOT GRANTED");
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(CustomMediaPlayer.getInstance().isVideoPlayerShowing) {
            CustomMediaPlayer.getInstance().closeVideoPlayer();
        } else if(CustomMediaPlayer.getInstance().isNewsDetailShowing) {
            CustomMediaPlayer.getInstance().closeNewsDetail();
        } else if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isUploadFormShowing) {
            closeUploadForm();
        }else if(getCurrentPanel(panelState) != panelNewPost) {
            viewPagerNewPost.setCurrentItem(0);

            setHomeListener();

            changePanel(PanelState.PANEL_NEW_POST);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {
            if(fabMenu.isExpanded()) {
                fabMenu.collapse();
            }else if(CustomMediaPlayer.getInstance().isAudioPlayerShowing) {
                if (CustomMediaPlayer.getInstance().isTrackPaused())
                    CustomMediaPlayer.getInstance().closeMusicPlayer();
                else
                    moveTaskToBack(true);
            } else {

                popupExit.showPopupExit("Quit Neo Dangdut?", "Not Now", "Yes");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        searchButton = menu.findItem(R.id.action_search);
        searchButton.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            if(CustomMediaPlayer.getInstance().isNewsDetailShowing)
                CustomMediaPlayer.getInstance().closeNewsDetail();
            if(CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                CustomMediaPlayer.getInstance().closeVideoPlayer();

            FragmentSearchShop tempSearchShop = (FragmentSearchShop) pagerAdapterSearch.getItem(0);
            FragmentSearchCommunity tempSearchComm =  (FragmentSearchCommunity) pagerAdapterSearch.getItem(1);

            tempSearchShop.clearList();
            tempSearchComm.clearList();

            searchInput.setText("");

            changePanel(PanelState.PANEL_SEARCH);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if(isNetworkAvailable()) {
            // SIDE MENU
            if (view == sideMenuTopup) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();

                changePanel(PanelState.PANEL_TOPUP);
            }

            if (view == sideMenuHome) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_NEW_POST);

                setHomeListener();
            }

            if (view == sideMenuLibrary) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_LIBRARY);

                FragmentLibraryMusic fragLibMusic = (FragmentLibraryMusic) pagerAdapterLibrary.getItem(0);
                fragLibMusic.reloadData();

                FragmentLibraryVideo fragLibVideo = (FragmentLibraryVideo) pagerAdapterLibrary.getItem(1);
                fragLibVideo.reloadData();
            }

            if (view == sideMenuShopMusic) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_SHOP_MUSIC);

                FragmentShopMusicTopSongs temp = (FragmentShopMusicTopSongs) pagerAdapterShopMusic.getItem(0);
                temp.refreshListview();
            }

            if (view == sideMenuShopVideo) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_SHOP_VIDEO);

                FragmentShopVideoTopVideos temp = (FragmentShopVideoTopVideos) pagerAdapterShopVideo.getItem(0);
                temp.refreshListview();
            }

            if (view == sideMenuDownloads) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_DOWNLOAD);
            }

            if (view == sideMenuSetting) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_SETTING);
            }

            if (view == sideMenuSignOut) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();

                userLoginData.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
            }

            // UPLOAD
            if (view == uploadButton) {
                String fileName = uploadInputTitle.getText().toString();
                String fileDescription = uploadInputDescription.getText().toString();

                ApiManager.getInstance().setOnUploadListener(new ApiManager.OnUpload() {
                    @Override
                    public void onSucceed() {
                        popupLoading.closePopupLoading();
                    }

                    @Override
                    public void onFailed(String message) {
                        Log.d("UPLOAD FAILED", message);
                        popupLoading.setMessage("Upload Failed");
                    }
                });

                if (isUploadMusic) {
                    ApiManager.getInstance().uploadContent(tempFile, "music", fileName, fileDescription);
                } else {
                    ApiManager.getInstance().uploadContent(tempFile, "video", fileName, fileDescription);
                }

                popupLoading.showPopupLoading("Uploading..");

            }
            // UPLOAD END

            // Profile
            if (view == sideMenuProfileButton) {
                if (CustomMediaPlayer.getInstance().isNewsDetailShowing)
                    CustomMediaPlayer.getInstance().closeNewsDetail();
                if (CustomMediaPlayer.getInstance().isVideoPlayerShowing)
                    CustomMediaPlayer.getInstance().closeVideoPlayer();
                changePanel(PanelState.PANEL_PROFILE);

            }

            if (view == profileEdit) {
                canEditProfile = !canEditProfile;
                profileName.setEnabled(canEditProfile);
                profileCountry.setEnabled(canEditProfile);
                profileCity.setEnabled(canEditProfile);
                profileDescription.setEnabled(canEditProfile);

                if (canEditProfile) {
                    editName.setVisibility(View.VISIBLE);
                    editCountry.setVisibility(View.VISIBLE);
                    editCity.setVisibility(View.VISIBLE);
                } else {
                    editName.setVisibility(View.INVISIBLE);
                    editCountry.setVisibility(View.INVISIBLE);
                    editCity.setVisibility(View.INVISIBLE);

                    String name = profileName.getText().toString();
                    String city = profileCity.getText().toString();
                    String country = profileCountry.getText().toString();
                    String profileDesc = profileDescription.getText().toString();
                    ApiManager.getInstance().updateUserData(name, city, country, profileDesc);
                }
            }

            if(view == profilePicture) {
                if(canEditProfile) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    startActivityForResult(Intent.createChooser(intent, "Select picture"), UPLOAD_PROFPIC);
                }
            }
            // Profile END

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            // SIDE MENU END


            // TOPUP
            if (view == topupButton3k) {
                ApiManager.getInstance().topUpCredit("neo3000");
            }

            if (view == topupButton5k) {
                ApiManager.getInstance().topUpCredit("neo5000");
            }

            if (view == topupButton10k) {
                ApiManager.getInstance().topUpCredit("neo10000");
            }

            if (view == topupButton20k) {
                ApiManager.getInstance().topUpCredit("neo20000");
            }

            if (view == topupButton50k) {
                ApiManager.getInstance().topUpCredit("neo50000");
            }
            // TOPUP END

            // SEARCH
            if (view == searchPanelButton) {
                searchKey = searchInput.getText().toString();

                FragmentSearchShop tempSearchShop = (FragmentSearchShop) pagerAdapterSearch.getItem(0);
                FragmentSearchCommunity tempSearchComm = (FragmentSearchCommunity) pagerAdapterSearch.getItem(1);

                tempSearchShop.clearList();
                tempSearchComm.clearList();

                tempSearchShop.setSearchKey(searchKey);
                tempSearchComm.setSearchKey(searchKey);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                setHomeListener();

                ApiManager.getInstance().getUserAccessToken();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Connection Error")
                    .setMessage("No Internet Connection")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isNetworkAvailable())
                                ApiManager.getInstance().getUserAccessToken();
                        }

                    })
                    .setNegativeButton("Close", null)
                    .show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d("PURCHASE", productId);
        Log.d("PURCHASE", details.purchaseInfo.responseData);
        Log.d("PURCHASE", details.purchaseInfo.signature);

        sku = productId;
        originalJSON = details.purchaseInfo.responseData;
        purchaseSignature = details.purchaseInfo.signature;

        // TODO lempar ke api
        ApiManager.getInstance().getUserAccessToken();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d("PURCHASE", "ERROR");
    }

    @Override
    public void onBillingInitialized() {
        Log.d("BILLING", "OKAY");
    }

    @Override
    protected void onDestroy() {
        if(billingProc != null)
            billingProc.release();

        super.onDestroy();
    }
}
