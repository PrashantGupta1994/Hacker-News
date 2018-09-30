package com.news.hackernews.NetworkAPIExecutor;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.lang.ref.WeakReference;


public class CustomVolleyRequestQueue {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    private static CustomVolleyRequestQueue mInstance;
    private Context mCtx;
    private RequestQueue mRequestQueue;

    private CustomVolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        Context mC = contextWeakReference.get();
        if (mInstance == null) {
            mInstance = new CustomVolleyRequestQueue(mC);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network, POOL_SIZE);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
}
