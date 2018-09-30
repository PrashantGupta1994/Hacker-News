package com.news.hackernews.Screens.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.news.hackernews.R;
import com.news.hackernews.Realm.RealmTopComments;

import java.lang.ref.WeakReference;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class NewsDetailsAdapter extends RealmRecyclerViewAdapter<RealmTopComments, NewsDetailsAdapter.ViewHolder> {

    /** @param context is passed from Activity to handle environment.
     *  @param data holds the realm list of Feed comments by position of list item.
     *  @param autoUpdate as name suggest, it auto updates the list with new comments as comes in.
     * */

    private Context context;

    public NewsDetailsAdapter(Context context, OrderedRealmCollection<RealmTopComments> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        WeakReference<Context> weakContext = new WeakReference<>(context);
        this.context = weakContext.get();
    }

    @NonNull
    @Override
    public NewsDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_views, parent, false);
        return new NewsDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDetailsAdapter.ViewHolder holder, int position) {
        RealmTopComments realmTopComments = getData().get(position);
        holder.mTitle.setText(realmTopComments.getText());
        //holder.mNewsDetails.setText(realmNewsList.getPoints() + " - " + realmNewsList.getAuthor());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mNewsDetails;

        public ViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.tvStoryTitle);
            mNewsDetails = v.findViewById(R.id.tvStoryDetails);
        }
    }
}
