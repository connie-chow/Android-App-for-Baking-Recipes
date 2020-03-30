package com.example.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecipeFragment extends Fragment {

    RecipeStepAdapter mRecipeStepAdapter;
    private Context globalContext = null;

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


        RecyclerView stepsView = (RecyclerView) rootView.findViewById(R.id.rv_recipe_steps);
        // recyclerview is in the fragment....
        //https://stackoverflow.com/questions/30598871/can-not-resolve-method-findviewbyidint/30599010

        // detect if mobile or tablet based on dpi, do the span in the xml file
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        stepsView.setLayoutManager(layoutManager);


        mRecipeStepAdapter = new RecipeStepAdapter(container.getContext(), new ArrayList<TestStep>());
        stepsView.setAdapter(mRecipeStepAdapter);


        // setup test steps data
        TestStep s0 = new TestStep(
                "0", "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                ""
        );
        TestStep s1 = new TestStep(
                "1", "Starting prep", "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
                "",
                ""
        );
        TestStep s2 = new TestStep(
                "2", "Prep the cookie crust.", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4",
                ""
        );
        TestStep s3 = new TestStep(
                "3", "Press the crust into baking form.", "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4",
                ""
        );
        TestStep s4 = new TestStep(
                "4", "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                ""
        );
        TestStep s5 = new TestStep(
                "5", "Recipe Introduction", "Recipe Introduction",
                "",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"
        );
        TestStep s6 = new TestStep(
                "6", "Recipe Introduction", "Recipe Introduction",
                "",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"
        );

        mRecipeStepAdapter.add(s0);
        mRecipeStepAdapter.add(s1);
        mRecipeStepAdapter.add(s2);
        mRecipeStepAdapter.add(s3);
        mRecipeStepAdapter.add(s4);
        mRecipeStepAdapter.add(s5);
        mRecipeStepAdapter.add(s6);
        mRecipeStepAdapter.notifyDataSetChanged();


        // Return the rootView
        return rootView;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
/*
        // to be implemented
        try {
            activityCallback = (ButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ButtonClickListener");
        }
        */

    }


    ButtonClickListener activityCallback;

    // to be implemented
    public interface ButtonClickListener {
        public void onButtonClick(int position, String text);
        /*
        Context context = getActivity().getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        */
    }

}


/*
https://developer.android.com/training/basics/fragments/creating
https://developer.android.com/reference/android/support/v4/app/ListFragment
https://stackoverflow.com/questions/29116031/listfragment-in-android-studio
https://github.com/udacity/Android_Me/blob/TFragments.02-Exercise-DisplayThreeFragments/app/src/main/java/com/example/android/android_me/ui/BodyPartFragment.java

 */