package com.credit.backup.model;

public class ImageHelperClass {
    String id;
    String name;
    String image;
    String comment;
    String publishedAt;

    public ImageHelperClass() {
    }

    public ImageHelperClass(String id, String name, String image, String comment, String publishedAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.comment = comment;
        this.publishedAt = publishedAt;
    }

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
}
