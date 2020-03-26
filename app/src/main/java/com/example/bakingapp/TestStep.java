package com.example.bakingapp;

// one step in a recipe
public class TestStep {

    String s_id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public TestStep( String id, String shortDescription, String description, String videoURL, String thumbnailURL ) {
        this.s_id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }
}
