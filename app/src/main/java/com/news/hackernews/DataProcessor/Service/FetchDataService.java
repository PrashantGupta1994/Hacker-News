package com.news.hackernews.DataProcessor.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.news.hackernews.ConstantHelper.ExtraUtils;
import com.news.hackernews.DataProcessor.RealmManager;
import com.news.hackernews.NetworkAPIExecutor.APIConfig;
import com.news.hackernews.NetworkAPIExecutor.CustomJSONObjectRequest;
import com.news.hackernews.NetworkAPIExecutor.CustomVolleyRequestQueue;
import com.news.hackernews.DataProcessor.RealmConcurrency.RealmRunnable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchDataService extends Service implements Response.Listener, Response.ErrorListener {

    private RequestQueue mQueue;
    private List<JSONObject> mList = new ArrayList<>();

    private IBinder binder = new LocalBinder();
    private final static String TAG = FetchDataService.class.getSimpleName();

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public FetchDataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        for (int a = 0; a < RealmManager.with(this).getStoryIDs().size(); a++) {
            String URL = APIConfig.EX_URL_STORY_DETAILS + RealmManager.with(this).getStoryIDs().get(a).getId();
            mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
            CustomJSONObjectRequest mJsonRequest = new CustomJSONObjectRequest(Request.Method.GET, URL, new JSONObject(), this, this);
            mJsonRequest.setTag(TAG);
            mQueue.add(mJsonRequest);
        }
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mQueue.cancelAll(TAG);
    }

    @Override
    public void onResponse(final Object response) {
        mList.add((JSONObject) response);
        if (mList.size() == RealmManager.with(this).getStoryIDs().size()){
            for (JSONObject x : mList){
                Runnable worker = new RealmRunnable(x);
                executor.execute(worker);
            }
            executor.shutdown();
        }
    }

    public class LocalBinder extends Binder {
        public FetchDataService getService() {
            return FetchDataService.this;
        }
    }

    @Override
    public void onDestroy() {
        if (mQueue != null) {
            mQueue.cancelAll(TAG);
        }
        if (!executor.isTerminated()){
            executor.shutdown();
        }
        stopSelf();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
