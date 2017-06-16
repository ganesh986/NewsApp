package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by matteo on 14/06/2017.
 */

public class ArticlesAdapter extends ArrayAdapter<Articles> {

    public ArticlesAdapter(Activity context, ArrayList<Articles> articles)    {super(context, 0, articles);}

    static class Article {
        TextView text_title;
        TextView text_published;
        TextView text_argument;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        
        Article singleArticle;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);

            singleArticle = new Article();
            singleArticle.text_title = (TextView) convertView.findViewById(R.id.title);
            singleArticle.text_published = (TextView) convertView.findViewById(R.id.date);
            singleArticle.text_argument = (TextView) convertView.findViewById(R.id.argument);
            convertView.setTag(singleArticle);
        } else {
            singleArticle = (Article) convertView.getTag();
        }

        Articles actualArticle = getItem(position);

        if (actualArticle != null) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(actualArticle.getDate());
                String formattedDate = formatDate(convertedDate);
                singleArticle.text_published.setText(formattedDate);
                Log.d(TAG, "getView: "+ formattedDate);
            } catch (ParseException e) {
                Log.e(TAG, "getView: "+e.getMessage() );
            }
            singleArticle.text_title.setText(actualArticle.getTitle());
            singleArticle.text_argument.setText(actualArticle.getArgument());
            Log.d(TAG, "getView: "+ actualArticle.getDate());
        }
        return convertView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
}