package com.example.a_chang.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {

    private static final String LOG_TAG = NewsFeedLoader.class.getName();
    private String mUrl;

    public NewsFeedLoader (Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: One Recent tasks");
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        Log.i(LOG_TAG, "TEST: Switch to different app");

        if (mUrl == null){
            return null;
        }
        List<NewsFeed> newsFeeds = QueryUtils.fetchNewsFeedData (mUrl);
        return newsFeeds;
    }
}
