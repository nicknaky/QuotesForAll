package com.mushroomrobot.quotesforall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
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

/**
 * Created by Nick on 4/23/2015.
 */
public class SettingsActivity extends Activity {

    int mAppWidgetId;

    public static int setColor;




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

            RemoteViews views = new RemoteViews(SettingsActivity.this.getPackageName(),R.layout.quotes_appwidget);

            TextView textView = (TextView) findViewById(R.id.color_textview);
            String selectedColor = textView.getText().toString();
            Log.v("SELECTED COLOR",selectedColor);
            setColor = Color.parseColor(selectedColor);

            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, SettingsActivity.this, QuotesWidgetProvider.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {mAppWidgetId});
            sendBroadcast(intent);
            setResult(RESULT_OK, intent);
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

