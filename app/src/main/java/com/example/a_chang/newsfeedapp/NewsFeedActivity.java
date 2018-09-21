package com.example.a_chang.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    private static final String LOG_TAG = NewsFeedActivity.class.getName();

    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?";
    private static final String OTHER_PARAM = "&show-fields=trailText&show-tags=contributor";
    private static final String KEY = BuildConfig.API_KEY;

    private static final int NEWSFEED_LOADER_ID = 1;

    private NewsFeedAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);

        ListView newsFeedListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_list);
        newsFeedListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());
        newsFeedListView.setAdapter(mAdapter);

        newsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewsFeed currentNewsFeed = mAdapter.getItem(position);
                Uri newsFeedUri = Uri.parse(currentNewsFeed.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsFeedUri);
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);
                }
            }
        });

        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null & networkInfo.isConnectedOrConnecting();
        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internet connection");
        }

    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String catalog = sharedPrefs.getString(
                getString(R.string.settings_catalog_key),
                getString(R.string.settings_catalog_default)
        );
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL + "api-key=" + KEY + OTHER_PARAM);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("section", catalog);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsFeedLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsFeeds) {
        Log.i(LOG_TAG, "TEST: Return to app");

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText("No News found");
        mAdapter.clear();

        if (newsFeeds != null && !newsFeeds.isEmpty()) {
            mAdapter.addAll(newsFeeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
        Log.i(LOG_TAG, "TEST: Go to the home screen and return to app");
        mAdapter.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
