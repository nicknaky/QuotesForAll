package com.mushroomrobot.quotesforall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Nick on 4/23/2015.
 */
public class SettingsActivity extends Activity {

    int mAppWidgetId;

    //Default settings
    public static String selectedColor = "Red";
    public static String selectedSize = "12";
    public static String selectedFont = "Normal";

    static TextView colorTextView, sizeTextView, fontTextView;

    CheckBox settingsCheckBox;

    static String color = "color";
    static String size = "size";
    static String font = "font";
    static int checkedColorItem = 2;
    static int checkedSizeItem = 2;
    static int checkedFontItem = 0;


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

        colorTextView = (TextView) findViewById(R.id.color_textview);
        colorTextView.setOnClickListener(mClickListener);
        if (selectedColor!=null){
            colorTextView.setText(selectedColor);
        }
        sizeTextView = (TextView) findViewById(R.id.size_textview);
        sizeTextView.setOnClickListener(mClickListener);
        if (selectedSize!=null){
            sizeTextView.setText(selectedSize);
        }
        fontTextView = (TextView) findViewById(R.id.font_textview);
        fontTextView.setOnClickListener(mClickListener);
        if (selectedFont!=null){
            fontTextView.setText(selectedFont);
        }

        settingsCheckBox = (CheckBox) findViewById(R.id.settings_checkbox);
        //TODO: set checkbox listener here

        Button button = (Button) findViewById(R.id.apply_button);
        button.setOnClickListener(apply);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            FragmentManager fm = getFragmentManager();
            DialogFragment newFragment;

            if (v==colorTextView){
                newFragment = new DialogColorFragment();
                newFragment.show(fm, color);
            }
            else if(v==sizeTextView){
                newFragment = new DialogSizeFragment();
                newFragment.show(fm, size);
            }
            else if (v==fontTextView){
                newFragment = new DialogFontFragment();
                newFragment.show(fm, font);
            }
        }
    };

    private View.OnClickListener apply = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            selectedColor = colorTextView.getText().toString();
            Log.v("SELECTED COLOR",selectedColor);
            selectedSize = sizeTextView.getText().toString();
            Log.v("SELECTED SIZE", selectedSize);
            selectedFont = fontTextView.getText().toString();
            Log.v("SELECTED FONT", selectedFont);


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

            builder.setTitle(R.string.pick_color).setSingleChoiceItems(R.array.colors_array, checkedColorItem, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedColorItem = which;
                    colorTextView.setText(getActivity().getResources().getStringArray(R.array.colors_array)[which]);
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }

    public static class DialogSizeFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.pick_size).setSingleChoiceItems(R.array.sizes_array, checkedSizeItem, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedSizeItem = which;
                    TextView textView = (TextView) getActivity().findViewById(R.id.size_textview);
                    textView.setText(getActivity().getResources().getStringArray(R.array.sizes_array)[which]);
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }

    public static class DialogFontFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.pick_font).setSingleChoiceItems(R.array.fonts_array, checkedFontItem, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedFontItem = which;
                    TextView textView = (TextView) getActivity().findViewById(R.id.font_textview);
                    textView.setText(getActivity().getResources().getStringArray(R.array.fonts_array)[which]);
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
}

