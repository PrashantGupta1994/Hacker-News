package com.news.hackernews.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmTopComments extends RealmObject {

    //@PrimaryKey
    private int id;

    private String created_at;
    private String author;
    private String text;

    public RealmTopComments() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }
}
