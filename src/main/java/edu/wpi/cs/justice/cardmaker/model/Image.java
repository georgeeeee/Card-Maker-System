package edu.wpi.cs.justice.cardmaker.model;

/** Image element in a card
 *
 *  @author justice509
 */
public class Image {
    final String elementId;
    String imageUrl;
    String locationX;
    String locationY;
    String width;
    String height;

    public Image(String elementId, String imageUrl, String locationX, String locationY, String width, String height) {
        super();
        this.elementId = elementId;
        this.imageUrl = imageUrl;
        this.locationX = locationX;
        this.locationY = locationY;
        this.width = width;
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getElementId() {
        return elementId;
    }

    public String getLocationX() {
        return this.locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return this.locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
