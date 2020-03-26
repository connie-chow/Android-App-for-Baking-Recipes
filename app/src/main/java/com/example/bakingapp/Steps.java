package com.example.bakingapp;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps")
public class Steps {

    int getId;

    @PrimaryKey
    @NonNull
    private String s_id;

    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    // Constructor for creating movie entry object to insert into database
    public Steps(
            String s_id,
            String shortDescription,
            String description,
            String videoURL,
            String thumbnailURL) {

        this.s_id = s_id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }


    // Write/Read these private values
    public String getS_id() { return s_id; }
    public String getShortDescription() { return shortDescription; }
    public String getDescription() { return description; }
    public String getVideoURL() { return videoURL; }
    public String getThumbnailURL() { return thumbnailURL; }

    public void setId(String s) { this.s_id = s; }
    public void setShortDescription(String s) { this.shortDescription = s; }
    public void setDescription(String s) { this.description = s; }
    public void setVideoURL(String s) { this.videoURL = s; }
    public void setThumbnailURL(String s) { this.thumbnailURL = s; }

}
