package edu.wpi.cs.justice.cardmaker.http;

public class EditImageRequest {
    public  String pageId;
    public String elementId;
    public String locationX;
    public String locationY;
    public String width;
    public String height;
    public String imgUrl;
	
	public EditImageRequest (String width,String height,String locationX,String locationY,String pageId,String imgUrl,String elementId) {
		this.width = width;
		this.height = height;
        this.pageId = pageId;
        this.locationX = locationX;
        this.locationY = locationY;
        this.imgUrl = imgUrl;
        this.elementId = elementId;
	}
}