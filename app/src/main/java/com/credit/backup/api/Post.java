package com.credit.backup.api;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String name;
    @SerializedName("picture")
    private String image;
    @SerializedName("comment")
    private String comment;
    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("body")
    private String text;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
