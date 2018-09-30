package com.news.hackernews.DataProcessor;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.support.annotation.NonNull;
import android.util.Log;

import com.news.hackernews.Realm.RealmNewsList;
import com.news.hackernews.Realm.RealmStoryID;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmManager {

    private Realm realm;
    private static RealmManager instance;

    private RealmResults<RealmNewsList> newsLists;
    private RealmResults<RealmStoryID> storyIDs;

    private RealmManager(Application application) {
        /*if (realm != null){
            realm.close();
        }*/
        realm = Realm.getDefaultInstance();
    }

    public static RealmManager with(Activity activity) {
        WeakReference<Activity> weakActivity = new WeakReference<>(activity);
        if (instance == null) {
            instance = new RealmManager(weakActivity.get().getApplication());
        }
        return instance;
    }

    public static RealmManager with(Service service) {
        WeakReference<Service> weakService = new WeakReference<>(service);
        if (instance == null) {
            instance = new RealmManager(weakService.get().getApplication());
        }
        return instance;
    }

    public Realm getRealm(){
        return realm;
    }

    public void closeRealm(){
        realm.close();
    }

    public boolean isEmpty(){
        return realm.isEmpty();
    }

    public void writeNewsIDToRealm(final JSONArray array){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@Nonnull Realm realm) {
                    for (int a = 0; a < 20; a++){
                        try {
                            RealmStoryID id = realm.createObject(RealmStoryID.class);
                            id.setId(array.getInt(a));
                        } catch (JSONException ignored) {
                        }
                    }
                }
            });
        }finally {
            realm.close();
        }
    }

    public RealmResults<RealmNewsList> getRealmNewsList(){
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                newsLists = realm.where(RealmNewsList.class).findAll();
            }
        });
        return newsLists;
    }

    public RealmResults<RealmStoryID> getStoryIDs(){
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                storyIDs = realm.where(RealmStoryID.class).findAll();
            }
        });
        return storyIDs;
    }
}
