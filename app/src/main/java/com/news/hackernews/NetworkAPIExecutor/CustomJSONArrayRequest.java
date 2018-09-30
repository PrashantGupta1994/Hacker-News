package com.news.hackernews.NetworkAPIExecutor;


import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class CustomJSONArrayRequest extends JsonArrayRequest{

    public CustomJSONArrayRequest(String url,
                                  Response.Listener<JSONArray> listener,
                                  @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public CustomJSONArrayRequest(int method,
                                  String url,
                                  @Nullable JSONArray jsonRequest,
                                  Response.Listener<JSONArray> listener,
                                  @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }
}
