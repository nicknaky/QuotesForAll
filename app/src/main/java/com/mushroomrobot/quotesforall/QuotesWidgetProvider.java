package com.mushroomrobot.quotesforall;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by NLam on 4/23/2015.
 */
public class QuotesWidgetProvider extends AppWidgetProvider {

    Context mContext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++){
            int appWidgetId = appWidgetIds[i];

            Random r = new Random();
            String[] mQuotesArray = context.getResources().getStringArray(R.array.Quotes);
            int randomIndex = r.nextInt(mQuotesArray.length);
            String randomQuote = mQuotesArray[randomIndex];


            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.quotes_appwidget);
            views.setTextViewText(R.id.widget_text, randomQuote);


            Intent intent = new Intent(context,QuotesWidgetProvider.class);
            intent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }
}
