package com.pitados.neodangdut.util;

import com.pitados.neodangdut.model.AlbumData;
import com.pitados.neodangdut.model.BannerModel;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.NewsData;
import com.pitados.neodangdut.model.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrestuputranto on 5/23/16.
 */
public class DataPool {
    private static DataPool instance;

    // List
    // HOME
    public List<BannerModel> listHomeBanner;
    public List<MusicData> listHomeTopMusic;
    public List<VideoData> listHomeTopVideos;
    public List<NewsData> listHomeLatestNews;
    // Community
    public List<CommunityContentData> listCommunityMusic;
    public List<CommunityContentData> listCommunityVideo;
    public List<NewsData> listAllNews;
    // SHOP MUSIC
    public List<MusicData> listShopMusicTopSongs;
    public List<AlbumData> listShopMusicTopAlbums;
    public List<MusicData> listShopMusicNewSongs;
    public List<MusicData> listShopMusicAllSongs;
    // SHOP VIDEO
    public List<VideoData> listShopVideoTopVideos;
    public List<VideoData> listShopVideoNewVideos;
    public List<VideoData> listShopVideoAllVideos;

    public DataPool() {
        listHomeBanner = new ArrayList<>();
        listHomeTopMusic = new ArrayList<>();
        listHomeTopVideos = new ArrayList<>();
        listHomeLatestNews = new ArrayList<>();

        listCommunityMusic = new ArrayList<>();
        listCommunityVideo = new ArrayList<>();
        listAllNews = new ArrayList<>();

        listShopMusicTopSongs = new ArrayList<>();
        listShopMusicTopAlbums = new ArrayList<>();
        listShopMusicNewSongs = new ArrayList<>();
        listShopMusicAllSongs = new ArrayList<>();

        listShopVideoTopVideos = new ArrayList<>();
        listShopVideoNewVideos = new ArrayList<>();
        listShopVideoAllVideos = new ArrayList<>();
    }

    public static DataPool getInstance() {
        if(instance == null)
            instance = new DataPool();
        return  instance;
    }
}