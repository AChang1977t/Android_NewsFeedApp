package com.example.a_chang.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils(){

    }

    public static List<NewsFeed> fetchNewsFeedData (String requestUrl){
        Log.i(LOG_TAG,"TEST: Come up with your ownscenarios!\n");

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG,"problem making the HTTP request.", e);
        }
        List<NewsFeed> newsFeeds = extractFeatureFromJson(jsonResponse);
        return newsFeeds;
    }

    private static URL createUrl (String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if  (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream (inputStream);
            } else {
                Log.e(LOG_TAG,"Error response code"+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"problem retrieving the newsfeed JSON results.", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }return jsonResponse;
    }


    private static String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line !=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<NewsFeed> extractFeatureFromJson (String newsFeedJSON){

        if (TextUtils.isEmpty(newsFeedJSON)){
            return null;
        }
        List<NewsFeed> newsFeeds = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsFeedJSON);
            JSONObject responseObject = baseJsonResponse.optJSONObject("response");
            JSONArray resultsArray = responseObject.optJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentNewFeed = resultsArray.getJSONObject(i);
                String catalog = currentNewFeed.getString("sectionName");
                String title = currentNewFeed.getString("webTitle");
                String date = currentNewFeed.getString("webPublicationDate");
                String url = currentNewFeed.getString("webUrl");

                String author = "";
                if (currentNewFeed.has("tags")){
                    JSONArray tags = currentNewFeed.getJSONArray("tags");
                    if (tags.length()>0){
                        JSONObject Author = tags.getJSONObject(0);
                        author = Author.getString("webTitle");
                    }
                }

                String description = "";
                if (currentNewFeed.has("fields")){
                    JSONObject fields = currentNewFeed.getJSONObject("fields");
                    if (fields.has("trailText")){
                        description = fields.getString("trailText");
                    }
                }
                newsFeeds.add(new NewsFeed(catalog,title,description,author,date,url));
            }
        } catch (JSONException e){
            Log.e("QueryUtils","problem parsing the newsfeed JSON results", e);
        }
        return newsFeeds;
    }

}
