package com.example.drklrd.osmcontributions.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by drklrd on 10/11/17.
 */

public class HashtagsResponse {

    @SerializedName("hashtags")
    private String[] hashtags;

    public String[] getHashtags(){
        return hashtags;
    }

    public  void setHashtags(String[] hashtags){
        this.hashtags = hashtags;
    }


}


