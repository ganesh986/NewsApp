package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by matteo on 14/06/2017.
 */

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public static class NewsSettings extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference Sport = findPreference("sport");
            Preference Politics = findPreference("politics");
            Preference Technology = findPreference("technology");
            Preference Business = findPreference("business");
            Preference Lifestyle = findPreference("lifestyle");
            Preference maxArticles = findPreference("max_articles");

            preferenceValue(0, Sport);
            preferenceValue(0, Politics);
            preferenceValue(0, Technology);
            preferenceValue(0, Business);
            preferenceValue(0, Lifestyle);
            preferenceValue(1, maxArticles);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            if(newValue instanceof String) {
                preference.setSummary(newValue.toString());
            }
            return true;
        }

        private void preferenceValue(int varType, Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            if(varType == 0)  {
                boolean preferenceBoolean = preferences.getBoolean(preference.getKey(), false);
                onPreferenceChange(preference, preferenceBoolean);
            }   else    {
                String preferenceString = preferences.getString(preference.getKey(), "10");
                onPreferenceChange(preference, preferenceString);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
