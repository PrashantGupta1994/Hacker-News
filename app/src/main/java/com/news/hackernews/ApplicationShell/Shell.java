package com.news.hackernews.ApplicationShell;

import android.app.Application;
import android.content.Context;

import com.news.hackernews.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Shell extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize custom font Library from - Github
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/font.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        //Config Realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(Realm.DEFAULT_REALM_NAME)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
