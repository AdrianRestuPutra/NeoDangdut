package com.pitados.neodangdut;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class Consts {
    public static String FRAGMENT_PAGE_NUMBER_KEY = "PageNumber";
    public static String FRAGMENT_PAGE_TITLE_KEY = "PageTitle";

    // Directory
    public static String APP_BASE_DIR = "Directory";

    // Notification
    public static String NOTIF_DOWNLOAD_TITLE = "Download File";
    public static String NOTIF_DOWNLOAD_DESCRIPTION = "Downloading in Progress..";

    public static String AUDIO_SAMPLE_URL = "https://archive.org/download/count_monte_cristo_0711_librivox/count_of_monte_cristo_001_dumas.mp3";
    public static String VIDEO_SAMPLE_URL = "https://ia600208.us.archive.org/4/items/Popeye_forPresident/Popeye_forPresident_512kb.mp4";

    // API
    public static String TAG_API_TOKEN = "get_token";
    public static String TAG_API_HOME = "get_home";
    public static String TAG_API_COMMUNITY_MUSIC = "get_comm_music";
    public static String TAG_API_COMMUNITY_VIDEO = "get_comm_video";
    public static String TAG_API_SHOP_MUSIC_TOPSONGS = "get_shop_top";


    public static String URL_GET_TOKEN = "http://api.neodangdut.com";
    public static String URL_GET_BANNER = URL_GET_TOKEN+"/banner";

    public static String URL_GET_MUSIC = URL_GET_TOKEN+"/music";
    public static String URL_GET_VIDEO = URL_GET_TOKEN+"/video";
    public static String URL_GET_NEWS = URL_GET_TOKEN+"/article";
    public static String URL_GET_COMMUNITY_CONTENT = URL_GET_TOKEN+"/content";
    public static String URL_GET_ALBUM = URL_GET_TOKEN+"/album";

    // TAG TOKEN
    public static String TAG_TOKEN = "access_token";
    public static String TAG_TOKEN_TYPE = "token_type";
    public static String TAG_BANNER = "banner";

}
