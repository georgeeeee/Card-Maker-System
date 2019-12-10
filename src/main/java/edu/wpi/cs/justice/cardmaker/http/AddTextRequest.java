package edu.wpi.cs.justice.cardmaker.http;

public class AddTextRequest {
    public final String text;
    public final String fontName;
    public final String fontType;
    public final String locationX;
    public final String locationY;
    public final String pageId;
    public final String fontSize;

    public AddTextRequest(String text, String fontName, String fontType, String locationX, String locationY, String pageId, String fontSize) {
        this.text = text;
        this.fontName = fontName;
        this.fontType = fontType;
        this.locationX = locationX;
        this.locationY = locationY;
        this.pageId = pageId;
        this.fontSize = fontSize;
    }
}