package com.pitados.neodangdut.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.Map;

/**
 * Created by adrianrestuputranto on 5/26/16.
 */
public class HttpUploadUtil extends AsyncTask<Void, Void, String> {

    private String url;
    private String token;
    private File fileUpload;
    private Map<String, Object> params;

    private HttpPostUtilListener listener;

    public HttpUploadUtil(String url, String token, File fileUpload, Map<String, Object> params) {
        this.url = url;
        this.token = token;
        this.fileUpload = fileUpload;
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
            request = HttpRequest.post(url).authorization("Bearer "+token).form(this.params);
            String result = "";

            if(request.ok()) {
                result = request.body();
//                Log.d("BODY", result);
            }

            result = request.body();

            Log.d("UPLOAD", result);
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
