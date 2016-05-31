package com.pitados.neodangdut.util;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;

/**
 * Created by adrianrestuputranto on 5/26/16.
 */
public class HttpPostUtil extends AsyncTask<Void, Void, String> {

    private String url;
    private String token;
    private Map<String, String> params;

    private HttpPostUtilListener listener;

    public HttpPostUtil(String url, String token, Map<String, String> params) {
        this.url = url;
        this.token = token;
        this.params = params;
    }

    public void setOnHttpPostUtilListener(HttpPostUtilListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.beforeExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpRequest request = null;
            if(token.length() > 0)
                request = HttpRequest.post(url).authorization("Bearer "+token).form(this.params);
            else
                request = HttpRequest.post(url).form(this.params);
            String result = "";

            if(request.ok()) {
                result = request.body();
//                Log.d("BODY", result);
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

    public interface HttpPostUtilListener {
        public void afterExecute(String result);
        public void beforeExecute();
    }
}
