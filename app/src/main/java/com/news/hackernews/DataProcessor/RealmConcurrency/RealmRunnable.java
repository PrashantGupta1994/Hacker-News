package com.news.hackernews.DataProcessor.RealmConcurrency;

import android.support.annotation.NonNull;
import android.util.Log;

import com.news.hackernews.Realm.RealmNewsList;
import com.news.hackernews.Realm.RealmTopComments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;

public class RealmRunnable implements Runnable {

    private Realm realm = null;
    private JSONObject mObject;
    private RealmList<RealmTopComments> realmTopComments = new RealmList<>();

    public RealmRunnable(JSONObject mObject) {
        this.mObject = mObject;
    }


    @Override
    public void run() {
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    RealmNewsList newsList = realm.createObject(RealmNewsList.class);
                    RealmTopComments topComments = realm.createObject(RealmTopComments.class);
                    processData(mObject, newsList, topComments);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
    }

    private void processData(JSONObject mObject, RealmNewsList newsList, RealmTopComments topComments){
        try {
            newsList.setId(mObject.getInt("setIds"));
            newsList.setTitle(mObject.getString("title"));
            newsList.setUrl(mObject.getString("url"));
            newsList.setPoints(mObject.getInt("points"));
            newsList.setCreatedAt(mObject.getString("created_at"));
            newsList.setAuthor(mObject.getString("author"));

            JSONArray mArray = mObject.getJSONArray("children");
            for (int a = 0; a < mArray.length(); a++){
                JSONObject object = mArray.getJSONObject(a);
                topComments.setId(object.getInt("setIds"));
                topComments.setText(object.getString("text"));
                topComments.setCreatedAt(object.getString("created_at"));
                topComments.setAuthor(object.getString("author"));
                realmTopComments.add(topComments);
            }
            newsList.setChildren(realmTopComments);
            realmTopComments.clear();
        }catch (JSONException e){
            Log.e("", e.getLocalizedMessage());
        }
    }
}
