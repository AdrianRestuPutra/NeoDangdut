package com.pitados.neodangdut.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adrianrestuputranto on 5/17/16.
 */
public class ApiManager {
    private String CLIENT_ID = "15299018236589";
    private String CLIENT_SECRET = "7521e4a1c2b5cbf67024b3f9b1b4f7e1";
    private OnTokenReceived listener;
    private static ApiManager instance;

    public String TOKEN = "token";
    public String TOKEN_TYPE = "type";
    public long getTokenTime;

    public ApiManager() {

    }

    public static ApiManager getInstance() {
        if(instance == null)
            instance = new ApiManager();

        return instance;
    }


    public void getToken() {
        if(System.currentTimeMillis() - getTokenTime > 3600000l) {
            StringRequest request = new StringRequest(Request.Method.POST, Consts.URL_GET_TOKEN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("API Success", response);

                            try {
                                JSONObject obj = new JSONObject(response);
                                TOKEN = obj.getString(Consts.TAG_TOKEN);
                                TOKEN_TYPE = obj.getString(Consts.TAG_TOKEN_TYPE);

                                listener.onTokenSaved();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            getTokenTime = System.currentTimeMillis();

                            Log.d("API DATA", TOKEN + " | " + TOKEN_TYPE + " | " + getTokenTime);

                            // TODO get all data here
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("API Error", "Bad Request");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("client_id", CLIENT_ID);
                    params.put("client_secret", CLIENT_SECRET);
                    params.put("grant_type", "client_credentials");
                    return params;
                }
            };


            AppController.getInstance().addToRequestQueue(request, Consts.TAG_API_TOKEN);
        } else {
            listener.onTokenSaved();
        }
    }

    public void setOnTokenReceived(OnTokenReceived listener) {
        this.listener = listener;
    }

    public interface OnTokenReceived {
        void onTokenSaved();
    }
}
