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

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking";

    @Headers("Content-Type: application/json")
    @GET("baking.json")
    Call <List<Feed>> loadRecipeData();
    //void feed(Callback<List<Feed>> cb);

}
