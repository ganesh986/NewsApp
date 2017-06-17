package com.example.android.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.android.newsapp.ArticlesUtility.isConnected;

public class ArticlesActivity extends Fragment implements LoaderManager.LoaderCallbacks<List<Articles>>{

    /** URL for guardian API */
    private final static String guardianURL = "https://content.guardianapis.com/search?q=debate&api-key=test&tag=";
    //Option for set max number of articles
    private final static String guardianPagesize = "&page-size=";
    private String stringFullURL;
    private View firstView;
    private ListView listArticles;

    /** TextView that is displayed when the list is empty */
    private TextView emptyView;
    private ArticlesAdapter adapterArticles;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firstView = inflater.inflate(R.layout.activity_articles, container, false);
        listArticles = (ListView) firstView.findViewById(R.id.list);
        emptyView = (TextView) firstView.findViewById(R.id.empty_view);
        progressBar = (ProgressBar) firstView.findViewById(R.id.loading_indicator);
        adapterArticles = new ArticlesAdapter(getActivity(), new ArrayList<Articles>());
        listArticles.setAdapter(adapterArticles);
        listArticles.setEmptyView(firstView.findViewById(R.id.empty_view));

        listArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // visit the clicked book website
                Articles articleToVisit = adapterArticles.getItem(position);
                // Check internet connecion
                if (isConnected(getActivity())) {
                    // try to open the article website
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(articleToVisit.getWebURL())));
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onItemClick: "+e.getMessage());
                    }
                } else {
                    adapterArticles.clear();
                    ((TextView)firstView.findViewById(R.id.empty_view)).setText("No internet");
                }

            }
        });

        SharedPreferences preferredSections = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String stringSection = getArguments().getString("section");
        String maxNumArticles = preferredSections.getString("max_articles", "15");
        int intLoaderId = getArguments().getInt("sectionLoader");

        stringFullURL = guardianURL + stringSection + "/" + stringSection + guardianPagesize + maxNumArticles;
        LoaderManager lm = getActivity().getSupportLoaderManager();
        lm.initLoader(intLoaderId, null, this);
        return firstView;
    }


    @Override
    public Loader<List<Articles>> onCreateLoader(int id, Bundle args) {
        // if returned from article website, delete the adapter's content
        adapterArticles.clear();
        //progressbar appear
        progressBar.setVisibility(View.VISIBLE);
        // emptyview disappear
        emptyView.setVisibility(View.INVISIBLE);

        // Async
        return new ArticleLoader(getActivity(), stringFullURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> data) {
        adapterArticles.clear();

        if(data != null && !data.isEmpty())    {
            adapterArticles.addAll(data);
        }   else    {
            // no datas -> emptyview text
            emptyView.setText("No articles");

            if(!isConnected(getActivity())) {
                // no connection -> No internet text
                emptyView.setText("No internet");
            }
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Articles>> loader) {
        adapterArticles.clear();
    }
}
