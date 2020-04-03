package com.example.bakingapp;

import com.example.bakingapp.model.Feed;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

//https://www.youtube.com/watch?v=Lx1e_fdnNxA

public interface RedditAPI {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @Headers("Content-Type: application/json")
    @GET("baking.json")
    Call <List<Feed>> loadRecipeData();
    //https://stackoverflow.com/questions/24154917/retrofit-expected-begin-object-but-was-begin-array
    // has to return a List of the Feed object, original without the <List> was expecting JSON with
    // structure Feed: { instead of { }....
    //https://www.youtube.com/watch?v=Lx1e_fdnNxA - How to with Retrofit setting it up
    // auto generate classes based on JSON structure:
    // https://www.wisdomgeek.com/development/android-development/retrofit-gson-parsing-json-android/
    // https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
    // example parse json: https://stackoverflow.com/questions/40566472/how-to-parse-complex-json-with-retrofit-2-in-android-studio

    //void feed(Callback<List<Feed>> cb);

}
