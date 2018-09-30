package com.news.hackernews.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmNewsList extends RealmObject {

    //@PrimaryKey
    private int id;

    private String title;
    private String url;
    private String created_at;
    private String author;
    private int points;

    private RealmList<RealmTopComments> children = null;

    public RealmNewsList() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setChildren(RealmList<RealmTopComments> children) {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getAuthor() {
        return author;
    }

    public RealmList<RealmTopComments> getChildren() {
        return children;
    }

    public int getPoints() {
        return points;
    }
}
