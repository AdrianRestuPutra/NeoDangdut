package com.pitados.neodangdut.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.EMVideoView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomPagerAdapter;
import com.pitados.neodangdut.fragments.FragmentHome;
import com.pitados.neodangdut.fragments.FragmentHomeMusic;
import com.pitados.neodangdut.fragments.FragmentHomeNews;
import com.pitados.neodangdut.fragments.FragmentHomeVideo;
import com.pitados.neodangdut.fragments.FragmentLibraryMusic;
import com.pitados.neodangdut.fragments.FragmentLibraryVideo;
import com.pitados.neodangdut.fragments.FragmentShopMusicAllSongs;
import com.pitados.neodangdut.fragments.FragmentShopMusicNewSongs;
import com.pitados.neodangdut.fragments.FragmentShopMusicTopAlbums;
import com.pitados.neodangdut.fragments.FragmentShopMusicTopSongs;
import com.pitados.neodangdut.fragments.FragmentShopVideoAllVideos;
import com.pitados.neodangdut.fragments.FragmentShopVideoNewVideos;
import com.pitados.neodangdut.fragments.FragmentShopVideoTopVideos;
import com.pitados.neodangdut.model.FragmentModel;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.StateManager;

import org.onepf.oms.OpenIabHelper;
import org.onepf.oms.appstore.googleUtils.IabHelper;
import org.onepf.oms.appstore.googleUtils.IabResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int REQUEST_STORAGE_PERMISSION_CODE = 100;
    private enum PanelState {
        PANEL_NEW_POST,
        PANEL_LIBRARY,
        PANEL_SHOP_MUSIC,
        PANEL_SHOP_VIDEO,
        PANEL_DOWNLOAD
    }
    private PanelState panelState;

    private DrawerLayout drawer;
    private FloatingActionsMenu fabMenu;
    private com.getbase.floatingactionbutton.FloatingActionButton fabMusic, fabVideo;
    // Panel
    private RelativeLayout panelNewPost, panelLibrary, panelShopMusic, panelShopVideo, panelDownloads;
    // Media Panel
    private LinearLayout panelMusicPlayer;
    private RelativeLayout musicPlayerPauseButton, musicPlayerShuffleButton, musicPlayerLoopbutton;
    private ImageView musicPlayerPauseIcon;
    private ImageView musicPlayerThumbnail;
    private TextView musicPlayerTitle, musicPlayerArtistName, musicPlayerDuration;
    private SeekBar musicPlayerProgress;
    // VIDEO
    private RelativeLayout panelVideoPlayer;
    private EMVideoView videoView;
    // TODO widget video
    private TextView videoDate, videoDescription;
    // NEWS
    private RelativeLayout panelNewsDetail;
    private ImageView newsDetailThumbnail;
    private TextView newsDetailTitle;
    private TextView newsDetailDate;
    private TextView newsDetailDescription;

    // Fragments
    private ViewPager viewPagerNewPost, viewPagerLibrary, viewPagerShopMusic, viewPagerShopVideo;
    private TabLayout slidingTabNewPost, slidingTabLibrary, slidingTabShopMusic, slidingTabShopVideo;
    private CustomPagerAdapter pagerAdapterHome, pagerAdapterLibrary, pagerAdapterShopMusic, pagerAdapterShopVideo;

    // In App Purchase
    private String storeKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5F8fASyrDFdaXrkoW8kNtwH5JIkLnNuTD5uE1a37TbI5LDZRVgvMIYAtZ9CAHAfLnJ6OEZt0lvLLJSKVuS47VqYVhGZciOkX8TEihONBRwis6i9A3JnKfyqm0iiT+P0CEktOLuFLROIo13utCIO++6h7A7/WLfxNV+Jnxfs9OEHyyPS+MdHxa0wtZGeAGiaN65BymsBQo7J/ABt2DFyMJP1R/nJM45F8yu4D6wSkUNKzs/QbPfvHJQzq56/B/hbx59EkzkInqC567hrlUlX4bU5IvOTF/B1G+UMuKg80m3I1IcQk4FD2D9oJ3E+8IXG/1UdejrOsmqDAzE7LkMl8xwIDAQAB";
    private OpenIabHelper iabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isStoragePermissionGranted();

        initOpenIAB();

        // init panel
        panelNewPost = (RelativeLayout) findViewById(R.id.panel_new_post);
        panelLibrary = (RelativeLayout) findViewById(R.id.panel_library);
        panelShopMusic = (RelativeLayout) findViewById(R.id.panel_shop_music);
        panelShopVideo = (RelativeLayout) findViewById(R.id.panel_shop_video);
        panelDownloads = (RelativeLayout) findViewById(R.id.panel_downloads);
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

        musicPlayerPauseIcon = (ImageView) findViewById(R.id.music_player_pause_icon);
        musicPlayerPauseButton = (RelativeLayout) findViewById(R.id.music_player_pause);
        musicPlayerPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomMediaPlayer.getInstance().stopTrack();
            }
        });
        musicPlayerProgress = (SeekBar) findViewById(R.id.music_player_progress);
        musicPlayerThumbnail = (ImageView) findViewById(R.id.music_player_thumbnail);
        musicPlayerShuffleButton = (RelativeLayout) findViewById(R.id.music_player_random);
        musicPlayerLoopbutton = (RelativeLayout) findViewById(R.id.music_player_loop);
        musicPlayerDuration = (TextView) findViewById(R.id.music_player_duration);

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

        // TODO insert widget id
        videoView = (EMVideoView) findViewById(R.id.video_player_view);
        videoDate = (TextView) findViewById(R.id.video_player_detail_date);
        videoDate = (TextView) findViewById(R.id.video_player_detail_description);


        panelNewsDetail = (RelativeLayout) findViewById(R.id.news_detail);
        newsDetailThumbnail = (ImageView) findViewById(R.id.news_detail_thumbnail);
        newsDetailTitle = (TextView) findViewById(R.id.news_detail_title);
        newsDetailDate = (TextView) findViewById(R.id.news_detail_date);
        newsDetailDescription = (TextView) findViewById(R.id.news_detail_description);

        CustomMediaPlayer.getInstance().setAudioPanel(this, panelMusicPlayer, musicPlayerProgress, musicPlayerDuration,
                musicPlayerThumbnail, musicPlayerTitle, musicPlayerArtistName, musicPlayerPauseIcon);

        CustomMediaPlayer.getInstance().setVideoPanel(panelVideoPlayer, videoView, videoDate, videoDescription);

        CustomMediaPlayer.getInstance().setNewsPanel(panelNewsDetail, newsDetailThumbnail, newsDetailTitle, newsDetailDate, newsDetailDescription);

        // init state
        panelState = PanelState.PANEL_NEW_POST;
        ConnManager.getInstance().init(getBaseContext());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        // Set base directory
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            Consts.APP_BASE_DIR = pi.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // TODO handle floating button
//        uploadFab = (FloatingActionButton) findViewById(R.id.fab_upload);
//        uploadFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO show dialog to upload / upload screen
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMusic = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_upload_music);
        fabMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Upload Music", Snackbar.LENGTH_SHORT).show();
            }
        });

        fabVideo = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_upload_video);
        fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Upload Video", Snackbar.LENGTH_SHORT).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // TODO init new post
        initPanelNewPost();
        // TODO init library
        initPanelLibrary();
        // TODO init shop
        initPanelShopMusic();
        initPanelShopVideo();
        // TODO init download

        // Load All data
        ApiManager.getInstance().getToken();
        ApiManager.getInstance().setOnTokenReceived(new ApiManager.OnTokenReceived() {
            @Override
            public void onTokenSaved() {
                ApiManager.getInstance().getHomeBanner();
                ApiManager.getInstance().getHomeTopMusic();
                ApiManager.getInstance().getHomeTopVideos();
                ApiManager.getInstance().getHomeLatestNews();
                // Community
                ApiManager.getInstance().getCommunityMusic();
                ApiManager.getInstance().getCommunityVideo();
                ApiManager.getInstance().getAllNews();
                // SHOP MUSIC
                ApiManager.getInstance().getShopMusicTopSongs();
                ApiManager.getInstance().getShopMusicTopAlbums();
                ApiManager.getInstance().getShopMusicNewSongs();
//                ApiManager.getInstance().getShopMusicAllSongs();
                // SHOP VIDEO
                ApiManager.getInstance().getShopVideoTopVideos();
                ApiManager.getInstance().getShopVideoNewVideos();
//                ApiManager.getInstance().getShopVideoAllVideos();
                // TODO library

            }
        });
    }

    private void initOpenIAB() {
        OpenIabHelper.Options opts = new OpenIabHelper.Options.Builder()
                .addStoreKey(OpenIabHelper.NAME_GOOGLE, storeKey)
                .addAvailableStoreNames(OpenIabHelper.NAME_GOOGLE)
                .addPreferredStoreName(OpenIabHelper.NAME_GOOGLE)
                .setVerifyMode(OpenIabHelper.Options.VERIFY_EVERYTHING)
                .setStoreSearchStrategy(OpenIabHelper.Options.SEARCH_STRATEGY_INSTALLER)
                .build();

        iabHelper = new OpenIabHelper(getBaseContext(), opts);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if(result.isSuccess()) {
                    // TODO query inventory
                    Log.d("Open IAB", "Open IAB ready!");
                } else {
                    Log.e("Open IAB", result.getMessage());
                }
            }
        });
    }

    // Method init
    private void initPanelNewPost() {
        // pool fragment
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
        // pool fragment TEST
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

                // TODO load data
                break;

            case PANEL_LIBRARY:
                panelLibrary.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_LIBRARY;
                fabMenu.setVisibility(View.INVISIBLE);

                // TODO load data music
                break;

            case PANEL_SHOP_MUSIC:
                panelShopMusic.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_SHOP_MUSIC;
                fabMenu.setVisibility(View.INVISIBLE);

//                FragmentShopMusicTopSongs tempFragment = (FragmentShopMusicTopSongs) pagerAdapterShopMusic.getItem(0);
//                tempFragment.loadData();
                break;

            case PANEL_SHOP_VIDEO:
                panelShopVideo.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_SHOP_VIDEO;
                fabMenu.setVisibility(View.INVISIBLE);

                // TODO load data
                break;

            case PANEL_DOWNLOAD:
                panelDownloads.setVisibility(View.VISIBLE);
                panelState = PanelState.PANEL_DOWNLOAD;
                fabMenu.setVisibility(View.INVISIBLE);
                break;
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
                // TODO notif user
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(CustomMediaPlayer.getInstance().isVideoPlayerShowing) {
            CustomMediaPlayer.getInstance().closeVideoPlayer();
        } else if(CustomMediaPlayer.getInstance().isNewsDetailShowing) {
            CustomMediaPlayer.getInstance().closeNewsDetail();
        } else {
            DataPool.getInstance().listHomeBanner.clear();
            DataPool.getInstance().listHomeTopVideos.clear();
            DataPool.getInstance().listHomeTopMusic.clear();
            MainActivity.this.finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            // TODO handle search
            Toast.makeText(getBaseContext(), "search", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_post) {
            // TODO handle new post
            changePanel(PanelState.PANEL_NEW_POST);
            Toast.makeText(getBaseContext(), "New Post", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_library) {
            // TODO handle library
            changePanel(PanelState.PANEL_LIBRARY);
            Toast.makeText(getBaseContext(), "Library", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_music) {
            // TODO handle shop music
            changePanel(PanelState.PANEL_SHOP_MUSIC);
            Toast.makeText(getBaseContext(), "Shop Music", Toast.LENGTH_SHORT).show();
            // TODO to music tab
        } else if (id == R.id.nav_video) {
            // TODO handle shop video
            changePanel(PanelState.PANEL_SHOP_VIDEO);
            Toast.makeText(getBaseContext(), "Shop Video", Toast.LENGTH_SHORT).show();
            // TODO to video tab
        } else if (id == R.id.nav_downloads) {
            // TODO handle downloads
            changePanel(PanelState.PANEL_DOWNLOAD);
            Toast.makeText(getBaseContext(), "Download", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            // TODO handle settings
        } else if (id == R.id.nav_signout) {
            // TODO handle signout
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
