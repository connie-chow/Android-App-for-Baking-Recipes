package com.example.bakingapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class RecipeDetailsFragment extends Fragment {

    public static String step_id;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecipeDetailsFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        // Get a reference to the ImageView in the fragment layout
        TextView tv = (TextView) rootView.findViewById(R.id.recipe_details_txt);
        tv.setText("recipe step details: instructions...");

        Bundle extras = getActivity().getIntent().getExtras();
        String step_id = extras.getString("step_id");

        tv.setText(step_id + step_id + step_id + step_id + step_id );

        // Return the rootView
        return rootView;
/*

        // Exoplayer: https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
        //https://exoplayer.dev/hello-world.html




        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_body_part, container, false);

        // Get a reference to the ImageView in the fragment layout
        ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);

        // Set the image to the first in our list of head images
        imageView.setImageResource(AndroidImageAssets.getHeads().get(0));

        // TODO (3) If a list of image ids exists, set the image resource to the correct item in that list
        // Otherwise, create a Log statement that indicates that the list was not found

        // Return the rootView
        return rootView;

 */

    }




}
