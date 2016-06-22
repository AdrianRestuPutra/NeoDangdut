package com.pitados.neodangdut.util;

import com.pitados.neodangdut.activities.MainActivity;
import com.pitados.neodangdut.model.AlbumData;
import com.pitados.neodangdut.model.BannerModel;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.NewsData;
import com.pitados.neodangdut.model.UserProfileData;
import com.pitados.neodangdut.model.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrestuputranto on 5/23/16.
 */
public class DataPool {
    private static DataPool instance;

    public UserProfileData userProfileData;

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
    public List<CommunityContentData> listSearchCommunityMusic;
    public List<CommunityContentData> listSearchCommunityVideo;
    // UPLOADED FILE
    public List<CommunityContentData> listUploadedMusic;
    public List<CommunityContentData> listUploadedVideo;
    // LIBRARY
    public List<LibraryData> listLibraryMusic;
    public List<LibraryData> listLibraryVideo;
    // SHOP MUSIC
    public List<MusicData> listShopMusicFeatured;
    public List<MusicData> listShopMusicTopSongs;
    public List<AlbumData> listShopMusicTopAlbums;
    public List<AlbumData> listSearchShopMusicAlbum;
    public List<MusicData> listShopMusicNewSongs;
    public List<MusicData> listShopMusicAllSongs;
    public List<MusicData> listSearchShopMusic;
    // SHOP VIDEO
    public List<VideoData> listShopVideoFeatured;
    public List<VideoData> listShopVideoTopVideos;
    public List<VideoData> listShopVideoNewVideos;
    public List<VideoData> listShopVideoAllVideos;
    public List<VideoData> listSearchShopVideo;

    public AlbumData selectedAlbum;

    public MainActivity mainActivity;

    public DataPool() {
        userProfileData = new UserProfileData();

        listHomeBanner = new ArrayList<>();
        listHomeTopMusic = new ArrayList<>();
        listHomeTopVideos = new ArrayList<>();
        listHomeLatestNews = new ArrayList<>();

        listCommunityMusic = new ArrayList<>();
        listCommunityVideo = new ArrayList<>();
        listAllNews = new ArrayList<>();
        listSearchCommunityMusic = new ArrayList<>();
        listSearchCommunityVideo = new ArrayList<>();

        listUploadedMusic = new ArrayList<>();
        listUploadedVideo = new ArrayList<>();

        listLibraryMusic = new ArrayList<>();
        listLibraryVideo = new ArrayList<>();

        listShopMusicFeatured = new ArrayList<>();
        listShopMusicTopSongs = new ArrayList<>();
        listShopMusicTopAlbums = new ArrayList<>();
        listShopMusicNewSongs = new ArrayList<>();
        listShopMusicAllSongs = new ArrayList<>();
        listSearchShopMusicAlbum = new ArrayList<>();
        listSearchShopMusic = new ArrayList<>();

        listShopVideoFeatured = new ArrayList<>();
        listShopVideoTopVideos = new ArrayList<>();
        listShopVideoNewVideos = new ArrayList<>();
        listShopVideoAllVideos = new ArrayList<>();
        listSearchShopVideo = new ArrayList<>();
    }

    public static DataPool getInstance() {
        if(instance == null)
            instance = new DataPool();
        return  instance;
    }
}
