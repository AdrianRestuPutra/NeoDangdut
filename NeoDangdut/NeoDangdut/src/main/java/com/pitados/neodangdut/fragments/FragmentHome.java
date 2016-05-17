package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.app.AppController;
import com.pitados.neodangdut.custom.CustomListTopTrackAdapter;
import com.pitados.neodangdut.custom.CustomListTopVideoAdapter;
import com.pitados.neodangdut.model.BannerModel;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.ApiManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentHome extends Fragment implements AdapterView.OnItemClickListener, BaseSliderView.OnSliderClickListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // Widgets
    private SliderLayout homeBanner;
    private PagerIndicator homeBannerIndicator;
    private ListView listViewTopTrack, listViewTopVideo, listViewLatestNews;

    // Adapters
    private CustomListTopTrackAdapter listTrackAdapter;
    private CustomListTopVideoAdapter listVideoAdapter;

    public static FragmentHome newInstance(int page, String title) {
        FragmentHome home = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt(Consts.FRAGMENT_PAGE_NUMBER_KEY, page);
        args.putString(Consts.FRAGMENT_PAGE_TITLE_KEY, title);
        home.setArguments(args);
        return home;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(Consts.FRAGMENT_PAGE_NUMBER_KEY);
        pageTitle = getArguments().getString(Consts.FRAGMENT_PAGE_TITLE_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);

        ApiManager.getInstance().getToken();
        ApiManager.getInstance().setOnTokenReceived(new ApiManager.OnTokenReceived() {
            @Override
            public void onTokenSaved() {
                getBanner();
            }
        });

        // TODO init widgets
        homeBanner = (SliderLayout) view.findViewById(R.id.home_slider);
        homeBannerIndicator = (PagerIndicator) view.findViewById(R.id.home_slider_indicator);
        homeBanner.setCustomIndicator(homeBannerIndicator);

        listViewTopTrack = (ListView) view.findViewById(R.id.list_view_top_track);
        listViewTopTrack.setFocusable(false);

        listViewTopVideo = (ListView) view.findViewById(R.id.list_view_top_video);
        listViewTopVideo.setFocusable(false);

        // TEST music
        List<MusicData> listTopTrack = new ArrayList<>();
        listTopTrack.add(new MusicData());
        listTopTrack.add(new MusicData());
        listTopTrack.add(new MusicData());
        listTopTrack.add(new MusicData());
        listTopTrack.add(new MusicData());
        listTrackAdapter = new CustomListTopTrackAdapter(context, listTopTrack);
        listViewTopTrack.setAdapter(listTrackAdapter);

        // TEST video
        List<VideoData> listTopVideo = new ArrayList<>();
        listTopVideo.add(new VideoData());
        listTopVideo.add(new VideoData());
        listVideoAdapter = new CustomListTopVideoAdapter(context, listTopVideo);
        listViewTopVideo.setAdapter(listVideoAdapter);

        // On click listener
        listViewTopTrack.setOnItemClickListener(this);

        return view;
    }

    private void getBanner() {
        StringRequest request = new StringRequest(Request.Method.GET, Consts.URL_GET_BANNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<BannerModel> listBanner = new ArrayList<>();
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray arr = data.getJSONArray(Consts.TAG_BANNER);
                            for(int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                int id = obj.getInt("id");
                                String imageLink = obj.getString("image");
                                String title = obj.getString("title");
                                String description = obj.getString("description");
                                String link = obj.getString("link");

                                BannerModel model = new BannerModel(id, imageLink, title, description, link);
                                listBanner.add(model);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for(BannerModel temp : listBanner) {
                            DefaultSliderView sliderImage = new DefaultSliderView(context);
                            sliderImage
                                .image(temp.imageLink)
                                .setOnSliderClickListener(FragmentHome.this);

                            // TODO extra
                            sliderImage.bundle(new Bundle());
                            sliderImage.getBundle().putString("extra", "TODO extra");

                            homeBanner.addSlider(sliderImage);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Bad Request");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ApiManager.getInstance().TOKEN_TYPE + " " + ApiManager.getInstance().TOKEN);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("shop", "0");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request, Consts.TAG_API_BANNER);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        if(adapterView == listViewTopTrack) {
            Toast.makeText(context, "TODO handle top track item : "+pos, Toast.LENGTH_SHORT).show();
        }
        if(adapterView == listViewTopVideo) {
            Toast.makeText(context, "TODO handle top video item : "+pos, Toast.LENGTH_SHORT).show();
        }
        if(adapterView == listViewLatestNews) {
            Toast.makeText(context, "TODO handle news item : "+pos, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(context, "TODO link to page", Toast.LENGTH_SHORT).show();
    }

//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }
}
