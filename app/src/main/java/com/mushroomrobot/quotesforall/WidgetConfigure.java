package com.mushroomrobot.quotesforall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nick on 4/23/2015.
 */
public class WidgetConfigure extends Activity {

    int mAppWidgetId;


    public static String selectedColor = "Red";
    public static String selectedSize = "12";
    public static String selectedFont = "Normal";

    public static String defaultColor = "Red";
    public static String defaultSize = "12";
    public static String defaultFont = "Normal";

    public static boolean checkBoxFlag = true;

    static TextView colorTextView, sizeTextView, fontTextView;
    static CheckBox settingsCheckBox;

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
        sizeTextView = (TextView) findViewById(R.id.size_textview);
        sizeTextView.setOnClickListener(mClickListener);
        fontTextView = (TextView) findViewById(R.id.font_textview);
        fontTextView.setOnClickListener(mClickListener);
        settingsCheckBox = (CheckBox) findViewById(R.id.settings_checkbox);
        settingsCheckBox.setOnClickListener(settingsCheckListener);

        Button button = (Button) findViewById(R.id.apply_button);
        button.setOnClickListener(applyListener);

        try {

            if (extras.getString("EXTRA_COLOR") != null) {
                colorTextView.setText(extras.getString("EXTRA_COLOR"));
                selectedColor = extras.getString("EXTRA_COLOR", "Red");
            } else colorTextView.setText(defaultColor);
            if (extras.getString("EXTRA_SIZE") != null) {
                sizeTextView.setText(extras.getString("EXTRA_SIZE"));
                selectedSize = extras.getString("EXTRA_SIZE", "12");
            } else sizeTextView.setText(defaultSize);
            if (extras.getString("EXTRA_FONT") != null) {
                fontTextView.setText(extras.getString("EXTRA_FONT"));
                selectedFont = extras.getString("EXTRA_FONT", "Normal");
            } else fontTextView.setText(defaultFont);
            if (extras.getBoolean("EXTRA_CHECKBOX")){
                settingsCheckBox.setChecked(true);
            }else settingsCheckBox.setChecked(false);
        } catch (java.lang.NullPointerException e){
            System.err.println("NullPointerException: " + e.getMessage());
        }

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

    private View.OnClickListener settingsCheckListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if (settingsCheckBox.isChecked()){
                checkBoxFlag=true;
            } else checkBoxFlag=false;
        }
    };

    private View.OnClickListener applyListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            selectedColor = colorTextView.getText().toString();
            Log.v("SELECTED COLOR",selectedColor);
            selectedSize = sizeTextView.getText().toString();
            Log.v("SELECTED SIZE", selectedSize);
            selectedFont = fontTextView.getText().toString();
            Log.v("SELECTED FONT", selectedFont);

            SharedPreferences settings = getSharedPreferences("SETTINGS",0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("COLOR",selectedColor);
            editor.putString("SIZE",selectedSize);
            editor.putString("FONT",selectedFont);
            editor.putBoolean("CHECKBOX_FLAG", checkBoxFlag);
            editor.commit();

            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, WidgetConfigure.this, WidgetProvider.class);
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

            List<String> colors_list = Arrays.asList(getActivity().getResources().getStringArray(R.array.colors_array));
            checkedColorItem = colors_list.indexOf(selectedColor);

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

            List<String> sizes_list = Arrays.asList(getActivity().getResources().getStringArray(R.array.sizes_array));
            checkedSizeItem = sizes_list.indexOf(selectedSize);

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

            List<String> fonts_list = Arrays.asList(getActivity().getResources().getStringArray(R.array.fonts_array));
            checkedFontItem = fonts_list.indexOf(selectedFont);

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

