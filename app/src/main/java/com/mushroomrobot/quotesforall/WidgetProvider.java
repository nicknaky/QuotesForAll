package com.mushroomrobot.quotesforall;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by NLam on 4/23/2015.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static String selectedColor, selectedSize, selectedFont;
    public static boolean checkBoxFlag;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++){
            int appWidgetId = appWidgetIds[i];

            Random r = new Random();
            String[] mQuotesArray = context.getResources().getStringArray(R.array.Quotes);
            int randomIndex = r.nextInt(mQuotesArray.length);
            String randomQuote = mQuotesArray[randomIndex];

            SharedPreferences settings = context.getSharedPreferences("SETTINGS",0);
            selectedColor = settings.getString("COLOR","Red");
            selectedSize = settings.getString("SIZE", "12");
            selectedFont = settings.getString("FONT", "Normal");
            checkBoxFlag = settings.getBoolean("CHECKBOX_FLAG", true);

            RemoteViews views;
            switch(selectedFont){
                case "Normal": views = new RemoteViews(context.getPackageName(),R.layout.quotes_appwidget_normal);
                    break;
                case "Light": views = new RemoteViews(context.getPackageName(),R.layout.quotes_appwidget_light);
                    break;
                case "Thin": views = new RemoteViews(context.getPackageName(),R.layout.quotes_appwidget_thin);
                    break;
                case "Condensed": views = new RemoteViews(context.getPackageName(),R.layout.quotes_appwidget_condensed);
                    break;
                default: views = new RemoteViews(context.getPackageName(),R.layout.quotes_appwidget_normal);
            }
            views.setTextViewText(R.id.widget_text, randomQuote);
            views.setTextColor(R.id.widget_text, Color.parseColor(selectedColor));
            views.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, Integer.valueOf(selectedSize));

            if (!checkBoxFlag) {
                views.setImageViewResource(R.id.widget_button, R.drawable.widget_options_transparent);
            } else views.setImageViewResource(R.id.widget_button, R.drawable.widget_options);

            Intent intent = new Intent(context,WidgetProvider.class);
            intent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent);


            Intent configIntent = new Intent(context,WidgetConfigure.class);
            configIntent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            configIntent.putExtra("EXTRA_COLOR", selectedColor);
            configIntent.putExtra("EXTRA_SIZE",selectedSize);
            configIntent.putExtra("EXTRA_FONT",selectedFont);
            configIntent.putExtra("EXTRA_CHECKBOX",checkBoxFlag);
            PendingIntent pendingConfigIntent = PendingIntent.getActivity(context,0,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_button, pendingConfigIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }
}
