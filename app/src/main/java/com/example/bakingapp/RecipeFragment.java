package com.example.bakingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class RecipeFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecipeFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Recipe fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Get a reference to the ImageView in the fragment layout
        TextView tv = (TextView) rootView.findViewById(R.id.recipe_steps_txt);
        tv.setText("recipe steps 1...");

        // Return the rootView
        return rootView;

    }



}


/*
https://developer.android.com/training/basics/fragments/creating
https://developer.android.com/reference/android/support/v4/app/ListFragment
https://stackoverflow.com/questions/29116031/listfragment-in-android-studio
https://github.com/udacity/Android_Me/blob/TFragments.02-Exercise-DisplayThreeFragments/app/src/main/java/com/example/android/android_me/ui/BodyPartFragment.java

 */