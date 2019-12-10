package edu.wpi.cs.justice.cardmaker.model;

public class ImageUrl{
    String Url;

    public  ImageUrl(String ImageUrl) {
        this.Url = ImageUrl;
    }
    public String getImageUrl() {
        return Url;
    }

    public void setImageUrl(String imageUrl) {
        this.Url = imageUrl;
    }
}