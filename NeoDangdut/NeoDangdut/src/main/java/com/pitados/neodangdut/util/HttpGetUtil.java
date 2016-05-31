package com.pitados.neodangdut.util;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;

/**
 * Created by adrianrestuputranto on 5/26/16.
 */
public class HttpGetUtil extends AsyncTask<Void, Void, String> {

    private String url;
    private String token;
    private Map<String, String> params;

    private HttpGetUtilListener listener;

    public HttpGetUtil(String url, String token, Map<String, String> params) {
        this.url = url;
        this.token = token;
        this.params = params;

    }

    public void setOnHttpGetUtilListener(HttpGetUtilListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.beforeExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpRequest request = HttpRequest.get(url, this.params, true).authorization("Bearer "+token);
            String result = "";

            if(request.ok()) {
                result = request.body();
//                Log.d("Result", result);
            }

            return result;
        } catch (HttpRequest.HttpRequestException e) {
            return null;
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.d("CONNECTION", "On Progress");
    }

    @Override
    protected void onPostExecute(String result) {
        listener.afterExecute(result);
    }

    public interface HttpGetUtilListener {
        public void afterExecute(String result);
        public void beforeExecute();
    }
}
