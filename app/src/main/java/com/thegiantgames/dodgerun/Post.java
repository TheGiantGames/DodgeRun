package com.thegiantgames.dodgerun;

public class Post {

    private String description;
    private String name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public Post(String name, String description , String image) {
        this.description = description;
        this.name = name;
        this.image = image;
    }



    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
