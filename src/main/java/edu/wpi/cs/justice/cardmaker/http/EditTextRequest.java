package edu.wpi.cs.justice.cardmaker.http;

public class EditTextRequest {
    public  String pageId;
    public String elementId;
    public String locationX;
    public String locationY;
    public String fontName;
    public String fontType;
    public String fontSize;
    public String text;
	
	public EditTextRequest (String fontName,String fontType,String fontSize,String locationX,String locationY,String pageId,String imgUrl,String elementId,String text) {
		this.fontName = fontName;
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.pageId = pageId;
        this.locationX = locationX;
        this.locationY = locationY;
        this.text = text;
        this.elementId = elementId;
	}
}