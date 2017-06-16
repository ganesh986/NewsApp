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

    final static String guardianURL = "https://content.guardianapis.com/search?q=debate&api-key=test&tag=";
    final static String guardianPagesize = "&page-size=";
    String stringFullURL;
    View firstView;
    ListView listArticles;
    TextView emptyView;
    private ArticlesAdapter adapterArticles;
    ProgressBar progressBar;

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
                Articles articleToVisit = adapterArticles.getItem(position);

                if (isConnected(getActivity())) {

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
        adapterArticles.clear();
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);

        return new ArticleLoader(getActivity(), stringFullURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> data) {
        adapterArticles.clear();

        if(data != null && !data.isEmpty())    {
            adapterArticles.addAll(data);
        }   else    {

            emptyView.setText("No articles");

            if(!isConnected(getActivity())) {

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
