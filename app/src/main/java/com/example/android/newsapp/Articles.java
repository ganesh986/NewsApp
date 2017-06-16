package com.example.android.newsapp;

/**
 * Created by matteo on 14/06/2017.
 */

public class Articles {
    private String mTitle;
    private String mDate;
    private String mURL;
    private String mArgument;

    public Articles(String title, String date, String webURL, String argument)    {
        mTitle = title;
        mDate = date;
        mURL = webURL;
        mArgument = argument;
    }

    public String getTitle() {return mTitle;}

    public String getDate()    {return mDate;}

    public String getWebURL()  {return mURL;}

    public String getArgument()   {return mArgument;}
}
