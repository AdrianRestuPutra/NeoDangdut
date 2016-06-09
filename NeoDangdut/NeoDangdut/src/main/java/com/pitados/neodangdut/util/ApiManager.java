package com.pitados.neodangdut.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.model.AlbumData;
import com.pitados.neodangdut.model.BannerModel;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.NewsData;
import com.pitados.neodangdut.model.UserLoginData;
import com.pitados.neodangdut.model.VideoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adrianrestuputranto on 5/17/16.
 */
public class ApiManager {
    public enum ApiType {
        HOME_BANNER,
        HOME_TOP_MUSIC,
        HOME_TOP_VIDEO,
        HOME_LATEST_NEWS,
        COMMUNITY_MUSIC,
        COMMUNITY_VIDEO,
        COMMUNITY_NEWS,
        LIBRARY_MUSIC,
        LIBRARY_VIDEO,
        SHOP_MUSIC_TOP_SONG,
        SHOP_MUSIC_TOP_ALBUM,
        SHOP_MUSIC_NEW_SONGS,
        SHOP_MUSIC_ALL_SONG,
        SHOP_VIDEO_TOP_VIDEO,
        SHOP_VIDEO_NEW_VIDEO,
        SHOP_VIDEO_ALL_VIDEO
    }

    public enum LikeType {
        ARTICLE,
        MUSIC,
        VIDEO,
        USER
    }

    public enum PurchaseType {
        SINGLE,
        ALBUM
    }

    private Context context;

    private String CLIENT_ID = "15299018236589";
    private String CLIENT_SECRET = "7521e4a1c2b5cbf67024b3f9b1b4f7e1";

    // Listener
    private OnTokenReceived listener;
    private OnUserAccessTokenReceived userAccessListener;
    private OnHomeDataReceived dataListener;
    private OnCommunityMusicReceived commMusicListener;
    private OnCommunityVideoReceived commVideoListener;
    private OnCommunityNewsReceived commNewsListener;
    private OnLibraryMusicReceived libMusicListener;
    private OnLibraryVideoReceived libVideoListener;
    private OnShopMusicTopSongReceived shopTopSongListener;
    private OnShopMusicTopAlbumReceived shopTopAlbumListener;
    private OnShopMusicNewSongsReceived shopNewSongsListener;
    private OnShopMusicAllSongsReceived shopAllSongsListener;
    private OnShopVideoTopVideosReceived shopVideoTopVideosListener;
    private OnShopVideoNewVideosReceived shopVideoNewVideosListener;
    private OnShopVideoAllVideosReceived shopVideoAllVideosListener;

    private static ApiManager instance;

    public String TOKEN = "token";

    public String USER_ACCESS_TOKEN = "user_access_token";
    public String REFRESH_TOKEN = "refresh_token";

    public String TOKEN_TYPE = "type";
    public long getTokenTime, getUserAccessTokenTime;

    private int limitData = 20;

    // DATA
    private UserLoginData userLoginData;

    public ApiManager() {

    }

    public static ApiManager getInstance() {
        if(instance == null)
            instance = new ApiManager();

        return instance;
    }

    public void setContext(Context context) {
        this.context = context;

        userLoginData = new UserLoginData(context);
    }

    public void getToken() {
        if(System.currentTimeMillis() - getTokenTime > 3600000l) {
            Map<String, String> params = new HashMap<>();
            params.put("client_id", CLIENT_ID);
            params.put("client_secret", CLIENT_SECRET);
            params.put("grant_type", "client_credentials");
            HttpPostUtil request = new HttpPostUtil(Consts.URL_GET_TOKEN, "", params);

            request.setOnHttpPostUtilListener(new HttpPostUtil.HttpPostUtilListener() {
                @Override
                public void afterExecute(String result) {
                    if(result != null) {
                        Log.d("Result API", result);

                        try {
                            JSONObject rawData = new JSONObject(result);

                            if(!rawData.has("error")) {
                                TOKEN = rawData.getString("access_token");
                                TOKEN_TYPE = rawData.getString("token_type");
                                getTokenTime = System.currentTimeMillis();

                                listener.onTokenSaved();
                            } else {
                                String errorMessage = rawData.getString("error_description");

                                listener.onError(errorMessage);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void beforeExecute() {

                }
            });
            request.execute();

        } else {
            listener.onTokenSaved();
        }
    }

    public void getUserAccessToken() {
        if(System.currentTimeMillis() - getUserAccessTokenTime > 3600000l) {
            Map<String, String> params = new HashMap<>();
            params.put("client_id", CLIENT_ID);
            params.put("client_secret", CLIENT_SECRET);
            params.put("scope", "user_actions");
            params.put("grant_type", "password");
            params.put("username", userLoginData.getUsername());
            params.put("password", userLoginData.getPassword());
            HttpPostUtil request = new HttpPostUtil(Consts.URL_GET_TOKEN, "", params);

            request.setOnHttpPostUtilListener(new HttpPostUtil.HttpPostUtilListener() {
                @Override
                public void afterExecute(String result) {
                    if(result != null) {
                        Log.d("Result API", result);

                        try {
                            JSONObject rawData = new JSONObject(result);


                            if(!rawData.has("error")) {
                                USER_ACCESS_TOKEN = rawData.getString("access_token");
                                REFRESH_TOKEN = rawData.getString("refresh_token");
                                getUserAccessTokenTime = System.currentTimeMillis();

                                userLoginData.setRefreshToken(REFRESH_TOKEN);

                                getUserData();

                                userAccessListener.onUserAccessTokenSaved();
                            } else {
                                String errorMessage = rawData.getString("error_description");

                                listener.onError(errorMessage);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void beforeExecute() {

                }
            });
            request.execute();

        } else {
            refreshUserAccessToken();
        }
    }

    public void refreshUserAccessToken() {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("scope", "user_actions");
        params.put("grant_type", "refresh_token");
        params.put("username", userLoginData.getUsername());
        params.put("password", userLoginData.getPassword());
        params.put("refresh_token", userLoginData.getRefreshToken());
        HttpPostUtil request = new HttpPostUtil(Consts.URL_GET_TOKEN, "", params);

        request.setOnHttpPostUtilListener(new HttpPostUtil.HttpPostUtilListener() {
            @Override
            public void afterExecute(String result) {
                if (result != null) {
                    Log.d("Result API", result);

                    try {
                        JSONObject rawData = new JSONObject(result);

                        USER_ACCESS_TOKEN = rawData.getString("access_token");
                        REFRESH_TOKEN = rawData.getString("refresh_token");
                        getUserAccessTokenTime = System.currentTimeMillis();

                        userAccessListener.onUserAccessTokenSaved();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void beforeExecute() {

            }
        });
        request.execute();


    }

    public boolean tokenNotExpired() {
        if(System.currentTimeMillis() - getTokenTime > 3600000l)
            return true;

        return false;
    }


    // USER DATA

    public void getUserData() {
        Map<String, String> params = new HashMap<>();

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_USER_DATA, ApiManager.getInstance().USER_ACCESS_TOKEN ,params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {

                try {
                        JSONObject rawData = new JSONObject(result);
                        JSONObject profileData = rawData.getJSONObject("profile");

                        if(!rawData.has("error")) {
                            String userID = profileData.getString("user_id");
                            String fullName = profileData.getString("name");
                            String photoURL = profileData.getString("photo");
                            String credit = profileData.getString("credits");

                            userLoginData.setUserID(userID);
                            userLoginData.setFullName(fullName);
                            userLoginData.setPhotoURL(photoURL);
                            userLoginData.setCredit(credit);
                        } else {
                            String errorMessage = rawData.getString("error_description");

                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void beforeExecute() {
                // TODO something
            }
        });

        request.execute();
    }


    public void getHomeBanner() {
        Map<String, String> params = new HashMap<>();
        params.put("shop", "0");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_BANNER, ApiManager.getInstance().TOKEN ,params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {

                try {
                    JSONObject data = new JSONObject(result);
                    JSONArray arr = data.getJSONArray(Consts.TAG_BANNER);
                    int total = data.getInt("total");

                    // Add to list
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        int id = obj.getInt("id");

                        String imageLink = obj.getString("image");
                        String title = obj.getString("title");
                        String description = obj.getString("description");
                        String link = obj.getString("link");

                        BannerModel model = new BannerModel(id, imageLink, title, description, link);
                        if(DataPool.getInstance().listHomeBanner.size() < total)
                            DataPool.getInstance().listHomeBanner.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

                if(dataListener != null)
                    dataListener.onDataLoaded(ApiType.HOME_BANNER);
            }

            @Override
            public void beforeExecute() {
                // TODO something
            }
        });

        request.execute();
    }

    // HOME
    public void getHomeTopMusic() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "popularity");
        params.put("limit", "5");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_MUSIC, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);

                try {

                    JSONObject raw = new JSONObject(result);
                    JSONArray arrMusic = raw.getJSONArray("music");

                    for (int i = 0; i < arrMusic.length(); i++) {
                        JSONObject obj = arrMusic.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");
                        String labelID = labelObj.getString("id");
                        String labelName = labelObj.getString("name");
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        MusicData tempData = new MusicData(i, ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName, albumID, albumName, albumCover,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        if(DataPool.getInstance().listHomeTopMusic.size() < 5)
                            DataPool.getInstance().listHomeTopMusic.add(tempData);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(dataListener != null)
                    dataListener.onDataLoaded(ApiType.HOME_TOP_MUSIC);
            }

            @Override
            public void beforeExecute() {
                // TODO something
            }
        });
        request.execute();
    }

    public void getHomeTopVideos() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "popularity");
        params.put("limit", "2");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_VIDEO, ApiManager.getInstance().TOKEN ,params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);

                try {
                    JSONObject raw = new JSONObject(result);
                    JSONArray arrVideo = raw.getJSONArray("video");

                    for (int i = 0; i < arrVideo.length(); i++) {
                        JSONObject obj = arrVideo.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String labelID = labelObj.getString("id");
                        String labelName = labelObj.getString("name");
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        VideoData tempData = new VideoData(ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        if(DataPool.getInstance().listHomeTopVideos.size() < 2)
                            DataPool.getInstance().listHomeTopVideos.add(tempData);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(dataListener != null)
                    dataListener.onDataLoaded(ApiType.HOME_TOP_VIDEO);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getHomeLatestNews() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "popularity");
        params.put("limit", "5");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_NEWS, TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                try {
                    JSONObject rawData = new JSONObject(result);
                    JSONArray arrData = rawData.getJSONArray("article");

                    for(int i = 0; i < arrData.length(); i++) {
                        JSONObject obj = arrData.getJSONObject(i);

                        String ID = obj.getString("id");
                        String title = obj.getString("title");
                        String thumbnailURL = obj.getString("image");
                        String content = obj.getString("content");
                        String tags = obj.getString("tags");
                        String slug = obj.getString("slug");
                        int totalLikes = obj.getInt("total_likes");
                        String created = obj.getString("created");
                        String isLikeable = obj.getString("is_likeable");

                        NewsData tempNews = new NewsData(ID, title, thumbnailURL, content, tags, slug, totalLikes, created, isLikeable);
                        if(DataPool.getInstance().listHomeLatestNews.size() < 5)
                            DataPool.getInstance().listHomeLatestNews.add(tempNews);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(dataListener != null)
                    dataListener.onDataLoaded(ApiType.HOME_LATEST_NEWS);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }
    // HOME END

    // COMMUNITY
    public void getCommunityMusic() {
        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(limitData)); // get every 20
        params.put("offset", String.valueOf(DataPool.getInstance().listCommunityMusic.size()));
        params.put("order", "id");
        params.put("sort", "desc");
        params.put("category", "music");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_COMMUNITY_CONTENT, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {

                try {
                    JSONObject rawData = new JSONObject(result);
                    JSONArray arrCommunityMusic = rawData.getJSONArray("contents");

                    for (int i = 0; i < arrCommunityMusic.length(); i++) {
                        JSONObject obj = arrCommunityMusic.getJSONObject(i);
                        JSONObject userObj = obj.getJSONObject("user");

                        String ID = obj.getString("id");
                        String userID = userObj.getString("id");
                        String userName = userObj.getString("name");
                        String firstName = userObj.getString("first_name");
                        String lastName = userObj.getString("last_name");
                        String photoURL = userObj.getString("photo");
                        int userTotalLikes = userObj.getInt("total_likes");
                        String songName = obj.getString("name");
                        String description = obj.getString("description");
                        String category = obj.getString("category");
                        String fileURL = obj.getString("file");
                        String coverURL = obj.getString("cover");
                        int songTotalLikes = obj.getInt("total_likes");
                        int songTotalViews = obj.getInt("total_views");
                        String created = obj.getString("created");
                        String isLikeable = obj.getString("is_likeable");


                        CommunityContentData tempData = new CommunityContentData(ID, userID, userName, firstName, lastName, photoURL, userTotalLikes,
                                songName, description, category, fileURL, coverURL,
                                songTotalLikes, songTotalViews, created, isLikeable);
                        DataPool.getInstance().listCommunityMusic.add(tempData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(commMusicListener != null)
                    commMusicListener.onDataLoaded(ApiType.COMMUNITY_MUSIC);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getCommunityVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(limitData)); // get every 20
        params.put("offset", String.valueOf(DataPool.getInstance().listCommunityVideo.size()));
        params.put("order", "id");
        params.put("sort", "desc");
        params.put("category", "video");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_COMMUNITY_CONTENT, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {

                try {
                    JSONObject rawData = new JSONObject(result);
                    JSONArray arrCommunityMusic = rawData.getJSONArray("contents");

                    for (int i = 0; i < arrCommunityMusic.length(); i++) {
                        JSONObject obj = arrCommunityMusic.getJSONObject(i);
                        JSONObject userObj = obj.getJSONObject("user");

                        String ID = obj.getString("id");
                        String userID = userObj.getString("id");
                        String userName = userObj.getString("name");
                        String firstName = userObj.getString("first_name");
                        String lastName = userObj.getString("last_name");
                        String photoURL = userObj.getString("photo");
                        int userTotalLikes = userObj.getInt("total_likes");
                        String songName = obj.getString("name");
                        String description = obj.getString("description");
                        String category = obj.getString("category");
                        String fileURL = obj.getString("file");
                        String coverURL = obj.getString("cover");
                        int songTotalLikes = obj.getInt("total_likes");
                        int songTotalViews = obj.getInt("total_views");
                        String created = obj.getString("created");
                        String isLikeable = obj.getString("is_likeable");

                        CommunityContentData tempData = new CommunityContentData(ID, userID, userName, firstName, lastName, photoURL, userTotalLikes,
                                songName, description, category, fileURL, coverURL,
                                songTotalLikes, songTotalViews, created, isLikeable);
                        DataPool.getInstance().listCommunityVideo.add(tempData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(commVideoListener != null)
                    commVideoListener.onDataLoaded(ApiType.COMMUNITY_VIDEO);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getAllNews() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "id");
        params.put("limit", String.valueOf(limitData));
        params.put("sort", "desc");
        params.put("offset", String.valueOf(DataPool.getInstance().listAllNews.size()));

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_NEWS, TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                try {
                    JSONObject rawData = new JSONObject(result);
                    JSONArray arrData = rawData.getJSONArray("article");

                    for(int i = 0; i < arrData.length(); i++) {
                        JSONObject obj = arrData.getJSONObject(i);

                        String ID = obj.getString("id");
                        String title = obj.getString("title");
                        String thumbnailURL = obj.getString("image");
                        String content = obj.getString("content");
                        String tags = obj.getString("tags");
                        String slug = obj.getString("slug");
                        int totalLikes = obj.getInt("total_likes");
                        String created = obj.getString("created");
                        String isLikeable = obj.getString("is_likeable");

                        NewsData tempNews = new NewsData(ID, title, thumbnailURL, content, tags, slug, totalLikes, created, isLikeable);
                        DataPool.getInstance().listAllNews.add(tempNews);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(commNewsListener != null)
                    commNewsListener.onDataLoaded(ApiType.COMMUNITY_NEWS);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }
    // COMMUNITY END

    // LIBRARY
    public void getLibraryMusic() {
        Map<String, String> params = new HashMap<>();
        params.put("category", "music");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_LIBRARY, USER_ACCESS_TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {
                    JSONObject rawData = new JSONObject(result);
                    JSONArray history = rawData.getJSONArray("history");

                    for(int i = 0; i < history.length(); i++) {
                        JSONObject obj = history.getJSONObject(i);
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject singerAlbumObj = albumObj.getJSONObject("singer");
                        JSONObject singerObj = obj.getJSONObject("singer");

                        String ID = obj.getString("id");
                        String name = obj.getString("name");
                        String category = obj.getString("category");
                        String fileURL = obj.getString("file");
                        String cover = obj.getString("cover");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String created = obj.getString("created");
                        String track = obj.getString("track");

                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");

                        String singerAlbumID = singerAlbumObj.getString("id");
                        String singerAlbumName = singerAlbumObj.getString("name");

                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");

                        String size = obj.getString("size");

                        LibraryData temp = new LibraryData(ID, name, category, fileURL, cover, duration, price, created, track,
                                albumID, albumName, albumCover,
                                singerAlbumID, singerAlbumName,
                                singerID, singerName, size);

                        DataPool.getInstance().listLibraryMusic.add(temp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(libMusicListener != null)
                    libMusicListener.onDataLoaded(ApiType.LIBRARY_MUSIC);
            }

            @Override
            public void beforeExecute() {

            }
        });
        request.execute();
    }

    public void getLibraryVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("category", "video");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_LIBRARY, USER_ACCESS_TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {
                    JSONObject rawData = new JSONObject(result);
                    JSONArray history = rawData.getJSONArray("history");

                    for(int i = 0; i < history.length(); i++) {
                        JSONObject obj = history.getJSONObject(i);
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject singerAlbumObj = albumObj.getJSONObject("singer");
                        JSONObject singerObj = obj.getJSONObject("singer");

                        String ID = obj.getString("id");
                        String name = obj.getString("name");
                        String category = obj.getString("category");
                        String fileURL = obj.getString("file");
                        String cover = obj.getString("cover");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String created = obj.getString("created");
                        String track = obj.getString("track");

                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");

                        String singerAlbumID = singerAlbumObj.getString("id");
                        String singerAlbumName = singerAlbumObj.getString("name");

                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");

                        String size = obj.getString("size");

                        LibraryData temp = new LibraryData(ID, name, category, fileURL, cover, duration, price, created, track,
                                albumID, albumName, albumCover,
                                singerAlbumID, singerAlbumName,
                                singerID, singerName, size);

                        DataPool.getInstance().listLibraryVideo.add(temp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(libVideoListener != null)
                    libVideoListener.onDataLoaded(ApiType.LIBRARY_MUSIC);
            }

            @Override
            public void beforeExecute() {

            }
        });
        request.execute();
    }
    // LIBRARY END

    // SHOP MUSIC
    public void getFeaturedShopMusic() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "total_purchased");
        params.put("limit", "5");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_MUSIC, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {

                    JSONObject raw = new JSONObject(result);
                    JSONArray arrMusic = raw.getJSONArray("music");

                    for (int i = 0; i < arrMusic.length(); i++) {
                        JSONObject obj = arrMusic.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");
                        String labelID = labelObj.getString("id");
                        String labelName = labelObj.getString("name");
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        MusicData tempData = new MusicData(i, ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName, albumID, albumName, albumCover,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopMusicFeatured.add(tempData);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopTopSongListener != null)
                    shopTopSongListener.onFeaturedLoaded();
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getShopMusicTopSongs() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "popularity");
        params.put("limit", "50");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_MUSIC, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {

                    JSONObject raw = new JSONObject(result);
                    JSONArray arrMusic = raw.getJSONArray("music");

                    for (int i = 0; i < arrMusic.length(); i++) {
                        JSONObject obj = arrMusic.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");
                        String labelID = labelObj.getString("id");
                        String labelName = labelObj.getString("name");
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        MusicData tempData = new MusicData(i, ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName, albumID, albumName, albumCover,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopMusicTopSongs.add(tempData);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopTopSongListener != null)
                    shopTopSongListener.onDataLoaded(ApiType.SHOP_MUSIC_TOP_SONG);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getShopMusicTopAlbums() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "popularity");
        params.put("limit", "50");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_ALBUM, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {

                    JSONObject raw = new JSONObject(result);
                    JSONArray arrAlbum = raw.getJSONArray("album");

                    for (int i = 0; i < arrAlbum.length(); i++) {
                        JSONObject obj = arrAlbum.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");

                        String ID = obj.getString("id");
                        String albumName = obj.getString("name");
                        String coverURL = obj.getString("cover");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String releaseDate = obj.getString("released");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String totalPurchased = obj.getString("total_purchased");

                        AlbumData tempData = new AlbumData(ID, albumName, coverURL, singerID, singerName,
                                releaseDate, price, discount, totalPurchased);

                        DataPool.getInstance().listShopMusicTopAlbums.add(tempData);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopTopAlbumListener != null)
                    shopTopAlbumListener.onDataLoaded(ApiType.SHOP_MUSIC_TOP_ALBUM);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getShopMusicNewSongs() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "id");
        params.put("limit", String.valueOf(limitData));
        params.put("sort", "desc");
        params.put("offset", String.valueOf(DataPool.getInstance().listShopMusicNewSongs.size()));

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_MUSIC, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {

                    JSONObject raw = new JSONObject(result);
                    JSONArray arrMusic = raw.getJSONArray("music");

                    for (int i = 0; i < arrMusic.length(); i++) {
                        JSONObject obj = arrMusic.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");
//                        String labelID = labelObj.getString("id");
                        String labelID = "id-default";
//                        String labelName = labelObj.getString("name");
                        String labelName = "name-default";
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        MusicData tempData = new MusicData(i, ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName, albumID, albumName, albumCover,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopMusicNewSongs.add(tempData);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopNewSongsListener != null)
                    shopNewSongsListener.onDataLoaded(ApiType.SHOP_MUSIC_NEW_SONGS);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getShopMusicAllSongs() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "name");
        params.put("limit", String.valueOf(limitData));
        params.put("sort", "asc");
        params.put("offset", String.valueOf(DataPool.getInstance().listShopMusicAllSongs.size()));

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_MUSIC, ApiManager.getInstance().TOKEN, params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);
                try {

                    JSONObject raw = new JSONObject(result);
                    JSONArray arrMusic = raw.getJSONArray("music");

                    for (int i = 0; i < arrMusic.length(); i++) {
                        JSONObject obj = arrMusic.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String albumID = albumObj.getString("id");
                        String albumName = albumObj.getString("name");
                        String albumCover = albumObj.getString("cover");
                        String labelID = labelObj.getString("id");
                        String labelName = labelObj.getString("name");
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        MusicData tempData = new MusicData(i, ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName, albumID, albumName, albumCover,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopMusicAllSongs.add(tempData);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopAllSongsListener != null)
                    shopAllSongsListener.onDataLoaded(ApiType.SHOP_MUSIC_ALL_SONG);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }
    // SHOP MUSIC END

    // SHOP VIDEO
    public void getShopVideoTopVideos() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "popularity");
        params.put("limit", "50");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_VIDEO, ApiManager.getInstance().TOKEN ,params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);

                try {
                    JSONObject raw = new JSONObject(result);
                    JSONArray arrVideo = raw.getJSONArray("video");

                    for (int i = 0; i < arrVideo.length(); i++) {
                        JSONObject obj = arrVideo.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
//                        String labelID = labelObj.getString("id");
//                        String labelName = labelObj.getString("name");
                        String labelID = "";
                        String labelName = "";
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        VideoData tempData = new VideoData(ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopVideoTopVideos.add(tempData);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopVideoTopVideosListener != null)
                    shopVideoTopVideosListener.onDataLoaded(ApiType.SHOP_VIDEO_TOP_VIDEO);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getShopVideoNewVideos() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "id");
        params.put("limit", "50");
        params.put("sort", "desc");

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_VIDEO, ApiManager.getInstance().TOKEN ,params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);

                try {
                    JSONObject raw = new JSONObject(result);
                    JSONArray arrVideo = raw.getJSONArray("video");

                    for (int i = 0; i < arrVideo.length(); i++) {
                        JSONObject obj = arrVideo.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
//                        String labelID = labelObj.getString("id");
//                        String labelName = labelObj.getString("name");
                        String labelID = "";
                        String labelName = "";
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        VideoData tempData = new VideoData(ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopVideoNewVideos.add(tempData);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(shopVideoNewVideosListener != null)
                    shopVideoNewVideosListener.onDataLoaded(ApiType.SHOP_VIDEO_NEW_VIDEO);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void getShopVideoAllVideos() {
        Map<String, String> params = new HashMap<>();
        params.put("order", "name");
        params.put("limit", String.valueOf(limitData));
        params.put("sort", "asc");
        params.put("offset", String.valueOf(DataPool.getInstance().listShopVideoAllVideos.size()));

        HttpGetUtil request = new HttpGetUtil(Consts.URL_GET_VIDEO, ApiManager.getInstance().TOKEN ,params);

        request.setOnHttpGetUtilListener(new HttpGetUtil.HttpGetUtilListener() {
            @Override
            public void afterExecute(String result) {
                Log.d("Result", result);

                try {
                    JSONObject raw = new JSONObject(result);
                    JSONArray arrVideo = raw.getJSONArray("video");

                    for (int i = 0; i < arrVideo.length(); i++) {
                        JSONObject obj = arrVideo.getJSONObject(i);
                        JSONObject singerObj = obj.getJSONObject("singer");
                        JSONObject albumObj = obj.getJSONObject("album");
                        JSONObject labelObj = obj.getJSONObject("label");

                        String ID = obj.getString("id");
                        String cover = obj.getString("cover");
                        String songTitle = obj.getString("name");
                        String previewURL = obj.getString("preview");
                        String duration = obj.getString("duration");
                        String price = obj.getString("price");
                        String discount = obj.getString("discount");
                        String singerID = singerObj.getString("id");
                        String singerName = singerObj.getString("name");
                        String labelID = labelObj.getString("id");
                        String labelName = labelObj.getString("name");
                        String description = obj.getString("description");
                        String totalPlayed = obj.getString("total_played");
                        String totalPurchased = obj.getString("total_purchased");

                        VideoData tempData = new VideoData(ID, cover, songTitle, previewURL, duration,
                                price, discount, singerID, singerName,
                                labelID, labelName,
                                description, totalPlayed, totalPurchased);
                        DataPool.getInstance().listShopVideoAllVideos.add(tempData);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (shopVideoAllVideosListener != null)
                    shopVideoAllVideosListener.onDataLoaded(ApiType.SHOP_VIDEO_ALL_VIDEO);
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }
    // SHOP VIDEO END

    // USER ACTION
    public void likeItem(final LikeType type, final String id) {
        String url = Consts.URL_LIKE;

        if(type == LikeType.ARTICLE) {
            url += "article";
        } else if(type == LikeType.MUSIC) {
            url += "music";
        } else if(type == LikeType.VIDEO) {
            url += "video";
        } else {
            url += "user";
        }

        Map<String, String> params = new HashMap<>();
        params.put("unique", id);

        HttpPostUtil request = new HttpPostUtil(url, USER_ACCESS_TOKEN, params);

        request.setOnHttpPostUtilListener(new HttpPostUtil.HttpPostUtilListener() {
            @Override
            public void afterExecute(String result) {
                try {
                    JSONObject rawData = new JSONObject(result);

                    if(rawData.has("status")) {
                        String status = rawData.getString("status");

                        Log.d("Status Like", status);
                    } else {
                        unlikeItem(type, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }
    public void unlikeItem(LikeType type, String id) {
        String url = Consts.URL_UNLIKE;

        if(type == LikeType.ARTICLE) {
            url += "article";
        } else if(type == LikeType.MUSIC) {
            url += "music";
        } else if(type == LikeType.VIDEO) {
            url += "video";
        } else {
            url += "user";
        }

        Map<String, String> params = new HashMap<>();
        params.put("unique", id);

        HttpPostUtil request = new HttpPostUtil(url, USER_ACCESS_TOKEN, params);

        request.setOnHttpPostUtilListener(new HttpPostUtil.HttpPostUtilListener() {
            @Override
            public void afterExecute(String result) {
                try {
                    JSONObject rawData = new JSONObject(result);

                    if(rawData.has("status")) {
                        String status = rawData.getString("status");

                        Log.d("Status Like", status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void purchaseItem(PurchaseType type, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        if(type == PurchaseType.SINGLE)
            params.put("category", "single");
        else
            params.put("category", "album");

        HttpPostUtil request = new HttpPostUtil(Consts.URL_PURCHASE, USER_ACCESS_TOKEN, params);

        request.setOnHttpPostUtilListener(new HttpPostUtil.HttpPostUtilListener() {
            @Override
            public void afterExecute(String result) {

            }

            @Override
            public void beforeExecute() {

            }
        });

        request.execute();
    }

    public void setOnTokenReceived(OnTokenReceived listener) {  this.listener = listener; }
    public void setOnUserAccessTokenReceved(OnUserAccessTokenReceived listener) { this.userAccessListener = listener; }
    public void setOnHomeListener(OnHomeDataReceived listener) { this.dataListener =  listener; }

    public void setOnCommunityMusicListener(OnCommunityMusicReceived listener) { this.commMusicListener = listener; }
    public void setOnCommunityVideoListener(OnCommunityVideoReceived listener) { this.commVideoListener = listener; }
    public void setOnCommunityNewsListener(OnCommunityNewsReceived listener) { this.commNewsListener = listener; }

    public void setOnLibraryMusicListener(OnLibraryMusicReceived listener) { this.libMusicListener = listener; }
    public void setOnLibraryVideoListener(OnLibraryVideoReceived listener) { this.libVideoListener = listener; }

    public void setOnShopMusicTopSongListener(OnShopMusicTopSongReceived listener) { this.shopTopSongListener = listener; }
    public void setOnShopMusicTopAlbumListener(OnShopMusicTopAlbumReceived listener) { this.shopTopAlbumListener = listener; }
    public void setOnShopMusicNewSongsListener(OnShopMusicNewSongsReceived listener) { this. shopNewSongsListener = listener; }
    public void setOnShopMusicAllSongsListener(OnShopMusicAllSongsReceived listener) { this.shopAllSongsListener = listener; }

    public void setOnShopVideoTopVideosListener(OnShopVideoTopVideosReceived listener) { this.shopVideoTopVideosListener = listener; }
    public void setOnShopVideoNewVideosListener(OnShopVideoNewVideosReceived listener) { this.shopVideoNewVideosListener = listener; }
    public void setOnShopVideoAllVideosListener(OnShopVideoAllVideosReceived listener) { this.shopVideoAllVideosListener = listener; }

    public interface OnTokenReceived {
        void onTokenSaved();
        void onError(String message);
    }

    public interface OnUserAccessTokenReceived {
        void onUserAccessTokenSaved();
    }

    public interface OnHomeDataReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnCommunityMusicReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnCommunityVideoReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnCommunityNewsReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnLibraryMusicReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnLibraryVideoReceived {
        void onDataLoaded(ApiType type);
    }

    public interface  OnShopMusicTopSongReceived {
        void onDataLoaded(ApiType type);
        void onFeaturedLoaded();
    }

    public interface OnShopMusicTopAlbumReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnShopMusicNewSongsReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnShopMusicAllSongsReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnShopVideoTopVideosReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnShopVideoNewVideosReceived {
        void onDataLoaded(ApiType type);
    }

    public interface OnShopVideoAllVideosReceived {
        void onDataLoaded(ApiType type);
    }
}
