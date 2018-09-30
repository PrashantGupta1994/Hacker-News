package com.news.hackernews.Screens.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.news.hackernews.ApplicationShell.ShellActivity.BaseActivity;
import com.news.hackernews.ConstantHelper.ExtraUtils;
import com.news.hackernews.DataProcessor.RealmManager;
import com.news.hackernews.NetworkAPIExecutor.APIConfig;
import com.news.hackernews.NetworkAPIExecutor.CustomJSONArrayRequest;
import com.news.hackernews.NetworkAPIExecutor.CustomJSONObjectRequest;
import com.news.hackernews.NetworkAPIExecutor.CustomVolleyRequestQueue;
import com.news.hackernews.R;
import com.news.hackernews.Realm.RealmNewsList;
import com.news.hackernews.Screens.Adapter.NewsViewAdapter;
import com.news.hackernews.DataProcessor.Service.FetchDataService;
import com.news.hackernews.Screens.Model.TitleAndSubtitle;
import com.news.hackernews.databinding.ActivityHackerNewsListBinding;

import org.json.JSONArray;
import org.json.JSONObject;


public class HackerNewsList extends BaseActivity implements Response.Listener, Response.ErrorListener{

    private static final String TAG = HackerNewsList.class.getSimpleName();
    private static final String URL = APIConfig.HN_URL_BASE + APIConfig.HN_URL_TOP_STORIES;

    private boolean isConnected = false;
    private RecyclerView mRecyclerView;

    private FetchDataService mFetchDataService = null;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHackerNewsListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_hacker_news_list);
        binding.setTitleAndSubtitle(new TitleAndSubtitle(getString(R.string.top_stories), getString(R.string.app_name)));

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!RealmManager.with(this).getStoryIDs().isEmpty()) {
            Intent intent = new Intent(this, FetchDataService.class);
            startService(intent);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
        populateNewsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.m_reload) {
            mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
            CustomJSONArrayRequest mJsonRequest = new CustomJSONArrayRequest(Request.Method.GET, URL, new JSONArray(), this, this);
            mJsonRequest.setTag(TAG);
            mQueue.add(mJsonRequest);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populateNewsData(){
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new NewsViewAdapter(
                this,
                RealmManager.with(this).getRealmNewsList(),
                true));
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initViews(){
        mRecyclerView = findViewById(R.id.news_list);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FetchDataService.LocalBinder bridge = (FetchDataService.LocalBinder) service;
            mFetchDataService = bridge.getService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mFetchDataService = null;
            isConnected = false;
        }
    };

    @Override
    protected void onDestroy() {
        if (isConnected){
            unbindService(serviceConnection);
            isConnected = false;
            stopService(new Intent(this, FetchDataService.class));
        }
        if (mQueue != null) {
            mQueue.cancelAll(TAG);
        }
        super.onDestroy();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mQueue.cancelAll(TAG);
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        Log.e(TAG, error.toString());
    }

    @Override
    public void onResponse(Object response) {
        JSONArray mArray = ((JSONArray) response);
        RealmManager.with(this).writeNewsIDToRealm(mArray);
    }
}
