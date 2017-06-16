package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.example.android.newsapp.ArticlesUtility.createURL;
import static com.example.android.newsapp.ArticlesUtility.extractFeatureFromJson;
import static com.example.android.newsapp.ArticlesUtility.getJSONData;
import static com.example.android.newsapp.ArticlesUtility.makeHttpRequest;

/**
 * Loads a list of news by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Articles>> {

    Context mContext;
    String stringGetArticlesFromHere;

    public ArticleLoader(Context context, String articlesURL)   {
        super(context);
        mContext = context;
        stringGetArticlesFromHere = articlesURL;
    }

    /**
     * start the loader
     */
    protected void onStartLoading()	{
        forceLoad();
    }

    /**
     *
     * @return a list of news
     */
    @Override
    public List<Articles> loadInBackground() {
        List<Articles> loadIntoThis;

        URL urlGetDataFromHere;

        // get the URL from the string
        try {
            urlGetDataFromHere = createURL(stringGetArticlesFromHere);
        }   catch   (MalformedURLException e)   {
            Log.e("loadInBackGround", e.getMessage());
            return null;
        }

        // make connection
        HttpURLConnection articleConnection = makeHttpRequest(urlGetDataFromHere);
        String stringArticleJSONData;

        if(articleConnection != null)  {
            stringArticleJSONData = getJSONData(articleConnection);
        }   else    {
            return null;
        }

        if(!stringArticleJSONData.isEmpty())   {
            loadIntoThis = extractFeatureFromJson(getContext(), stringArticleJSONData);
        }   else    {
            return null;
        }
        return loadIntoThis;
    }
}
