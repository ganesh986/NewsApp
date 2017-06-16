package com.example.android.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import static android.content.ContentValues.TAG;

/**
 * Created by matteo on 14/06/2017.
 */

public class ArticlesUtility {
    
    private ArticlesUtility() {}


    static URL createURL(String stringURL) throws MalformedURLException {
        return new URL(stringURL);
    }

    static HttpURLConnection makeHttpRequest(URL urlConnection)   {
        HttpURLConnection ArticlesConnection = null;

        try {
            ArticlesConnection = (HttpURLConnection) urlConnection.openConnection();
            ArticlesConnection.setReadTimeout(10000);
            ArticlesConnection.setConnectTimeout(15000);
            ArticlesConnection.setRequestMethod("GET");
            ArticlesConnection.connect();

            
            if (ArticlesConnection.getResponseCode() == 200) {
                return ArticlesConnection;
            }   else    {
                return ArticlesConnection;
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: "+ e.getMessage());
        }
        return ArticlesConnection;
    }

    static String getJSONData(HttpURLConnection connection)    {
        StringBuilder articleList = new StringBuilder();

        try {
            InputStream articleStream = connection.getInputStream();
            articleList = new StringBuilder();

            if (articleStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(articleStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();

                while (line != null) {
                    articleList.append(line);
                    line = reader.readLine();
                }
            }
        }   catch   (IOException e) {
            Log.e(TAG, "getJSONData: "+ e.getMessage() );
        }
        return articleList.toString();
    }

    static List<Articles> extractFeatureFromJson(Context context, String stringArticleDatas)   {
        List<Articles> tempNews = new ArrayList<>();

        try {
            JSONObject articleJSON = new JSONObject(stringArticleDatas);

            JSONObject JSONResponse = articleJSON.getJSONObject("response");
            JSONArray JSONResults = JSONResponse.getJSONArray("results");
            for(int i=0; i< JSONResults.length(); i++)  {
                JSONObject iArticle = JSONResults.getJSONObject(i);
                String stringArticleTitle = iArticle.getString("webTitle");
                String stringPublicationDate = iArticle.getString("webPublicationDate");
                String stringWebURL = iArticle.getString("webUrl");
                String stringArgument = iArticle.getString("sectionName");

                Articles newArticle = new Articles(stringArticleTitle, stringPublicationDate, stringWebURL,stringArgument);
                tempNews.add(newArticle);
            }
        }   catch (JSONException e) {
            Log.e(TAG, "extractFeatureFromJson: "+e.getMessage());
            return null;
        }
        return tempNews;
    }

    static boolean isConnected(Context context)   {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}
