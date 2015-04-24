package com.mushroomrobot.quotesforall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Nick on 4/23/2015.
 */
public class SettingsActivity extends Activity {

    int mAppWidgetId;




    String color = "color";
    static int checkedItem = 2;
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            FragmentManager fm = getFragmentManager();
            DialogFragment newFragment = new DialogColorFragment();
            newFragment.show(fm, color);
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }



        TextView textView = (TextView) findViewById(R.id.color_textview);
        textView.setOnClickListener(mClickListener);

        Button button = (Button) findViewById(R.id.apply_button);
        button.setOnClickListener(apply);
    }

    private View.OnClickListener apply = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(SettingsActivity.this);


            Random r = new Random();
            String[] mQuotesArray = getResources().getStringArray(R.array.Quotes);
            int randomIndex = r.nextInt(mQuotesArray.length);
            String randomQuote = mQuotesArray[randomIndex];

            RemoteViews views = new RemoteViews(SettingsActivity.this.getPackageName(),R.layout.quotes_appwidget);
            views.setTextViewText(R.id.widget_text, randomQuote);

            TextView textView = (TextView) findViewById(R.id.color_textview);
            String selectedColor = textView.getText().toString();

            Log.v("SELECTED COLOR",selectedColor);
            views.setTextColor(R.id.widget_text,Color.parseColor(selectedColor));


            Intent intent = new Intent(SettingsActivity.this,QuotesWidgetProvider.class);
            intent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_IDS,mAppWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent);

            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();

        }
    };


    public static class DialogColorFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.pick_color).setSingleChoiceItems(R.array.colors_array, checkedItem, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedItem = which;

                    TextView textView = (TextView) getActivity().findViewById(R.id.color_textview);
                    textView.setText(getActivity().getResources().getStringArray(R.array.colors_array)[which]);

                    dialog.dismiss();
                }


            });

            return builder.create();
        }

    }
}

