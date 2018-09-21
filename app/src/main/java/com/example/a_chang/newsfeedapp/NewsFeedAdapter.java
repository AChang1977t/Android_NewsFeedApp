package com.example.a_chang.newsfeedapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import junit.framework.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {
    public NewsFeedAdapter(Context context, ArrayList<NewsFeed> newsFeeds) {
        super(context, 0, newsFeeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_feed_list_item, parent, false);
        }
        NewsFeed currentNewsFeed = getItem(position);

        TextView catalogView = (TextView) listItemView.findViewById(R.id.catalogOfNews);
        catalogView.setText(currentNewsFeed.getCatalog());
        TextView titleView = (TextView) listItemView.findViewById(R.id.titleOfNews);
        titleView.setText(currentNewsFeed.getTitle());
        TextView descriptionView = (TextView) listItemView.findViewById(R.id.descriptionOfNews);
        descriptionView.setText(currentNewsFeed.getDescription());
        TextView authorView = (TextView) listItemView.findViewById(R.id.authorOfNews);
        authorView.setText(currentNewsFeed.getAuthor());
        TextView dateView = (TextView) listItemView.findViewById(R.id.dateOfNews);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(currentNewsFeed.getDate());

        } catch (ParseException e) {

        }
        dateView.setText(simpleDateFormat.format(date));

        return listItemView;
    }
}
