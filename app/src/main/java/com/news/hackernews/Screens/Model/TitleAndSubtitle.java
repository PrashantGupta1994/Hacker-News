package com.news.hackernews.Screens.Model;


public class TitleAndSubtitle {
    private String mTitle;
    private String mSubtitle;

    public TitleAndSubtitle(String mTitle, String mSubtitle) {
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }
}
