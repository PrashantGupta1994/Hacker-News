package com.news.hackernews.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmStoryID extends RealmObject {

    //@PrimaryKey
    private int id;

    public RealmStoryID() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
