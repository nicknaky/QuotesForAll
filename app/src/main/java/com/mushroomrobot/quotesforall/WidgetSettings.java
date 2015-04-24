package com.mushroomrobot.quotesforall;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Nick on 4/23/2015.
 */
public class WidgetSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_widget);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_font_color_value_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_font_size_value_key)));
        //findPreference(getString(R.string.pref_font_type_value_key));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        int mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Random r = new Random();
        String[] mQuotesArray = getResources().getStringArray(R.array.Quotes);
        int randomIndex = r.nextInt(mQuotesArray.length);
        String randomQuote = mQuotesArray[randomIndex];

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.quotes_appwidget);

        views.setTextViewText(R.id.widget_text, randomQuote);

        views.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, R.string.pref_font_size_value_key);
        views.setTextColor(R.id.widget_text, R.string.pref_font_color_value_key);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {


        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        setResult(RESULT_OK, resultValue);
        finish();

    }

}
