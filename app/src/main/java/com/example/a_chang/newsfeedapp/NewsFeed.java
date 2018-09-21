package com.example.a_chang.newsfeedapp;

public class NewsFeed {
    private String mCatalog;
    private String mTitle;
    private String mDescription;
    private String mAuthor;
    private String mDate;
    private String mUrl;

    public NewsFeed(String catalog, String title, String description, String author, String date, String url){
        mCatalog = catalog;
        mTitle = title;
        mDescription = description;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    public String getCatalog(){return mCatalog;}
    public String getTitle(){return mTitle;}
    public String getDescription(){return mDescription;}
    public String getAuthor(){return mAuthor;}
    public String getDate(){return mDate;}
    public String getUrl(){return mUrl;}
}
