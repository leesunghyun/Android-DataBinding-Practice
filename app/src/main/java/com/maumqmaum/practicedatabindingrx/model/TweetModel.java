package com.maumqmaum.practicedatabindingrx.model;

import lombok.Setter;

public class TweetModel {

    @Setter
    private String text;

    public String getText(){
        return text;
    }

}
