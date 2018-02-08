package com.free.ahmed.twitterintcore;

import java.io.Serializable;

/**
 * Created by ahmed on 2/7/2018.
 */

public class User implements Serializable{
    private String id;
    private String imageUrl;
    private String bio;
    private String name;
    private String screenName;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "id: " + id
                + "\n name: " + name
                + "\n imageUrl: " + imageUrl
                + "\n bio: " + bio;
    }
}
