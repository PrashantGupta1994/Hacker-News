package com.news.hackernews.Screens.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.news.hackernews.ConstantHelper.ExtraUtils;
import com.news.hackernews.R;
import com.news.hackernews.Realm.RealmNewsList;
import com.news.hackernews.Screens.Activities.HackerNewsDetailsActivity;

import java.lang.ref.WeakReference;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class NewsViewAdapter extends RealmRecyclerViewAdapter<RealmNewsList, NewsViewAdapter.ViewHolder> {

    /** @param context is passed from Activity to handle environment.
     *  @param data holds the realm list of Feed comments by position of list item.
     *  @param autoUpdate as name suggest, it auto updates the list with new comments as comes in.
     * */

    private Context context;

    public NewsViewAdapter(Context context, OrderedRealmCollection<RealmNewsList> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        WeakReference<Context> weakContext = new WeakReference<>(context);
        this.context = weakContext.get();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_views, parent, false);
        return new NewsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RealmNewsList realmNewsList = getData().get(position);
        holder.mTitle.setText(realmNewsList.getTitle());
        holder.mNewsDetails.setText(realmNewsList.getPoints() + " - " + realmNewsList.getAuthor());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitle;
        private TextView mNewsDetails;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mTitle = v.findViewById(R.id.tvStoryTitle);
            mNewsDetails = v.findViewById(R.id.tvStoryDetails);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                RealmNewsList realmNewsList = getData().get(position);
                String id = realmNewsList.getUrl();
                context.startActivity(new Intent(context, HackerNewsDetailsActivity.class).putExtra(ExtraUtils.INTENT_ID, id));
            }
        }
    }
}

