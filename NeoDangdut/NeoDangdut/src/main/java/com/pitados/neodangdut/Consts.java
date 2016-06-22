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
    public static String NOTIF_DOWNLOAD_TITLE = "Download File : ";
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

    public static String URL_SIGNUP = URL_GET_TOKEN+"/user/signup";

    public static String URL_GET_USER_DATA = URL_GET_TOKEN+"/me";

    public static String URL_GET_MUSIC = URL_GET_TOKEN+"/music";
    public static String URL_GET_VIDEO = URL_GET_TOKEN+"/video";
    public static String URL_GET_NEWS = URL_GET_TOKEN+"/article";
    public static String URL_GET_COMMUNITY_CONTENT = URL_GET_TOKEN+"/content";
    public static String URL_GET_ALBUM = URL_GET_TOKEN+"/album";
    public static String URL_GET_LIBRARY = URL_GET_TOKEN+"/me/history";

    public static String URL_LIKE = URL_GET_TOKEN+"/me/like/";
    public static String URL_UNLIKE = URL_GET_TOKEN+"/me/unlike/";
    public static String URL_PURCHASE = URL_GET_TOKEN+"/me/purchase";
    public static String URL_ADD_LISTEN = URL_GET_TOKEN+"/content/listen";
    public static String URL_CHANGE_PHOTO = URL_GET_TOKEN+"/me/photo";
    public static String URL_UPDATE_PROFILE = URL_GET_TOKEN+"/me/update";

    public static String URL_CONFIRM = URL_GET_TOKEN+"/IAP/android";

    public static String URL_HELP_SUPPORT = "http://neodangdut.com/help";
    public static String URL_PRIVACY_POLICY = "http://neodangdut.com/privacy-policy";
    public static String URL_ABOUT = "http://neodangdut.com/about";


    // TAG TOKEN
    public static String TAG_TOKEN = "access_token";
    public static String TAG_TOKEN_TYPE = "token_type";
    public static String TAG_BANNER = "banner";

}
