package com.example.android.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by matteo on 14/06/2017.
 */

public class SectionAdapter extends FragmentPagerAdapter {
    Context mContext;
    int intTabCounter = 0;
    int[] intTabLabels;

    public SectionAdapter(Context context, FragmentManager FM, boolean[] preferences)  {
        super(FM);
        mContext = context;
        intTabLabels = new int[preferences.length];
        for(int i = 0; i<=preferences.length-1; i++)  {
            if(preferences[i])  {
                intTabLabels[intTabCounter] = i;
                intTabCounter++;
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        ArticlesActivity sFragment = new ArticlesActivity();
        String[] stringSectionSearch = mContext.getResources().getStringArray(R.array.sectionsearch);
        bundle.putString("section", stringSectionSearch[intTabLabels[position]]);
        bundle.putInt("sectionLoader", intTabLabels[position]);
        sFragment.setArguments(bundle);
        return sFragment;
    }

    @Override
    public int getCount() {
        return intTabCounter;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] stringTabLabels = mContext.getResources().getStringArray(R.array.tablabels);
        return stringTabLabels[intTabLabels[position]];
    }
}