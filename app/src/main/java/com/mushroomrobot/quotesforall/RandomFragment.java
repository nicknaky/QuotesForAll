package com.mushroomrobot.quotesforall;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by NLam on 4/15/2015.
 */
public class RandomFragment extends Fragment {


    public Random r = new Random();

    public RandomFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_random,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_full_list){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container,new ListFragment())
                    .addToBackStack(null)
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.random_fragment,container, false);

        String[] mQuotesArray = getResources().getStringArray(R.array.Quotes);
        int randomIndex = r.nextInt(mQuotesArray.length);
        String randomQuote = mQuotesArray[randomIndex];

        final TextView textView = (TextView) rootView.findViewById(R.id.random_textview);
        textView.setText(randomQuote);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(randomize());
            }
        });


        return rootView;
    }

    private String randomize(){
        String[] mQuotesArray = getResources().getStringArray(R.array.Quotes);
        int randomIndex = r.nextInt(mQuotesArray.length);
        String randomQuote = mQuotesArray[randomIndex];
        return randomQuote;
    }
}
