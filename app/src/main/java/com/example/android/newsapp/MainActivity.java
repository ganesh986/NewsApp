package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    boolean[] booleanSectionPreferences = new boolean[5];
    public static Context parentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        
        booleanSectionPreferences[0] = settings.getBoolean("sport", false);
        booleanSectionPreferences[1] = settings.getBoolean("politics", false);
        booleanSectionPreferences[2] = settings.getBoolean("technology", false);
        booleanSectionPreferences[3] = settings.getBoolean("business", false);
        booleanSectionPreferences[4] = settings.getBoolean("lifestyle", false);


        ViewPager ViewPag = (ViewPager) findViewById(R.id.viewpager);
        SectionAdapter adapt = new SectionAdapter(this, getSupportFragmentManager(), booleanSectionPreferences);
        ViewPag.setAdapter(adapt);

        TabLayout Tab = (TabLayout) findViewById(R.id.tabs);
        Tab.setupWithViewPager(ViewPag);

        parentContext = getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings)    {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            startActivity(startSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
